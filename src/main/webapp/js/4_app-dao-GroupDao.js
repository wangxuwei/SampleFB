var app = app || {};
(function($) {
	//-------- Remote dao ---------//
	function RemoteGroupDao() {
		this.constructor._super.constructor.call(this, "Group");
	}


	brite.inherit(RemoteGroupDao, brite.dao.RemoteDao);

	RemoteGroupDao.prototype.getGroupList = function(id) {
		return $.ajax({
			type : "GET",
			url : contextPath + "/getGroupList.json",
			dataType : "json"
		}).pipe(function(val) {
			return val.result;
		});
	}


	RemoteGroupDao.prototype.getGroupById = function(id) {
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


	RemoteGroupDao.prototype.updateGroup = function(data) {
		return $.ajax({
			type : "post",
			url : contextPath + "/createGroup.do",
			data : data,
			dataType : "json"
		}).pipe(function(val) {
			return val;
		});
	}


	app.RemoteGroupDao = RemoteGroupDao;
})(jQuery); 