(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .controller('SuperheroController', SuperheroController);

    SuperheroController.$inject = ['$scope', '$state', 'Superhero'];

    function SuperheroController ($scope, $state, Superhero) {
        var vm = this;
        
        vm.superheroes = [];

        loadAll();

        function loadAll() {
            Superhero.query(function(result) {
                vm.superheroes = result;
            });
        }
    }
})();
