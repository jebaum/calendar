var PropTypes = require('react').PropTypes;
var React = require('react');
var cx = require('react/addons').addons.classSet;

var Glyph = React.createClass({
  propTypes: {
    type: PropTypes.string.isRequired,
  },

  render: function() {
    var classes = {};
    classes['glyphicon'] = true;
    classes['glyphicon-' + this.props.type] = true;

    return <span className={cx(classes)} />;
  }
});

module.exports = Glyph;
