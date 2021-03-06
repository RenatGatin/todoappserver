(function(angular) {

	var SharingService = function() {
		function set(key, data) {
			sessionStorage.setItem(key, JSON.stringify(data));
		}

		function get(key) {
			return JSON.parse(sessionStorage.getItem(key));
		}

		var clearAllData = function() {
			var i = sessionStorage.length;
			while (i--) {
				var key = sessionStorage.key(i);
				sessionStorage.removeItem(key);
			}
		}

		return {
			set : set,
			get : get,
			clear : clearAllData
		};
	};

	/**
	 * Service used to make Http POST request with multipart/form-data
	 */
	var HttpMultipartFormService = function($http) {
		return this.post = function(uploadUrl, data) {
			var fd = new FormData();

			for ( var key in data)
				fd.append(key, data[key]);

			$http.post(uploadUrl, fd, {
				transformRequest : angular.identity,
				headers : {
					'Content-Type' : undefined
				}
			});
		};
	};
	HttpMultipartFormService.$inject = [ '$http' ];
	
	var CommonService = function($rootScope, AppConstants, httpService, toaster, $cookies, $state, $window, SharingService) {
		function checkSession(redirectState) {
			var accessToken = $cookies.get('access_token');
			if (accessToken && accessToken.length > 0) {
				getProfile();
			} else {
				cleanCacheAndGo(redirectState);
			}
		}
		
		function getProfile() {
			httpService.get(AppConstants.BASE_URL + AppConstants.GET_API_COMMON_PROFILE, null, false, function(response){
				if (response.status == 200) {
					var data = response.data;
					if (data.status.code == AppConstants.SUCCESS) {
						$rootScope.profile = data.entity;
						if ($state.current && !$state.current.secured) {
							$state.go('dashboard');							
						}
					} else {
						toaster.pop('error', 'Error fetching profile data. Status message: ' + data.status.message);
					}
				} else {
					toaster.pop('error', 'Error fetching profile data. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
				}
		    }, function(response){
				toaster.pop('error', 'Error fetching profile data. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
		    });
		}
		
		function logout() {
			var accessToken = $cookies.get('access_token');
			if (accessToken && accessToken.length > 0) {
			
				httpService.logout(accessToken, function(response){
					if (response.status == 200) {
						var data = response.data;
						if (data.status.code == AppConstants.SUCCESS || data.status.code == AppConstants.ACCESS_TOKEN_NOT_FOUND) {
							cleanCacheAndGo('home');
							
						} else {
							toaster.pop('error', 'Logout error. Status message: ' + data.status.message);
						}
					} else {
						toaster.pop('error', 'Logout error. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
					}
			    }, function(response){
					if (response.data && response.data.error) {
						toaster.pop('error', 'Logout error. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
					} else {
						toaster.pop('error', 'Logout error. Response: ' + JSON.stringify(response));
					}
			    });
			} else {
				cleanCacheAndGo('home');
			}
		}
		
		function cleanCacheAndGo(redirectState) {
			$cookies.remove("access_token");
			$cookies.remove("refresh_token");
			$rootScope.profile = null;

			if (redirectState && redirectState != $state.current.name) {
				$state.go(redirectState);
			}
		}

		return {
			getProfile    : getProfile,
			checkSession  : checkSession,
			logout        : logout
		};
	};
	CommonService.$inject = [ '$rootScope', 'AppConstants', 'httpService', 'toaster', '$cookies', '$state', '$window', 'SharingService' ];

	angular.module("todoapp")
			.factory("SharingService", SharingService)
			.factory("HttpMultipartFormService", HttpMultipartFormService)
			.factory("CommonService", CommonService);
}(angular));