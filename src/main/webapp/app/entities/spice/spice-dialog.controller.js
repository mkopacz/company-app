(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('SpiceDialogController', SpiceDialogController);

    SpiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Spice'];

    function SpiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Spice) {
        var vm = this;

        vm.spice = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.spice.id !== null) {
                Spice.update(vm.spice, onSaveSuccess, onSaveError);
            } else {
                Spice.save(vm.spice, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('companyApp:spiceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
