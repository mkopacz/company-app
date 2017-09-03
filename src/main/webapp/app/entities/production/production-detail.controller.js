(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionDetailController', ProductionDetailController);

    ProductionDetailController.$inject = ['$scope', '$rootScope', 'previousState', 'entity'];

    function ProductionDetailController($scope, $rootScope, previousState, entity) {
        var vm = this;

        vm.production = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('companyApp:productionUpdate', function(event, result) {
            vm.production = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
