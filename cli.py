import sys, json, datetime, time
import curses
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

    @property
    def minute(self):
        return self.startDate.minute
    
    @property
    def hour(self):
        return self.startDate.hour

    @property
    def day(self):
        return self.startDate.day

    @property
    def month(self):
        return self.startDate.month

    @property
    def year(self):
        return self.startDate.year

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
            if i == e.hour:
                event_time = '%s:%s' % (e.hour, e.minute)
                print(event_time.rjust(5, ' '), e.title)

def datetime_to_seconds_since_epoch(dt):
    return time.mktime(dt.timetuple())

class CursesView():
    def __init__(self):
        self.stdscr = curses.initscr()
        curses.noecho()
        curses.cbreak()

        self.h, self.w = self.stdscr.getmaxyx()

    def cleanup(self):
        curses.nocbreak()
        curses.echo()
        curses.endwin()

    def get_displayable_hours(self):
        """
        Determine maximum height for each hour block
        must be above 3
        adjust hour range to what can be displayed on screen
        return: hour_range, hour_height
        """
        hours = min(int(self.h/3), 24)
        hour_range = range(0,hours)
        hour_height = int(self.h/hours)

        return hour_range, hour_height

    def display_daily(self, all_events):
        hour_range, hour_height = self.get_displayable_hours()

        # Construct hour windows
        hour_wins = []
        for hour in hour_range:
            win_height = hour_height
            # Last gets an extra
            if hour == hour_range[-1]:
                win_height += 1
            win_width = self.w
            win_y = hour_height * hour
            win_x = 0
            win = curses.newwin(win_height, win_width, win_y, win_x)
            hour_wins.append(win)

        # Construct hour window borders
        # characters for sides, corners borders
        # ls, rs, ts, bs, tl, tr, bl, br
        for i in range(len(hour_wins)):
            w = hour_wins[i]
        
            # Special border on top for 1st
            tl = tr = curses.ACS_VLINE
            if i == 0:
                tl = curses.ACS_ULCORNER
                tr = curses.ACS_URCORNER
                
            # Special border on bottom for last
            bl = br = curses.ACS_VLINE
            b = ' ' 
            if i == len(hour_wins) - 1:
                bl = curses.ACS_LLCORNER
                br = curses.ACS_LRCORNER
                b = curses.ACS_HLINE
    
            w.border(curses.ACS_VLINE, curses.ACS_VLINE, 0, b, tl, tr, bl, br)

        # Write hour information
        for i in range(len(hour_wins)):
            w = hour_wins[i]    
            hour = "%s:00" % (i)
            hour = hour.rjust(5, '0')
            w.addstr(1,1,hour)

            for e in all_events:
                if i == e.startDate.hour:
                    w.addstr(1,7,e.title)
                    w.addstr(2,7,e.description)

            w.refresh()

        time.sleep(5)

if __name__ == '__main__':
    # if len(sys.argv) < 3:
    #     print("Usage: python cli.py <start> <end>")
    #     sys.exit(1)

    events = get_events(0, datetime_to_seconds_since_epoch(datetime.datetime.now()))
    c = CursesView()
    try:
        c.display_daily(events)
    except Exception as e:
        c.cleanup()
        print(e)
    finally:
        c.cleanup()

    # events = get_events(sys.argv[1], sys.argv[2])
    display_daily_term(events)
