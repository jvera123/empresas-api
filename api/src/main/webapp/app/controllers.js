'use strict';

var apiControllers = angular.module('apiControllers');

apiControllers.controller('NavbarController', ['$rootScope', '$scope', '$state', function($rootScope, $scope, $state) {
	$scope.user = $rootScope.user;
	
	$scope.salir = function () {
		delete $rootScope.user;
		$state.go('login');
	}
	
	$scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
		if(toState === 'login') return;
		if(!angular.isDefined($rootScope.user)) $state.go('login');
	});
}]);

apiControllers.controller('UserLoginController', ['$rootScope', '$scope', '$state', 'ApiService', function($rootScope, $scope, $state, ApiService) {
	$scope.acceder = function() {
		ApiService.autenticar({}, $scope.user,
		function(response) {
			$rootScope.user = response;
			$state.go('tabs.estados');
		}, function(error) {
			$scope.loginError = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
		});
	}
}]);

apiControllers.controller('RefreshMemoryController', ['$scope', '$mdDialog', function($scope, $mdDialog) {
    $scope.cerrar = function() {
    	$mdDialog.hide();
    }
}]);

apiControllers.controller('RefreshController', ['$scope', '$mdDialog', 'estado', function($scope, $mdDialog, estado) {
    $scope.estado = estado;
    $scope.cerrar = function() {
    	$mdDialog.hide();
    }
}]);

apiControllers.controller('RefreshInstanciaController', ['$scope', '$mdDialog', 'i', function($scope, $mdDialog, i) {
    $scope.i = i;
    $scope.cerrar = function() {
    	$mdDialog.hide();
    }
}]);

apiControllers.controller('EliminarInstanciaControler', ['$scope', '$mdDialog', 'i', 'ApiService', function($scope, $mdDialog, i, ApiService) {
	$scope.i = i;
	$scope.cancelar = function() {
    	$mdDialog.hide();
    }
	$scope.confirmar = function() {
		
	}
}]);

apiControllers.controller('EstadosController', ['$rootScope', '$scope', '$state', '$mdDialog', 'ApiService', 'filterFilter', 
                                               function($rootScope, $scope, $state, $mdDialog, ApiService, filterFilter) {
	$scope.init = function() {
		$scope.efecInstitucion=reqEFEC.fiid;
		ApiService.obtenerEstado(
		function(response){
			if(response.length > 0){
				$scope.estados = response;
				$scope.estado = $scope.estados[0];
			}
		},function(error){
			var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
			$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
		});
	}
	
	$scope.doRefreshMemory = function(tipo) {
		var reqRefresh =this.getType(tipo);
		$mdDialog.show({
			clickOutsideToClose: false,
			escapeToClose: false,
			template:
				'<md-dialog>' +
	            '	<md-dialog-content class="md-dialog-content">' +
	            '		<h2 class="md-title">Recargar en memoria</h2>' +
	            '		<p> Aguarde mientras se cargan las empresas en memoria </p>' + 
	            '		<md-progress-linear md-mode="indeterminate"></md-progress-linear>' +
	            '  </md-dialog-content>' +
	            '  <md-dialog-actions></md-dialog-actions>' +
	            '</md-dialog>',
		});
		ApiService.refreshMemoria({}, reqRefresh,
		function(response) {
			$mdDialog.hide();
			var alert = $mdDialog.alert().title('Recargar en memoria').textContent('Se cargaron las empresas en memoria exitosamente').ok('Aceptar');
			$mdDialog.show(alert).finally(function() {
				$scope.init();
			});
		}, function(error) {
			$mdDialog.hide();
			var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
			$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
		});
	}
	$scope.getType = function(type){
		var reqRefresh= req;
		switch (type) {
		case 'DEMO':
			reqRefresh= req;
			break;
		case 'TPGO':
			reqRefresh= reqTPGO ;
			break;
		default:
			reqRefresh= reqEFEC;
			break;
		}
		return reqRefresh;
	}
	
	$scope.doRefresh = function(tipo) {
		var reqRefresh =this.getType(tipo);
		var confirm = $mdDialog.confirm().title('Ejecutar Refresh').textContent('Confirma que desea ejecutar refresh hacia BASE24?').ok('Confirmar').cancel('Cancelar');
		$mdDialog.show(confirm).then(function() {
			$mdDialog.hide();
			$mdDialog.show({
				clickOutsideToClose: false,
				escapeToClose: false,
				template:
					'<md-dialog>' +
		            '	<md-dialog-content class="md-dialog-content">' +
		            '		<h2 class="md-title">Refresh de Empresas</h2>' +
		            '		<p> Aguarde mientras se ejecuta el Refresh de Empresas </p>' + 
		            '		<md-progress-linear md-mode="indeterminate"></md-progress-linear>' +
		            '  </md-dialog-content>' +
		            '  <md-dialog-actions></md-dialog-actions>' +
		            '</md-dialog>',
			});
			ApiService.realizarRefresh({}, reqRefresh,
			function(response) {
				$mdDialog.hide();
				$mdDialog.show({
					template:
						'<md-dialog>' +
			            '	<md-dialog-content class="md-dialog-content">' +
			            '		 <h2 class="md-title">Resultado del Refresh de Empresas</h2>' +
			            '		 <md-list>' +
			            '			<md-list-item>' +
			            '				<p><strong> Fecha </strong></p>' +
			            '				<p> {{ estado.fecha }} </p>' +
			            '			</md-list-item>' +
			            '			<md-list-item>' +
			            '				<p><strong> Rubros </strong></p>' +
			            '				<p> {{ estado.cantRubros }} </p>' +
			            '			</md-list-item>' +
			            '			<md-list-item>' +
			            '				<p><strong> Sub Rubros </strong></p>' +
			            '				<p> {{ estado.cantSubRubros }} </p>' +
			            '			</md-list-item>' +
			            '			<md-list-item>' +
			            '				<p><strong> Empresas </strong></p>' +
			            '				<p> {{ estado.cantEmpresas }} </p>' +
			            '			</md-list-item>' +
			            '			<md-list-item>' +
			            '				<p><strong> Empresas con error </strong></p>' +
			            '				<p> {{ estado.cantEmpresasError }} </p>' +
			            '			</md-list-item>' +
			            '			<md-list-item>' +
			            '				<p><strong> Prepagos </strong></p>' +
			            '				<p> {{ estado.cantPrepagos }} </p>' +
			            '			</md-list-item>' +
			            '			<md-list-item>' +
			            '				<p><strong> Prepagos con error </strong></p>' +
			            '				<p> {{ estado.cantPrepagosError }} </p>' +
			            '			</md-list-item>' +
			            '			<md-list-item>' +
			            '				<p><strong> Tiempo </strong></p>' +
			            '				<p> {{ estado.tiempo }} </p>' +
			            '			</md-list-item>' +
			            '		 </md-list>' +
						'  </md-dialog-content>' +
			            '  <md-dialog-actions>' +
			            '    <md-button ng-click="cerrar()" class="md-primary">Aceptar</md-button>' +
			            '  </md-dialog-actions>' +
			            '</md-dialog>',
		            locals: {
		                estado: response
		            },
		            controller: 'RefreshController'
				}).then(function() {
					$scope.init();
				});
			}, function(error) {
				$mdDialog.hide();
				var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
				$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
			});
		});
	}
	
	$scope.init();
}]);

