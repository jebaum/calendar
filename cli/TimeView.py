from ScrollableView import *

import time
import datetime
import calendar

class TimeView(ScrollableView):
  def __init__(self):
    super(TimeView, self).__init__()
    self.view_header = "Init"
    self.get_month_period(datetime.datetime.now())

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