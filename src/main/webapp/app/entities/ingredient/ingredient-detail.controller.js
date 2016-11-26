(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('IngredientDetailController', IngredientDetailController);

    IngredientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Ingredient', 'Spice', 'Product'];

    function IngredientDetailController($scope, $rootScope, $stateParams, previousState, entity, Ingredient, Spice, Product) {
        var vm = this;

        vm.ingredient = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('companyApp:ingredientUpdate', function(event, result) {
            vm.ingredient = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
