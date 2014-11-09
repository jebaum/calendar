var Actions = require('./Actions');
var React = require('react');

var Sidebar = React.createClass({
  setCalendarTitleMonth: function() {
    Actions.setCalendarTitle('month');
  },

  setCalendarTitleWeek: function() {
    Actions.setCalendarTitle('week');
  },

  setCalendarTitleDay: function() {
    Actions.setCalendarTitle('day');
  },

  render: function() {
    return (
      <div className="sidebar-contents">
        <ul>
          <li onClick={this.setCalendarTitleMonth}>Month view</li>
          <li onClick={this.setCalendarTitleWeek}>Week view</li>
          <li onClick={this.setCalendarTitleDay}>Day view</li>
        </ul>
      </div>
    );
  }
});

module.exports = Sidebar;
