var Glyph = require('./Glyph');
var React = require('react');
var cx = require('react/addons').addons.classSet;

var Navbar = React.createClass({
  render: function() {
    var classes = cx({
      'navbar': true,
      'navbar-default': true,
      'navbar-fixed-top': true,
    });
    return (
      <div>
        <nav className={classes}>
          <h1 className="navbar-title">
            <Glyph type="calendar" />
            YAMLCal
          </h1>
        </nav>
        <div className="navbar-spacer" />
      </div>
    );
  }
});

module.exports = Navbar;
