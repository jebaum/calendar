var React = require('react');
var PropTypes = require('react').PropTypes;

var s = "{'sample_response': 1234}";

var Calendar = React.createClass({
  propTypes: {
    type: PropTypes.oneOf(['day', 'week', 'month', 'year']),
  },

  render: function() {
    return (
      <div className="calendar">
        <h1>{this.props.type + ' view'}</h1>
        <textarea value={s} />
      </div>
    );
  }
});

module.exports = Calendar;
