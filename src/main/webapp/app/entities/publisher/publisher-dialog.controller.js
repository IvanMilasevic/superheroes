(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .controller('PublisherDialogController', PublisherDialogController);

    PublisherDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Publisher'];

    function PublisherDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Publisher) {
        var vm = this;

        vm.publisher = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.publisher.id !== null) {
                Publisher.update(vm.publisher, onSaveSuccess, onSaveError);
            } else {
                Publisher.save(vm.publisher, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('superheroesApp:publisherUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
