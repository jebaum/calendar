var AppBar = require('material-ui').AppBar;
var AppCanvas = require('material-ui').AppCanvas;
var Calendar = require('./Calendar');
var CalendarStore = require('./CalendarStore');
var Dispatcher = require('./Dispatcher');
var React = require('react');
var Sidebar = require('./Sidebar');
var StateFromStore = require('./StateFromStore');

var App = React.createClass({
  mixins: [StateFromStore({
    calendarTitle: {
      store: CalendarStore,
      fetch: function(store, fetchParams) {
        return store.getTitle();
      }
    },
    calendarText: {
      store: CalendarStore,
      fetch: function(store, fetchParams) {
        return store.getText();
      }
    }
  })],

  render: function() {
    return (
      <AppCanvas predefinedLayout={1}>
        <AppBar title="YAMLCal" />
        <div className="mui-app-content-canvas with-nav">
          <Sidebar />
          <div className="subContent">
            <Calendar
              type={this.state.calendarTitle}
              text={this.state.calendarText}
            />
          </div>
        </div>
      </AppCanvas>
    );
  }
});

module.exports = App;
