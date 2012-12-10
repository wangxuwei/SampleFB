;(function() {
	brite.registerView("GroupList", {
		loadTmpl : true,
		emptyParent : true,
		parent : ".left-content"
	}, {
		create : function(data, config) {
			var html = $("#tmpl-GroupList").render(data);
			var $e = $(html);
			return $e;
		},

		events : {
			//show group panel view
			"btap;.item" : function(e) {
				var view = this;
				var $e = view.$el;
				var obj = $(e.currentTarget).bEntity();
				$e.find(".groupListContain").find("li").removeClass("active");
				$(e.currentTarget).closest("li").addClass("active");
				var id = obj == null ? "" : obj.id;
				$(e.currentTarget).trigger("DO_SELECT_GROUP", {
					id : id
				});
			},
			"btap;.editBtn" : function(e) {
				var view = this;
				var $e = view.$el;
				var obj = $(e.currentTarget).bEntity();
				$e.find(".groupListContain").find("li").removeClass("active");
				$(e.currentTarget).closest("li").addClass("active");
				obj.subject = $(e.currentTarget).attr("data-value");
				editGroup.call(view, obj);
			},
			"btap;.newGroupBtn" : function(e) {
				var view = this;
				var $e = view.$el;
				editGroup.call(view);
			},

		},

		docEvents : {
			"DO_SELECT_GROUP" : function(event, extra) {
				var view = this;
				var id = extra.id; 
				
				view.$el.find("li.sel").removeClass("sel");
				view.$el.find("i.icon-folder-open").removeClass("icon-folder-open").addClass("icon-folder-close");

				// select the li
				var $selectedLi = view.$element.find("li[data-obj_id='" + id + "']");
				$selectedLi.addClass("sel");
				$selectedLi.find("i.icon-folder-close").removeClass("icon-folder-close").addClass("icon-folder-open");

				// keep that for dataChangeEvent (to keep the item selected)
				view.selectedFolderId = id;
			}

		},

		daoEvents : {
			// on dataChange of contact, just refresh all for now (can be easily optimized)
		},

		postDisplay : function(data, config) {
			var view = this;
			var $e = view.$el;
		
			view.refresh.call(view);
		},
		refresh : function(id) {
			var view = this;
			var $e = view.$el;
			brite.dao("Group").list().done(function(list) {
				if (list) {
					var html = $("#tmpl-GroupList-item").render(list);
					$e.find(".groupListContain").html(html);	
					if (!id) {
						 var a = $e.find(".groupListContain a");
						 if (a.length > 0) {
							 a = $(a.get(0));
							 a.trigger("btap");
						 };
					 };
				};
			});
		}
	});
	// --------- View Private Methods --------- //

	function editGroup(po) {
		var view = this;
		var $e = view.$el;

		if ($e.find(".addItem").length < 1) {
			var html = $("#tmpl-GroupList-add-item").html();
			$e.find(".addGroupContain").append(html);
		}

		$e.find(".addItem input[type='text']").focus().on("keyup", function(event) {
			var data = {
				name : $(this).val()
			};
			// press ENTER
			if (event.which === 13) {
				if ($(this).val() == "") {
					return;
				};
				if (po && po.id) {
					data = {
						name : $(this).val(),
						id : po.id
					};

					brite.dao("Group").update(data).done(function(po) {
						$e.find(".addItem").remove();
						brite.display("MainContent", null, {
							id : po.id
						});
						view.refresh.call(view);
					});
				} else {
					brite.dao("Group").create(data).done(function(po) {
						$e.find(".addItem").remove();
						brite.display("MainContent", null, {
							id : po.id
						});
						view.refresh.call(view);
					});
				};

			}
			// press ESC
			else if (event.which === 27) {
				$e.find(".addItem").remove();
			}
		}).on("blur", function() {
			$e.find(".addItem").remove();
		});

		if (po) {
			$e.find(".addItem input[name='addItem']").val(po.subject);
		};
	}

	// --------- /View Private Methods --------- //
})();

