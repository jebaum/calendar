'use strict';

var CalendarStore = require('./CalendarStore');
var DateUtil = require('./DateUtil');
var Event = require('./Event');
var EventStore = require('./EventStore');
var React = require('react');
var Paper = require('material-ui').Paper;
var PropTypes = require('react').PropTypes;
var StateFromStore = require('./StateFromStore');
var classSet = require('react/addons').addons.classSet;
var moment = require('moment-range');
var _ = require('underscore');

var Cell = React.createClass({
  render: function() {
    var classes = classSet({
      'cell': true,
      'cell-today': this.props.today,
      'cell-disabled': this.props.disabled,
    });
    return (
      <div className={classes}>
          {this.props.children}
      </div>
    );
  }
});

var Calendar = React.createClass({
  mixins: [StateFromStore({
    mode: {
      store: CalendarStore,
      fetch: function(store, fetchParams) {
        return store.getMode();
      }
    },
    date: {
      store: CalendarStore,
      fetch: function(store, fetchParams) {
        return store.getDate();
      }
    },
    events: {
      store: EventStore,
      fetch: function(store, fetchParams) {
        return store.getEvents();
      }
    }
  })],

  propTypes: {
    type: PropTypes.oneOf(['day', 'week', 'month', 'year']),
  },

  renderDay: function(events, date) {
    var dayRange = DateUtil.getDay(date);
    var eventBlocks = _.map(
      _.filter(events, function(event) {
        return dayRange.overlaps(event.range);
      }),
      function(event) {
        return <Event event={event} range={dayRange} />;
    });

    return (
      <div>
        <h1>{date.format('dddd, MMMM Do')}</h1>
        <div className="calendar-wrapper calendar-wrapper-day">
          <Paper rounded={false}>
            <div className="calendar">
              <Cell>
                {dayRange.start.date()}
                {eventBlocks}
              </Cell>
            </div>
          </Paper>
        </div>
      </div>
    );
  },

  renderWeek: function(events, date) {
    var range = DateUtil.getWeek(date);
    var cells = [];
    range.by('days', function(day) {
      var dayRange = moment().range(day, moment(day).add(1, 'days'));
      var eventBlocks = _.map(
        _.filter(events, function(event) {
          return dayRange.overlaps(event.range);
        }),
        function(event) {
          return <Event event={event} range={dayRange} />;
      });

      var isToday = day.date() === moment().date();
      var isDisabled = day.month() !== moment().month();
      var key = '' + day.date() + '-' + day.month();
      cells.push(
        <Cell today={isToday} disabled={isDisabled} key={key}>
          {day.date()}
          {eventBlocks}
        </Cell>
      );
    });

    return (
      <div>
        <h1>{range.start.format('MMMM Do') + ' - ' + range.end.format('Do')}</h1>
        <div className="calendar-wrapper calendar-wrapper-week">
          <Paper rounded={false}>
            <div className="calendar">
              {cells}
            </div>
          </Paper>
        </div>
      </div>
    );
  },

  renderMonth: function(events, date) {
    var range = DateUtil.getMonth(date);
    var fullRange = moment().range(
      range.start.startOf('week'),
      range.end.endOf('week')
    );

    var cells = [];
    var i = 0;
    fullRange.by('day', function(day) {
      var isToday =
        (day.date() === moment().date() && day.month() === moment().month());
      var isDisabled = day.month() !== date.month();
      var key = '' + day.date() + '-' + day.month();
      cells.push((
        <Cell today={isToday} disabled={isDisabled} key={key}>
          {day.date()}
        </Cell>
      ));
      i++;
      if (i === 7) {
        cells.push(<br />);
        i = 0;
      }
    });

    return (
      <div>
        <h1>{date.format('MMMM')}</h1>
        <div className="calendar-wrapper calendar-wrapper-month">
          <Paper rounded={false}>
            <div className="calendar">
              {cells}
            </div>
          </Paper>
        </div>
      </div>
    );
  },

  render: function() {
    var mode = this.state.mode;
    var events = this.state.events;
    var date = this.state.date;
    switch (mode) {
      case 'month':
        return this.renderMonth(events, date);
      case 'week':
        return this.renderWeek(events, date);
      case 'day':
        return this.renderDay(events, date);
      default:
        return console.error(
          'Attempting to render unsupported calendar mode ' + mode
        );
    }
  }
});

module.exports = Calendar;
