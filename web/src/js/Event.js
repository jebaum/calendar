var Actions = require('./Actions');
var Dialog = require('material-ui').Dialog;
var PropTypes = require('react').PropTypes;
var React = require('react');

var Event = React.createClass({
  propTypes: {
    range: PropTypes.object.isRequired,
    event: PropTypes.object.isRequired,
  },

  _onClick: function() {
    var id = this.props.event.id;
    Actions.setEvent({
      id: id,
      name: 'foo',
      range: this.props.event.range
    });

    this.refs.editEvent.show();
  },

  _onDialogSubmit: function() {
    console.log('dialog submit');
  },

  render: function() {
    var range = this.props.range;
    var eventRange = range.intersect(this.props.event.range);
    var total = range.end - range.start;
    var topOffset = Math.max(eventRange.start - range.start, 0) / total;
    var duration = (eventRange.end - eventRange.start) / total;

    var style = {
      'top': '' + (topOffset * 100) + '%',
      'minHeight': '' + (duration * 100) + '%',
    };

    var dialogActions = [
      { text: 'CANCEL' },
      { text: 'SUBMIT', onClick: this._onDialogSubmit }
    ];

    return (
      <div
        className="event-block"
        style={style}
        onClick={this._onClick}>
        {this.props.event.name}

        <Dialog ref="editEvent" title="Edit Event" actions={dialogActions}>
          Edit Event Here
        </Dialog>
      </div>
    );
  }
});

module.exports = Event;
