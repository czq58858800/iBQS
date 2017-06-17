'use strict';

angular.module('app')
    .controller('indexController', [ '$rootScope', '$scope', '$http', '$state','$stateParams',
        function($rootScope, $scope, $http, $state,$stateParams) {
            $scope.title = '首页';

        } ]);