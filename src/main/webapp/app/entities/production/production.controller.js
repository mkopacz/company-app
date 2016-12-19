(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionController', ProductionController);

    ProductionController.$inject = ['$scope', '$state', 'Production'];

    function ProductionController ($scope, $state, Production) {
        var vm = this;

        vm.productions = [];

        loadAll();

        function loadAll() {
            Production.query(function(result) {
                vm.productions = result;
            });
        }
    }
})();
