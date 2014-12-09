var PropTypes = require('react').PropTypes;
var React = require('react');
var cx = require('react/addons').addons.classSet;

var Well = React.createClass({
  propTypes: {
    size: PropTypes.oneOf(['large', 'small', 'default']),
  },

  getDefaultProps: function() {
    return {
      size: 'default',
    }
  },

  render: function() {
    var classes = cx({
      'well': true,
      'well-lg': this.props.size === 'large',
      'well-sm': this.props.size === 'small',
    });
    return (
      <div className={classes}>
        {this.props.children}
      </div>
    );
  }
});

module.exports = Well;
