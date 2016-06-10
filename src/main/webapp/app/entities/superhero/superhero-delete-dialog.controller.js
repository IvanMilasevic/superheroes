(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .controller('SuperheroDeleteController',SuperheroDeleteController);

    SuperheroDeleteController.$inject = ['$uibModalInstance', 'entity', 'Superhero'];

    function SuperheroDeleteController($uibModalInstance, entity, Superhero) {
        var vm = this;

        vm.superhero = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Superhero.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
