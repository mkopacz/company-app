'use strict';

describe('Controller Tests', function() {

    describe('ProductionItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProductionItem, MockProduct, MockProduction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProductionItem = jasmine.createSpy('MockProductionItem');
            MockProduct = jasmine.createSpy('MockProduct');
            MockProduction = jasmine.createSpy('MockProduction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProductionItem': MockProductionItem,
                'Product': MockProduct,
                'Production': MockProduction
            };
            createController = function() {
                $injector.get('$controller')("ProductionItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'companyApp:productionItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