apiControllers.controller('InstanciasController', ['$rootScope', '$scope', '$state', '$mdDialog', 'ApiService', 'filterFilter', 
    function($rootScope, $scope, $state, $mdDialog, ApiService, filterFilter) {
	
	$scope.init = function() {
		ApiService.obtenerInstancias(
			function (response) {
				if(response.length > 0){
					$scope.instancias = response;
				} else {
					delete($scope.instancias);
				}
			},function(error){
				var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
				$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
			});
	}
	
	$scope.refreshInstancia = function(idInstancia) {
		$mdDialog.show({
			clickOutsideToClose: false,
			escapeToClose: false,
			template:
				'<md-dialog>' +
	            '	<md-dialog-content class="md-dialog-content">' +
	            '		<h2 class="md-title">Refresh Instancia</h2>' +
	            '		<p> Actualizando listas en ' + idInstancia + '</p>' + 
	            '		<md-progress-linear md-mode="indeterminate"></md-progress-linear>' +
	            '  </md-dialog-content>' +
	            '  <md-dialog-actions></md-dialog-actions>' +
	            '</md-dialog>',
		});
		ApiService.refreshInstancia({nombre:idInstancia},{},
		function(response) {
			$mdDialog.hide();
			var alert = $mdDialog.alert().title('Refresh Instancia').textContent('Se actualizaron correctamente las listas en ' + idInstancia).ok('Aceptar');
			$mdDialog.show(alert).finally(function() {
				$scope.init();
			});
		}, function(error) {
			$mdDialog.hide();
			var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
			$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
		});
	}
	
	$scope.eliminarInstancia = function(idInstancia) {
		var confirm = $mdDialog.confirm().title('Eliminar Instancia').textContent('Confirma que desea eliminar instancia ' + idInstancia + '?').ok('Confirmar').cancel('Cancelar');
		$mdDialog.show(confirm).then(function() {
			ApiService.eliminarInstancia({nombre:idInstancia},{},
			function(response) {
				$mdDialog.hide();
				var alert = $mdDialog.alert().title('Eliminar Instancia').textContent('Instancia ' + idInstancia + ' eliminada correctamente').ok('Aceptar');
				$mdDialog.show(alert).finally(function() {
					$scope.init();
				});
			}, function(error) {
				$mdDialog.hide();
				var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
				$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
			});
		});
	}
	$scope.init();
}]);
	
	
apiControllers.controller('ConsultasController', ['$scope', '$mdDialog', 'ApiService', 'filterFilter', function($scope, $mdDialog, ApiService, filterFilter) {
	$scope.init = function() {
		$scope.institucionEFEC= reqEFEC.fiid;
		$scope.showSubRubroPMC = false;
		$scope.showSubRubroTPGO = false;
		$scope.showSubRubroEFEC= false;
		$scope.showcomboEmpresaPMC = false;
		$scope.showcomboEmpresaTGPO = false;
		$scope.showcomboEmpresaEFEC = false;
		$scope.showBuscarEmpresaPMC = false;
		$scope.showBuscarEmpresaTPGO = false;
		$scope.showBuscarEmpresaEFEC = false;
		
		delete $scope.listaPMC;
		delete $scope.rubrosPMC;
		delete $scope.rubrosTPGO;
		delete $scope.listaTPGO;
		delete $scope.rubrosEFEC;
		delete $scope.listaEFEC ;
		delete $scope.subRubroPMC;
		delete $scope.subRubroTPG0;
		delete $scope.subRubroEFEC;
		delete $scope.combEmpresas;
		delete $scope.combBuscarEmpresas;
		delete $scope.empresaPMC;
		
		//PMC
		ApiService.obtenerRubros({}, req, 
				function(response) {
			$scope.listaPMC = response;
			$scope.rubrosPMC = filterFilter($scope.listaPMC, { tipo : '0' });
			$scope.rubrosPMC = $scope.rubrosPMC.concat(filterFilter($scope.listaPMC, { idRubro : '1' }));	
			$scope.subRubroPMC = [];
			angular.forEach($scope.listaPMC, function(rubro) {
				if(rubro.tipo != '0') {
					$scope.subRubroPMC.push(rubro);
				}
			});			
		}, function(error) {
			var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
			$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
		});
	
	//MarketPlace
	ApiService.obtenerRubros({}, reqTPGO, 
			function(response) {
		$scope.listaTPGO = response;
		$scope.rubrosTPGO = filterFilter($scope.listaTPGO, { tipo : '0' });
		$scope.rubrosTPGO = $scope.rubrosTPGO.concat(filterFilter($scope.listaTPGO, { idRubro : '1' }));	
		$scope.subRubroTPGO = [];
		angular.forEach($scope.listaTPGO, function(rubro) {
			if(rubro.tipo != '0') {
				$scope.subRubroTPGO.push(rubro);
			}
		});
	}, function(error) {
		var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
		$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
	});
	
	//Cash
	ApiService.obtenerRubros({}, reqEFEC , 
			function(response) {
		$scope.listaEFEC  = response;
		$scope.rubrosEFEC = filterFilter($scope.listaEFEC, { tipo : '0' });
		$scope.rubrosEFEC = $scope.rubrosEFEC.concat(filterFilter($scope.listaEFEC, { idRubro : '1' }));	
		$scope.subRubroEFEC = [];
		angular.forEach($scope.listaEFEC, function(rubro) {
			if(rubro.tipo != '0') {
				$scope.subRubroEFEC.push(rubro);
			}
		});
	}, function(error) {
		var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
		$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
	});

	}
	
	$scope.buscarEmpresas = function(idRubro,institucion) {
		
		var requestInstitucion = this.getType(institucion);
		
		if(angular.isDefined(idRubro)) {
			$scope.combEmpresas = [];
			ApiService.obtenerEmpresas({nombre: idRubro}, requestInstitucion,
					function(response) {
				$scope.empresas = response;
				angular.forEach($scope.empresas, function(e) {
					e.isEmpresa = true;
					$scope.combEmpresas.push(e);
				});
			}, function(error) {
				var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
				$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
			});
			
			ApiService.obtenerPrepagos({nombre: idRubro}, requestInstitucion,
					function(response) {
				$scope.prepagos = response;
				angular.forEach($scope.prepagos, function(p) {
					p.isPrepago = true;
					p.nombre = p.descripcion;
					$scope.combEmpresas.push(p);
				});
			}, function(error) {
				var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
				$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
			});
		}
	}
	
	$scope.buscarEmpresa = function(institucion) {
		var requestInstitucion = this.getType(institucion);
		var nombreEmpresa;
		delete $scope.empresa;
		$scope.combBuscarEmpresas = [];
		switch (institucion) {
		case 'DEMO':
			$scope.showBuscarEmpresaDEMO = true;
			$scope.showBuscarEmpresaTPGO = false;
			$scope.showBuscarEmpresaEFEC = false;
			nombreEmpresa = $scope.nombreEmpresaDEMO;
			break;
		case 'TPGO':
			$scope.showBuscarEmpresaTPGO = true;
		    $scope.showBuscarEmpresaDEMO = false;
		    $scope.showBuscarEmpresaEFEC = false;
		    nombreEmpresa = $scope.nombreEmpresaTPGO;
			break;
		default:
		    $scope.showBuscarEmpresaEFEC = true;
			$scope.showBuscarEmpresaDEMO = false;
			$scope.showBuscarEmpresaTPGO = false;
			nombreEmpresa = $scope.nombreEmpresaEFEC;
			break;
		}
		
		ApiService.obtenerEmpresasPorNombre({nombre: nombreEmpresa}, requestInstitucion,
				function(response) {
			$scope.empresasEncontradas = response;
			angular.forEach($scope.empresasEncontradas, function(e) {
				e.isEmpresa = true;
				$scope.combBuscarEmpresas.push(e);
			});
		}, function(error) {
			var mensaje = angular.isDefined(error.data.mensaje) ? error.data.mensaje : defErr;
			$mdDialog.show($mdDialog.alert().clickOutsideToClose(false).escapeToClose(false).title('Ocurrió un Error').textContent(mensaje).ariaLabel(mensaje).ok('Aceptar'));
		});
	}
	
	$scope.setRubro = function(idRubro,institucion) {
		delete $scope.empresa;
		delete $scope.combEmpresas;
		delete $scope.subRubro;
		$scope.showcomboEmpresaPMC = false;
		$scope.showcomboEmpresaTPGO = false;
		$scope.showBuscarEmpresaEFEC= false;
		$scope.showSubRubroPMC = false;
		$scope.showSubRubroTPGO = false;
		$scope.showSubRubroEFEC = false;
		switch (institucion) {
		case 'DEMO':
			$scope.subRubro = filterFilter($scope.subRubroPMC, { tipo : idRubro });
			if($scope.subRubro.length == 0) {
				delete $scope.subRubro;
				$scope.showcomboEmpresaPMC = true;
				$scope.buscarEmpresas(idRubro,institucion);
			}else{
				$scope.showSubRubroPMC = true;
			}
			break;
		case 'TPGO':
			 $scope.subRubro = filterFilter($scope.subRubroTPGO, { tipo : idRubro });
			   if($scope.subRubro.length == 0) {
				 delete $scope.subRubro;
				 $scope.showcomboEmpresaTPGO = true;
				 $scope.buscarEmpresas(idRubro,institucion);
			   }else{
				 $scope.showSubRubroTPGO = true;
			   }
			
			
			break;
		default:
			 $scope.subRubro = filterFilter($scope.subRubroEFEC, { tipo : idRubro });
		   if($scope.subRubro.length == 0) {
			 delete $scope.subRubro;
			 $scope.showcomboEmpresaEFEC = true;
			 $scope.buscarEmpresas(idRubro,institucion);
		   }else{
			 $scope.showSubRubroEFEC = true;
		   }
			break;
		}
		
		
	}
	
	$scope.setSubRubro = function(idRubro,institucion) {
		$scope.showcomboEmpresaPMC = false;
		$scope.showcomboEmpresaTPGO = false;
		$scope.showcomboEmpresaEFEC = false;
		delete $scope.empresa;
		delete $scope.combEmpresas;
		switch (institucion) {
		case 'DEMO':
			$scope.showcomboEmpresaPMC = true;
			$scope.buscarEmpresas(idRubro,institucion);
			break;
		case 'TPGO':
			$scope.showcomboEmpresaTPGO = true;
		    $scope.buscarEmpresas(idRubro,institucion);
			break;
		default:
			$scope.showcomboEmpresaEFEC= true;
		   $scope.buscarEmpresas(idRubro,institucion);

			break;
		}
		

	}
	
	$scope.setEmpresa = function(idEmpresa) {
		$scope.empresa = filterFilter($scope.combEmpresas, { codigo : idEmpresa })[0];
	}
	
	$scope.setBuscarEmpresa = function(idEmpresa) {
		$scope.empresa = filterFilter($scope.combBuscarEmpresas, { codigo : idEmpresa })[0];
	}
	$scope.getType = function(type){
		var reqRefresh= req;
		switch (type) {
		case 'DEMO':
			reqRefresh= req;
			break;
		case 'TPGO':
			reqRefresh= reqTPGO;
			break;
		default:
			reqRefresh= reqEFEC;
			break;
		}
		return reqRefresh;
	}
	
	$scope.init();
}]);