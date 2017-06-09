'use strict';

angular.module('app')
    .controller('albumController', [ '$rootScope', '$scope', '$http', '$state',
        function($rootScope, $scope, $http, $state) {
            $scope.title = '表情 - 专辑管理';
            $scope.param = { };
            $scope.param.enable = 1;
            $scope.loading = false;

            var id = $state.params.id;
            if (id != null) {
                $scope.param.subjectId = id;
            }

            $scope.search = function () {
                console.dir($state.params)
                $scope.loading = true;
                $.ajax({
                    type: 'PUT',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    url : '/shuo/album/read/list',
                    data: angular.toJson($scope.param)
                }).then(function(result) {
                    $scope.loading = false;
                    if (result.code == 200) {
                        $scope.pageInfo = result;
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
                    url : '/shuo/album/delete',
                    data: {
                        id:id,
                        enable:enable
                    }
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


            // 翻页
            $scope.pagination = function (page) {
                $scope.param.pageNum=page;
                console.dir(page)
                $scope.search();
            };
        } ]);