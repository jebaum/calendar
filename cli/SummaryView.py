import curses
from ScrollableView import *

class SummaryView(ScrollableView):
  def update(self):
    for x in range(0, self.content_w-1, 10):
      self.pad.addstr(0,x,str(x))
    for y in range(1, self.content_h-1):
      self.pad.addstr(y,0,str(y))
      for x in range(3, self.content_w-1):
        self.pad.addch(y,x, ord('a') + (x*x+y*y) % 10)