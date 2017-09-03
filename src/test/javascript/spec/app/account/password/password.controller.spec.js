'use strict';

describe('Controller Tests', function() {
    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);

    describe('PasswordController', function() {

        var $scope, $httpBackend, $q;
        var MockAuth;
        var createController;

        beforeEach(inject(function($injector) {
            $scope = $injector.get('$rootScope').$new();
            $q = $injector.get('$q');
            $httpBackend = $injector.get('$httpBackend');

            MockAuth = jasmine.createSpyObj('MockAuth', ['changePassword']);
            var locals = {
                '$scope': $scope,
                'Auth': MockAuth
            };
            createController = function() {
                $injector.get('$controller')('PasswordController as vm', locals);
            }
        }));

        it('should show error if passwords do not match', function() {
            //GIVEN
            createController();
            $scope.vm.password = 'password1';
            $scope.vm.confirmPassword = 'password2';
            //WHEN
            $scope.vm.changePassword();
            //THEN
            expect($scope.vm.doNotMatch).toEqual(true);
            expect($scope.vm.error).toEqual(false);
            expect($scope.vm.success).toEqual(false);
        });

        it('should call Auth.changePassword when passwords match', function() {
            //GIVEN
            MockAuth.changePassword.and.returnValue($q.resolve());
            createController();
            $scope.vm.password = $scope.vm.confirmPassword = 'myPassword';

            //WHEN
            $scope.$apply($scope.vm.changePassword);

            //THEN
            expect(MockAuth.changePassword).toHaveBeenCalledWith('myPassword');
        });

        it('should set success to true upon success', function() {
            //GIVEN
            MockAuth.changePassword.and.returnValue($q.resolve());
            createController();
            $scope.vm.password = $scope.vm.confirmPassword = 'myPassword';

            //WHEN
            $scope.$apply($scope.vm.changePassword);

            //THEN
            expect($scope.vm.doNotMatch).toEqual(false);
            expect($scope.vm.error).toEqual(false);
            expect($scope.vm.success).toEqual(true);
        });

        it('should notify of error if change password fails', function() {
            //GIVEN
            MockAuth.changePassword.and.returnValue($q.reject());
            createController();
            $scope.vm.password = $scope.vm.confirmPassword = 'myPassword';

            //WHEN
            $scope.$apply($scope.vm.changePassword);

            //THEN
            expect($scope.vm.doNotMatch).toEqual(false);
            expect($scope.vm.success).toEqual(false);
            expect($scope.vm.error).toEqual(true);
        });
    });
});
