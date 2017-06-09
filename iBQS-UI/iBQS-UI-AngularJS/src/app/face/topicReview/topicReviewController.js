'use strict';

angular.module('app')
    .controller('topicReviewController', [ '$rootScope', '$scope', '$http', '$state',
        function($rootScope, $scope, $http, $state) {
            $scope.title = '话题主持人管理';
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
                    url : '/shuo/topic/review/read/list',
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

            $scope.searchTopic = function (topics) {
                $scope.param.topicId = topics.id;
                $scope.search();
            }

            $scope.search();
            
            $scope.searchEnable = function (enable) {
                $scope.param.enable = enable;
                $scope.search();
            }

            $scope.searchAudit = function (audit) {
                $scope.param.audit = audit;
                $scope.search();
            }

            $scope.clearSearch = function() {
                $scope.param.keyword= null;
                $scope.search();
            }

            $scope.disableItem = function(id) {
                if (confirm('确认要删除？')) {
                    $scope.loading = true;
                    $.ajax({
                        type: 'DELETE',
                        dataType: 'json',
                        contentType:'application/json;charset=UTF-8',
                        url : '/shuo/topic/review',
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
            };

            $scope.updateAudit = function (id,audit) {
                $scope.loading = true;
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/topic/review/updateAudit',
                    data: angular.toJson({
                        "id":id,
                        "audit":audit
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

            $scope.toSubject = function (keyword) {
                $state.go('main.face.subject.list', {keyword: keyword});
            };

            // 翻页
            $scope.pagination = function (page) {
                $scope.param.pageNum=page;
                $scope.search();
            };
        } ]);