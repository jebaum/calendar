var moment = require('moment');

var DateUtil = {
  getYear: function() {
    return {
      start: moment.startOf('year'),
      end: moment().endOf('year'),
    };
  },

  getMonth: function() {
    return {
      start: moment().startOf('month'),
      end: moment().endOf('month'),
    };
  },

  getWeek: function() {
    return {
      start: moment().startOf('week'),
      end: moment().endOf('week'),
    };
  },

  getDay: function() {
    return {
      start: moment().startOf('day'),
      end: moment().endOf('day'),
    };
  }
}

module.exports = DateUtil;
