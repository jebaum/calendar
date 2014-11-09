var Dispatcher = require('./Dispatcher');
var _ = require('underscore');
// var Store = require('./Store');

function CalendarStore() {
  this.title = 'year';
  this.listeners = [];
}

CalendarStore.prototype = {
  get: function() {
    return this.title;
  },

  set: function(t) {
    this.title = t;
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
Dispatcher.register('SET_CALENDAR_TITLE', calendarStore.set.bind(calendarStore));

module.exports = calendarStore;
