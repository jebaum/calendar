var request = require('superagent');

var API = {
  get: function(params, callback, error) {
    params = params || {};

    request
      .get('/events')
      .query(params)
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
  },

  post: function(params, callback, error) {
    params = params || {};

    request
      .post('/events')
      .send(params)
      .end(function(res) {
        if (res.ok) {
          if (callback) {
            callback(JSON.parse(res.text));
          }
        } else {
          console.error('Failed to POST');
          if (error) {
            error(res.text);
          }
        }
      });
  }
}

module.exports = API;
