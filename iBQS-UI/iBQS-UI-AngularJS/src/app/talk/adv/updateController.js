'use strict';

angular.module('app')
    .controller('advUpdateController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
        function($scope, $rootScope, $state, $timeout, toaster) {
            var title = "";
            $scope.myImage='';
            $scope.record = {};
            if($state.includes('**.adv.update')){
                title="编辑广告";
                var id = $state.params.id;
                activate(id);
                validate(id);
            }else if($state.includes('**.adv.create')){
                title="添加广告";
                validate(null);
            }

            $scope.title = $rootScope.title = title;
            $scope.loading = true;
            //初始化验证
            //validate($scope);

            $.ajax({
                url : '/qiniuUpToken'
            }).then(function(result) {
                if (result.code == 200) {
                    $scope.qiniuToken = result.data;
                } else {
                    $scope.msg = result.msg;
                }
            });

            $scope.submit= function(){
                $scope.loading = true;
                if ($scope.myImage != null && $scope.myImage != "" ) {
                    console.log($scope.qiniuToken.uptoken)
                    console.log($scope.qiniuToken.domain)
                    $scope.uploadQiniu({
                        fileData:$scope.myImage,
                        key:getFormatKey("banner",$scope.myImage.name),
                        uptoken:$scope.qiniuToken.uptoken,
                        success:function (data,res) {
                            console.log("data:",data)
                            console.log("res:",res)
                            var imagePath = $scope.qiniuToken.domain+data.key;
                            $scope.record['image'] = imagePath;
                            saveData();
                        },
                        error:function (res) {
                            $scope.msg = "图片上传失败";
                        }
                    });
                } else {
                    saveData();
                }
            };

            function getFormatKey(prefix,filename) {
                var tempArr = filename.split(".");
                var ext = "";
                if (tempArr.length === 1 || (tempArr[0] === "" && tempArr.length === 2)) {
                    ext = "";
                } else {
                    ext = tempArr.pop().toLowerCase(); //get the extension and make it lower-case
                }
                var zeroize = function (value, length) {
                    if (!length) length = 2;
                    value = String(value);
                    for (var i = 0, zeros = ''; i < (length - value.length); i++) {
                        zeros += '0';
                    }
                    return zeros + value;
                };
                var now = new Date();
                var folderPath = now.getFullYear()+zeroize(now.getMonth())+zeroize(now.getDay());
                var filePath = zeroize(now.getHours())+zeroize(now.getMinutes())+zeroize(now.getSeconds());
                var newFilePath = "/"+folderPath+"/"+filePath;
                return prefix+newFilePath+ext;
            }


            function saveData(){
                var m = $scope.record;
                if(m){
                    $scope.isDisabled = true;//提交disabled
                    $.ajax({
                        type: 'POST',
                        url : '/shuo/adv',
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
                            $state.go('main.talk.adv.list');
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
                    url : '/shuo/adv/read/detail',
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

            $('.upload-pic-box a').click(function() {
                $('#adv_image').click();
            });

            //上传图片
            $scope.$on('$viewContentLoaded', function() {
                var small = document.getElementById("adv_image");
                var result= document.getElementById("result");
                if ( typeof(FileReader) === 'undefined' ){
                    result.innerHTML = "抱歉，你的浏览器不支持 FileReader，请使用现代浏览器操作！";
                    input.setAttribute( 'disabled','disabled' );
                } else {
                    small.addEventListener( 'change',readImageFile,false );
                }
            });

            function readImageFile(){
                var file = this.files[0];
                //这里我们判断下类型如果不是图片就返回 去掉就可以上传任意文件
                if(!/image\/\w+/.test(file.type)){
                    alert("请确保文件为图像类型");
                    return false;
                }
                $scope.myImage = file;
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function(e){
                    // result.innerHTML = '<img src="'+this.result+'" alt=""/>';
                    small_adv_image.innerHTML = '<div class="sitetip"></div><img width="375" height="94" src="'+this.result+'" alt= ""/>';
                    $("#small_adv_image img").each(function() {
                        var maxWidth =473;      // 图片最大宽度
                        var maxHeight =100;     // 图片最大高度
                        var ratio = 0;      // 缩放比例
                        var width = $(this).width();    // 图片实际宽度
                        var height = $(this).height();  // 图片实际高度
                        if(width > maxWidth){           // 检查图片是否超宽
                            ratio = maxWidth / width;       // 计算缩放比例
                            $(this).css("width", maxWidth); // 设定实际显示宽度
                            height = height * ratio;        // 计算等比例缩放后的高度
                            $(this).css("height", height);  // 设定等比例缩放后的高度
                        }
                        if(height > maxHeight){         // 检查图片是否超高
                            ratio = maxHeight / height; // 计算缩放比例
                            $(this).css("height", maxHeight);   // 设定实际显示高度
                            width = width * ratio;              // 计算等比例缩放后的高度
                            $(this).css("width", width);        // 设定等比例缩放后的高度
                        }
                    });
                }
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