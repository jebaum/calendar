import sys, json, datetime, time
import requests

class Event(object):
    def __init__(self):
        self.title = None
        self.startTime = 0
        self.endTime = 0
        self.location = None
        self.category = None
        self.description = None
        self.json_keys = ["title", "startTime", "endTime", "location", "category", "description"]

    def __repr__(self):
        return self.__str__()

    def __str__(self):
        json_dict = {}
        for k in self.json_keys:
            json_dict[k] = getattr(self, k)

        return json.dumps(json_dict)

    def clamp_seconds_since_epoch(self, sec):
        sec_string = str(sec)
        if len(sec_string) > 10:
            return int(sec_string[:10])
        elif len(sec_string) < 10:
            return int(sec_string.ljust(10, '0'))
        return sec_string

    def load_from_json(self, json_dict):
        for k,v in json_dict.items():
            setattr(self, k, v)

        # Adjust startTime / endTime to ensure in seconds since epoch
        self.startTime = self.clamp_seconds_since_epoch(self.startTime)
        self.endTime = self.clamp_seconds_since_epoch(self.endTime)

        self.startDate = datetime.datetime.fromtimestamp(self.startTime)
        self.endDate = datetime.datetime.fromtimestamp(self.endTime)

def get_events(startTime, endTime):
    """
    Make request for events between start and end time, convert to event class
    and return as a list of events
    """
    url = 'http://localhost:4567/date_start/%s/date_end/%s' % (startTime, endTime)
    r = requests.get(url)
    events = []
    for event in r.json():
       e = Event()
       e.load_from_json(event)
       events.append(e)

    return events

def display_daily_term(all_events):
    """
    Display today's events, printed to stdout
    Attempt to match orgmode style
    """
    # Filter events to only those today
    now = datetime.datetime.now()
    events = [e for e in all_events if e.startDate.day == now.day and \
                                       e.startDate.month == now.month and \
                                       e.startDate.year == now.year]

    # Print list of hours / events
    for i in range(0,24):
        hour = "%s:00" % (i)
        print(hour.rjust(5, ' '), '-'*10)

        for e in events:
            if i == e.startDate.hour:
                event_time = '%s:%s' % (e.startDate.hour, e.startDate.minute)
                print(event_time.rjust(5, ' '), e.title)

def datetime_to_seconds_since_epoch(dt):
    return time.mktime(dt.timetuple())

if __name__ == '__main__':
    # if len(sys.argv) < 3:
    #     print("Usage: python cli.py <start> <end>")
    #     sys.exit(1)

    # events = get_events(sys.argv[1], sys.argv[2])
    events = get_events(0, datetime_to_seconds_since_epoch(datetime.datetime.now()))
    display_daily_term(events)
