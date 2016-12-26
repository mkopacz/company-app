(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionController', ProductionController);

    ProductionController.$inject = ['Production'];

    function ProductionController (Production) {
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
