import curses
from ScrollableView import *
from event import Event

class ListView(ScrollableView):
  def __init__(self):
    super(ListView, self).__init__()
    self.event_lists = []
    for i in range(20):
      self.event_lists.append([])

    self.box_header_width = 0

  def set_content_size(self, w, h):
    self.content_w = w
    self.content_h = 1 + len(self.event_lists) + sum([self.height_for_event_list(x) for x in self.event_lists])

    self.pad.resize(self.content_h,self.content_w+1)
    self.needs_update = True

  def update_size(self):
    self.set_content_size(self.content_w,self.content_h)

  def add_event(self, i, e):
    self.event_lists[i].append(e)
    self.update_size()

  def height_for_event_list(self, el):
    return max(1,2 * len(el))

  def draw_box(self, y, events):
    h = self.height_for_event_list(events)
    xoff = self.box_header_width + 1
    
    # Draw header
    self.draw_box_header(y)

    # Right vertical line
    for i in range(h):
      self.pad.addch(y+i,0,curses.ACS_VLINE)
    # Left vertical line
    for i in range(h):
      self.pad.addch(y+i, self.content_w-1,curses.ACS_VLINE)

    # Draw events
    for i in range(len(events)):
      e = events[i]
      # Start time 
      self.pad.addstr(y+i*2,xoff+1, str(e.hour).rjust(2,'0') + ":" + str(e.minute).rjust(2,'0'))
      self.pad.addstr(y+i*2,xoff+7,str(e.title))
      self.pad.addstr(y+1+i*2,xoff+7,str(e.description))

  def draw_box_header(self,y):
    pass

  def draw_separator(self,y):
    self.pad.addch(y,0,curses.ACS_LTEE)
    self.pad.addch(y,self.content_w-1,curses.ACS_RTEE)
    for i in range(1,self.content_w-1):
      self.pad.addch(y,i,curses.ACS_HLINE)

  def draw_top_border(self):
    y = 0
    self.pad.addch(y,0,curses.ACS_ULCORNER)
    self.pad.addch(y,self.content_w-1,curses.ACS_URCORNER)
    for i in range(1,self.content_w-1):
      self.pad.addch(y,i,curses.ACS_HLINE)

  def draw_bottom_border(self):
    y = self.content_h-1
    self.pad.addch(y,0,curses.ACS_LLCORNER)
    self.pad.addch(y,self.content_w-1,curses.ACS_LRCORNER)
    for i in range(1,self.content_w-1):
      self.pad.addch(y,i,curses.ACS_HLINE)

  def update(self):
    self.draw_top_border()

    yoff = 1
    l = len(self.event_lists)
    for i in range(l):
      self.draw_box(yoff, self.event_lists[i])
      if i != l-1:
        self.draw_separator(yoff+self.height_for_event_list(self.event_lists[i]))
      yoff += self.height_for_event_list(self.event_lists[i])+1

    self.draw_bottom_border()