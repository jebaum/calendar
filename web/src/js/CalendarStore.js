var API = require('./API');
var DateUtil = require('./DateUtil');
var Dispatcher = require('./Dispatcher');
var _ = require('underscore');

function CalendarStore() {
  this.text = 'sample text';
  this.title = 'month';
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
Dispatcher.register('SET_CALENDAR', calendarStore.setTitle.bind(calendarStore));

module.exports = calendarStore;
