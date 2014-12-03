import curses

class ScrollableView(object):
  """
  The "view" is a rectangular section of the terminal window that the contents
  will be displayed into.The "content" rectangle may be any size, and will
  move around under the "view". This functions like a pad in the python curses
  library.
  """

  def __init__(self):
    self.pad = curses.newpad(10,10)

    self.view_x = 0
    self.view_y = 0
    self.view_w = 0
    self.view_h = 0
    self.content_x = 0
    self.content_y = 0
    self.content_w = 100
    self.content_h = 100

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

    self.needs_update = True

  def scroll_y(self, amount):
    self.content_y += amount

    # Clamp to min / max
    if self.content_y < 0:
      self.content_y = 0
    if self.content_y > self.content_h-self.view_h-1:
      self.content_y = self.content_h-self.view_h-1

    self.needs_update = True

  def update(self):
    # Stub 
    for x in range(0, self.content_w-1, 10):
      self.pad.addstr(0,x,str(x))
    for y in range(1, self.content_h-1):
      self.pad.addstr(y,0,str(y))
      for x in range(3, self.content_w-1):
        self.pad.addch(y,x, ord('a') + (x*x+y*y) % 26)

  def display(self):
    if self.needs_update:
      self.pad.erase()
      self.update()
      self.needs_update = False

      # Refresh the pad
      # render top left corner of content into view rectangle
      # args 1,2 are y,x for top left corner of pad
      # args 3,4 are top left corner of terminal window rectangle to render into
      # args 5,6 are bottom right corner of terminal window rectangle to render into
      self.pad.refresh(self.content_y,self.content_x, self.view_y,self.view_x, self.view_y+self.view_h, self.view_x+self.view_w)
