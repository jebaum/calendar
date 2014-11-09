var express = require('express')
var fs = require('fs');
var app = express()

app.get('/', function (req, res) {
  fs.readFile('index.html', 'utf-8', function(err, data) {
  	if (err) {
  		throw err;
  	} else {
  		res.send(data);
  	}
  })
});

app.get('/build/bundle.js', function (req, res) {
  fs.readFile('build/bundle.js', 'utf-8', function(err, data) {
  	if (err) {
  		throw err;
  	} else {
  		res.send(data);
  	}
  })
});

app.get('/build/style.css', function (req, res) {
  fs.readFile('build/style.css', 'utf-8', function(err, data) {
  	if (err) {
  		throw err;
  	} else {
  		res.send(data);
  	}
  })
});

app.get('/events', function (req, res) {
	var startDate = req.query.start_date;
	var endDate = req.query.end_date;
  var e = {
  	startDate: startDate,
  	endDate: endDate,
  };

  res.send(JSON.stringify(e));
});

var server = app.listen(4567, function () {

  var host = server.address().address
  var port = server.address().port

  console.log('Listening at http://%s:%s', host, port)

})