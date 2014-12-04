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
  def get_events(self, event_filter=None):
    """
    Return events, fetching from server as needed
    If filter is specified as tuple (start_datetime, end_datetime), filter to that period
    """
    if self.cache[1] is None:
      events = []
      for i in range(200):
        e = Event()
        seconds_since_epoch = (datetime.datetime.now() - datetime.datetime.utcfromtimestamp(0)).total_seconds()
        e.startTime = seconds_since_epoch + random.randint(-60*60*24*30,60*60*24*30)
        e.endTime = e.startTime + random.randint(60*60,60*60*10)
        e.title = "Event " + str(i)
        e.date_from_time_seconds()
        e.description = e.startDate.strftime("%B %d %Y, %H:%M:%S")
        events.append(e)

      self.cache = (events, 1)
      # self.cache = (self.get_events_from_json_file("json.txt"), None)

    return self.filter_events(self.cache[0], event_filter)

  def update_event(self, e):
    """
    Update e on cache and server
    """
    pass

  # 
  # INTERNAL
  # 
  def filter_events(self, events, event_filter):
    """
    Given a list of events, only return those in period defined by filter tuple
    filter tuple of form (start_datetime, end_datetime)
    """
    if event_filter is None:
      return events

    filtered = []
    for e in events:
      if e.startDate > event_filter[0] and e.startDate < event_filter[1]:
        filtered.append(e)

    return filtered

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