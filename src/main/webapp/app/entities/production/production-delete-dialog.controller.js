(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionDeleteController',ProductionDeleteController);

    ProductionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Production'];

    function ProductionDeleteController($uibModalInstance, entity, Production) {
        var vm = this;

        vm.production = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Production.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
