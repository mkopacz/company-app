(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionDialogController', ProductionDialogController);

    ProductionDialogController.$inject = ['$timeout', '$scope', '$uibModalInstance', 'entity', 'Production', 'Product'];

    function ProductionDialogController ($timeout, $scope, $uibModalInstance, entity, Production, Product) {
        var vm = this;

        vm.production = entity;
        vm.clear = clear;
        vm.save = save;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        vm.products = Product.query();
        vm.addProductionItem = addProductionItem;
        vm.removeProductionItem = removeProductionItem;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function addProductionItem(index) {
            vm.production.productionItems.splice(index + 1, 0, {
                amount: null,
                id: null
            });
        }

        function removeProductionItem(index) {
            vm.production.productionItems.splice(index, 1);
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.production.id !== null) {
                Production.update(vm.production, onSaveSuccess, onSaveError);
            } else {
                Production.save(vm.production, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('companyApp:productionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
