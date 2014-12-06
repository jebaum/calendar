var Actions = require('./Actions');
var DropDownMenu = require('material-ui').DropDownMenu;
var Icon = require('material-ui').Icon;
var FlatButton = require('material-ui').FlatButton;
var React = require('react');
var Toolbar = require('material-ui').Toolbar;
var ToolbarGroup = require('material-ui').ToolbarGroup;

var AppToolbar = React.createClass({

  onChange: function(e, index, item) {
    Actions.setCalendar(item.value);
  },

  render: function() {
    var modeMenuItems = [
      { value: 'month', text: 'Month'},
      { value: 'week', text: 'Week'},
      { value: 'day', text: 'Day'},
    ];

    return (
      <Toolbar>
        <ToolbarGroup key={0} float="left">
          <DropDownMenu menuItems={modeMenuItems} onChange={this.onChange} />
        </ToolbarGroup>
        <ToolbarGroup key={1} float="right">
          <Icon icon="navigation-chevron-left" onClick={Actions.setDateBackward} />
          <Icon icon="navigation-chevron-right" onClick={Actions.setDateForward} />
          <FlatButton label="Today" type="FLAT" onClick={Actions.setDate}/>
        </ToolbarGroup>
      </Toolbar>
    );
  }
})

module.exports = AppToolbar;
