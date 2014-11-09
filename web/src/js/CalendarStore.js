var API = require('./API');
var DateUtil = require('./DateUtil');
var Dispatcher = require('./Dispatcher');
var _ = require('underscore');

function CalendarStore() {
  this.text = 'sample text';
  this.title = 'default';
  this.listeners = [];
}

CalendarStore.prototype = {
  getText: function() {
    return this.text;
  },

  setText: function(t) {
    this.text = t;
    this.notify();
  },

  getTitle: function() {
    return this.title;
  },

  setTitle: function(t) {
    this.title = t;

    var dateRange;
    switch(t) {
      case 'year':
        dateRange = DateUtil.getLastYear();
        break;
      case 'month':
        dateRange = DateUtil.getLastMonth();
        break;
      case 'week':
        dateRange = DateUtil.getLastWeek();
        break;
      case 'day':
        dateRange = DateUtil.getLastDay();
        break;
      default:
        console.error('invalid calendar title');
    }

    API.get(
      {
        startDate: dateRange.start.valueOf(),
        endDate: dateRange.end.valueOf(),
      },
      this.setText.bind(this),
      console.error
    );
  },

  addChangeListener: function(callback) {
    this.listeners.unshift(callback);
  },

  notify: function() {
    this.listeners.forEach(function(callback) {
      callback();
    });
  }
}

var calendarStore = new CalendarStore();
Dispatcher.register('SET_CALENDAR', calendarStore.setTitle.bind(calendarStore));

module.exports = calendarStore;
