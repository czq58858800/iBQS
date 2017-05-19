'use strict';

angular.module('app')
    .controller('subjectController', [ '$rootScope', '$scope', '$http', '$state','$stateParams',
        function($rootScope, $scope, $http, $state,$stateParams) {
            $scope.title = '表情管理';
            $scope.param = { };
            $scope.param.enable = 1;
            $scope.loading = false;
            $scope.param.orderHotTime = 1;

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
                    url : '/shuo/subject/read/list',
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

            $scope.clearSearch = function() {
                $scope.param.keyword= null;
                $scope.search();
            }

            $scope.searchHot = function () {
                $scope.param.orderHot = true;
                $scope.search();
            }

            $scope.searchNew = function () {
                $scope.param.orderHot = null;
                $scope.search();
            }

            $scope.disableItem = function(id, enable) {
                $scope.loading = true;
                $.ajax({
                    type: 'DELETE',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/subject/delete',
                    data: angular.toJson({
                        id:id,
                        enable:enable
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
            
            $scope.updateHot = function (id,isHot) {
                $scope.loading = true;
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/subject/updateHot',
                    data: angular.toJson({
                        id:id,
                        isHot:isHot
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

            $scope.toComments = function (subjectId) {
                $state.go('main.shuo.subject.update', {subjectId: subjectId});
            };

            // 翻页
            $scope.pagination = function (page) {
                $scope.param.pageNum=page;
                $scope.search();
            };
        } ]);