(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .controller('PublisherDetailController', PublisherDetailController);

    PublisherDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Publisher'];

    function PublisherDetailController($scope, $rootScope, $stateParams, entity, Publisher) {
        var vm = this;

        vm.publisher = entity;

        var unsubscribe = $rootScope.$on('superheroesApp:publisherUpdate', function(event, result) {
            vm.publisher = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
