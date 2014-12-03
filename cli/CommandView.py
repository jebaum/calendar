import curses
from ScrollableView import *

class CommandView(ScrollableView):
  def update(self):
    for x in range(0, self.content_w-4, 4):
      self.pad.addstr(0,x,"cmd")