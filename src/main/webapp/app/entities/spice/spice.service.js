(function() {
    'use strict';
    angular
        .module('companyApp')
        .factory('Spice', Spice);

    Spice.$inject = ['$resource'];

    function Spice ($resource) {
        var resourceUrl =  'api/spices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
