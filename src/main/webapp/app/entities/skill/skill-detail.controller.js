(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .controller('SkillDetailController', SkillDetailController);

    SkillDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Skill', 'Superhero'];

    function SkillDetailController($scope, $rootScope, $stateParams, entity, Skill, Superhero) {
        var vm = this;

        vm.skill = entity;

        var unsubscribe = $rootScope.$on('superheroesApp:skillUpdate', function(event, result) {
            vm.skill = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
