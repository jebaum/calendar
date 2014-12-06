var Actions = require('./Actions');
var Dialog = require('material-ui').Dialog;
var Input = require('material-ui').Input;
var PropTypes = require('react').PropTypes;
var React = require('react');
var moment = require('moment-range');

var EventEditor = React.createClass({
  propTypes: {
    event: PropTypes.object.isRequired,
  },

  show: function() {
    this.refs.editEvent.show();
  },

  _onDialogSubmit: function() {
    var name = this.refs.name.getValue();
    var start = moment(
      this.refs.start.getValue(), 'YYYY-MM-DDTHH:mm'
    );
    var end = moment(
      this.refs.end.getValue(), 'YYYY-MM-DDTHH:mm'
    );
    var event = this.props.event;
    event.name = name;
    event.range = moment().range(start, end);
    Actions.setEvent(event);
    this.refs.editEvent.dismiss();
  },

  render: function() {
    var event = this.props.event;
    var name = event.name;
    var range = event.range;

    var dialogActions = [
      { text: 'CANCEL', onClick: this._onDialogCancel },
      { text: 'SUBMIT', onClick: this._onDialogSubmit }
    ];
    return (
      <Dialog ref="editEvent" title="Edit Event" actions={dialogActions}>
        <Input placeholder="Name" name="name" ref="name" type="text" defaultValue={name} />
        <Input placeholder="Start" name="start" ref="start" type="datetime-local"
          defaultValue={range.start.format('YYYY-MM-DDTHH:mm')} />
        <Input placeholder="End" name="end" ref="end" type="datetime-local"
          defaultValue={range.end.format('YYYY-MM-DDTHH:mm')} />
      </Dialog>
    );
  }
});

module.exports = EventEditor;
