import sys
import curses
import traceback
import time
import signal
import random

from ListView import *
from DailyView import *
from WeeklyView import *
from MonthlyView import *
from SummaryView import *
from CommandView import *
from event import *
from eventstore import *

global resize_flag
def handle_sigwinch(signum, frame):
  global resize_flag
  resize_flag = True

signal.signal(signal.SIGWINCH, handle_sigwinch)

class NcursesDisplay:
  def __init__(self):
    pass

  def setup_curses(self):
    # main window, stdscr
    self.stdscr = curses.initscr()
    # self.stdscr.nodelay(1)
    curses.halfdelay(1)
    self.stdscr.refresh()

    # Height, width of terminal window
    self.h, self.w = self.stdscr.getmaxyx()

    # Ncurses settings
    curses.noecho()
    curses.cbreak()
    # Disable cursor if possible
    if curses.tigetstr("cnorm"):
      curses.curs_set(0)

    global resize_flag
    resize_flag = False

  def cleanup_curses(self):
    # End curses session to prevent window from breaking on error
    curses.nocbreak()
    curses.echo()
    curses.endwin()

  def display(self):
    try:
      self.setup_curses()

      es = EventStore()
      self.DailyView = DailyView(es)
      self.WeeklyView = WeeklyView(es)
      self.MonthlyView = MonthlyView(es)

      self.ListView = self.DailyView
      self.CommandView = CommandView(self.stdscr)
      self.resize_views()

      global resize_flag

      while True:
        self.ListView.display()
        # self.CommandView.display()
        # curses.doupdate()
        
        c = self.stdscr.getch()

        if c == curses.KEY_RESIZE or resize_flag:
          self.cleanup_curses()
          self.setup_curses()
          self.resize_views()

        if c == ord('q'):
          # Exit
          break
        # HJKL Scrolling
        elif c == ord('h'):
          self.ListView.change_time_period(-1)
          self.refresh_views()
        elif c == ord('l'):
          self.ListView.change_time_period(1)
          self.refresh_views()
        elif c == ord('j'):
          self.ListView.scroll_y(1)
        elif c == ord('k'):
          self.ListView.scroll_y(-1)
        
        # Switch views with D/W/M
        elif c == ord('d'):
          self.ListView = self.DailyView
          self.refresh_views()
        elif c == ord('w'):
          self.ListView = self.WeeklyView
          self.refresh_views()
        elif c == ord('m'):
          self.ListView = self.MonthlyView
          self.refresh_views()

        # Enter command
        elif c == ord(':'):
          self.CommandView.edit()
          # Refresh to clear help box, apply any commands
          self.refresh_views()
          self.CommandView.display_response()

        # time.sleep(0.01)

      self.cleanup_curses()
    except Exception as e:
      # Safely exit on exception rather than damage terminal text rendering
      self.cleanup_curses()
      print(e)
      traceback.print_exc()
      sys.exit(1)

  def refresh_views(self):
    self.stdscr.erase()
    self.stdscr.refresh()
    self.DailyView.refresh()
    self.WeeklyView.refresh()
    self.MonthlyView.refresh()

  def resize_views(self):
      self.DailyView.set_view_position(0,0)
      self.DailyView.set_view_size(self.w-1,self.h-2)
      self.DailyView.set_content_size(self.w,self.h)
      
      self.WeeklyView.set_view_position(0,0)
      self.WeeklyView.set_view_size(self.w-1,self.h-2)
      self.WeeklyView.set_content_size(self.w,self.h)

      self.MonthlyView.set_view_position(0,0)
      self.MonthlyView.set_view_size(self.w-1,self.h-2)
      self.MonthlyView.set_content_size(self.w,self.h)

      self.CommandView.set_pos(0, self.h-1)
      self.CommandView.set_size(self.w-1,1)