;(function() {
	brite.registerView("MainScreen", {
		loadTmpl : true,
		parent : "#bodyPage"
	}, {
		create : function(data, config) {
			var dfd = $.Deferred();
			renderer.render("MainScreen", data).done(function(html) {
				var $e = $(html);
				dfd.resolve($e);
			});
			return dfd.promise();
		},

		docEvents : {

		},

		postDisplay : function(data, config) {
			var view = this;
			var $e = view.$el;

			brite.display("Friends");
		}

	});
	// --------- View Registration --------- //

	// load screen
	$(function() {
		brite.display("MainScreen");
	});
})();
