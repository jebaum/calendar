var API = require('./API');
var Dispatcher = require('./Dispatcher');
var moment = require('moment-range');
var _ = require('underscore');

var _events = [];
var _listeners = [];

function syncEvents() {
  if (_events.length === 0) {
    API.get(
      null,
      function(apiEvents) {
        return addEvents(_.map(apiEvents, eventTranslator));
      },
      console.error
    );
  } else {
    API.post(
      {events: translateEvents(_events)},
      console.log,
      console.error
    );
  }
}

function getEvents() {
  syncEvents();
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
    description: apiEvent.description,
    range: moment().range(
      moment(apiEvent.start * 1000),
      moment(apiEvent.end * 1000)
    ),
  }
}

function translateEvents(events) {
  return _.map(
    events,
    function(event) {
      return {
        name: event.name,
        description: event.description,
        start: event.range.start.unix(),
        end: event.range.end.unix(),
      };
  });
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
