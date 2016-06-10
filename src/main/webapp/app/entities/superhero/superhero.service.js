(function() {
    'use strict';
    angular
        .module('superheroesApp')
        .factory('Superhero', Superhero);

    Superhero.$inject = ['$resource', 'DateUtils'];

    function Superhero ($resource, DateUtils) {
        var resourceUrl =  'api/superheroes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateOfAppearance = DateUtils.convertDateTimeFromServer(data.dateOfAppearance);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
