import curses
from ListView import *

class DailyView(ListView):
  def __init__(self, EventStore):
    super(DailyView, self).__init__()

    self.event_lists = []
    for i in range(24):
      self.event_lists.append([])

    self.EventStore = EventStore