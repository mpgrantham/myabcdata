angular
	.module('headerController', [])
	.controller('HeaderCtrl', ['$scope', '$location', '$route', 'UserModel', 'ObservedModel', HeaderCtrl]);
    
function HeaderCtrl($scope, $location, $route, UserModel, ObservedModel)
{
	$scope.user = UserModel.user;
			
	$scope.init = function init()
    {
	}
	
	$scope.selectedTab = 'home';
	
	// Receive tab events to highlight selected tab.
	$scope.$on("tabEvent", function (event, args) 
	{
		$scope.selectedTab = args.selectedTab;
	});
	
	$scope.showSignIn = function showSignIn($event)
    {
		$event.preventDefault();
		
		$location.path('signin');
	}
	
	$scope.showRegister = function showRegister($event)
    {
		$event.preventDefault();
		
		$location.path('register');
	}
	
	$scope.navigate = function navigate($event, navLocation)
    {
		$scope.selectedTab = navLocation;
		
		$event.preventDefault();
				
		$location.path(navLocation);
	}
					
	$scope.signOut = function signOut($event)
    {
		$event.preventDefault();
				
		UserModel.signOut().then(function()
	    {
			$scope.user = UserModel.user;
			
			ObservedModel.clear();
			
			if ( $scope.selectedTab == 'home' )
			{
				$route.reload();
			}
			else
			{
				$location.path('home'); 
				
				$scope.selectedTab = 'home';
			}
		}
		);
    }
	
	$scope.toggleMenu = function toggleMenu($event)
    {
		var e = angular.element(document.getElementById("dropDownMenu"));
		
		if ( e.css('display') == 'none' )
		{
			var left = angular.element($event.target).prop('offsetLeft');
			
			left -= 48;
			
			var ddm = document.getElementById("dropDownMenu")
	    	
	    	ddm.style.left = left + 'px';

			e.css('display', 'block');
		}
		else
		{
			e.css('display', 'none');
		}
	}
   
    // Place at the end to ensure everything is defined
    $scope.init();
}