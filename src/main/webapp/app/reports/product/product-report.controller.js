(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductReportController', ProductReportController);

    ProductReportController.$inject = ['Product', 'ProductReport', '$http', 'FileSaver', 'Blob', 'DateUtils'];

    function ProductReportController (Product, ProductReport, $http, FileSaver, Blob, DateUtils) {
        var vm = this;

        vm.products = [];
        vm.report = null;

        vm.isLoading = false;
        vm.reportReq = {};

        vm.getReport = loadReport;
        vm.downloadReport = downloadReport;
        vm.loadReport = loadReport;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        loadProducts();

        function loadProducts() {
            Product.query(function(data) {
                vm.products = data;
            });
        }

        function loadReport() {
            vm.isLoading = true;
            ProductReport.get({
                id : vm.reportReq.productId,
                from: DateUtils.convertLocalDateToServer(vm.reportReq.from),
                to: DateUtils.convertLocalDateToServer(vm.reportReq.to)
            }, function(data) {
                vm.report = data;
                vm.isLoading = false;
            });
        }

        function downloadReport() {
            vm.isLoading = true;
            download(vm.reportReq, function(data) {
                var blob = new Blob([data], {
                    type: 'application/pdf'
                });
                FileSaver.saveAs(blob, 'report.pdf');
                vm.isLoading = false;
            });
        }

        function download(reportReq, callback) {
            $http.get('api/products/' + reportReq.productId + '/report', {
                headers: { accept: 'application/pdf' },
                responseType: 'arraybuffer',
                params: {
                    from: DateUtils.convertLocalDateToServer(reportReq.from),
                    to: DateUtils.convertLocalDateToServer(reportReq.to)
                }
            }).then(function(response) {
                callback(response.data);
            });
        }

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }

})();
