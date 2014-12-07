'use strict';

var Actions = require('./Actions');
var CalendarStore = require('./CalendarStore');
var DateUtil = require('./DateUtil');
var Event = require('./Event');
var EventStore = require('./EventStore');
var React = require('react');
var Link = require('./Link');
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
      <td className={classes}>
          {this.props.children}
      </td>
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
            <table className="calendar">
              <tbody>
                <tr>
                  <Cell>
                    {dayRange.start.date()}
                    {eventBlocks}
                  </Cell>
                </tr>
              </tbody>
            </table>
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

    var sameMonth = range.start.format('MMMM') === range.end.format('MMMM');
    var title = range.start.format('MMMM Do') + ' - ' +
      (sameMonth ? range.end.format('Do') : range.end.format('MMMM Do'));

    return (
      <div>
        <h1>{title}</h1>
        <div className="calendar-wrapper calendar-wrapper-week">
          <Paper rounded={false}>
            <table className="calendar">
              <tbody>
                <tr>
                  {cells}
                </tr>
              </tbody>
            </table>
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

    var rows = [];
    var i = 0;
    var row = [];
    fullRange.by('day', function(day) {
      var isToday =
        (day.date() === moment().date() && day.month() === moment().month());
      var isDisabled = day.month() !== date.month();
      var key = '' + day.date() + '-' + day.month();

      var dayRange = moment().range(day, moment(day).add(1, 'days'));
      var numEvents = _.filter(events, function(event) {
        return dayRange.overlaps(event.range);
      }).length;
      var setDate = function() {
        Actions.setDate(day);
        Actions.setCalendar('day');
      };
      row.push(
        <Cell today={isToday} disabled={isDisabled} key={key}>
          <Link onClick={setDate}>{day.date()}</Link>
          {numEvents ? <span className="num-events">{numEvents}</span> : null}
        </Cell>
      );
      i++;
      if (i === 7) {
        rows.push(
          <tr>
            {row}
          </tr>
        );
        i = 0;
        row = []
      }
    });
    if (row.length !== 0) {
      rows.push(
        <tr>
          {row}
        </tr>
      );
    }

    return (
      <div>
        <h1>{date.format('MMMM')}</h1>
        <div className="calendar-wrapper calendar-wrapper-month">
          <Paper rounded={false}>
            <table className="calendar">
              <tbody>
                {rows}
              </tbody>
            </table>
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
