(function() {
    'use strict';

    angular
        .module('superheroesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('publisher', {
            parent: 'entity',
            url: '/publisher',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'superheroesApp.publisher.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/publisher/publishers.html',
                    controller: 'PublisherController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('publisher');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('publisher-detail', {
            parent: 'entity',
            url: '/publisher/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'superheroesApp.publisher.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/publisher/publisher-detail.html',
                    controller: 'PublisherDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('publisher');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Publisher', function($stateParams, Publisher) {
                    return Publisher.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('publisher.new', {
            parent: 'publisher',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publisher/publisher-dialog.html',
                    controller: 'PublisherDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('publisher', null, { reload: true });
                }, function() {
                    $state.go('publisher');
                });
            }]
        })
        .state('publisher.edit', {
            parent: 'publisher',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publisher/publisher-dialog.html',
                    controller: 'PublisherDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Publisher', function(Publisher) {
                            return Publisher.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('publisher', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('publisher.delete', {
            parent: 'publisher',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publisher/publisher-delete-dialog.html',
                    controller: 'PublisherDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Publisher', function(Publisher) {
                            return Publisher.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('publisher', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
