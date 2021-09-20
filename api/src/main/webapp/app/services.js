'use strict';

var apiServices = angular.module('apiServices', ['ngResource']);

apiServices.factory('ApiService', ['$resource', function ($resource) {
	return $resource('/empresas-api/servicios/:action/:nombre', { action: '@action', nombre: '@nombre' }, {
		autenticar: { method: 'POST', isArray: false, params: { action: 'autenticar' } },
		realizarRefresh: { method: 'POST', isArray: false, params: { action: 'refresh'} },
		refreshMemoria: { method: 'POST', isArray: false, params: {action: 'actualizarMemoria'}},
		refreshInstancia: { method: 'POST', isArray: false, params: { action: 'notificar'} },
		obtenerEstado: { method: 'GET', isArray: true, params: { action: 'estados'} },
		obtenerInstancias: { method: 'GET', isArray: true, params: {action: 'instancias'} },
		obtenerRubros: { method: 'POST', isArray: true, params: {action: 'rubros'}},
		obtenerEmpresas: { method: 'POST', isArray: true, params: {action: 'empresas'}},
		obtenerEmpresasPorNombre: { method: 'POST', isArray: true, params: {action: 'empresasPorNombre'}},
		obtenerPrepagos: { method: 'POST', isArray: true, params: {action: 'prepagos'}},
		eliminarInstancia: { method: 'DELETE', isArray: false, params: {action: 'instancias'}},
	});
}]);

apiServices.service('AuthServices', ['$rootScope', function ($rootScope) {
    return {
        isAuthorized: function (authorizedRoles) {
            if (!angular.isArray(authorizedRoles)) {
                if (authorizedRoles == '*') {
                    return true;
                }
                authorizedRoles = [authorizedRoles];
            }
            var isAuthorized = false;
            if(!angular.isDefined($rootScope.user)) return false;
            angular.forEach(authorizedRoles, function (authorizedRole) {
            	angular.forEach($rootScope.user.listaRoles, function(rol) {
            		if(rol === authorizedRole) {
            			isAuthorized = true;
            		}
            	})
            });
            return isAuthorized;
        }
    };
}]);