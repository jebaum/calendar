var moment = require('moment-range');

var DateUtil = {
  getYear: function(date) {
    date = date || moment();
    return moment().range(
      moment(date).startOf('year'),
      moment(date).endOf('year')
    );
  },

  getMonth: function(date) {
    date = date || moment();
    return moment().range(
      moment(date).startOf('month'),
      moment(date).endOf('month')
    );
  },

  getWeek: function(date) {
    date = date || moment();
    return moment().range(
      moment(date).startOf('week'),
      moment(date).endOf('week')
    );
  },

  getDay: function(date) {
    date = date || moment();
    return moment().range(
      moment(date).startOf('day'),
      moment(date).endOf('day')
    );
  }
}

module.exports = DateUtil;
