(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth'];

    function SettingsController (Principal, Auth) {
        var vm = this;

        vm.success = false;

        vm.settingsAccount = {};
        vm.save = save;

        var copyAccount = function (account) {
            return {
                login: account.login,
                firstName: account.firstName,
                lastName: account.lastName,
                email: account.email
            };
        };

        Principal.identity()
            .then(function(account) {
                vm.settingsAccount = copyAccount(account);
            });

        function save () {
            Auth.updateAccount(vm.settingsAccount)
                .then(function() {
                    vm.success = true;
                    Principal.identity(true)
                        .then(function(account) {
                            vm.settingsAccount = copyAccount(account);
                        });
                }).catch(function() {
                    vm.success = false;
                });
        }
    }
})();
