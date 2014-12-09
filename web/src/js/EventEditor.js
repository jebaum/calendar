var Actions = require('./Actions');
var Dialog = require('material-ui').Dialog;
var Input = require('material-ui').Input;
var PropTypes = require('react').PropTypes;
var React = require('react');
var moment = require('moment-range');

var EventEditor = React.createClass({
  propTypes: {
    event: PropTypes.object,
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
    var event = this.props.event || {};
    event.name = name;
    event.range = moment().range(start, end);
    if (event.id)
      Actions.setEvent(event);
    else
      Actions.addEvent(event);
    this.refs.editEvent.dismiss();
  },

  render: function() {
    var event = this.props.event;
    var name = event ? event.name : null;
    var description = event ? event.description : null;
    var range = event ? event.range : null;

    var dialogActions = [
      { text: 'CANCEL', onClick: this._onDialogCancel },
      { text: 'SUBMIT', onClick: this._onDialogSubmit }
    ];
    return (
      <Dialog ref="editEvent" title="Edit Event" actions={dialogActions}>
        <Input placeholder="Name" name="name" ref="name" type="text"
          defaultValue={name} />
        <Input placeholder="Description" name="description" ref="description" type="text"
          defaultValue={description} multiline={true} />
        <Input placeholder="Start" name="start" ref="start" type="datetime-local"
          defaultValue={range ? range.start.format('YYYY-MM-DDTHH:mm') : null} />
        <Input placeholder="End" name="end" ref="end" type="datetime-local"
          defaultValue={range ? range.end.format('YYYY-MM-DDTHH:mm') : null} />
      </Dialog>
    );
  }
});

module.exports = EventEditor;
