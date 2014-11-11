var DateUtil = require('./DateUtil');
var React = require('react');
var Paper = require('material-ui').Paper;
var PropTypes = require('react').PropTypes;
var classSet = require('react/addons').addons.classSet;

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
      var isToday = day.getUTCDate() === (new Date()).getUTCDate();
      var isDisabled = day.getUTCMonth() !== (new Date()).getUTCMonth();
      var key = '' + day.getUTCDate() + '-' + day.getUTCMonth();
      cells.push(
        <Cell today={isToday} disabled={isDisabled} key={key}>
          {day.getUTCDate()}
        </Cell>
      );
      day = new Date(
        day.getUTCFullYear(),
        day.getUTCMonth(),
        day.getUTCDate() + 1
      );
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
    var day = new Date(
      range.start.getUTCFullYear(),
      range.start.getUTCMonth(),
      range.start.getUTCDate() - range.start.getUTCDay()
    );
    var end = new Date(
      range.end.getUTCFullYear(),
      range.end.getUTCMonth(),
      range.end.getUTCDate() + ( 7 - range.end.getUTCDay())
    );
    var cells = [];
    var i = 0;
    while (day < end) {
      var isToday = day.getUTCDate() === (new Date()).getUTCDate();
      var isDisabled = day.getUTCMonth() !== (new Date()).getUTCMonth();
      var key = '' + day.getUTCDate() + '-' + day.getUTCMonth();
      cells.push((
        <Cell today={isToday} disabled={isDisabled} key={key}>
          {day.getUTCDate()}
        </Cell>
      ));
      day = new Date(
        day.getUTCFullYear(),
        day.getUTCMonth(),
        day.getUTCDate() + 1
      );
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
