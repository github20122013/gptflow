function EventBus() {
    this.events = {};
}

EventBus.prototype.on = function(event, callback) {
    if (!this.events[event]) {
        this.events[event] = [];
    }
    this.events[event].push(callback);
}

EventBus.prototype.emit = function(event, ...args) {
    if (this.events[event]) {
        this.events[event].forEach(callback => {
            callback.apply(null, args);
        });
    }
}