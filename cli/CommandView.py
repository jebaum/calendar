import curses
from curses.textpad import Textbox, rectangle

class CommandView(object):
  def __init__(self, stdscr, maxw, maxh):
    self.stdscr = stdscr
    self.maxw = maxw
    self.maxh = maxh

    self.textwin, self.textbox = self.make_textbox(1,self.maxw-1,self.maxh-1,2)

  def make_textbox(self, h,w,y,x):
    nw = curses.newwin(h,w,y,x)
    textbox = curses.textpad.Textbox(nw)
    nw.attron(0)
    return nw,textbox

  def display_response(self):
    self.stdscr.addstr(self.maxh-1,0,self.cmd)
    self.stdscr.refresh()

  def edit(self):
    rectangle(self.stdscr,self.maxh-5,0,self.maxh-2, self.maxw-1)
    self.stdscr.addstr(self.maxh-4,2,'Ctrl-H Delete')
    self.stdscr.addstr(self.maxh-3,2,'Ctrl-G Enter Command')
    self.stdscr.addstr(self.maxh-1,0,': ')
    self.stdscr.refresh()

    # Clear from last command
    self.textwin.erase()
    self.textwin.refresh()

    # Edit and save command
    self.textbox.edit()
    self.cmd = self.textbox.gather()