'use strict';

var req = {fiid: 'DEMO',canal: 'P', instancia: 'EMPRESAS-API'};
var reqTPGO = {fiid: 'TPGO',canal: 'K', instancia: 'EMPRESAS-API'};
var reqEFEC = {fiid: 'EFEC',canal: 'J', instancia: 'EMPRESAS-API'};
var defErr = "No se puede realizar la operación";

angular.module('apiControllers', []);

var apiApp = angular.module('apiApp', [
   'ngAnimate',
   'ngMaterial',
   'md.data.table',
   'ui.router',
   'apiControllers',
   'apiFilters',
   'apiServices',
   'apiDirectives'
]);

apiApp.config(['$compileProvider', '$mdThemingProvider', '$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider',
               function($compileProvider, $mdThemingProvider, $stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {
	$compileProvider.debugInfoEnabled(false);
	$mdThemingProvider.theme('default').primaryPalette('blue').accentPalette('red');
	$urlRouterProvider.otherwise("/login");
	
	$stateProvider
	.state('login', {
		url: "/login",
		views: {
			"tabs": {
				templateUrl: "partials/login.html",
				controller: "UserLoginController"
			}
		}
	})
	.state('tabs', {
		url: "/tabs",
		abstract: true,
		views: {
			"navbar": {
				templateUrl: "partials/navbar.html",
				controller: "NavbarController"
			},
			"tabs": {
				templateUrl: "partials/tabs.html"
			}
		}
	})
	.state('tabs.estados', {
		url: "/estados",
		views: {
			"content": {
				templateUrl: "partials/estados.html",
				controller: "EstadosController"
			}
		}
	})
	.state('tabs.instancias', {
		url: "/instancias",
		views: {
			"content": {
				templateUrl: "partials/instancias.html",
				controller: "InstanciasController"
			}
		}
	})
	.state('tabs.consultas', {
		url: "/consultas",
		views: {
			"content": {
				templateUrl: "partials/consultas.html",
				controller: "ConsultasController"
			}
		}
	})
}]);