import curses
from curses.textpad import Textbox, rectangle

class CommandView(object):
  def __init__(self, stdscr, maxw, maxh):
    self.stdscr = stdscr
    self.maxw = maxw
    self.maxh = maxh

    self.textwin, self.textbox = self.maketextbox(1,self.maxw-1,self.maxh-1,2)

  def maketextbox(self, h,w,y,x):
    nw = curses.newwin(h,w,y,x)
    textbox = curses.textpad.Textbox(nw)
    nw.attron(0)
    return nw,textbox

  def edit(self):
    self.stdscr.addstr(self.maxh-1,0,': ')
    self.stdscr.refresh()

    self.textbox.edit()
    cmd = self.textbox.gather()
    raise Exception(cmd)