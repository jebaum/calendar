var API = require('./API');
var DateUtil = require('./DateUtil');
var Dispatcher = require('./Dispatcher');
var moment = require('moment-range');
var _ = require('underscore');

function CalendarStore() {
  this.mode = 'month';
  this.date = moment();
  this.listeners = [];
}

CalendarStore.prototype = {
  getMode: function() {
    return this.mode;
  },

  setMode: function(m) {
    this.mode = m;
    this.notify();
  },

  getDate: function() {
    return this.date;
  },

  setDate: function(d) {
    this.date = d;
    this.notify();
  },

  modifyDate: function(delta) {
    switch (this.mode) {
      case 'month':
        this.date.add(delta, 'months');
        break;
      case 'week':
        this.date.add(delta, 'weeks');
        break;
      case 'day':
        this.date.add(delta, 'days');
        break;
      default:
        console.error('attempt to modify date with mode %s', this.mode);
    }
    this.notify();
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
Dispatcher.register('SET_CALENDAR', calendarStore.setMode.bind(calendarStore));
Dispatcher.register('SET_DATE', calendarStore.setDate.bind(calendarStore));
Dispatcher.register('SET_DATE_FORWARD', function() {
  calendarStore.modifyDate(1);
});
Dispatcher.register('SET_DATE_BACKWARD', function() {
  calendarStore.modifyDate(-1);
});

module.exports = calendarStore;
