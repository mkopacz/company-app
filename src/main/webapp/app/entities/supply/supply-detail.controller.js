(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('SupplyDetailController', SupplyDetailController);

    SupplyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Supply', 'Spice'];

    function SupplyDetailController($scope, $rootScope, $stateParams, previousState, entity, Supply, Spice) {
        var vm = this;

        vm.supply = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('companyApp:supplyUpdate', function(event, result) {
            vm.supply = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
