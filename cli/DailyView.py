import curses
from ListView import *

class DailyView(ListView):
  def __init__(self, EventStore):
    super(DailyView, self).__init__()

    self.event_lists = []
    for i in range(24):
      self.event_lists.append([])

    self.EventStore = EventStore

    self.box_header_width = 5
    self.view_header = "Today"

  def draw_box_header(self,y):
    self.pad.addstr(y,1,str(y).rjust(self.box_header_width))