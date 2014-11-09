var Calendar = require('./Calendar');
var CalendarStore = require('./CalendarStore');
var Dispatcher = require('./Dispatcher');
var Navbar = require('./Navbar');
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
      <div>
        <Navbar />
        <div className="below-nav">
          <div className="sidebar-section">
            <Sidebar />
          </div>
          <div className="main-section">
            <Calendar
              type={this.state.calendarTitle}
              text={this.state.calendarText}
            />
          </div>
        </div>
      </div>
    );
  }
});

module.exports = App;
