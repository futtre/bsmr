function noInter() {
    
    function Inter(job) {
        this.job = job;
        var interId = ('' + Math.random()).split('.')[1];
        job.setOwnInterUrl('nointer://' + interId);
    }
    
    Inter.prototype.feed = function(splitId, bucketId, urls, target) {
        var job = this.job;            
        for (var i in urls) {
            var url = urls[i];
        	job.markUnreachable(url);
        }
        job.onChunkFail(splitId, bucketId);
    };
    
    var factory = function(job) {
    	return new Inter(job);
    };
    
    return factory;
}
