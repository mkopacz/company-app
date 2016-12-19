(function() {
    'use strict';
    angular
        .module('companyApp')
        .factory('Production', Production);

    Production.$inject = ['$resource', 'DateUtils'];

    function Production ($resource, DateUtils) {
        var resourceUrl =  'api/productions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datetime = DateUtils.convertDateTimeFromServer(data.datetime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
