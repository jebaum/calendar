import curses
from curses.textpad import Textbox, rectangle
import re

class CommandView(object):
  def __init__(self, stdscr, EventStore):
    self.stdscr = stdscr
    self.EventStore = EventStore

    self.x = 0
    self.y = 0
    self.w = 0
    self.h = 0

    self.textwin = None
    self.textbox = None

    self.cmd = ""
    self.response = ""

  def set_pos(self, x, y):
    self.x = x
    self.y = y
    self.update_textbox()

  def set_size(self, w, h):
    self.w = w
    self.h = h
    self.update_textbox()

  def update_textbox(self):
    self.textwin, self.textbox = self.make_textbox(self.x+2,self.y,self.w,self.h)

  def make_textbox(self,x,y,w,h):
    nw = curses.newwin(h,w,y,x)
    textbox = curses.textpad.Textbox(nw)
    nw.attron(0)
    return nw,textbox

  def display_response(self):
    self.stdscr.addstr(self.y,self.x,str(self.response))
    self.stdscr.refresh()

  def apply_command(self):
    if 'filter' in self.cmd:
      match = re.match(r'filter (.*)=(.*)', self.cmd, re.DOTALL)
      if match == None:
        self.response = 'Filter not recognized, must be format key=xxx'
        return
      self.response = self.EventStore.add_filter(match.group(1).strip(), match.group(2).strip())
    elif 'update' in self.cmd:
      pass
    elif 'delete' in self.cmd:
      match = re.match(r'delete id=(.*)', self.cmd, re.DOTALL)
      if match == None:
        self.response = 'ID not recognized, must be format "delete id=xxx"'
        return
      self.response = self.EventStore.delete_event(match.group(1).strip())

  def draw_help(self):
    rectangle(self.stdscr,self.y-4,self.x,self.y-1, self.w)
    self.stdscr.addstr(self.y-3,self.x+2,'Ctrl-H Delete')
    self.stdscr.addstr(self.y-2,self.x+2,'Ctrl-G Enter Command')

  def validate(self, ch):
    if ch == ord(curses.erasechar()):
      (y,x) = self.textwin.getyx()
      self.textwin.move(y,x-1)
      self.textwin.delch()
    return ch

  def edit(self):
    self.update_textbox()

    # Clear from last command
    self.textwin.erase()
    self.textwin.refresh()

    # self.draw_help()
    self.stdscr.addstr(self.y,self.x,': ')
    self.stdscr.refresh()

    # Edit and save command
    self.textbox.edit(self.validate)
    self.cmd = self.textbox.gather()
    self.response = self.cmd
    self.apply_command()