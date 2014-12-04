import curses
from curses.textpad import Textbox, rectangle

class CommandView(object):
  def __init__(self, stdscr):
    self.stdscr = stdscr

    self.x = 0
    self.y = 0
    self.w = 0
    self.h = 0

    self.textwin = None
    self.textbox = None

  def set_pos(self, x, y):
    self.x = x
    self.y = y
    self.update_textbox()

  def set_size(self, w, h):
    self.w = w
    self.h = h
    self.update_textbox()

  def update_textbox(self):
    self.textwin, self.textbox = self.make_textbox(self.x,self.y,self.w,self.h)

  def make_textbox(self,x,y,w,h):
    nw = curses.newwin(h,w,y,x)
    textbox = curses.textpad.Textbox(nw)
    nw.attron(0)
    return nw,textbox

  def display_response(self):
    self.stdscr.addstr(self.y,self.x,self.cmd)
    self.stdscr.refresh()

  def edit(self):
    self.update_textbox()

    # rectangle(self.stdscr,self.maxh-5,0,self.maxh-2, self.maxw-1)
    # self.stdscr.addstr(self.maxh-4,2,'Ctrl-H Delete')
    # self.stdscr.addstr(self.maxh-3,2,'Ctrl-G Enter Command')
    # self.stdscr.addstr(self.maxh-1,0,': ')
    # self.stdscr.refresh()

    # Clear from last command
    self.textwin.erase()
    self.textwin.refresh()

    # Edit and save command
    self.textbox.edit()
    self.cmd = self.textbox.gather()