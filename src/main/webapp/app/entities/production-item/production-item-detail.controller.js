(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionItemDetailController', ProductionItemDetailController);

    ProductionItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProductionItem', 'Product', 'Production'];

    function ProductionItemDetailController($scope, $rootScope, $stateParams, previousState, entity, ProductionItem, Product, Production) {
        var vm = this;

        vm.productionItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('companyApp:productionItemUpdate', function(event, result) {
            vm.productionItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
