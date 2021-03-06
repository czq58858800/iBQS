'use strict';

angular.module('app')
    .controller('topicController', [ '$rootScope', '$scope', '$http', '$state',
        function($rootScope, $scope, $http, $state) {
            $scope.title = '话题管理';
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
                    url : '/shuo/topic/read/list',
                    data: angular.toJson($scope.param)
                }).then(function(result) {
                    $scope.loading = false;
                    if (result.code == 200) {
                        $scope.pageInfo = result.data;
                        $scope.tags = result.tags;
                    } else {
                        $scope.pageInfo = null;
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

            $scope.searchAudit = function (audit) {
                $scope.param.audit = audit;
                $scope.search();
            }

            $scope.searchHot = function (isHot) {
                if ($scope.param.isHot == isHot)
                    $scope.param.isHot = null;
                else
                    $scope.param.isHot = isHot;
                $scope.search();
            }

            $scope.searchTags = function (tags) {
                if ($scope.param.tags == tags)
                    $scope.param.tags = null;
                else
                    $scope.param.tags = tags;
                $scope.search();
            }

            $scope.clearSearch = function() {
                $scope.param.keyword= null;
                $scope.search();
            }

            $scope.disableItem = function(id, enable) {
                $scope.update({
                    id:id,
                    enable:enable
                })
            }

            $scope.update = function (param) {
                $scope.loading = true;
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/topic/update',
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
            };

            $scope.updateHot = function (id,isHot) {
                if(isHot == 1 || confirm('确认要取消热门？')) {
                    var hotParams = {
                        id: id,
                        isHot: isHot
                    };
                    if (isHot == 1) {
                        hotParams["hotTime"] = new Date()
                    }
                    $scope.update(hotParams)
                }
            };

            $scope.updateAudit = function (id,audit) {
                $scope.update({
                    id:id,
                    audit:audit
                })
            };

            $scope.toSubject = function (keyword) {
                $state.go('main.face.subject.list', {keyword: keyword});
            };

            // 翻页
            $scope.pagination = function (page) {
                $scope.param.pageNum=page;
                $scope.search();
            };
        } ]);