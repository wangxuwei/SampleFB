( function(d) {
		var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
		if (d.getElementById(id)) {
			return;
		}
		js = d.createElement('script');
		js.id = id;
		js.async = true;
		js.src = "//connect.facebook.net/en_US/all.js";
		ref.parentNode.insertBefore(js, ref);
	}(document));

function fbLogin() {
	 FB.login(function(response) {
        if (response.authResponse) {
			doFBLogin(response.authResponse).done(function(user) {
					console.log(user);
					if ( typeof user == "object") {
						window.location = contextPath;
					}
				});
        } else {
        }
    });
}

  function doFBLogin(responseData) {
            return $.ajax({
                type:"POST",
                url:"fbLogin.do",
                data:responseData,
                dataType:"json"
            }).pipe(function (val) {
                        return val;
                    });
        }


// Additional JS functions here
window.fbAsyncInit = function() {
	FB.init({
		appId : '504604412891475', // App ID
		status : true, // check login status
		cookie : true, // enable cookies to allow the server to access the session
		xfbml : true // parse XFBML
	});
	// Additional init code here
	// Additional init code here
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			// connected
			$(".btnFBLogin").show();
		} else if (response.status === 'not_authorized') {
			// not_authorized
			fbLogin();
		} else {
			// not_logged_in
			fbLogin();
		}
	});
}; 