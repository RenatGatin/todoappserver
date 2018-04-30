(function(angular) {
	var HomeController = function($scope, $rootScope, SharingService, $timeout, AppConstants, CommonService, httpService, $httpParamSerializer, $http, $state, $stateParams, toaster) {

		$scope.errorMessage = $stateParams.message;

		$scope.doLogin = function(credentials, formName) {
			if ($('#' + formName).parsley().validate()) {
				httpService.login(credentials, function(response){
					
					if (response.status == 200) {
						httpService.setTokens(response.data);
						SharingService.set('reloadedHome', false);
						CommonService.getProfile();
						
					} else {
						toaster.pop('error', 'Login error. HTTP status: ' + response.status + '. Message: ' + resonse.data.error_description);
					}
			    }, function(response){
					if (response.data && response.data.error) {
						toaster.pop('error', 'Login error. HTTP status: ' + response.status + '. Message: ' + response.data.error_description);
					} else {
						toaster.pop('error', 'Login error. Response: ' + JSON.stringify(response));
					}
			    });
			}
		}
		
		$scope.securedApi = function() {
			var url = '/api/user/ping';
			httpService.get(url, null, false, function(response){
				$scope.securedApiResponse = JSON.stringify(response);
		    }, function(response){
				$scope.securedApiResponse = JSON.stringify(response);
		    });
		};
		
		$scope.signUp = function (event) {
	        var formElement = angular.element(event.target);
	        
	        formElement.addClass('was-validated');
	        if (formElement[0].checkValidity() === false) {
	            event.preventDefault();
	            event.stopPropagation();
	            return;
	        }
	        
			toaster.pop('success', 'OK', 'Form is valid!');

	    }; 
	    
		$scope.showAlert = function() {
			swal({
				title : "Test header",
				type : "success",
				showCancelButton : true,
				confirmButtonColor : "#8FC23F",
				confirmButtonText : "Continue",
				closeOnConfirm : true
			}, function() {
				// do nothing
			});
		}

	};
	HomeController.$inject = [ '$scope', '$rootScope','SharingService', '$timeout', 'AppConstants', 'CommonService', 'httpService', '$httpParamSerializer', '$http', '$state', '$stateParams', 'toaster'];
	angular.module('todoapp.controllers').controller('HomeController', HomeController);
}(angular));