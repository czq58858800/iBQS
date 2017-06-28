'use strict';

angular.module('app')
    .controller('materialUpdateController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
        function($scope, $rootScope, $state, $timeout, toaster) {
            var title = "";
            if($state.includes('**.material.update')){
                title="编辑贴纸管理";
                var id = $state.params.id;
                activate(id);
                validate(id);
            }else if($state.includes('**.material.create')){
                title="添加贴纸管理";
                validate(null);
            }

            $scope.title = $rootScope.title = title;
            $scope.loading = true;
            //初始化验证
            //validate($scope);
            $scope.submit= function(){
                $scope.loading = true;
                saveData();
            };

            function saveData(){
                var m = $scope.record;
                if(m){
                    $scope.isDisabled = true;//提交disabled
                    $.ajax({
                        type: 'POST',
                        url : '/shuo/category/update',
                        dataType: 'json',
                        contentType:'application/json;charset=UTF-8',
                        data: angular.toJson(m)
                    }).then(callback);
                }
                function callback(result){
                    if(result.code ==200){//成功
                        toaster.clear('*');
                        toaster.pop('success', '', "保存成功");
                        $timeout(function(){
                            $state.go('main.face.material.list');
                        },2000);
                    }else{
                        toaster.clear('*');
                        toaster.pop('error', '', result.msg);
                        $scope.isDisabled = false;
                    }
                    $scope.loading = false;
                }
            }

            $scope.selectType = function (type) {
                if (type == null || type == '') {
                    $scope.record.type = '1';
                } else {
                    if (type == $scope.record.type) {
                        if (type == '1')
                            $scope.record.type = '0';
                        else
                            $scope.record.type = '1';
                    } else {
                        $scope.record.type = type;
                    }
                }
            }

            $scope.selectIsHot = function (isHot) {
                if (isHot == null || isHot == '') {
                    $scope.record.isHot = 'true';
                } else {
                    if (isHot == $scope.record.isHot) {
                        if (enable == 'true')
                            $scope.record.isHot = 'false';
                        else
                            $scope.record.isHot = 'true';
                    } else {
                        $scope.record.isHot = isHot;
                    }
                }
            }

            // 初始化页面
            function activate(id) {
                $scope.loading = true;
                $.ajax({
                    type: 'PUT',
                    url : '/shuo/category/read/detail',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
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
                    var temp = $scope.record.tags;
                    if(temp == null || temp == "") {
                        if (!$scope.record.tags) {
                            $scope.record.tags = tags;
                        } else {
                            $scope.record.tags += "," + tags;
                        }
                        $($event.target).removeClass("label-default");
                        $($event.target).addClass("label-success");
                    }
                }
            }

            $scope.str = function (tags,name) {
                if (tags == name) {
                    return "success";
                }
                return "default";
            }

            //表单验证
            function validate(userId){
                //notEqual 规则
                $.validator.addMethod('notEqual', function(value, ele){
                    return value != this.settings.rules[ele.name].notEqual;
                });
                jQuery('form').validate({
                    rules: {
                        value: {
                            required: true,
                            stringCheck:[],
                            maxLengthB:[10]//,
                            //isExist:['/user/checkName',userId]
                        },
                        sort: {
                            required: true
                        }
                    },
                    messages: {
                        account: {
                            required: '请填写帐号',
                            maxLengthB:"关键词不得超过{0}个字符"
                        },
                        sort: {
                            required: '请填写排序'
                        }
                    },
                    submitHandler: function() {
                        $scope.submit();
                    }
                });
            }
        }]);