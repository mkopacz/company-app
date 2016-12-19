(function() {
    'use strict';

    angular
        .module('companyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('production-item', {
            parent: 'entity',
            url: '/production-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProductionItems'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/production-item/production-items.html',
                    controller: 'ProductionItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('production-item-detail', {
            parent: 'entity',
            url: '/production-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProductionItem'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/production-item/production-item-detail.html',
                    controller: 'ProductionItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProductionItem', function($stateParams, ProductionItem) {
                    return ProductionItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'production-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('production-item-detail.edit', {
            parent: 'production-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/production-item/production-item-dialog.html',
                    controller: 'ProductionItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductionItem', function(ProductionItem) {
                            return ProductionItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('production-item.new', {
            parent: 'production-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/production-item/production-item-dialog.html',
                    controller: 'ProductionItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('production-item', null, { reload: 'production-item' });
                }, function() {
                    $state.go('production-item');
                });
            }]
        })
        .state('production-item.edit', {
            parent: 'production-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/production-item/production-item-dialog.html',
                    controller: 'ProductionItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductionItem', function(ProductionItem) {
                            return ProductionItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('production-item', null, { reload: 'production-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('production-item.delete', {
            parent: 'production-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/production-item/production-item-delete-dialog.html',
                    controller: 'ProductionItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProductionItem', function(ProductionItem) {
                            return ProductionItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('production-item', null, { reload: 'production-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
