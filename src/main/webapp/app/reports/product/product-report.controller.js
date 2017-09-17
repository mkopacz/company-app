(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductReportController', ProductReportController);

    ProductReportController.$inject = ['Product', 'ProductReport'];

    function ProductReportController (Product, ProductReport) {
        var vm = this;

        vm.products = [];
        vm.report = null;

        vm.isLoading = false;
        vm.productId = null;

        vm.loadReport = loadReport;

        loadProducts();

        function loadProducts() {
            Product.query(function(data) {
                vm.products = data;
            });
        }

        function loadReport() {
            vm.isLoading = true;
            ProductReport.get({id : vm.productId}, function(data) {
                vm.report = data;
                vm.isLoading = false;
            });
        }
    }

})();
