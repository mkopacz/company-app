(function() {
    'use strict';

    angular
        .module('companyApp')
        .factory('PasswordResetFinish', PasswordResetFinish);

    PasswordResetFinish.$inject = ['$resource'];

    function PasswordResetFinish($resource) {
        return $resource('api/account/reset_password/finish', {}, {});
    }
})();
