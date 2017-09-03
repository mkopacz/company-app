(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('SpiceDetailController', SpiceDetailController);

    SpiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Spice'];

    function SpiceDetailController($scope, $rootScope, $stateParams, previousState, entity, Spice) {
        var vm = this;

        vm.spice = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('companyApp:spiceUpdate', function(event, result) {
            vm.spice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
