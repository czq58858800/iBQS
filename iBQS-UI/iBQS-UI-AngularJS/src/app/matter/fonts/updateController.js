'use strict';

angular.module('app')
    .controller('fontsUpdateController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
        function($scope, $rootScope, $state, $timeout, toaster) {
            var title = "";

            $scope.myImage='';
            $scope.myFont='';

            if($state.includes('**.fonts.update')){
                title="编辑字体";
                var id = $state.params.id;
                activate(id);
                validate(id);
            }else if($state.includes('**.fonts.create')){
                title="添加字体";
                $scope.record = {};
                $scope.record.lang = "cn";
                $scope.record.enable = "true";
                validate(null);
            }

            $scope.title = $rootScope.title = title;
            $scope.loading = true;

            $scope.selectLang = function (lang) {
                if (lang == null || lang == '') {
                    $scope.record.lang = "cn";
                } else {
                    if (lang == $scope.record.lang) {
                        if (lang == "cn")
                            $scope.record.lang = "en";
                        else
                            $scope.record.lang = "cn";
                    } else {
                        $scope.record.lang = lang;
                    }
                }
            }


            $scope.selectEnable = function (enable) {
                if (enable == null || enable == '') {
                    $scope.record.enable = 'true';
                } else {

                    if (enable == $scope.record.enable) {
                        if (enable == 'true')
                            $scope.record.enable = 'false';
                        else
                            $scope.record.enable = 'true';
                    } else {
                        $scope.record.enable = enable;
                    }
                }
            }

            $.ajax({
                url : '/qiniuUpToken'
            }).then(function(result) {
                if (result.code == 200) {
                    $scope.qiniuToken = result.data;
                } else {
                    $scope.msg = result.msg;
                }
            });

            //初始化验证
            //validate($scope);
            $scope.submit= function(){
                $scope.loading = true;
                if ($scope.myImage != null && $scope.myImage != "" ) {
                    $scope.uploadQiniu({
                        fileData:$scope.myImage,
                        key:getFormatKey("font/cover",$scope.myImage.name),
                        uptoken:$scope.qiniuToken.uptoken,
                        success:function (data,res) {
                            var imagePath = $scope.qiniuToken.domain+data.key;
                            $scope.record.cover = imagePath;

                            if ($scope.myFont != null && $scope.myFont != "" ) {
                                // 上传字体文件
                                $scope.uploadQiniu({
                                    fileData: $scope.myFont,
                                    key: getFormatKey("font", $scope.myFont.name),
                                    uptoken: $scope.qiniuToken.uptoken,
                                    success: function (data, res) {
                                        var fontPath = $scope.qiniuToken.domain + data.key;
                                        $scope.record.font = fontPath;
                                        saveData();
                                    },
                                    error: function (res) {
                                        $scope.msg = "字体上传失败";
                                    }
                                });
                            } else {
                                saveData();
                            }

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
                return prefix+newFilePath+"."+ext;
            }

            function saveData(){
                var m = $scope.record;
                if(m){
                    $scope.isDisabled = true;//提交disabled
                    $.ajax({
                        type: 'POST',
                        url : '/shuo/fonts',
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
                            $state.go('main.matter.fonts.list');
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
                    url : '/shuo/fonts/read/detail',
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

            //上传图片
            $scope.$on('$viewContentLoaded', function() {
                var small = document.getElementById("cover_image");
                var font = document.getElementById("font_file");
                var result= document.getElementById("result");
                if ( typeof(FileReader) === 'undefined' ){
                    result.innerHTML = "抱歉，你的浏览器不支持 FileReader，请使用现代浏览器操作！";
                    input.setAttribute( 'disabled','disabled' );
                } else {
                    small.addEventListener( 'change',readImageFile,false);
                    font.addEventListener( 'change',readFontFile,false);
                }
            });

            $('.upload-cover-box a').click(function() {
                $('#cover_image').click();
            });

            $('.upload-font-box a').click(function() {
                $('#font_file').click();
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
                    small_cover_image.innerHTML = '<div class="sitetip"></div><img width="50" height="50" src="'+this.result+'" alt= ""/>';
                }
            }

            function readFontFile(){
                var file = this.files[0];
                //这里我们判断下类型如果不是图片就返回 去掉就可以上传任意文件
                if(!/.ttf/.test(file.name.toLowerCase())){
                    alert("请确保文件为字体类型");
                    return false;
                }
                $scope.myFont = file;
                small_font_file.innerHTML = '<div class="sitetip"></div><label for="font_file"><a >'+file.name+'</a> </label>';
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