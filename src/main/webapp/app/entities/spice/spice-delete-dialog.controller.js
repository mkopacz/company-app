(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('SpiceDeleteController',SpiceDeleteController);

    SpiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Spice'];

    function SpiceDeleteController($uibModalInstance, entity, Spice) {
        var vm = this;

        vm.spice = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Spice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
