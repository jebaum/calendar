var express = require('express')
var fs = require('fs');
var moment = require('moment');
var app = express();

var events = [
  {
    name: 'event1',
    start: moment().startOf('week').add(5, 'hours').unix(),
    end: moment().startOf('week').add(6, 'hours').unix(),
  },
  {
    name: 'event2',
    start: moment().endOf('week').subtract(20, 'hours').unix(),
    end: moment().endOf('week').subtract(12, 'hours').unix(),
  },
]

app.use('/build', express.static('./build/'));

app.get('/', function (req, res) {
  fs.readFile('index.html', 'utf-8', function(err, data) {
    if (err) {
      throw err;
    } else {
      res.send(data);
    }
  })
});

app.get('/events', function (req, res) {
  res.send(JSON.stringify(events));
});

var server = app.listen(4567, function () {

  var host = server.address().address
  var port = server.address().port

  console.log('Listening at http://%s:%s', host, port)
})
