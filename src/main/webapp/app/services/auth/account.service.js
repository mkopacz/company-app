(function() {
    'use strict';

    angular
        .module('companyApp')
        .factory('Account', Account);

    Account.$inject = ['$resource'];

    function Account ($resource) {
        return $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        return response;
                    }
                }
            }
        });
    }
})();
