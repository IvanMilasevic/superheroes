'use strict';

describe('Controller Tests', function() {

    describe('Skill Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSkill, MockSuperhero;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSkill = jasmine.createSpy('MockSkill');
            MockSuperhero = jasmine.createSpy('MockSuperhero');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Skill': MockSkill,
                'Superhero': MockSuperhero
            };
            createController = function() {
                $injector.get('$controller')("SkillDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'superheroesApp:skillUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
