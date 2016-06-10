(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .controller('SuperheroDialogController', SuperheroDialogController);

    SuperheroDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Superhero', 'Publisher', 'Skill'];

    function SuperheroDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Superhero, Publisher, Skill) {
        var vm = this;

        vm.superhero = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.publishers = Publisher.query();
        vm.skills = Skill.query();
        vm.superheroes = Superhero.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.superhero.id !== null) {
                Superhero.update(vm.superhero, onSaveSuccess, onSaveError);
            } else {
                Superhero.save(vm.superhero, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('superheroesApp:superheroUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateOfAppearance = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
