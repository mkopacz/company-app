(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductReportController', ProductReportController);

    ProductReportController.$inject = ['Product', 'ProductReport', '$http', 'FileSaver', 'Blob'];

    function ProductReportController (Product, ProductReport, $http, FileSaver, Blob) {
        var vm = this;

        vm.products = [];
        vm.report = null;

        vm.isLoading = false;
        vm.productId = null;

        vm.getReport = loadReport;
        vm.downloadReport = downloadReport;
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

        function downloadReport() {
            vm.isLoading = true;
            download(vm.productId, function(data) {
                var blob = new Blob([data], {
                    type: 'application/pdf'
                });
                FileSaver.saveAs(blob, 'report.pdf');
                vm.isLoading = false;
            });
        }

        function download(productId, callback) {
            $http.get('api/products/' + productId + '/report', {
                headers: { accept: 'application/pdf' },
                responseType: 'arraybuffer'
            }).then(function(response) {
                callback(response.data);
            });
        }
    }

})();
