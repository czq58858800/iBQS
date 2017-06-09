'use strict';

angular.module('app')
    .controller('userController', [ '$rootScope', '$scope', '$http', '$state','$stateParams',
        function($rootScope, $scope, $http, $state,$stateParams) {
            $scope.title = '用户管理';
            $scope.param = { };
            $scope.param.enable = 1;
            $scope.loading = false;



            var keyword = $state.params.keyword;
            if (keyword != null) {
                $scope.param.keyword = keyword;
            }

            $scope.search = function () {
                $scope.loading = true;
                $.ajax({
                    type: 'PUT',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/user/read/list',
                    data: angular.toJson($scope.param)
                }).then(function(result) {
                    $scope.loading = false;
                    if (result.code == 200) {
                        $scope.pageInfo = result.data;
                    } else {
                        $scope.msg = result.msg;
                    }
                    $scope.$apply();
                });
            }

            $scope.search();

            $scope.searchEnable = function (enable) {
                $scope.param.enable = enable;
                $scope.search();
            }

            $scope.clearSearch = function() {
                $scope.param.keyword= null;
                $scope.search();
            }

            $scope.update = function (param) {
                $scope.loading = true;
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/user/update',
                    data: angular.toJson(param)
                }).then(function(result) {
                    $scope.loading = false;
                    if (result.code == 200) {
                        $scope.search();
                    } else {
                        $scope.msg = result.msg;
                    }
                    $scope.$apply();
                });
            }

            $scope.disableItem = function(id, enable) {
                $scope.update({
                    id:id,
                    enable:enable
                })
            }

            $scope.updateHot = function (id,isHot) {
                $scope.update({
                    id:id,
                    isHot:isHot
                })
            }

            // 翻页
            $scope.pagination = function (page) {
                $scope.param.pageNum=page;
                $scope.search();
            };
        } ]);