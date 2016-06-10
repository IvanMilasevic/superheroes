(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('superhero', {
            parent: 'entity',
            url: '/superhero',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'superheroesApp.superhero.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/superhero/superheroes.html',
                    controller: 'SuperheroController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('superhero');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('superhero-detail', {
            parent: 'entity',
            url: '/superhero/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'superheroesApp.superhero.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/superhero/superhero-detail.html',
                    controller: 'SuperheroDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('superhero');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Superhero', function($stateParams, Superhero) {
                    return Superhero.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('superhero.new', {
            parent: 'superhero',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/superhero/superhero-dialog.html',
                    controller: 'SuperheroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                pseudonym: null,
                                dateOfAppearance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('superhero', null, { reload: true });
                }, function() {
                    $state.go('superhero');
                });
            }]
        })
        .state('superhero.edit', {
            parent: 'superhero',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/superhero/superhero-dialog.html',
                    controller: 'SuperheroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Superhero', function(Superhero) {
                            return Superhero.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('superhero', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('superhero.delete', {
            parent: 'superhero',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/superhero/superhero-delete-dialog.html',
                    controller: 'SuperheroDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Superhero', function(Superhero) {
                            return Superhero.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('superhero', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
