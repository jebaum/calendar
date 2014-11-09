var Dispatcher = require('./Dispatcher');

var Actions = {
  setCalendar: function(title) {
    Dispatcher.dispatch('SET_CALENDAR', title);
  }
};

module.exports = Actions;
