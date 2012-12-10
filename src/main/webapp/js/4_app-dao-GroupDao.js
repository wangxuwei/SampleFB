var app = app || {};
(function($){
	//-------- Remote dao ---------//
	function RemoteGroupDao(){
		this.constructor._super.constructor.call(this,"Group");
	}
	brite.inherit(RemoteGroupDao,brite.dao.RemoteDao);
	
	RemoteGroupDao.prototype.getGroupById = function(id){
		var data = {};
		data.id = id;

		return $.ajax({
			type : "GET",
			url : contextPath + "/getGroupById.json",
			data : data,
			dataType : "json"
		}).pipe(function(val) {
			return val.result;
		});
	}

	app.RemoteGroupDao = RemoteGroupDao;
})(jQuery);