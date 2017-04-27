'use strict';

angular.module('app')
    .controller('subjectController', [ '$rootScope', '$scope', '$http', '$state',
        function($rootScope, $scope, $http, $state) {
            $scope.title = '表情管理';
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

            $scope.disableItem = function(id, enable) {
                $scope.loading = true;
                $.ajax({
                    type: 'DELETE',
                    dataType: 'json',
                    url : '/shuo/subject/delete',
                    data: {
                        id:id,
                        enable:enable
                    }
                }).then(function(result) {
                    $scope.loading = false;
                    if (result.httpCode == 200) {
                        $scope.search();
                    } else {
                        $scope.msg = result.msg;
                    }
                    $scope.$apply();
                });
            }

            $scope.toComments = function (subjectjectId) {
                $state.go('main.shuo.subject.update', {subjectjectId: subjectjectId});
            };

            // 翻页
            $scope.pagination = function (page) {
                $scope.param.pageNum=page;
                $scope.search();
            };
        } ]);