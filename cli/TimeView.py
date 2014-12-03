from ScrollableView import *

import time
import datetime
import calendar

class TimeView(ScrollableView):
  def __init__(self):
    super(TimeView, self).__init__()
    self.view_header = "Init"

    self.header_format = "%B %d %Y, %H:%M:%S"
    self.periodSize = datetime.timedelta(days = 1)
    self.periodOffset = 0

  @property
  def now(self):
    return datetime.datetime.now()

  def get_view_header(self):
    return self.get_period()[0].strftime(self.header_format)

  def get_period(self):
    # Return period adjusted with self.periodOffset
    start_dt,end_dt = self.get_day_period(self.now)
    start_dt += self.periodOffset*self.periodSize
    end_dt += self.periodOffset*self.periodSize
    return (start_dt,end_dt)

  def change_time_period(self, amount):
    self.periodOffset += amount
    self.needs_update = True

  def get_day_period(self, dt):
    # 00:00 to 24:00
    start_dt = dt - datetime.timedelta(hours = dt.hour, minutes = dt.minute, seconds = dt.second)
    end_dt = start_dt + datetime.timedelta(hours = 24)
    return (start_dt,end_dt)

  def get_week_period(self, dt):
    # Sunday to Saturday
    start_day_dt = self.get_day_period(dt)[0]
    start_dt = start_day_dt - datetime.timedelta(days = start_day_dt.weekday()+1)
    end_dt = start_dt + datetime.timedelta(days = 6)
    return (start_dt,end_dt)

  def get_month_period(self, dt):
    # Dec 1 to Dec 31
    start_day_dt = self.get_day_period(dt)[0]
    start_dt = start_day_dt - datetime.timedelta(days = start_day_dt.day - 1)
    end_dt = start_dt + datetime.timedelta(days = calendar.monthrange(start_day_dt.year, start_day_dt.month)[1])
    return (start_dt,end_dt)