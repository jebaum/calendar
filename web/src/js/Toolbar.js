var Actions = require('./Actions');
var DropDownMenu = require('material-ui').DropDownMenu;
var Icon = require('material-ui').Icon;
var PaperButton = require('material-ui').PaperButton;
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
    var modeMenu = (
      <DropDownMenu menuItems={modeMenuItems} onChange={this.onChange} />
    );
    var groupItems = [
      <Icon icon="navigation-chevron-left" onClick={Actions.setDateBackward} />,
      <Icon icon="navigation-chevron-right" onClick={Actions.setDateForward} />,
      <PaperButton label="Today" type="FLAT" onClick={Actions.setDate}/>,
    ];
    var groups = [
      <ToolbarGroup float="left" groupItems={[modeMenu]} />,
      <ToolbarGroup float="right" groupItems={groupItems} />,
    ];

    return <Toolbar groups={groups} />;
  }
})

module.exports = AppToolbar;
