(function() {
    'use strict';

    angular
        .module('companyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('spice', {
            parent: 'entity',
            url: '/spice',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Spices'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spice/spices.html',
                    controller: 'SpiceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('spice-detail', {
            parent: 'entity',
            url: '/spice/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Spice'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spice/spice-detail.html',
                    controller: 'SpiceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Spice', function($stateParams, Spice) {
                    return Spice.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'spice',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('spice-detail.edit', {
            parent: 'spice-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spice/spice-dialog.html',
                    controller: 'SpiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Spice', function(Spice) {
                            return Spice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spice.new', {
            parent: 'spice',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spice/spice-dialog.html',
                    controller: 'SpiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                producer: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('spice', null, { reload: 'spice' });
                }, function() {
                    $state.go('spice');
                });
            }]
        })
        .state('spice.edit', {
            parent: 'spice',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spice/spice-dialog.html',
                    controller: 'SpiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Spice', function(Spice) {
                            return Spice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spice', null, { reload: 'spice' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spice.delete', {
            parent: 'spice',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spice/spice-delete-dialog.html',
                    controller: 'SpiceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Spice', function(Spice) {
                            return Spice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spice', null, { reload: 'spice' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
