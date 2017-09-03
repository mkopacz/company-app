(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductDialogController', ProductDialogController);

    ProductDialogController.$inject = ['$timeout', '$scope', '$uibModalInstance', 'entity', 'Product', 'Spice'];

    function ProductDialogController ($timeout, $scope, $uibModalInstance, entity, Product, Spice) {
        var vm = this;

        vm.product = entity;
        vm.clear = clear;
        vm.save = save;

        vm.spices = Spice.query();
        vm.addIngredient = addIngredient;
        vm.removeIngredient = removeIngredient;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function addIngredient(index) {
            vm.product.ingredients.splice(index + 1, 0, {
                amount: null,
                id: null
            });
        }

        function removeIngredient(index) {
            vm.product.ingredients.splice(index, 1);
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.product.id !== null) {
                Product.update(vm.product, onSaveSuccess, onSaveError);
            } else {
                Product.save(vm.product, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('companyApp:productUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
