var app = app || {};
(function($){
	//-------- Remote dao ---------//
	function RemoteContactDao(){
		this.constructor._super.constructor.call(this,"Contact");
	}
	brite.inherit(RemoteContactDao,brite.dao.RemoteDao);
	
	RemoteContactDao.prototype.getContactById = function(id){
		var data = {};
		data.id = id;

		return $.ajax({
			type : "GET",
			url : contextPath + "/getContactById.json",
			data : data,
			dataType : "json"
		}).pipe(function(val) {
			return val.result;
		});
	}


	RemoteContactDao.prototype.addContact = function(data){
	var dfd = $.ajax({
			type : "POST",
			url : contextPath + "/addContact.do",
			data : data,
			dataType : "json"
		}).pipe(function(val) {
 			return val;
		});
		return dfd.promise();
	}

	app.RemoteContactDao = RemoteContactDao;
})(jQuery);