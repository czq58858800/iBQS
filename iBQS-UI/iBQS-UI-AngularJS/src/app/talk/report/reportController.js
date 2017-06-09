'use strict';

angular.module('app')
    .controller('reportController', [ '$rootScope', '$scope', '$http', '$state','$stateParams',
        function($rootScope, $scope, $http, $state,$stateParams) {
            $scope.title = '举报管理';
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
                    url : '/shuo/report/read/list',
                    data: angular.toJson($scope.param)
                }).then(function(result) {
                    $scope.loading = false;
                    if (result.code == 200) {
                        $scope.pageInfo = result.data;
                        angular.forEach(result.data.records, function(data){
                            var userId = data.userId;
                            getUserInfo(userId,function (userResult) {
                                data.user = userResult.data;
                            });

                            var type = data.type;
                            if (type == '1') {  // 表情
                                getSubjectInfo(data.valId,function (subjectResult) {
                                    data.valItem = subjectResult.data;
                                });
                            } else if (type == '2') { // 评论
                                getCommentsInfo(data.valId,function (commentsResult) {
                                    data.valItem = commentsResult.data;
                                });
                            } else if (type == '3') { // 用户
                                getUserInfo(data.valId,function (userResult) {
                                    data.valItem = userResult.data;
                                });
                            }
                        });
                    } else {
                        $scope.msg = result.msg;
                    }
                    $scope.$apply();
                });
            }



            function getCommentsInfo(subjectId,callback) {
                $.ajax({
                    async:false,
                    type: 'PUT',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/comments/read/detail',
                    data: angular.toJson({'id': subjectId})
                }).then(function(result) {
                    callback(result);
                });
            }

            function getSubjectInfo(subjectId,callback) {
                $.ajax({
                    async:false,
                    type: 'PUT',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/subject/read/detail',
                    data: angular.toJson({'id': subjectId})
                }).then(function(result) {
                    callback(result);
                });
            }

            function getUserInfo(userId,callback) {
                $.ajax({
                    async:false,
                    type: 'PUT',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/user/read/brief',
                    data: angular.toJson({'id': userId})
                }).then(function(result) {
                    callback(result);
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
                    url : '/shuo/report/update',
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