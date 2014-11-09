var request = require('superagent');

var API = {
  get: function(params, callback, error) {
    params = params || {};

    request
      .get('/events')
      .query({
        start_date: params.startDate,
        end_date: params.endDate,
      })
      .end(function(res) {
        if (res.ok) {
          if (callback) {
            callback(res.text);
          }
        } else {
          console.error('Request error: ' + res.text);
          if (error) {
            error(res.text);
          }
        }
      });
  }
}

module.exports = API;
