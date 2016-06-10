(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .controller('PublisherController', PublisherController);

    PublisherController.$inject = ['$scope', '$state', 'Publisher'];

    function PublisherController ($scope, $state, Publisher) {
        var vm = this;
        
        vm.publishers = [];

        loadAll();

        function loadAll() {
            Publisher.query(function(result) {
                vm.publishers = result;
            });
        }
    }
})();
