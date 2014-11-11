
var DateUtil = {
  getYear: function() {
    var today = new Date();

    var year = today.getUTCDate();

    return {
      start: new Date(year - 1),
      end: new Date(year),
    }
  },

  getMonth: function() {
    var today = new Date();

    var year = today.getUTCFullYear();
    var month = today.getUTCMonth();

    return {
      start: new Date(year, month),
      end: new Date(year, month + 1),
    }
  },

  getWeek: function() {
    var today = new Date();

    var year = today.getUTCFullYear();
    var month = today.getUTCMonth();

    var dayOfWeek = today.getUTCDay();
    var dayOfMonth = today.getUTCDate();

    var lastSunday = dayOfMonth - dayOfWeek;

    return {
      start: new Date(year, month, lastSunday),
      end: new Date(year, month, lastSunday + 7),
    }
  },

  getDay: function() {
    var today = new Date();

    var year = today.getUTCFullYear();
    var month = today.getUTCMonth();

    var dayOfMonth = today.getUTCDate();

    return {
      start: new Date(year, month, dayOfMonth),
      end: new Date(year, month, dayOfMonth + 1),
    }
  }
}

module.exports = DateUtil;
