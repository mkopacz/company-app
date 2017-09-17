(function() {
    'use strict';

    angular
        .module('companyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product-report', {
            parent: 'reports',
            url: '/product/reports',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Reports'
            },
            views: {
                'content@': {
                    templateUrl: 'app/reports/product/product-report.html',
                    controller: 'ProductReportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
    }

})();
