import curses
from ScrollableView import *
from Event import Event

class ListView(ScrollableView):
  def __init__(self):
    super(ListView, self).__init__()
    self.event_lists = []
    for i in range(20):
      self.event_lists.append([])

    self.box_header_width = 0
    self.view_header = ""

  def set_content_size(self, w, h):
    # Width defined by argument
    self.content_w = w

    # Height based on list size
    top_border_h = 1
    separator_border_h = 1
    item_heights = [self.height_for_event_list(x)+separator_border_h for x in self.event_lists]
    self.content_h = top_border_h + sum(item_heights)

    # Resize pad based on content, mark as needed for update
    self.pad.resize(self.content_h,self.content_w+1)
    self.needs_update = True

  def add_event(self, i, e):
    self.event_lists[i].append(e)
    # Update height
    self.set_content_size(self.content_w,0)

  def height_for_event_list(self, el):
    return max(1,2 * len(el))

  def update(self):
    self.draw_top_border()
    self.draw_view_header()

    yoff = 1
    l = len(self.event_lists)
    for i in range(l):
      self.draw_box(yoff, self.event_lists[i],i)
      if i != l-1:
        self.draw_separator(yoff+self.height_for_event_list(self.event_lists[i]))
      yoff += self.height_for_event_list(self.event_lists[i])+1

    self.draw_bottom_border()

  # 
  # DRAWING FUNCTIONS
  #
  def draw_box(self, y, events, index):
    h = self.height_for_event_list(events)
    xoff = self.box_header_width + 1
    
    # Draw side borders
    self.draw_item_border(y,h)

    # Draw header
    self.draw_box_header(y,index)

    # Draw events
    for i in range(len(events)):
      self.draw_event(y+i*2,xoff,events[i])

  def draw_box_header(self,y,index):
    pass

  def draw_event(self,y,x,e):
    # [startTime - endTime] [title] [(category)] [@location]
    #                       [description]
    startTimeStr = str(e.startHour).rjust(2,'0') + ":" + str(e.startMinute).rjust(2,'0')
    endTimeStr = str(e.endHour).rjust(2,'0') + ":" + str(e.endMinute).rjust(2,'0')
    timeStr = "%s - %s" % (startTimeStr, endTimeStr)

    # Start - End
    xoff = x
    self.pad.addstr(y,xoff+1, timeStr)
    xoff += len(timeStr)+2

    # Title, Description
    titleStr = str(e.title)
    descriptionStr = str(e.description)
    self.pad.addstr(y,xoff,titleStr)
    self.pad.addstr(y+1,xoff,descriptionStr)

    # Category
    xoff += len(titleStr)+1
    categoryStr = "(%s)" % (e.category)
    self.pad.addstr(y,xoff,categoryStr)

    # Location
    xoff += len(categoryStr)+1
    locationStr = "@%s" % (e.location)
    self.pad.addstr(y,xoff,locationStr)

    # ID
    idStr = "id: %s" % (e.id)
    xoff = self.content_w-1-len(idStr)
    self.pad.addstr(y,xoff,idStr)

  def draw_view_header(self):
    self.pad.addstr(0,3,self.view_header)

  #
  # BORDER FUNCTIONS
  #
  def draw_item_border(self,y,h):
    # Right vertical line
    for i in range(h):
      self.pad.addch(y+i,0,curses.ACS_VLINE)
    # Left vertical line
    for i in range(h):
      self.pad.addch(y+i, self.content_w-1,curses.ACS_VLINE)

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