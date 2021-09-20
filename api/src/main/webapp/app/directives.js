'use strict';

var apiDirectives = angular.module('apiDirectives', []);

apiDirectives.directive('access', ['AuthServices', function (AuthServices) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
        	var roles = attrs.access.split(',');
        	if (roles.length > 0 ) {
        		if (AuthServices.isAuthorized(roles)) {
        			element.removeClass('ng-hide');
        		} else {
        			element.addClass('ng-hide');
        		}
        	}
        }
    };
}]);