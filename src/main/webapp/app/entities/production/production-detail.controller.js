(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionDetailController', ProductionDetailController);

    ProductionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Production', 'ProductionItem'];

    function ProductionDetailController($scope, $rootScope, $stateParams, previousState, entity, Production, ProductionItem) {
        var vm = this;

        vm.production = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('companyApp:productionUpdate', function(event, result) {
            vm.production = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
