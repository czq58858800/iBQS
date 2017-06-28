'use strict';

angular.module('app')
    .controller('materialController', [ '$rootScope', '$scope', '$http', '$state','$stateParams',
        function($rootScope, $scope, $http, $state,$stateParams) {
            $scope.title = '贴纸管理';
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
                    url : '/shuo/category/read/list',
                    data: angular.toJson($scope.param)
                }).then(function(result) {
                    $scope.loading = false;
                    if (result.code == 200) {
                        $scope.pageInfo = result.data;
                        console.info(result)
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

            $scope.searchType = function (type) {
                if ($scope.param.type == type)
                    $scope.param.type = null;
                if (type == '0')
                    $scope.param.type = null;
                else
                    $scope.param.type = type;
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
                    url : '/shuo/category/update',
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
                        type: 'POST',
                        dataType: 'json',
                        contentType: 'application/json;charset=UTF-8',
                        url: '/shuo/category/delete',
                        data: angular.toJson({
                            "id": id
                        })
                    }).then(function (result) {
                        $scope.loading = false;
                        if (result.code == 200) {
                            $scope.search();
                        } else if (result.code == 405) {
                            $scope.msg = "贴纸不为空不允许删除"
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

            $scope.setPageNum = function () {
                $scope.search();
            }
            
            $scope.updateHot = function (id,isHot) {
                $scope.update({
                    id:id,
                    isHot:isHot
                })
            }

            $scope.toSticker = function (categoryId) {
                $state.go('main.matter.material.update.sticker', {categoryId: categoryId});
            };

            // 翻页
            $scope.pagination = function (page) {
                $scope.param.pageNum=page;
                $scope.search();
            };
        } ]);