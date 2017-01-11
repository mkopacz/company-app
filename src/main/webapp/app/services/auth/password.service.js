(function() {
    'use strict';

    angular
        .module('companyApp')
        .factory('Password', Password);

    Password.$inject = ['$resource'];

    function Password($resource) {
        return $resource('api/account/change_password', {}, {});
    }
})();
