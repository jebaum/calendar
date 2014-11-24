import sys, json, datetime, time, traceback
import curses
import requests
from .event import Event

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

def get_events_file(filename):
    events = []
    with open(filename, 'r') as f:
        j = json.load(f)
        for event in j:
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


def hour_string(hour, minute):
    hs = str(hour).rjust(2, '0')
    ms = str(minute).rjust(2, '0')
    return '%s:%s' % (hs, ms)

class HourPad():
    def __init__(self, hour, screen_width):
        self.initialize(hour, 2, screen_width)
        self.events = []

    def initialize(self, hour, height, width):
        self.h = height
        self.w = width
        self.hour = hour

        # Add extra for last hour to compensate for bottom border
        if self.hour == 23:
            self.h += 1

        # Create pad
        self.pad = curses.newpad(self.h, self.w)
        self.add_border()

        # Add hour to pad
        self.pad.addstr(1,1,hour_string(self.hour, 0))

    def add_border(self):
        # Special border on top for 1st
        tl = tr = curses.ACS_VLINE
        if self.hour == 0:
            tl = curses.ACS_ULCORNER
            tr = curses.ACS_URCORNER

        # Special border onbottom for last
        bl = br = curses.ACS_VLINE
        b = ' '
        if self.hour == 23:
            bl = curses.ACS_LLCORNER
            br = curses.ACS_LRCORNER
            b = curses.ACS_HLINE

        # characters for sides, corners borders
        # ls, rs, ts, bs, tl, tr, bl, br
        self.pad.border(curses.ACS_VLINE, curses.ACS_VLINE, 0, b, tl, tr, bl, br)

    def resize(self, h, w):
        self.initialize(self.hour, h, w)

    def draw(self, y, x):
        self.draw_events()
        # Display from ul corner (arg 3, arg 4) to br corner (arg 5, arg 6) showing ul corner of pad (arg 1, arg 2)
        self.pad.refresh(0, 0,
                         y, x,
                         y+self.h, x+self.w)

    def draw_events(self):
        for i in range(len(self.events)):
            e = self.events[i]
            y = i*2+1
            self.pad.addstr(y, 7, hour_string(e.hour, e.minute))
            self.pad.addstr(y, 13, e.title[0:self.w])
            self.pad.addstr(y+1, 13, e.description[0:self.w])


    def add_event(self, event):
        self.events.append(event)
        self.resize(2+len(self.events)*2, self.w)


class CursesView():
    def __init__(self):
        self.stdscr = curses.initscr()
        self.stdscr.nodelay(1)
        curses.noecho()
        curses.cbreak()
        curses.curs_set(0)

        self.h, self.w = self.stdscr.getmaxyx()
        self.offset = 0

        self.messages = []

    def cleanup(self):
        curses.nocbreak()
        curses.echo()
        curses.endwin()
        for m in self.messages:
            print(m)

    def refresh_pads(self, pads):
        y = -self.offset
        for i in range(24):
            p = pads[i]
            if y >= 0 and y+p.h < self.h-self.summary_h:
                p.draw(y,0)
            y += p.h

    def total_hour_pad_height(self):
        sum = 0
        for p in self.hour_pads:
            sum += p.h
        return sum

    def add_events(self,all_events):
        for e in all_events:
            self.hour_pads[e.hour].add_event(e)

    def initialize_summary(self):
        h = 2*len(self.events)+2
        self.summary_win = curses.newwin(h, self.w, self.h-h, 0)
        self.summary_h = h

    def draw_summary(self):
        for i in range(len(self.events)):
            e = self.events[i]
            self.summary_win.addstr(i*2+1, 0, hour_string(e.hour, e.minute))
            self.summary_win.addstr(i*2+1, 6, '-')
            self.summary_win.addstr(i*2+1, 8, hour_string(e.endDate.hour, e.endDate.minute))
            self.summary_win.addstr(i*2+1, 14, e.title)
            self.summary_win.addstr(i*2+2, 14, e.description)

        self.summary_win.border(' ', ' ', curses.ACS_HLINE, ' ', ' ', ' ', ' ', ' ')
        self.summary_win.refresh()

    def display_daily(self, all_events):
        self.events = all_events
        self.initialize_summary()

        # Construct hour windows
        self.hour_pads = []
        for hour in range(0,24):
            pad = HourPad(hour, self.w)
            self.hour_pads.append(pad)

        self.add_events(all_events)


        while True:
            self.refresh_pads(self.hour_pads)
            self.draw_summary()
            c = self.stdscr.getch()
            if c == ord('q'):
                # Exit
                break
            elif c == ord('j'):
                # Move down
                self.offset += 1
                self.offset = min(self.total_hour_pad_height()-(self.h-self.summary_h-10), self.offset)
                self.stdscr.clear()
                self.stdscr.refresh()
            elif c == ord('k'):
                # Move up
                self.offset -= 1
                self.offset = max(0,self.offset)
                self.stdscr.clear()
                self.stdscr.refresh()
            # move cursor for mac os x
            curses.setsyx(0,0)
            curses.doupdate()
            time.sleep(0.01)