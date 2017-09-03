(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('SpiceController', SpiceController);

    SpiceController.$inject = ['$scope', '$state', 'Spice'];

    function SpiceController ($scope, $state, Spice) {
        var vm = this;

        vm.spices = [];

        loadAll();

        function loadAll() {
            Spice.query(function(result) {
                vm.spices = result;
            });
        }
    }
})();
