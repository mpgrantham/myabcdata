angular
.module('homeController', [])
.controller('HomeCtrl', ['$rootScope', '$scope', '$location', 'UserModel', 'ObservedModel', HomeCtrl]);

function HomeCtrl ($rootScope, $scope, $location, UserModel, ObservedModel)
{
	$scope.sessionKey = UserModel.user.sessionKey;
	
	$scope.observedMessage = '';
				
    $scope.init = function init()
    {
    	$rootScope.$broadcast("tabEvent", {selectedTab: 'home' });
    	
    	if ( ObservedModel.observed.length > 0 )
    	{
    		$scope.observedMessage = 'You have access to ' + ObservedModel.observed.length + ' Observed';
    	}
    	else
    	{
    		$scope.observedMessage = 'You do not have access any Observed.  You can add an Observed from the Settings tab.';
    	}
    }
   
    // Place at the end to ensure everything is defined
    $scope.init();
}
