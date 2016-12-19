(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionItemController', ProductionItemController);

    ProductionItemController.$inject = ['$scope', '$state', 'ProductionItem'];

    function ProductionItemController ($scope, $state, ProductionItem) {
        var vm = this;

        vm.productionItems = [];

        loadAll();

        function loadAll() {
            ProductionItem.query(function(result) {
                vm.productionItems = result;
            });
        }
    }
})();
