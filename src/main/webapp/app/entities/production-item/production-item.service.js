(function() {
    'use strict';
    angular
        .module('companyApp')
        .factory('ProductionItem', ProductionItem);

    ProductionItem.$inject = ['$resource'];

    function ProductionItem ($resource) {
        var resourceUrl =  'api/production-items/:id';

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
