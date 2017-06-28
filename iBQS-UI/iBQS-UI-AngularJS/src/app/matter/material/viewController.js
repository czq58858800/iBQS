'use strict';

angular.module('app')
    .controller('materialViewController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
        function($scope, $rootScope, $state, $timeout, toaster) {
                var id = $state.params.id;

                $scope.title = "贴纸详情";

                activate(id);
                // 初始化页面
                function activate(id) {
                        $scope.loading = true;
                        $.ajax({
                            type: 'PUT',
                            dataType: 'json',
                            contentType:'application/json;charset=UTF-8',
                            url : '/shuo/category/read/detail',
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

                $scope.toEdit = function (id) {
                    alert(1)
                    $state.go('main.matter.material.update',{id:id});
                }


                $state.go('main.matter.material.view.sticker',{categoryId:id});
        }]);