var Actions = require('./Actions');
var CalendarStore = require('./CalendarStore');
var DropDownMenu = require('material-ui').DropDownMenu;
var EventEditor = require('./EventEditor');
var Icon = require('material-ui').Icon;
var FlatButton = require('material-ui').FlatButton;
var React = require('react');
var StateFromStore = require('./StateFromStore');
var Toolbar = require('material-ui').Toolbar;
var ToolbarGroup = require('material-ui').ToolbarGroup;
var _ = require('underscore');

var AppToolbar = React.createClass({
  mixins: [StateFromStore({
    calendarMode: {
      store: CalendarStore,
      fetch: function(store, fetchParams) {
        return store.getMode();
      }
  }})],

  _onChange: function(e, index, item) {
    Actions.setCalendar(item.value);
  },

  _addNewEvent: function() {
    this.refs.eventEditor.show();
  },

  render: function() {
    var modeMenuItems = [
      { value: 'month', text: 'Month'},
      { value: 'week', text: 'Week'},
      { value: 'day', text: 'Day'},
    ];

    var selectedIndex = _.indexOf(
      _.pluck(modeMenuItems, 'value'),
      this.state.calendarMode
    );

    return (
      <div>
        <Toolbar>
          <ToolbarGroup key={0} float="left">
            <DropDownMenu
              menuItems={modeMenuItems}
              onChange={this._onChange}
              selectedIndex={selectedIndex}
            />
          </ToolbarGroup>
          <ToolbarGroup key={1} float="right">
            <Icon icon="navigation-chevron-left" onClick={Actions.setDateBackward} />
            <Icon icon="navigation-chevron-right" onClick={Actions.setDateForward} />
            <FlatButton label="Today" type="FLAT" onClick={Actions.setDate}/>
            <FlatButton label="New Event" type="FLAT" onClick={this._addNewEvent} />
          </ToolbarGroup>
        </Toolbar>
        <EventEditor ref="eventEditor" />
      </div>
    );
  }
})

module.exports = AppToolbar;
