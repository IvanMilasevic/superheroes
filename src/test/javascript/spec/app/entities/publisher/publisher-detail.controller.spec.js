'use strict';

describe('Controller Tests', function() {

    describe('Publisher Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPublisher;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPublisher = jasmine.createSpy('MockPublisher');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Publisher': MockPublisher
            };
            createController = function() {
                $injector.get('$controller')("PublisherDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'superheroesApp:publisherUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
