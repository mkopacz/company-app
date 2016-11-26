(function() {
    'use strict';

    angular
        .module('companyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ingredient', {
            parent: 'entity',
            url: '/ingredient',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Ingredients'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ingredient/ingredients.html',
                    controller: 'IngredientController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('ingredient-detail', {
            parent: 'entity',
            url: '/ingredient/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Ingredient'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ingredient/ingredient-detail.html',
                    controller: 'IngredientDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Ingredient', function($stateParams, Ingredient) {
                    return Ingredient.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ingredient',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ingredient-detail.edit', {
            parent: 'ingredient-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ingredient/ingredient-dialog.html',
                    controller: 'IngredientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ingredient', function(Ingredient) {
                            return Ingredient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ingredient.new', {
            parent: 'ingredient',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ingredient/ingredient-dialog.html',
                    controller: 'IngredientDialogController',
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
                    $state.go('ingredient', null, { reload: 'ingredient' });
                }, function() {
                    $state.go('ingredient');
                });
            }]
        })
        .state('ingredient.edit', {
            parent: 'ingredient',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ingredient/ingredient-dialog.html',
                    controller: 'IngredientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ingredient', function(Ingredient) {
                            return Ingredient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ingredient', null, { reload: 'ingredient' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ingredient.delete', {
            parent: 'ingredient',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ingredient/ingredient-delete-dialog.html',
                    controller: 'IngredientDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ingredient', function(Ingredient) {
                            return Ingredient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ingredient', null, { reload: 'ingredient' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
