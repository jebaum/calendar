import curses

class ScrollableCursesInterface(object):
  """
  The "view" is a rectangular section of the terminal window that the contents
  will be displayed into.The "content" rectangle may be any size, and will
  move around under the "view". This functions like a pad in the python curses
  library.
  """

  def __init__(self):
    self.pad = curses.newpad(10,10)

    self.set_view_position(0,0)
    self.set_view_size(0,0)
    self.set_content_position(0,0)
    self.set_content_size(100,100)

    self.needs_update = True

  def set_view_position(self, x, y):
    self.view_x = x
    self.view_y = y

  def set_view_size(self, w, h):
    self.view_w = w
    self.view_h = h

  def set_content_position(self, x, y):
    """0, 0 refers to the upper left corner of the view window"""
    self.content_x = x
    self.content_y = y

  def set_content_size(self, w, h):
    self.content_w = w
    self.content_h = h
    self.pad.resize(h,w)
    self.needs_update = True

  def scroll_x(self, amount):
    self.content_x += amount

    # Clamp to min / max
    if self.content_x < 0:
      self.content_x = 0
    if self.content_x > self.content_w-self.view_w:
      self.content_x = self.content_w-self.view_w

  def scroll_y(self, amount):
    self.content_y += amount

    # Clamp to min / max
    if self.content_y < 0:
      self.content_y = 0
    if self.content_y > self.content_h-self.view_h:
      self.content_y = self.content_h-self.view_h

  def update(self):
    # Stub 
    for x in range(0, self.content_w-1, 10):
      self.pad.addstr(0,x,str(x))
    for y in range(1, self.content_h-1):
      self.pad.addstr(y,0,str(y))
      for x in range(3, self.content_w-1):
        self.pad.addch(y,x, ord('a') + (x*x+y*y) % 26)
    self.needs_update = False

  def display(self):
    if self.needs_update:
      self.update()

    # Refresh the pad
    # render top left corner of content into view rectangle
    # args 1,2 are y,x for top left corner of pad
    # args 3,4 are top left corner of terminal window rectangle to render into
    # args 5,6 are bottom right corner of terminal window rectangle to render into
    self.pad.refresh(self.content_y,self.content_x, self.view_y,self.view_x, self.view_y+self.view_h, self.view_x+self.view_w)

class DailyView(ScrollableCursesInterface):
  def update(self):
    for x in range(0, self.content_w-1, 10):
      self.pad.addstr(0,x,str(x))
    for y in range(1, self.content_h-1):
      self.pad.addstr(y,0,str(y))
      for x in range(3, self.content_w-1):
        self.pad.addch(y,x, ord('a') + (x*x+y*y) % 5)

    self.needs_update = False

class SummaryView(ScrollableCursesInterface):
  def update(self):
    for x in range(0, self.content_w-1, 10):
      self.pad.addstr(0,x,str(x))
    for y in range(1, self.content_h-1):
      self.pad.addstr(y,0,str(y))
      for x in range(3, self.content_w-1):
        self.pad.addch(y,x, ord('a') + (x*x+y*y) % 10)

    self.needs_update = False

class CommandView(ScrollableCursesInterface):
  def update(self):
    for x in range(0, self.content_w-4, 4):
      self.pad.addstr(0,x,"cmd")

    self.needs_update = False

class ListView(ScrollableCursesInterface):
  def __init__(self):
    self.list_size = 20
    self.box_heights = [1]*self.list_size
    super(ListView, self).__init__()

  def set_content_size(self, w, h):
    self.content_w = w
    self.content_h = self.list_size * sum(self.box_heights) + 1
    self.pad.resize(self.content_h,self.content_w+1)
    self.needs_update = True

  def update_size(self):
    # Leverages content_sizes resizing based on list_size and box_heights
    self.set_content_size(self.content_w,self.content_h)

  def update_box(self, index, value):
    self.box_heights[index] = value
    self.update_size()

  def draw_box(self, y, h, index):
    self.pad.addstr(y,1,str(index))
    # Right vertical line
    for i in range(h):
      self.pad.addch(y+i,0,curses.ACS_VLINE)
    # Left vertical line
    for i in range(h):
      self.pad.addch(y+i, self.content_w-1,curses.ACS_VLINE)

  def draw_separator(self,y):
    self.pad.addch(y,0,curses.ACS_LTEE)
    self.pad.addch(y,self.content_w-1,curses.ACS_RTEE)
    for i in range(1,self.content_w-1):
      self.pad.addch(y,i,curses.ACS_HLINE)

  def draw_top_border(self):
    y = 0
    self.pad.addch(y,0,curses.ACS_ULCORNER)
    self.pad.addch(y,self.content_w-1,curses.ACS_URCORNER)
    for i in range(1,self.content_w-1):
      self.pad.addch(y,i,curses.ACS_HLINE)

  def draw_bottom_border(self):
    y = self.content_h-1
    self.pad.addch(y,0,curses.ACS_LLCORNER)
    self.pad.addch(y,self.content_w-1,curses.ACS_LRCORNER)
    for i in range(1,self.content_w-1):
      self.pad.addch(y,i,curses.ACS_HLINE)

  def update(self):
    self.pad.erase()

    self.draw_top_border()

    yoff = 1
    for i in range(self.list_size):
      self.draw_box(yoff,self.box_heights[i],i)
      if i != self.list_size-1:
        self.draw_separator(yoff+self.box_heights[i])
      yoff += self.box_heights[i]+2

    self.draw_bottom_border()

    self.needs_update = False











