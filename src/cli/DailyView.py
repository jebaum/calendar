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

    self.box_header_width = 6 
    # self.view_header = "Today"
    self.view_header = self.get_view_header()

    self.get_period_func = self.get_day_period
    self.offset_period_func = self.apply_day_offset

  def draw_box_header(self,y,index):
    self.pad.addstr(y,2,"%s:00" % (str(index).rjust(2,'0')))

  def update(self):
    self.clear_events()
    self.EventStore.add_filter('date', self.period)
    self.add_events(self.EventStore.get_events())
    self.view_header = self.get_view_header()
    super(DailyView, self).update()

  def clear_events(self):
    for i in range(len(self.event_lists)):
      self.event_lists[i] = []

  def add_events(self,events):
    for e in events:
      self.add_event(e.startHour, e)
