(function() {
    'use strict';

    angular
        .module('companyApp')
        .factory('ProductReport', ProductReport);

    ProductReport.$inject = ['$resource'];

    function ProductReport ($resource) {
        var resourceUrl =  'api/products/:id/reports';

        return $resource(resourceUrl, {}, {
            'get': { method: 'GET', isArray: true }
        });
    }

})();
