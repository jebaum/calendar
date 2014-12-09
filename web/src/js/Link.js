var React = require('react');

var Link = React.createClass({
  render: function() {
    return (
      <span className="link" onClick={this.props.onClick}>
        {this.props.children}
      </span>
    );
  }
});

module.exports = Link;
