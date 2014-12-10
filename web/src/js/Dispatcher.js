
function Dispatcher() {
  this.callbacks = {};
}

Dispatcher.prototype = {
  register: function(action, callback) {
    this.callbacks[action] = callback;
  },

  dispatch: function(action, payload) {
    if (this.callbacks[action]) {
      this.callbacks[action](payload);
    } else {
      console.warn('No registered callbacks for action ' + action);
    }
  }
}

module.exports = new Dispatcher();
