var Actions = require('./Actions');
var React = require('react');

var Sidebar = React.createClass({
  setCalendarMonth: function() {
    Actions.setCalendar('month');
  },

  setCalendarWeek: function() {
    Actions.setCalendar('week');
  },

  setCalendarDay: function() {
    Actions.setCalendar('day');
  },

  render: function() {
    return (
      <div className="sidebar-contents">
        <ul>
          <li onClick={this.setCalendarMonth}>Month view</li>
          <li onClick={this.setCalendarWeek}>Week view</li>
          <li onClick={this.setCalendarDay}>Day view</li>
        </ul>
      </div>
    );
  }
});

module.exports = Sidebar;
