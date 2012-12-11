;(function() {
	brite.registerView("MainContent", {
		loadTmpl : true,
		emptyParent : true,
		parent : ".MainView-panels-inner"
	}, {
		create : function(data, config) {
			var view = this;
			view.groupId = data.id;
			var createDfd = $.Deferred();
			brite.dao("Group").getGroupById(view.groupId).done(function(po) {
				var html = $("#tmpl-MainContent").render(po);
				createDfd.resolve($(html));
			});
			return createDfd.promise();
		},

		events : {
			//show group panel view
			"btap;.addBtn" : function(e) {
				var view = this;
				var $e = view.$el;
				$(".subjectWarning").hide();
				$e.find("input[name='title']").val('');
				$e.find("input[name='taskId']").val('');
				$('#myModal').show();
				view.refreshFriendsList.call(view);
			},

			"btap;.showDeleteBtn" : function(e) {
				var view = this;
				var $e = view.$el;
				var obj = $(e.currentTarget);
				// create the delete-controls element
				var $controls = $($("#tmpl-MainContent-delControls").html());
				$e.find(".deleteControl").html($controls);

				var $inner = $e.find(".delete-controls-inner");
				//$inner.css("transform","scale(2)");
				setTimeout(function() {
					$inner.addClass("show");
				}, 10);
				$e.on("click", ".cancelBtn", function() {
					$e.find(".delete-controls-inner").removeClass("show").on("btransitionend", function() {
						$controls.remove();
						$e.find(".showDeleteBtn").show();
					});
				});

				$e.on("click", ".deleteBtn", function() {
					brite.dao("Project").remove(view.projectId).done(function() {
						refreshList();
					});
				});
				obj.hide();
			},
			"btap;.closeBtn" : function(e) {
				$('#myModal').hide();
			},
			"btap;.saveTaskBtn" : function(e) {
				var view = this;
				var $e = view.$el;
				var projectId = view.projectId;
				var id = $('#myModal').find("input[name='taskId']").val();
				var title = $('#myModal').find("input[name='title']").val();
				if (title == "") {
					return;
				};
				brite.dao("Task").updateTask("Task", id, projectId, title).done(function() {
					$('#myModal').hide();
					refresh.call(view);
				});
			},

			"btap;.opBtn" : function(e) {
				var view = this;
				var $e = view.$el;
				var obj = $(e.currentTarget).bEntity();
				var op = $(e.currentTarget).attr("op");
				if (op == 'edit') {
					var title = $(e.currentTarget).attr("data-value");
					$e.find("input[name='title']").val(title);
					$e.find("input[name='taskId']").val(obj.id);
					$('#myModal').show();
				} else {
					brite.dao("Task").opTask(obj.id, op).done(function() {
						refresh.call(view);
					});
				};
			},

		},

		docEvents : {
			//bind event with refresh contacts
		},

		daoEvents : {
			// on dataChange of contact, just refresh all for now (can be easily optimized)
		},

		postDisplay : function(data, config) {
			var view = this;
			var $e = view.$el;

			$('#myModal').on("keyup", function(event) {
				if (event.which === 27) {
					$('#myModal').hide();
				}
			});
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
				$items.html(html);
				$items.find(".addFriendBtn").click(function(){
					
				})
				dfd.resolve();
			});

		
			return dfd.promise();
		}
	});
	// --------- View Private Methods --------- //
	function refresh() {
		var view = this;
		var $e = view.$el;
		setTimeout(function() {
			brite.display("MainContent", null, {
					id : view.projectId
				});
		}, 100)
	}

	function refreshList() {
		var p = $(document).bFindComponents("ProjectList");
		if (p && p.length > 0) {
			p[0].refresh();
		}
	}

	// --------- /View Private Methods --------- //
})();

