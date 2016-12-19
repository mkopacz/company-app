(function() {
    'use strict';

    angular
        .module('companyApp')
        .controller('ProductionItemDialogController', ProductionItemDialogController);

    ProductionItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ProductionItem', 'Product', 'Production'];

    function ProductionItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ProductionItem, Product, Production) {
        var vm = this;

        vm.productionItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.products = Product.query({filter: 'productionitem-is-null'});
        $q.all([vm.productionItem.$promise, vm.products.$promise]).then(function() {
            if (!vm.productionItem.productId) {
                return $q.reject();
            }
            return Product.get({id : vm.productionItem.productId}).$promise;
        }).then(function(product) {
            vm.products.push(product);
        });
        vm.productions = Production.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.productionItem.id !== null) {
                ProductionItem.update(vm.productionItem, onSaveSuccess, onSaveError);
            } else {
                ProductionItem.save(vm.productionItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('companyApp:productionItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
