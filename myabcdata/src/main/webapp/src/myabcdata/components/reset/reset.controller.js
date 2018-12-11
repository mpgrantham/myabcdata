angular
.module('resetController', [])
.controller('ResetCtrl', ['$scope', '$location', '$routeParams', 'UserModel', ResetCtrl]);

function ResetCtrl($scope, $location, $routeParams, UserModel)
{
	$scope.user = UserModel.user;
	
	$scope.message = '';
	$scope.status = 0;
	
	$scope.newPassword = '';
	$scope.confirmPassword = '';
		
    $scope.init = function init()
    {
    	UserModel.resetKey = $routeParams.key;
    	$scope.newPassword = '';
    	$scope.confirmPassword = '';
    	
    	UserModel.checkResetKey().then(function(data)
    	{
    		if ( data.status == 'SUCCESS' )
			{
    			$scope.status = 1;
				$scope.message = data.message;
				
				var e = document.getElementById('newPassword');
		    	e.focus();
			}
			else
			{
				$scope.status = 2;
				$scope.message = data.message;
			}
    	});
    }
    
    $scope.resetPassword = function resetPassword()
    {
    	if ( $scope.newPassword != $scope.confirmPassword )
    	{
    		$scope.status = 2;
			$scope.message = 'Passwords must match';
			
    		return;
    	}
    	
    	UserModel.resetPassword($scope.newPassword).then(function(data)
    	{
    		if ( data.status == 'SUCCESS' )
			{
    			$scope.status = 0;
				$scope.message = data.message;
			}
			else
			{
				$scope.status = 2;
				$scope.message = data.message;
			}
    	});
    }
    
    $scope.cancelReset = function cancelReset()
    {
    	$location.path('home');
    }
    
    $scope.showSignIn = function showSignIn($event)
    {
		$event.preventDefault();
		
		$location.path('signin');
	}
    
   
    // Place at the end to ensure everything is defined
    $scope.init();
}
