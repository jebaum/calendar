import curses
from ListView import *
from TimeView import *

class DailyView(TimeView,ListView):
  def __init__(self, EventStore):
    # super init intializes in reverse order of multiple inheritance seemingly
    # e.g. Class(C1, C2) is init'ed in C2, C1 order
    super(DailyView, self).__init__()

    self.event_lists = []
    for i in range(24):
      self.event_lists.append([])

    self.EventStore = EventStore

    self.box_header_width = 5
    self.view_header = "Today"

  def draw_box_header(self,y):
    self.pad.addstr(y,1,str(y).rjust(self.box_header_width))