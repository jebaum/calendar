
var DateUtil = {
  getLastYear: function() {
    var today = new Date();

    var year = today.getUTCDate();

    return {
      start: new Date(year - 1),
      end: new Date(year),
    }
  },

  getLastMonth: function() {
    var today = new Date();

    var year = today.getUTCFullYear();
    var month = today.getUTCMonth();

    return {
      start: new Date(year, month - 1),
      end: new Date(year, month),
    }
  },

  getLastWeek: function() {
    var today = new Date();

    var year = today.getUTCFullYear();
    var month = today.getUTCMonth();

    var dayOfWeek = today.getUTCDay();
    var dayOfMonth = today.getUTCDate();

    var lastSunday = dayOfMonth - dayOfWeek;

    return {
      start: new Date(year, month, lastSunday - 7),
      end: new Date(year, month, lastSunday),
    }
  },

  getLastDay: function() {
    var today = new Date();

    var year = today.getUTCFullYear();
    var month = today.getUTCMonth();

    var dayOfMonth = today.getUTCDate();

    return {
      start: new Date(year, month, dayOfMonth - 1),
      end: new Date(year, month, dayOfMonth),
    }
  }
}

module.exports = DateUtil;
