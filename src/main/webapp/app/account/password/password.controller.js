(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('PasswordController', PasswordController);

    PasswordController.$inject = ['Auth'];

    function PasswordController (Auth) {
        var vm = this;

        vm.success = false;

        vm.error = false;
        vm.doNotMatch = false;

        vm.changePassword = changePassword;
        
        function changePassword () {
            vm.success = false;
            vm.error = false;
            vm.doNotMatch = false;

            if (vm.password !== vm.confirmPassword) {
                vm.doNotMatch = true;
            } else {
                Auth.changePassword(vm.password)
                    .then(function () {
                        vm.success = true;
                    }).catch(function () {
                        vm.error = true;
                    });
            }
        }
    }
})();
