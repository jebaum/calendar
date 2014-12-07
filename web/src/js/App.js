var AppBar = require('material-ui').AppBar;
var AppCanvas = require('material-ui').AppCanvas;
var Calendar = require('./Calendar');
var React = require('react');
var Sidebar = require('./Sidebar');
var AppToolbar = require('./Toolbar');

var App = React.createClass({
  render: function() {
    return (
      <AppCanvas predefinedLayout={1}>
        <AppBar title="YAMLCal" showMenuIconButton={false} />
        <div className="mui-app-content-canvas with-nav">
          <AppToolbar />
          <div className="subContent">
            <Calendar />
          </div>
        </div>
      </AppCanvas>
    );
  }
});

module.exports = App;
