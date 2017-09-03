(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('RequestResetController', RequestResetController);

    RequestResetController.$inject = ['$timeout', 'Auth'];

    function RequestResetController ($timeout, Auth) {
        var vm = this;

        vm.success = false;

        vm.error = false;
        vm.errorEmailNotExists = false;

        vm.resetAccount = {};
        vm.requestReset = requestReset;

        $timeout(function () {
            angular.element('#email').focus();
        });

        function requestReset () {
            vm.error = false;
            vm.errorEmailNotExists = false;

            Auth.resetPasswordInit(vm.resetAccount.email)
                .then(function () {
                    vm.success = true;
                }).catch(function (response) {
                    vm.success = false;
                    if (response.status === 400 && response.data === 'e-mail address not registered') {
                        vm.errorEmailNotExists = true;
                    } else {
                        vm.error = true;
                    }
                });
        }
    }
})();
