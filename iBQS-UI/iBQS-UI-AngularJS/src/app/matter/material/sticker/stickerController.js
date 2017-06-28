'use strict';

angular.module('app')
    .controller('stickerController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
        function($scope, $rootScope, $state, $timeout, toaster) {
                $scope.title = '贴纸管理';
                $scope.param = { };
                $scope.loading = false;

                var categoryId = $state.params.id;
                if (categoryId != null) {
                   $scope.param.categoryId = categoryId;
                   $scope.param.pageSize = 24;
                }

                $scope.search = function () {
                        $scope.loading = true;
                        $.ajax({
                                type: 'PUT',
                                dataType: 'json',
                                contentType:'application/json;charset=UTF-8',
                                url : '/shuo/material/read/list',
                                data: angular.toJson($scope.param)
                        }).then(function(result) {
                                $scope.loading = false;
                                if (result.code == 200) {
                                        $scope.pageInfo = result.data;
                                } else if(result.code == 405) {
                                    $scope.pageInfo = null;
                                } else {
                                   $scope.msg = result.msg;
                                }
                                $scope.$apply();
                        });
                }

                $scope.search();

                $scope.clearSearch = function() {
                        $scope.param.parentId = null;
                        $scope.param.keyword= null;
                        $scope.search();
                }

            $scope.disableItem = function(id) {
                if (confirm('确认要删除？')) {
                    $scope.loading = true;
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        contentType: 'application/json;charset=UTF-8',
                        url: '/shuo/material/delete',
                        data: angular.toJson({
                            "id": id
                        })
                    }).then(function (result) {
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

            $scope.batchDelete = function(id) {
                if (confirm('确认要删除？')) {
                    $scope.loading = true;
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        contentType: 'application/json;charset=UTF-8',
                        url: '/shuo/material/batchDelete',
                        data: angular.toJson({
                            "id": id
                        })
                    }).then(function (result) {
                        $scope.loading = false;
                        if (result.code == 200) {
                            $scope.msg = result.msg;
                            // $scope.search();
                        } else {
                            $scope.msg = result.msg;
                        }
                        $scope.$apply();
                    });
                }
            }

                // 翻页
                $scope.pagination = function (page) {
                    $scope.param.pageNum=page;
                    $scope.search();
                };
        }]);