'use strict';

describe('Controller Tests', function() {

    describe('Superhero Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSuperhero, MockPublisher, MockSkill;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSuperhero = jasmine.createSpy('MockSuperhero');
            MockPublisher = jasmine.createSpy('MockPublisher');
            MockSkill = jasmine.createSpy('MockSkill');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Superhero': MockSuperhero,
                'Publisher': MockPublisher,
                'Skill': MockSkill
            };
            createController = function() {
                $injector.get('$controller')("SuperheroDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'superheroesApp:superheroUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
