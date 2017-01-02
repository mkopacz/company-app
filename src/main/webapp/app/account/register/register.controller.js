(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('RegisterController', RegisterController);


    RegisterController.$inject = [ '$timeout', 'Auth'];

    function RegisterController ($timeout, Auth) {
        var vm = this;

        vm.success = false;

        vm.error = false;
        vm.errorUserExists = false;
        vm.errorEmailExists = false;
        vm.doNotMatch = false;

        vm.registerAccount = {};
        vm.register = register;

        $timeout(function () {
            angular.element('#login').focus();
        });

        function register () {
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = true;
            } else {
                vm.error = false;
                vm.errorUserExists = false;
                vm.errorEmailExists = false;
                vm.doNotMatch = false;

                vm.registerAccount.langKey =  'pl';

                Auth.createAccount(vm.registerAccount)
                    .then(function () {
                        vm.success = true;
                    }).catch(function (response) {
                        vm.success = false;
                        if (response.status === 400 && response.data === 'login already in use') {
                            vm.errorUserExists = true;
                        } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                            vm.errorEmailExists = true;
                        } else {
                            vm.error = true;
                        }
                    });
            }
        }
    }
})();
