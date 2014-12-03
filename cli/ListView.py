import curses
from ScrollableView import *

class ListView(ScrollableView):
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
    self.draw_top_border()

    yoff = 1
    for i in range(self.list_size):
      self.draw_box(yoff,self.box_heights[i],i)
      if i != self.list_size-1:
        self.draw_separator(yoff+self.box_heights[i])
      yoff += self.box_heights[i]+2

    self.draw_bottom_border()