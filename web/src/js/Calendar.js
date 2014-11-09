var React = require('react');
var PropTypes = require('react').PropTypes;

var Calendar = React.createClass({
  propTypes: {
    type: PropTypes.oneOf(['day', 'week', 'month', 'year']),
    text: PropTypes.string,
  },

  render: function() {
    return (
      <div className="calendar">
        <h1>{this.props.type + ' view'}</h1>
        <textarea value={this.props.text} />
      </div>
    );
  }
});

module.exports = Calendar;
