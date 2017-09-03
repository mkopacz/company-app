(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('SupplyDialogController', SupplyDialogController);

    SupplyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Supply', 'Spice'];

    function SupplyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Supply, Spice) {
        var vm = this;

        vm.supply = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.spices = Spice.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.supply.id !== null) {
                Supply.update(vm.supply, onSaveSuccess, onSaveError);
            } else {
                Supply.save(vm.supply, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('companyApp:supplyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.expirationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
