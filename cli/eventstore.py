import json, datetime
import random

from event import Event

class EventStore():
  def __init__(self):
    # cache: tuple (events:list, last_update:datetime)
    self.cache = ([], None)

    self.filters = {'date': None,
                    'title': None,
                    'description': None,
                    'category': None,
                    'location': None}

  # 
  # EXTERNAL
  # 
  def get_events(self):
    """
    Return events, fetching from server as needed
    Applies any active filters
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
        e.id = len(events)
        events.append(e)

      self.cache = (events, 1)
      # self.cache = (self.get_events_from_json_file("json.txt"), None)

    return self.filter_events(self.cache[0])

  def update_event(self, e):
    """
    Update e on cache and server
    """
    pass

  # 
  # INTERNAL
  # 
  def filter_events(self, events):
    """
    Given a list of events, iterates over self.filter functions and applies them
    """
    for k,f in self.filters.items():
      if f == None:
        continue
      events = filter(f, events)
    return events

  def add_filter(self, key, filter_info):
    if key in self.filters.keys() and (filter_info == None or 'None' in filter_info):
      self.filters[key] = None
      return 'Success! Clear filter (%s)' % (key)

    if key == 'date':
      date_filter = lambda e: True if e.startDate > filter_info[0] and e.startDate < filter_info[1] else False
      self.filters[key] = date_filter
      return True
    elif key in self.filters.keys():
      search_filter = lambda e: filter_info in getattr(e, key, None)
      self.filters[key] = search_filter
      return 'Success! Filter (%s) in (%s)' % (filter_info, key)

    return False

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