'use strict';

angular.module('app')
    .controller('topicUpdateController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
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
                            url : '/shuo/topic/read/detail',
                            data: angular.toJson({'id': id})
                        }).then(function(result) {
                            $scope.loading = false;
                            if (result.code == 200) {
                                    $scope.record = result.data;
                                    $scope.tags = result.tags;
                            } else {
                                    $scope.msg = result.msg;
                            }
                            $scope.$apply();
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


            $scope.setTags = function($event,tags) {

                if($($event.target).hasClass("label-success")){
                    var temp = $scope.record.tags;
                    if(temp.split(",").length == 1) {
                        $scope.record.tags = null;
                    } else {
                        if(temp.indexOf(tags+",") == 0){
                            $scope.record.tags = temp.replace(tags+",","")
                        } else {
                            $scope.record.tags = temp.replace(","+tags,"")
                        }
                    }
                    $($event.target).removeClass("label-success");
                    $($event.target).addClass("label-default");
                } else {
                    if (!$scope.record.tags){
                        $scope.record.tags = tags;
                    } else {
                        $scope.record.tags += ","+tags;
                    }
                    $($event.target).removeClass("label-default");
                    $($event.target).addClass("label-success");
                }
            }

                // $state.go('main.shuo.topic.update.album',{topicId:id});
        }]);