(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ResetFinishController', ResetFinishController);

    ResetFinishController.$inject = ['$stateParams', '$timeout', 'Auth', 'LoginService'];

    function ResetFinishController ($stateParams, $timeout, Auth, LoginService) {
        var vm = this;

        vm.success = false;

        vm.error = false;
        vm.doNotMatch = false;

        vm.resetAccount = {};
        vm.confirmPassword = null;
        vm.finishReset = finishReset;

        vm.login = LoginService.open;
        vm.keyMissing = angular.isUndefined($stateParams.key);

        $timeout(function () {
            angular.element('#password').focus();
        });

        function finishReset() {
            vm.error = false;
            vm.doNotMatch = false;

            if (vm.resetAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = true;
            } else {
                Auth.resetPasswordFinish({key: $stateParams.key, newPassword: vm.resetAccount.password})
                    .then(function () {
                        vm.success = true;
                    }).catch(function () {
                        vm.success = false;
                        vm.error = true;
                    });
            }
        }
    }
})();
