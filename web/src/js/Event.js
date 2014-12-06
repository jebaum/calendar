var Actions = require('./Actions');
var Dialog = require('material-ui').Dialog;
var EventEditor = require('./EventEditor');
var IconButton = require('material-ui').IconButton;
var Link = require('./Link');
var PropTypes = require('react').PropTypes;
var React = require('react');

var Event = React.createClass({
  propTypes: {
    range: PropTypes.object.isRequired,
    event: PropTypes.object.isRequired,
  },

  _onClick: function() {
    this.refs.eventEditor.show();
  },

  _getTimeString: function() {
    var event = this.props.event;
    var dayRange = this.props.range;
    var start = event.range.start;
    var end = event.range.end;

    var hasStart = start > dayRange.start;
    var hasEnd = end < dayRange.end;

    if (hasStart && hasEnd) {
      // test if start or end on hour
      // if so, display "3" instead of "3:00"
      var startHour = (start.format('mm')) === '00';
      var endHour = (end.format('mm')) === '00';

      // test if same AM or PM
      // if so, display "3 - 4 PM" instead of "3 PM - 4 PM"
      var bothAMPM = false;
      if (start.format('a') === end.format('a')) {
        bothAMPM = true;
      }

      var startString = bothAMPM ? (
        startHour ? start.format('h') : start.format('h:mm')) : (
        startHour ? start.format('h a') : start.format('h:mm a')
      );

      var endString = endHour ? end.format('h a') : end.format('h:mm a');
      return startString + ' - ' + endString;
    } else if (hasStart) {

      var startHour = (start.format('mm')) === '00';
      return 'starts at ' + (startHour ? start.format('ha') : start.format('h:mm a'));
    } else if (hasEnd) {
      var endHour = (end.format('mm')) === '00';
      return 'ends at ' +
        (endHour ? end.format('h a') : end.format('h:mm a'));
    }
    return 'all day';
  },

  render: function() {
    var range = this.props.range;
    var eventRange = range.intersect(this.props.event.range);
    var total = range.end - range.start;
    var topOffset = Math.max(eventRange.start - range.start, 0) / total;
    var duration = (eventRange.end - eventRange.start) / total;

    var style = {
      'top': '' + (topOffset * 100) + '%',
      'minHeight': '' + (duration * 100) + '%',
    };

    return (
      <div className="event-block" style={style}>
        <span className="mui-font-style-menu">
          {this._getTimeString()}
        </span>

        <br />

        <Link onClick={this._onClick}>{this.props.event.name}</Link>

        <EventEditor ref="eventEditor" event={this.props.event} />
      </div>
    );
  }
});

module.exports = Event;
