'use strict';

var app = angular.module('app', ['ui.load', 'ui.router', 'ngStorage', 'brantwills.paging', 'oc.lazyLoad', 'ngImgCrop']);

/* Controllers */
angular.module('app')
  .controller('AppCtrl', ['$scope', '$localStorage', '$window','$http','$state','$rootScope',
    function($scope,   $localStorage,   $window ,$http,$state,$rootScope) {
        // add 'ie' classes to html
        var isIE = !!navigator.userAgent.match(/MSIE/i);
        isIE && angular.element($window.document.body).addClass('ie');
        isSmartDevice($window) && angular.element($window.document.body).addClass('smart');
		
        // config
        $scope.app = {
            name: '表情说说',
            version: '0.0.2',
            // for chart colors
            color: {
                primary: '#7266ba',
                info: '#23b7e5',
                success: '#27c24c',
                warning: '#fad733',
                danger: '#f05050',
                light: '#e8eff0',
                dark: '#3a3f51',
                black: '#1c2b36'
            },
            settings: {
                themeID: 1,
                navbarHeaderColor: 'bg-black-only',
                navbarCollapseColor: 'bg-dark-blue-only',
                asideColor: 'bg-black',

                headerFixed: true,
                asideFixed: true,
                asideFolded: false,
                asideDock: false,
                container: false
            }
        }
        $scope.defaultAvatar = $rootScope.defaultAvatar = 'res/img/np.png';
        
        $scope.logout = function(){
            return $http({
                method:'POST',
                url: '/logout'
            }).then(function(result){
                var d = result.data;
                if(d.code==200){//注销成功
                    deleteUserInfo();
                    $state.go('access.login');
                }
            });
            function deleteUserInfo(){
                $.cookie('_ihome_uid',null);
            }
        }

        $localStorage.settings = $scope.app.settings;

        // save settings to local storage  暂不支持自定义布局
        /*if (angular.isDefined($localStorage.settings)) {
            $scope.app.settings = $localStorage.settings;
        } else {
            $localStorage.settings = $scope.app.settings;
        }*/
        $scope.$watch('app.settings', function () {
            if ($scope.app.settings.asideDock && $scope.app.settings.asideFixed) {
                // aside dock and fixed must set the header fixed.
                $scope.app.settings.headerFixed = true;
            }
            // save to local storage
            $localStorage.settings = $scope.app.settings;
        }, true);

        // angular translate
        //$scope.lang = { isopen: false };
        //$scope.langs = {en:'English', de_DE:'German', it_IT:'Italian'};
        function isSmartDevice($window) {
            // Adapted from http://www.detectmobilebrowsers.com
            var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
            // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
            return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
        }

		$.ajaxSetup({
			beforeSend: function(evt, request, settings) {
				//request.url = 'iBQS-Web' + request.url;
			},
			dataFilter: function(result) {
				try {
					result = JSON.parse(result);
					if(result.code == 401) {
						$state.go('access.login');
			            return null;
			        } else if(result.code == 303) {
			        } else if(result.code == 200) {
			        } else if(result.code == 500) {
                        toaster.clear('*');
                        toaster.pop('error', '', result.msg);
			        } else if(result.code == 405) {
			        } else if(result) {
	                    toaster.clear('*');
	                    toaster.pop('error', '', result.msg);
					}
					return JSON.stringify(result);
				} catch(e) {
					return result;
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				switch (jqXHR.status) {
				case (404):
	                toaster.clear('*');
	                toaster.pop('error', '', "未找到请求的资源");
					break;
                case (500):
                    toaster.clear('*');
                    toaster.pop('error', '', "服务器错误");
                    break;
				case (405):
					$state.go('access.login');
					break;
				}
			}
		});

        $scope.uploadQiniu = function (param) {
            var fileData = param.fileData;
            var Qiniu_UploadUrl = "http://up.qiniu.com";

            var xhr = new XMLHttpRequest();
            xhr.open('POST', Qiniu_UploadUrl, true);
            var formData, startDate;
            formData = new FormData();
            formData.append('key', param.key);
            formData.append('token', param.uptoken);
            formData.append('file', fileData);
            var taking;
            xhr.upload.addEventListener("progress", function(evt) {
                if (evt.lengthComputable) {
                    var nowDate = new Date().getTime();
                    taking = nowDate - startDate;
                    var x = (evt.loaded) / 1024;
                    var y = taking / 1000;
                    var uploadSpeed = (x / y);
                    var formatSpeed;
                    if (uploadSpeed > 1024) {
                        formatSpeed = (uploadSpeed / 1024).toFixed(2) + "Mb\/s";
                    } else {
                        formatSpeed = uploadSpeed.toFixed(2) + "Kb\/s";
                    }
                }
            }, false);

            xhr.onreadystatechange = function(response) {
                if (xhr.readyState == 4 && xhr.status == 200 && xhr.responseText != "") {
                    var blkRet = JSON.parse(xhr.responseText);
                    param.success(blkRet,xhr);
                } else if (xhr.status != 200 && xhr.responseText) {
                    param.error(xhr);
                }
            };
            startDate = new Date().getTime();
            xhr.send(formData);
        }


    }]);