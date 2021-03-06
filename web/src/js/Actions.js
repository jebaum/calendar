var Dispatcher = require('./Dispatcher');
var moment = require('moment-range');

var Actions = {
  setCalendar: function(title) {
    Dispatcher.dispatch('SET_CALENDAR', title);
  },

  setDate: function(date) {
    Dispatcher.dispatch('SET_DATE', date || moment());
  },

  setDateForward: function() {
    Dispatcher.dispatch('SET_DATE_FORWARD');
  },

  setDateBackward: function() {
    Dispatcher.dispatch('SET_DATE_BACKWARD');
  },

  addEvent: function(event) {
    Dispatcher.dispatch('ADD_EVENT', event);
  },

  setEvent: function(event) {
    Dispatcher.dispatch('SET_EVENT', event);
  },
};

module.exports = Actions;
