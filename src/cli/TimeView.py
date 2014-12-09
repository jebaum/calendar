from ScrollableView import *

import time
import datetime
import calendar

class TimeView(ScrollableView):
  def __init__(self):
    super(TimeView, self).__init__()
    self.view_header = "Init"

    self.periodOffset = 0

    # Options for subclasses
    self.header_format = "%B %d %Y, %H:%M:%S"
    self.get_period_func = self.get_month_period
    self.offset_period_func = self.apply_month_offset

  @property
  def now(self):
    return datetime.datetime.now()

  @property
  def period(self):
    return self.offset_period_func(self.get_period_func(self.now))

  def get_view_header(self):
    return self.period[0].strftime(self.header_format) + " - " + self.period[1].strftime(self.header_format)

  def change_time_period(self, amount):
    self.periodOffset += amount
    self.needs_update = True

  # 
  # Get period contatining dt methods
  #
  def get_day_period(self, dt):
    # 00:00 to 24:00
    start_dt = dt - datetime.timedelta(hours = dt.hour, minutes = dt.minute, seconds = dt.second)
    end_dt = start_dt + datetime.timedelta(hours = 24)
    return (start_dt,end_dt)

  def get_week_period(self, dt):
    # Sunday to Saturday
    start_day_dt = self.get_day_period(dt)[0]
    start_dt = start_day_dt - datetime.timedelta(days = start_day_dt.weekday()+1)
    end_dt = start_dt + datetime.timedelta(days = 7)
    return (start_dt,end_dt)

  def get_month_period(self, dt):
    # Dec 1 to Dec 31
    start_day_dt = self.get_day_period(dt)[0]
    start_dt = start_day_dt - datetime.timedelta(days = start_day_dt.day - 1)
    end_dt = start_dt + datetime.timedelta(days = calendar.monthrange(start_day_dt.year, start_day_dt.month)[1])
    return (start_dt,end_dt)

  #
  # Apply period offset methods
  # 
  def apply_day_offset(self, periodTuple):
    periodSize = datetime.timedelta(days = 1)
    start_dt,end_dt = periodTuple
    start_dt += self.periodOffset*periodSize
    end_dt += self.periodOffset*periodSize
    return (start_dt,end_dt)

  def apply_week_offset(self, periodTuple):
    periodSize = datetime.timedelta(days = 7)
    start_dt,end_dt = periodTuple
    start_dt += self.periodOffset*periodSize
    end_dt += self.periodOffset*periodSize
    return (start_dt,end_dt)

  def apply_month_offset(self, periodTuple):
    start_dt,end_dt = periodTuple
    sign = lambda x: 1 if x >= 0 else -1

    if self.periodOffset >= 0:
      for i in range(abs(self.periodOffset)):
        start_dt += datetime.timedelta(days = calendar.monthrange(start_dt.year, (start_dt.month-1)%12+1)[1])
        end_dt += datetime.timedelta(days = calendar.monthrange(end_dt.year, (end_dt.month-1)%12+1)[1])
    else:
      for i in range(abs(self.periodOffset)):
        start_dt -= datetime.timedelta(days = calendar.monthrange(start_dt.year, (start_dt.month-2)%12+1)[1])
        end_dt -= datetime.timedelta(days = calendar.monthrange(end_dt.year, (end_dt.month-2)%12+1)[1])

    # start_dt += self.periodOffset*periodSize
    # end_dt += self.periodOffset*periodSize
    return (start_dt,end_dt)