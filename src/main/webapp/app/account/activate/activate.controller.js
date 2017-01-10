(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ActivationController', ActivationController);

    ActivationController.$inject = ['$stateParams', 'Auth'];

    function ActivationController ($stateParams, Auth) {
        var vm = this;

        Auth.activateAccount({key: $stateParams.key})
            .then(function () {
                vm.error = false;
                vm.success = true;
            }).catch(function () {
                vm.success = false;
                vm.error = true;
            });
    }
})();
