import sys
import curses
import traceback

class NcursesDisplay:
  def __init__(self):
    pass

  def setup_curses(self):
    # main window, stdscr
    self.stdscr = curses.initscr()
    self.stdscr.nodelay(1)

    # Height, width of terminal window
    self.h, self.w = self.stdscr.getmaxyx()

    # Ncurses settings
    curses.noecho()
    curses.cbreak()
    # Disable cursor if possible
    if curses.tigetstr("cnorm"):
      curses.curs_set(0)

  def cleanup_curses(self):
    # End curses session to prevent window from breaking on error
    curses.nocbreak()
    curses.echo()
    curses.endwin()

  def display(self):
    try:
        self.setup_curses()
        self.cleanup_curses()
    except Exception as e:
        # Safely exit on exception rather than damage terminal text rendering
        self.cleanup_curses()
        print(e)
        traceback.print_exc()
        sys.exit(1)