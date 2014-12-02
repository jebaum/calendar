import json, datetime

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
      self.cache = (self.get_events_from_json_file("json.txt"), None)

    return self.cache

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