(function() {
    'use strict';

    angular
        .module('companyApp')
        .factory('PasswordResetInit', PasswordResetInit);

    PasswordResetInit.$inject = ['$resource'];

    function PasswordResetInit($resource) {
        return $resource('api/account/reset_password/init', {}, {});
    }
})();
