function Worker(masterUrl) {
    var HB_INTERVAL = 15 * 1000; //10s
    this.masterUrl = masterUrl;
    this._previousAction;
    this._job = {};
    this._callMaster();
    this._previousAction = "idle";
    this._sendHeartbeats(HB_INTERVAL);
}

Worker.prototype._callMaster = function() {
    var PROTOCOL = "worker";
    this.ws = new WebSocket(this.masterUrl, PROTOCOL);
    var worker = this;
    this.ws.onmessage = function(m) {
        var msg = JSON.parse(m.data);
        worker._react(msg);
    };
};

Worker.prototype._react = function(msg) {
    var payload = msg.payload;
    var action = payload.action;
    this._previousAction = action;
    if (payload.job) {
        this._initjob(payload.job);
    }
    if (action == "mapSplit") {
        var splitId = payload.mapStatus.splitId;
        this._job.onMap(splitId);
    }
    if (action == "reduceBucket") {
        var bucketId = payload.reduceStatus.bucketId;
        this._job.onReduceBucket(bucketId);
    }
    if (action == "reduceChunk") {
        var bucketId = payload.reduceStatus.bucketId;
        var splitId = payload.reduceStatus.splitId;
        var urls = payload.reduceStatus.locations;
        this._job.onReduceChunk(splitId, bucketId, urls);
    }
    if (action == "idle") {
        //console.log('worker idle');
    }
};

Worker.prototype._sendACK = function(payload) {
    var msg = {};
    msg.type =  "ACK";
    msg.payload = payload;
    this.ws.send(JSON.stringify(msg));	
};

Worker.prototype.suggestChunk = function(splitId, bucketId, unreachable) {
		var reduceStatus = {};
		reduceStatus.bucketId = bucketId;
		reduceStatus.splitId = splitId;
		var payload = {};
		payload.action = "reduceChunk";
		payload.reduceStatus = reduceStatus;
		payload.unreachable = unreachable;
		payload.jobId = this._job.id;
		this._sendACK(payload);
};

Worker.prototype.bucketComplete = function(bucketId, unreachable) {
	var payload = {};
	payload.action = "reduceBucket";
	payload.reduceStatus = {bucketId: bucketId};
	payload.unreachable = unreachable;
	payload.jobId = this._job.id;
	this._sendACK(payload);    
};

Worker.prototype.mapComplete = function(splitId) {
	var payload = {};
	payload.action = "mapSplit";
	payload.mapStatus = {splitId: splitId};
	payload.jobId = this._job.id;
	this._sendACK(payload);
};

Worker.prototype.hb = function() {
	var worker = this;
	var interUrl = null;
	var job = worker._job;
	if (typeof(job) != typeof(undefined)) {
  	    interUrl = job.interUrl || interUrl;
	}
	var payload = {};
	payload.action = worker._previousAction;
	payload.jobId = worker.jobId;
	payload.interUrl = interUrl;
    var msg = {};
    msg.type = "HB";
    msg.payload = payload;
    //console.log(msg);
    worker.ws.send(JSON.stringify(msg));
};


Worker.prototype._sendHeartbeats = function(interval) {
    var worker = this;
    var hb = function() {
    	worker.hb();
    };
    setInterval(hb, interval);
};

Worker.prototype._initjob = function(requested) {
    if (this._job.id != requested.jobId) {
        eval(requested.code);
        requested.mapper = mapper;
        requested.reducer = reducer;
        requested.input = input;
        requested.inter = inter;
        requested.output = output;
        requested.chooseBucket = chooseBucket;
        if (typeof(combiner) != typeof(undefined)) {
            requested.combiner = combiner;
        }

        this._job = new Job(requested, this);
    }
};



