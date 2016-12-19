(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionDialogController', ProductionDialogController);

    ProductionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Production', 'ProductionItem'];

    function ProductionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Production, ProductionItem) {
        var vm = this;

        vm.production = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.productionitems = ProductionItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

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

        vm.datePickerOpenStatus.datetime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
