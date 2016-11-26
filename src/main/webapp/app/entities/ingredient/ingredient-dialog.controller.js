(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('IngredientDialogController', IngredientDialogController);

    IngredientDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Ingredient', 'Spice', 'Product'];

    function IngredientDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Ingredient, Spice, Product) {
        var vm = this;

        vm.ingredient = entity;
        vm.clear = clear;
        vm.save = save;
        vm.spices = Spice.query({filter: 'ingredient-is-null'});
        $q.all([vm.ingredient.$promise, vm.spices.$promise]).then(function() {
            if (!vm.ingredient.spiceId) {
                return $q.reject();
            }
            return Spice.get({id : vm.ingredient.spiceId}).$promise;
        }).then(function(spice) {
            vm.spices.push(spice);
        });
        vm.products = Product.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ingredient.id !== null) {
                Ingredient.update(vm.ingredient, onSaveSuccess, onSaveError);
            } else {
                Ingredient.save(vm.ingredient, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('companyApp:ingredientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
