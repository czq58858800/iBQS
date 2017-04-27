'use strict';

angular.module('app')
    .controller('commentsController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
        function($scope, $rootScope, $state, $timeout, toaster) {
                $scope.title = '表情 - 评论管理';
                $scope.param = { };
                $scope.loading = false;

                var id = $state.params.id;
                if (id != null) {
                        $scope.param.subjectId = id;
                }

                $scope.search = function () {
                        $scope.loading = true;
                        $.ajax({
                                type: 'PUT',
                                dataType: 'json',
                                contentType:'application/json;charset=UTF-8',
                                url : '/shuo/comments/read/list',
                                data: angular.toJson($scope.param)
                        }).then(function(result) {
                                $scope.loading = false;
                                if (result.code == 200) {
                                        $scope.pageInfo = result.data;
                                    console.dir($scope.pageInfo)
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

                $scope.disableItem = function(id, enable) {

                }

                // 翻页
                $scope.pagination = function (page) {
                    $scope.param.pageNum=page;
                    $scope.search();
                };
        }]);