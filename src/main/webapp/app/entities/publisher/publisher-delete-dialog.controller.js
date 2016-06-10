(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .controller('PublisherDeleteController',PublisherDeleteController);

    PublisherDeleteController.$inject = ['$uibModalInstance', 'entity', 'Publisher'];

    function PublisherDeleteController($uibModalInstance, entity, Publisher) {
        var vm = this;

        vm.publisher = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Publisher.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
