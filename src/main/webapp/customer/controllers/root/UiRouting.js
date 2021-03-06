(function(angular) {
	var UiRouting = function($urlRouterProvider, $stateProvider, $httpProvider) {

		$urlRouterProvider.otherwise('/home');

		$stateProvider

		// ///////////////////
		// ///// HOME ////////
		// ///////////////////

		.state('home', {
			url 		: '/home',
			templateUrl : './pages/root/home.html',
			controller  : 'HomeController', 
			params		: {message : null}
		})
		
		.state('sign-in', {
			url 		: '/sign-in',
			templateUrl : './pages/root/sign-in/sign-in.html',
			controller  : 'SignInController', 
			data: {
			    css: './pages/root/sign-in/sign-in.css'
			}
		})
		
		.state('password-reset', {
			url 		: '/password-reset',
			templateUrl : './pages/root/sign-in/password-reset.html',
			controller  : 'PasswordResetController', 
			data: {
			    css: './pages/root/sign-in/sign-in.css'
			},
			resolve : {
				loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
					return $ocLazyLoad.load({
						files: ['./controllers/root/PasswordResetController.js']
					});						
				}]
			},
		})
		
		.state('password-do-reset', {
			url 		: '/password-do-reset/{resetkey}',
			templateUrl : './pages/root/sign-in/password-do-reset.html',
			controller  : 'PasswordDoResetController', 
			data: {
			    css: './pages/root/sign-in/sign-in.css'
			},
			resolve : {
				loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
					return $ocLazyLoad.load({
						files: ['./controllers/root/PasswordDoResetController.js']
					});						
				}]
			},
		})
		
		.state('sign-up', {
			url 		: '/sign-up',
			templateUrl : './pages/root/sign-up/sign-up.html',
			controller  : 'SignUpController', 
			data: {
			    css: './pages/root/sign-up/sign-up.css'
			},
			resolve : {
				loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
					return $ocLazyLoad.load({
						files: ['./controllers/root/SignUpController.js']
					});						
				}]
			},
			params : { signup: null }
		})
		
		.state('unauthorized', {
			url 		: '/unauthorized',
			templateUrl : '../common/pages/root/unauthorized.html',
			controller  : 'UnauthorizedController', 
			params		: {message : null}
		})
		
		.state('server-error', {
			url 		: '/server-error',
			templateUrl : '../common/pages/root/server-error.html',
			controller  : 'UnauthorizedController', 
			params		: {message : null}
		})
		
		.state('secured', {
			url : '/secured',
			templateUrl : './pages/secure/secured.html',
			controller : 'SecuredController', //will be loaded lazily from 'resolve'
			secured: true,
			resolve : {
				loadMyCtrl: ['$ocLazyLoad', '$cookies', function($ocLazyLoad, $cookies) {
					var access_token = $cookies.get('access_token');
					if (access_token) {
						return $ocLazyLoad.load({
							files: ['./controllers/secure/SecuredController.js?access_token=' + access_token]
						});						
					}
				}]
			}
		})
		
		.state('dashboard', {
			url : '/dashboard',
			templateUrl : './pages/secure/dashboard/dashboard.html',
			controller : 'DashboardController',
			secured: true,
			resolve : {
				loadMyCtrl: ['$ocLazyLoad', '$cookies', function($ocLazyLoad, $cookies) {
					var access_token = $cookies.get('access_token');
					if (access_token) {
						return $ocLazyLoad.load({
							files: ['./controllers/secure/dashboard/DashboardController.js?access_token=' + access_token]
						});						
					}
				}]
			}
		})
		
		.state('list', {
			url : '/dashboard/list/{id}',
			templateUrl : './pages/secure/dashboard/list.html',
			controller : 'ListController',
			secured: true,
			resolve : {
				loadMyCtrl: ['$ocLazyLoad', '$cookies', function($ocLazyLoad, $cookies) {
					var access_token = $cookies.get('access_token');
					if (access_token) {
						return $ocLazyLoad.load({
							files: ['./controllers/secure/dashboard/ListController.js?access_token=' + access_token]
						});						
					}
				}]
			}
		})
	};

	UiRouting.$inject = [ '$urlRouterProvider', '$stateProvider', '$httpProvider'];
	angular.module('todoapp.routers').config(UiRouting);
}(angular));