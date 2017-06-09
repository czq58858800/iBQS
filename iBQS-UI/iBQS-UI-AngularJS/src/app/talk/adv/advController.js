'use strict';

angular.module('app')
    .controller('advController', [ '$rootScope', '$scope', '$http', '$state','$stateParams',
        function($rootScope, $scope, $http, $state,$stateParams) {
            $scope.title = '广告管理';
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
                    url : '/shuo/adv/read/list',
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
                    url : '/shuo/adv/update',
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

            $scope.disableItem = function(id) {
                if (confirm('确认要删除？')) {
                    $scope.loading = true;
                    $.ajax({
                        type: 'DELETE',
                        dataType: 'json',
                        contentType:'application/json;charset=UTF-8',
                        url : '/shuo/adv',
                        data: angular.toJson({
                            "id":id
                        })
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