angular
.module('settingsController', [])
.controller('SettingsCtrl', ['$scope', '$location', 'UserModel', SettingsCtrl]);

function SettingsCtrl($scope, $location, UserModel)
{
	$scope.showUserSettings = true;
	
	$scope.user = UserModel.user;
	
	$scope.startPages = UserModel.startPages;
	
	$scope.message = '';
	$scope.status = 1;
	
	$scope.userCopy = {
    		username: '', 
    		password: '', 
    		email: '', 
    		userType: '0', 
    		startPage: 'HOME'
    };
	
	var initUser = function()
    {
		// make deep copy of user in case update is cancelled
    	$scope.userCopy.username = UserModel.user.username;
    	$scope.userCopy.password = UserModel.user.password;
    	$scope.userCopy.email = UserModel.user.email;
    	$scope.userCopy.startPage = UserModel.user.startPage;
    }
			
    $scope.init = function init()
    {
    	if ( UserModel.user.sessionKey == '' )
    	{
    		$location.path('home');
    	}
    	
    	$scope.message = 'Update Settings';
    	$scope.status = 1;
    	
    	initUser();
    }
    
    $scope.update = function update()
    {
    	UserModel.updateSettings().then(function(data)
	    {
    		if ( data.status == 'SUCCESS' )
			{
				$scope.message = data.message;
				$scope.status = 0;
				$scope.initUser();
			}
			else
			{
				$scope.message = data.message;
				$scope.status = 2;
			}
		}
		);
	}
    
    $scope.cancelUpdate = function cancelUpdate()
    {
    	UserModel.user.username= $scope.userCopy.username;
    	UserModel.user.password = $scope.userCopy.password;
    	UserModel.user.email = $scope.userCopy.email;
    	UserModel.user.startPage = $scope.userCopy.startPage;
    	
    	$location.path('home');
    }
    
    $scope.doneUpdate = function doneUpdate()
    {
    	$location.path('home');
    }
    
    // Place at the end to ensure everything is defined
    $scope.init();
}
