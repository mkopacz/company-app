(function() {
    'use strict';

    angular
        .module('companyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('supply', {
            parent: 'entity',
            url: '/supply?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Supplies'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/supply/supplies.html',
                    controller: 'SupplyController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('supply-detail', {
            parent: 'entity',
            url: '/supply/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Supply'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/supply/supply-detail.html',
                    controller: 'SupplyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Supply', function($stateParams, Supply) {
                    return Supply.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'supply',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('supply-detail.edit', {
            parent: 'supply-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supply/supply-dialog.html',
                    controller: 'SupplyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Supply', function(Supply) {
                            return Supply.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('supply.new', {
            parent: 'supply',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supply/supply-dialog.html',
                    controller: 'SupplyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                serialNumber: null,
                                expirationDate: null,
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('supply', null, { reload: 'supply' });
                }, function() {
                    $state.go('supply');
                });
            }]
        })
        .state('supply.edit', {
            parent: 'supply',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supply/supply-dialog.html',
                    controller: 'SupplyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Supply', function(Supply) {
                            return Supply.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('supply', null, { reload: 'supply' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('supply.delete', {
            parent: 'supply',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/supply/supply-delete-dialog.html',
                    controller: 'SupplyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Supply', function(Supply) {
                            return Supply.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('supply', null, { reload: 'supply' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
