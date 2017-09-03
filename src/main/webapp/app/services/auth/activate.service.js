(function() {
    'use strict';

    angular
        .module('companyApp')
        .factory('Activate', Activate);

    Activate.$inject = ['$resource'];

    function Activate ($resource) {
        return $resource('api/activate', {}, {});
    }
})();
