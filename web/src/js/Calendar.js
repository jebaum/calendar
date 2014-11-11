var DateUtil = require('./DateUtil');
var React = require('react');
var Paper = require('material-ui').Paper;
var PropTypes = require('react').PropTypes;
var classSet = require('react/addons').addons.classSet;
var moment = require('moment');

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
  propTypes: {
    type: PropTypes.oneOf(['day', 'week', 'month', 'year']),
    text: PropTypes.string,
  },

  renderDay: function() {
    return (
      <div>
        <h1>day view</h1>
        <div className="calendar-wrapper calendar-wrapper-day">
          <Paper rounded={false}>
            <div className="calendar">
              <Cell>
                {(new Date()).getUTCDate()}
              </Cell>
            </div>
          </Paper>
        </div>
      </div>
    );
  },

  renderWeek: function() {
    var range = DateUtil.getWeek();
    var day = range.start;
    var cells = [];
    while (day < range.end) {
      var isToday = day.date() === moment().date();
      var isDisabled = day.month() !== moment().month();
      var key = '' + day.date() + '-' + day.month();
      cells.push(
        <Cell today={isToday} disabled={isDisabled} key={key}>
          {day.date()}
        </Cell>
      );
      day.add(1, 'days');
    }

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

  renderMonth: function() {
    var range = DateUtil.getMonth();
    var day = range.start.startOf('week');
    var end = range.end.endOf('week');
    var cells = [];
    var i = 0;
    while (day < end) {
      var isToday = day.date() === moment().date();
      var isDisabled = day.month() !== moment().month();
      var key = '' + day.date() + '-' + day.month();
      cells.push((
        <Cell today={isToday} disabled={isDisabled} key={key}>
          {day.date()}
        </Cell>
      ));
      day.add(1, 'days');
      i++;
      if (i === 7) {
        cells.push(<br />);
        i = 0;
      }
    }

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
    if (this.props.type === 'month') {
      return this.renderMonth();
    } else if (this.props.type === 'week') {
      return this.renderWeek();
    } else if (this.props.type === 'day') {
      return this.renderDay();
    } else {
      console.error(
        'Attempting to render unsupported calendar type ' + this.props.type
      );
    }
  }
});

module.exports = Calendar;
