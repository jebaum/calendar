import curses
from ListView import *
from TimeView import *

class MonthlyView(TimeView,ListView):
  def __init__(self, EventStore):
    # super init intializes in reverse order of multiple inheritance seemingly
    # e.g. Class(C1, C2) is init'ed in C2, C1 order
    super(MonthlyView, self).__init__()

    self.event_lists = []
    for i in range(31):
      self.event_lists.append([])

    self.EventStore = EventStore

    self.box_header_width = 11

    self.get_period_func = self.get_month_period
    self.offset_period_func = self.apply_month_offset

  def draw_box_header(self,y,index):
    self.pad.addstr(y,2,str(index+1))

  def update(self):
    self.clear_events()
    self.add_events(self.EventStore.get_events(self.period))
    self.view_header = self.get_view_header()
    super(MonthlyView, self).update()

  def clear_events(self):
    for i in range(len(self.event_lists)):
      self.event_lists[i] = []

  def add_events(self,events):
    for e in events:
      index = e.startDay-1
      self.add_event(index, e)