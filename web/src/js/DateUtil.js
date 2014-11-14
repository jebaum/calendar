var moment = require('moment-range');

var DateUtil = {
  getYear: function() {
    return moment().range(
      moment().startOf('year'),
      moment().endOf('year')
    );
  },

  getMonth: function() {
    return moment().range(
      moment().startOf('month'),
      moment().endOf('month')
    );
  },

  getWeek: function() {
    return moment().range(
      moment().startOf('week'),
      moment().endOf('week')
    );
  },

  getDay: function() {
    return moment().range(
      moment().startOf('day'),
      moment().endOf('day')
    );
  }
}

module.exports = DateUtil;
