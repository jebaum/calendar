'use strict';

var DateUtil = require('./DateUtil');
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

var EventBlock = React.createClass({
  propTypes: {
    range: PropTypes.object.isRequired,
    event: PropTypes.object.isRequired,
  },

  render: function() {
    var eventRange = this.props.event.range;
    var range = this.props.range;
    var total = range.end - range.start;
    var topOffset = Math.max(eventRange.start - range.start, 0) / total;
    var duration = (eventRange.end - eventRange.start) / total;

    var style = {
      'top': '' + (topOffset * 100) + '%',
      'minHeight': '' + (duration * 100) + '%',
    }

    return (
      <div
        className="event-block"
        style={style}>
        {this.props.event.text}
      </div>
    );
  }
});

var Calendar = React.createClass({
  mixins: [StateFromStore({
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

  renderDay: function(events) {
    var dayRange = DateUtil.getDay();
    var eventBlocks = [];
    _.each(events, function(event) {
      if (dayRange.overlaps(event.range)) {
        var intersectingEvent = {
          range: dayRange.intersect(event.range),
          text: event.name,
        };
        eventBlocks.push(
          <EventBlock event={intersectingEvent} range={dayRange} />
        );
      }
    });

    return (
      <div>
        <h1>day view</h1>
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

  renderWeek: function(events) {
    var range = DateUtil.getWeek();
    var cells = [];
    range.by('days', function(day) {
      var eventBlocks = [];
      var dayRange = moment().range(day, moment(day).add(1, 'days'));
      _.each(events, function(event) {
        if (dayRange.overlaps(event.range)) {
          var intersectingEvent = {
            range: dayRange.intersect(event.range),
            text: event.name,
          };
          eventBlocks.push(
            <EventBlock event={intersectingEvent} range={dayRange} />
          );
        }
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
        <h1>week view</h1>
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

  renderMonth: function(events) {
    var range = DateUtil.getMonth();
    var fullRange = moment().range(
      range.start.startOf('week'),
      range.end.endOf('week')
    );

    var cells = [];
    var i = 0;
    fullRange.by('day', function(day) {
      var isToday = day.date() === moment().date();
      var isDisabled = day.month() !== moment().month();
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
        <h1>month view</h1>
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
    var events = this.state.events;
    if (this.props.type === 'month') {
      return this.renderMonth(events);
    } else if (this.props.type === 'week') {
      return this.renderWeek(events);
    } else if (this.props.type === 'day') {
      return this.renderDay(events);
    } else {
      console.error(
        'Attempting to render unsupported calendar type ' + this.props.type
      );
    }
  }
});

module.exports = Calendar;
