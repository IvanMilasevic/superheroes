(function() {
    'use strict';
    angular
        .module('superheroesApp')
        .factory('Publisher', Publisher);

    Publisher.$inject = ['$resource'];

    function Publisher ($resource) {
        var resourceUrl =  'api/publishers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
