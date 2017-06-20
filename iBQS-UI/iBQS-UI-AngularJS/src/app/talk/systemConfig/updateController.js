'use strict';

angular.module('app')
    .controller('systemConfigUpdateController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
        function($scope, $rootScope, $state, $timeout, toaster) {
            var title = "";
            if($state.includes('**.system.config.update')){
                title="编辑App配置管理";
                var id = $state.params.id;
                activate(id);
                validate(id);
            }else if($state.includes('**.system.config.create')){
                title="添加App配置管理";
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
                        url : '/shuo/systemConfig',
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
                            $state.go('main.talk.system.config.list');
                        },2000);
                    }else{
                        toaster.clear('*');
                        toaster.pop('error', '', result.msg);
                        $scope.isDisabled = false;
                    }
                    $scope.loading = false;
                }
            }

            // 初始化页面
            function activate(id) {
                $scope.loading = true;
                $.ajax({
                    type: 'PUT',
                    url : '/shuo/systemConfig/read/detail',
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    data: angular.toJson({'id': id})
                }).then(function(result) {
                    $scope.loading = false;
                    if (result.code == 200) {
                        $scope.record = result.data;
                    } else {
                        $scope.msg = result.msg;
                    }
                    $scope.$apply();
                });
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