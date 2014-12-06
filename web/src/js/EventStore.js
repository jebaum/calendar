var API = require('./API');
var Dispatcher = require('./Dispatcher');
var moment = require('moment-range');
var _ = require('underscore');

var _events = [];
var _listeners = [];

function getEvents() {
  if (_events.length === 0) {
    API.get(
      {
        startDate: moment(),
        endDate: moment(),
      },
      function(apiEvents) {
        return addEvents(_.map(apiEvents, eventTranslator));
      },
      console.error
    );
  }

  return _events;
}

function addEvents(events) {
  _.each(events, function(event) {
    event.id = _events.length;
    _events.push(event);
  });

  notify();
}

function clearEvents() {
  _events = [];
  notify();
}

function setEvent(event) {
  _events[event.id] = event;
  notify();
}

function addChangeListener(callback) {
  _listeners.push(callback);
}

function notify() {
  _listeners.forEach(function(callback) {
    callback();
  });
}

function eventTranslator(apiEvent) {
  return {
    name: apiEvent.name,
    range: moment().range(
      moment(apiEvent.start * 1000),
      moment(apiEvent.end * 1000)
    ),
  }
}

Dispatcher.register('ADD_EVENT', function(event) {
  addEvents([event]);
});
Dispatcher.register('SET_EVENT', setEvent);

module.exports = {
  getEvents: getEvents,
  addEvents: addEvents,
  addChangeListener: addChangeListener,
  notify: notify,
};
