import json, datetime
import random

from event import Event

class EventStore():
  def __init__(self):
    # cache: tuple (events:list, last_update:datetime)
    self.cache = ([], None)

  # 
  # EXTERNAL
  # 
  def get_events(self, filter=None):
    """
    Return events, fetching from server as needed
    If filter is specified as tuple (start_datetime, end_datetime), filter to that period
    """
    if self.cache[1] is None:
      events = []
      for i in range(50):
        e = Event()
        seconds_since_epoch = (datetime.datetime.now() - datetime.datetime.fromtimestamp(0)).total_seconds()
        e.startTime = seconds_since_epoch + random.randint(-60*60*24*30,60*60*24*30)*1000
        e.endTime = e.startTime + random.randint(60*60,60*60*10)*1000
        e.title = "Event " + str(i)
        e.date_from_time_seconds()
        events.append(e)

      self.cache = (events, 1)
      # self.cache = (self.get_events_from_json_file("json.txt"), None)

    return self.cache[0]

  def update_event(self, e):
    """
    Update e on cache and server
    """
    pass

  # 
  # INTERNAL
  # 
  def filter_events(self, events_list, filter):
    """
    Given a list of events, only return those in period defined by filter tuple
    filter tuple of form (start_datetime, end_datetime)
    """
    pass

  def get_events_from_json_file(self, filename):
    """
    For development & testing
    Load events from a json file instead of from server
    """
    events = []
    with open(filename, 'r') as f:
        j = json.load(f)
        for event in j:
            e = Event()
            e.load_from_json(event)
            events.append(e)

    return events