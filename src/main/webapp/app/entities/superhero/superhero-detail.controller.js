(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .controller('SuperheroDetailController', SuperheroDetailController);

    SuperheroDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Superhero', 'Publisher', 'Skill'];

    function SuperheroDetailController($scope, $rootScope, $stateParams, entity, Superhero, Publisher, Skill) {
        var vm = this;

        vm.superhero = entity;

        var unsubscribe = $rootScope.$on('superheroesApp:superheroUpdate', function(event, result) {
            vm.superhero = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
