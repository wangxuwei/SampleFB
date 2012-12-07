;(function() {

	// --------- View Registration --------- //
	brite.registerView("Friends", {
		loadTmpl : true,
		emptyParent : true,
		parent : ".MainView-panels-inner"
	}, {
		create : function(data, config) {
			var html = $("#tmpl-Friends").render(data);
			var $e = $(html);
			return $e;
		},
		events : {

		},
		docEvents : {

		},

		postDisplay : function(data, config) {
			var view = this;
			var $e = view.$el;

			view.refreshFriendsList.call(view);
		},
		refreshFriendsList : function() {
			var view = this;
			var $e = view.$el;
			var dfd = $.Deferred();
			var $items = $e.find(".listItem").empty();
			$.ajax({
				type : "get",
				url : contextPath + "/friends.json",
				data : {
					token : fbtoken,
					limit : 10,
					offset : 0
				},
				dataType : "json"
			}).done(function(data) {
				var html = $("#tmpl-Friends-list-rowItem").render(data);
				$items.html(html)
				dfd.resolve();
			});

		
			return dfd.promise();
		}

	});
})();
