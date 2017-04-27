'use strict';

angular.module('app')
    .controller('subjectUpdateController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
        function($scope, $rootScope, $state, $timeout, toaster) {
                var id = $state.params.id;

                activate(id);
                // 初始化页面
                function activate(id) {
                        $scope.loading = true;
                        $.ajax({
                            type: 'PUT',
                            dataType: 'json',
                            contentType:'application/json;charset=UTF-8',
                            url : '/shuo/subject/read/detail',
                            data: angular.toJson({'id': id})
                        }).then(function(result) {
                            $scope.loading = false;
                            if (result.code == 200) {
                                    $scope.record = result.data;
                                    getUserInfo(result.data.userId)
                            } else {
                                    $scope.msg = result.msg;
                            }
                        });
                }
                
                function getUserInfo(userId) {
                    $.ajax({
                        type: 'PUT',
                        dataType: 'json',
                        contentType:'application/json;charset=UTF-8',
                        url : '/shuo/user/read/brief',
                        data: angular.toJson({'id': userId})
                    }).then(function(result) {
                        $scope.loading = false;
                        if (result.code == 200) {
                            $scope.record.user = result.data;
                        }
                        else {
                            $scope.msg = result.msg;
                        }
                        $scope.$apply();
                    });
                }



                $state.go('main.shuo.subject.update.album',{subjectId:id});
        }]);