angular
.module('signInController', [])
.controller('SignInCtrl', ['$scope', '$location', 'UserModel', 'ObservedModel', SignInCtrl]);

function SignInCtrl($scope, $location, UserModel, ObservedModel)
{
	$scope.user = UserModel.user;
	
	$scope.message = '';
	$scope.status = 1;
	
	$scope.showForgotSection = 0;
	$scope.forgotMessage = '';
	$scope.forgotType = '';
	$scope.forgotEmail = '';
	$scope.signInDisabled = true;
	$scope.sendDisabled = true;

    $scope.init = function init()
    {
    	$scope.message = 'User Sign In';
    	$scope.status = 1;
    	
    	UserModel.initUser();
    	
    	var e = document.getElementById('username');
    	e.focus();
    }
    
    $scope.enableSignIn = function enableSignIn($event)
    {
    	if ( $scope.user.username.trim() == "" || $scope.user.password.trim() == "" )
    	{
    		$scope.signInDisabled = true;
    	}
    	else
    	{
    		$scope.signInDisabled = false;
    		if ( $event.keyCode == 13 )
    		{
    			$scope.signIn();
    		}
    	}
    }
    
    $scope.signIn = function signIn()
    {
    	UserModel.signIn().then(function()
	    {
			$scope.user = UserModel.user;
			
			if ( $scope.user.signInStatus == 0 )
			{
				ObservedModel.init(UserModel.user.sessionKey);
				
				ObservedModel.getUserObserved().then(function(data)
			    {
					// Check to see if user has any ENTRY or LOG observed before redirecting
					ObservedModel.setPageAccess();
					
					if ( $scope.user.startPage == 'ENTRY' && ObservedModel.entryAccess )
					{
						$location.path('entry'); 
					}
					else if ( $scope.user.startPage == 'LOG' && ObservedModel.logAccess )
					{
						$location.path('log'); 
					}
					else 
					{
						$location.path('home'); 
					}
			    });
			}
			else
			{
				$scope.message = UserModel.user.message;
				$scope.status = 2;
			}
		}
		);
    }
    
    $scope.cancelSignIn = function cancelSignIn($event)
    {
    	$location.path('home');
    }
    
    $scope.showForgetSection = function showForgetSection($event, type)
    {
    	$event.preventDefault();
    	
    	var e = document.getElementById('forgotEmail');
    	e.focus();
    	
    	$scope.forgotType = type;
    	$scope.showForgotSection = 1;
    	$scope.forgotMessage = 'Provide the Email associated with the User. The ' + type + ' will be sent to you.';
    }
    
    $scope.enableSend = function enableSend($event)
    {
    	if ( $scope.forgotEmail.trim() == "" )
    	{
    		$scope.sendDisabled = true;
    	}
    	else
    	{
    		$scope.sendDisabled = false;
    		if ( $event.keyCode == 13 )
    		{
    			$scope.sendForgot();
    		}
    	}
    }
       
    $scope.sendForgot = function sendForgot()
    {
    	UserModel.sendForgot($scope.forgotEmail, $scope.forgotType).then(function(data)
    	{
    		if ( data.status == 'SUCCESS' )
			{
				$scope.message = data.message;
				$scope.status = 1;
			}
			else
			{
				$scope.message = data.message;
				$scope.status = 2;
			}
    		
    		$scope.showForgotSection = 0;
    	});
    }
    
    $scope.cancelForgot = function cancelForgot()
    {
    	$scope.showForgotSection = 0;
    }
    
    
    // Place at the end to ensure everything is defined
    $scope.init();
}
