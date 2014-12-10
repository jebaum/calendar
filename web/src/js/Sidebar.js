var Actions = require('./Actions');
var LeftNav = require('material-ui').LeftNav;
var Menu = require('material-ui').Menu;
var MenuItem = require('material-ui').MenuItem;
var PaperButton = require('material-ui').PaperButton;
var React = require('react');

var Sidebar = React.createClass({
  onClick: function(e, index, item) {
    Actions.setCalendar(item.value);
  },

  render: function() {
    var menuItems = [
      { value: 'month', text: 'Month View'},
      { value: 'week', text: 'Week View'},
      { value: 'day', text: 'Day View'},
    ];

    return (
      <div className="subNav">
        <Menu menuItems={menuItems} zDepth={100} onItemClick={this.onClick}/>
      </div>
    );
  }
});

module.exports = Sidebar;
