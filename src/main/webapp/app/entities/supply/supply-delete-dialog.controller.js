(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('SupplyDeleteController',SupplyDeleteController);

    SupplyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Supply'];

    function SupplyDeleteController($uibModalInstance, entity, Supply) {
        var vm = this;

        vm.supply = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Supply.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
