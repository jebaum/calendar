var Dispatcher = require('./Dispatcher');

var Actions = {
  setCalendarTitle: function(title) {
    Dispatcher.dispatch('SET_CALENDAR_TITLE', title);
  }
};

module.exports = Actions;
