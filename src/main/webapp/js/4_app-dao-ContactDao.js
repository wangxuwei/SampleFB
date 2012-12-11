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
		return $.ajax({
			type : "POST",
			url : contextPath + "/getContactListByGroup.json",
			data : data,
			dataType : "json"
		}).pipe(function(val) {
			return val.result;
		});
	}

	app.RemoteContactDao = RemoteContactDao;
})(jQuery);