(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionItemDeleteController',ProductionItemDeleteController);

    ProductionItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProductionItem'];

    function ProductionItemDeleteController($uibModalInstance, entity, ProductionItem) {
        var vm = this;

        vm.productionItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProductionItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
