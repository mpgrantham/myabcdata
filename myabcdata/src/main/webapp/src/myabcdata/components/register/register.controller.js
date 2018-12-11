angular
.module('registerController', [])
.controller('RegisterCtrl', ['$scope', '$location', 'UserModel', RegisterCtrl]);

function RegisterCtrl($scope, $location, UserModel)
{
	$scope.user = UserModel.user;
	
	$scope.relationships = UserModel.relationships;
	
	$scope.message = '';
	$scope.status = 1;
	
	$scope.startPages = UserModel.startPages;
	
	$scope.includeObserved = false;
	
			
    $scope.init = function init()
    {
    	$scope.message = 'Register My ABC Data User';
    	$scope.status = 1;
    	UserModel.initUser();
    	
    	// Get Relationships
    	UserModel.getRelationships().then(function()
    	{
    		$scope.relationships = UserModel.relationships;
    	});
    	
    	var e = document.getElementById('username');
    	e.focus();
    }
    
    $scope.register = function register()
    {
    	
    	UserModel.register().then(function(data)
	    {
    		if ( data.status == 'SUCCESS' )
			{
				$scope.message = data.message;
				$scope.status = 0;
			}
			else
			{
				$scope.message = data.message;
				$scope.status = 2;
			}
		}
		);
	}
    
    $scope.cancelRegister = function cancelRegister()
    {
    	$location.path('home');
    }
    
    $scope.showSignIn = function showSignIn($event)
    {
		$event.preventDefault();
		
		$location.path('signin');
	}
    
    $scope.displayObservedSection = function displayObservedSection()
    {
    	if ( ! $scope.includeObserved )
    	{
    		$scope.user.observedName = '';
    		$scope.user.relationship = '0';
    	}
    }
    

    // Place at the end to ensure everything is defined
    $scope.init();
}
