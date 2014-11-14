var request = require('superagent');

var API = {
  get: function(params, callback, error) {
    params = params || {};

    request
      .get('/events')
      .query({
        start_date: params.startDate.unix(),
        end_date: params.endDate.unix(),
      })
      .end(function(res) {
        if (res.ok) {
          if (callback) {
            callback(JSON.parse(res.text));
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
