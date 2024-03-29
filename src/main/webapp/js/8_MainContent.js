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
					brite.dao("Group").remove(view.groupId).done(function() {
						refreshList();
					});
				});
				obj.hide();
			},
			"btap;.closeBtn" : function(e) {
				$('#myModal').hide();
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
				data = [{
					"about" : "",
					"bio" : "",
					"birthday" : "",
					"birthdayAsDate" : null,
					"education" : [],
					"email" : "",
					"favoriteAthletes" : [],
					"favoriteTeams" : [],
					"firstName" : "aa",
					"gender" : "",
					"hometown" : null,
					"hometownName" : "",
					"id" : "100001542382538",
					"interestedIn" : [],
					"languages" : [],
					"lastName" : "",
					"link" : "",
					"locale" : "",
					"location" : null,
					"meetingFor" : [],
					"metadata" : null,
					"middleName" : "",
					"name" : "Woofgl Liang",
					"political" : "",
					"quotes" : "",
					"relationshipStatus" : "",
					"religion" : "",
					"significantOther" : null,
					"sports" : [],
					"thirdPartyId" : "",
					"timezone" : 0,
					"type" : "",
					"updatedTime" : null,
					"username" : "",
					"verified" : false,
					"website" : "",
					"work" : []
				}, {
					"about" : "",
					"bio" : "",
					"birthday" : "",
					"birthdayAsDate" : null,
					"education" : [],
					"email" : "",
					"favoriteAthletes" : [],
					"favoriteTeams" : [],
					"firstName" : "bb",
					"gender" : "",
					"hometown" : null,
					"hometownName" : "",
					"id" : "100002348599426",
					"interestedIn" : [],
					"languages" : [],
					"lastName" : "",
					"link" : "",
					"locale" : "",
					"location" : null,
					"meetingFor" : [],
					"metadata" : null,
					"middleName" : "",
					"name" : "宋宇光222",
					"political" : "",
					"quotes" : "",
					"relationshipStatus" : "",
					"religion" : "",
					"significantOther" : null,
					"sports" : [],
					"thirdPartyId" : "",
					"timezone" : 0,
					"type" : "",
					"updatedTime" : null,
					"username" : "",
					"verified" : false,
					"website" : "",
					"work" : []
				}, {
					"about" : "",
					"bio" : "",
					"birthday" : "",
					"birthdayAsDate" : null,
					"education" : [],
					"email" : "",
					"favoriteAthletes" : [],
					"favoriteTeams" : [],
					"firstName" : "cc",
					"gender" : "",
					"hometown" : null,
					"hometownName" : "",
					"id" : "100003944136001",
					"interestedIn" : [],
					"languages" : [],
					"lastName" : "",
					"link" : "",
					"locale" : "",
					"location" : null,
					"meetingFor" : [],
					"metadata" : null,
					"middleName" : "",
					"name" : "Xuwei  Wang",
					"political" : "",
					"quotes" : "",
					"relationshipStatus" : "",
					"religion" : "",
					"significantOther" : null,
					"sports" : [],
					"thirdPartyId" : "",
					"timezone" : 0,
					"type" : "",
					"updatedTime" : null,
					"username" : "",
					"verified" : false,
					"website" : "",
					"work" : []
				}]
				var html = $("#tmpl-Friends-list-rowItem").render(data);
				$items.html(html);

				$items.find(".addContactBtn").click(function() {
					var $td = $(this).closest("td");
					var $inputs = $($td).find("input");
					var d = {};
					$inputs.each(function() {
						var $inp = $(this);
						d[$inp.attr("name")] = $inp.val();
					})

					d.groupId = view.groupId;
					brite.dao("Contact").addContact(d).done(function(po) {
						refresh.call(view);
						$('#myModal').hide();
					})
				})

				$items.find(".contact-name").each(function() {
					var fbid = $(this).attr("fbid");
					var po = {};
					for (var i=0; i < data.length; i++) {
					  if (data[i].id == fbid) {
					  	po = data[i];
					  };
					};
					var html = $("#tmpl-MainContent-ContactDetail").render(po); 
					$(this).popover({
						html : true,
						title : 'Detail',
						trigger : 'hover',
						content : html
					})
				});
			});
			dfd.resolve();
			return dfd.promise();
		}

	});
	// --------- View Private Methods --------- //
	function refresh() {
		var view = this;
		var $e = view.$el;
		setTimeout(function() {
			brite.display("MainContent", null, {
				id : view.groupId
			});
		}, 100)
	}

	function refreshList() {
		var p = $(document).bFindComponents("GroupList");
		if (p && p.length > 0) {
			p[0].refresh();
		}
	}

	// --------- /View Private Methods --------- //
})();

