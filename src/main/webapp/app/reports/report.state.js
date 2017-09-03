(function() {
    'use strict';

    angular
        .module('companyApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('report', {
            abstract: true,
            parent: 'app'
        });
    }

})();
