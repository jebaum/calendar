var bodyParser = require('body-parser')
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
  {
    name: 'event3',
    start: moment().endOf('week').subtract(32, 'hours').unix(),
    end: moment().endOf('week').subtract(18, 'hours').unix(),
  },
  {
    name: 'event4',
    start: moment().startOf('week').add(2, 'days').unix(),
    end: moment().startOf('week').add(3, 'days').unix(),
  }
]

app.use('/build', express.static('./build/'));
app.use(bodyParser.json());

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

app.post('/events', function (req, res) {
  events = req.body.events;
  res.send(JSON.stringify(events));
})

var server = app.listen(4567, function () {

  var host = server.address().address
  var port = server.address().port

  console.log('Listening at http://%s:%s', host, port)
})
