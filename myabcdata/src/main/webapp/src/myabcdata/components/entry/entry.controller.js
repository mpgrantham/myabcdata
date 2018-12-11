angular
.module('entryController', [])
.controller('EntryCtrl', ['$rootScope', '$scope', '$location', '$window', '$routeParams', '$sce', 'UserModel', 'ObservedModel', 'EntryModel', EntryCtrl]);

function EntryCtrl ($rootScope, $scope, $location, $window, $routeParams, $sce, UserModel, ObservedModel, EntryModel)
{
	$scope.observed = [];
	$scope.currentObserved = {};
	$scope.observedId = ObservedModel.observedId;
	
	$scope.incident = ObservedModel.incident;
		
	$scope.intensities = EntryModel.intensities;
	
	$scope.amPmOtions = ['AM', 'PM'];
	
	$scope.message = '';
	$scope.status = 1;
	
	$scope.selectedTabIdx = 0;
	
	$scope.abcTabs = ['Antecedent', 'Behavior', 'Consequence'];
		
	$scope.showAdd = false;
	
	$scope.addMessage = '';
	$scope.addStatus = 0;
	$scope.valueTypeCd = 'A';
	$scope.typeValue = '';
	$scope.currentValueTypeCd = 'Antecedent';
		
    $scope.init = function init()
    {
    	if ( UserModel.user.sessionKey == '' )
    	{
    		$location.path('home');
    	}
    	
    	// Broadcast a custom event
    	// This prevents coupling of code
    	// Must broadcast from rootScope, therefore it must be made available to controller
    	$rootScope.$broadcast("tabEvent", {selectedTab: 'entry' });
    	
    	$scope.observed = ObservedModel.getPageObserved('ENTRY');
    	
    	if ( $scope.observed.length == 0 )
    	{
    		$scope.message = $sce.trustAsHtml("User has no access to Observed that allow Incident Entry");
    		return;
    	}
    	
    	var incidentId_Param = 0;
    	var observedId_Param = $scope.observedId;
    	
    	if ( $routeParams.observedId )
    	{
    		observedId_Param = $routeParams.observedId;
    	}
    	    	
    	if ( $routeParams.incidentId )
    	{
    		incidentId_Param = $routeParams.incidentId;
    	}
    	
    	if ( incidentId_Param == 0 )
    	{
    		$scope.message = $sce.trustAsHtml("Add Incident");
    	}
    	else
    	{
    		$scope.message = $sce.trustAsHtml("Edit Incident");
    	}
    	
    	// Get incident from DB, then get Observed ABCS
    	
    	ObservedModel.initIncident();
    	
    	ObservedModel.initObserved(observedId_Param);
    	
    	$scope.currentObserved = ObservedModel.currentObserved;
    	$scope.observedId = ObservedModel.observedId;
    	
    	if ( incidentId_Param > 0 )
    	{
    		ObservedModel.setIncident(incidentId_Param);
    	}
    	
    	$scope.incident = ObservedModel.incident;
    	
    	var element = document.getElementById('incidentDate');
    	element.select();
    	element.focus();
    	    	    	
    	ObservedModel.getObservedABCs().then(function()
        {
    	});
    		
    	if ( $scope.intensities.length == 0 )
    	{
    		EntryModel.getIntensities().then(function()
    		{
    			$scope.intensities = EntryModel.intensities;
    		});
    	}
    }
    
    $scope.locationFocus = function locationFocus()
    {
    	$scope.currentValueTypeCd = 'Location';
    	
    	$scope.valueTypeCd = $scope.currentValueTypeCd.substring(0,1);
    }
    
    $scope.changeObserved = function changeObserved()
    {
    	ObservedModel.initIncident();
    	
    	ObservedModel.initObserved($scope.observedId);
    	$scope.currentObserved = ObservedModel.currentObserved;
    	$scope.observedId = ObservedModel.observedId;
    	
    	$scope.incident = ObservedModel.incident;
    	
    	var element = document.getElementById('incidentDate');
    	element.select();
    	element.focus();
    	
    	ObservedModel.getObservedABCs().then(function()
        {
    	});
    }
    
    $scope.switchTab = function switchTab(tabIdx)
    {
    	$scope.selectedTabIdx = tabIdx;
    	
    	$scope.currentValueTypeCd = $scope.abcTabs[tabIdx];
    	
    	$scope.valueTypeCd = $scope.currentValueTypeCd.substring(0,1);
    }
    
    $scope.backToLog = function backToLog($event)
    {
    	$event.preventDefault();
    	
    	$location.path('log');
    }
    
    $scope.addObserved = function addObserved()
    {
    	$location.path('observed');
    }
    
    $scope.save = function save()
    {
    	var errors = ObservedModel.validateIncident();
    	if ( errors.length > 0 )
    	{
    		var msg =  "Incident contains the following error(s):&nbsp;&nbsp;";
    		for ( var i = 0; i < errors.length; i++ )
    		{
    			if ( i > 0 )
    			{
    				msg += ";&nbsp;&nbsp;";
    			}
    			msg += errors[i];
    		}
    		
    		$scope.message = $sce.trustAsHtml(msg);
			$scope.status = 2;
    		return;
    	}
    	
    	ObservedModel.save().then(function(data)
	    {
    		if ( data.status == 'SUCCESS' )
			{
				$scope.message = $sce.trustAsHtml(data.message);
				$scope.status = 0;
				$scope.incident = ObservedModel.incident;
			}
			else
			{
				$scope.message = $sce.trustAsHtml(data.message);
				$scope.status = 2;
			}
    	}
		);
    }
    
    $scope.cancel = function cancel()
    {
    	$location.path('log');
    }
    
    $scope.doneSave = function doneSave()
    {
    	$location.path('log');
    }
    
    $scope.exportDataSheet = function exportDataSheet($event)
    {
    	$event.preventDefault();
    	
    	$window.location.href = 'exportDataSheet?obsId=' + $scope.observedId;
    }
    
    $scope.showAddValue = function showAddValue($event)
    {
    	$event.preventDefault();
    	
    	if ( $scope.showAdd )
    	{
    		$scope.showAdd = false;
    		return;
    	}
    	
    	$scope.typeValue = '';
    	
    	$scope.showAddMessage = false;
    	$scope.showAdd = true;
    	$scope.addMessage = 'Add Value'; 
    	$scope.addStatus = 1;
    	
    	var element = document.getElementById('typeValue');
    	element.select();
		element.focus();
    }
    
    $scope.closeAddValue = function cancelAddValue()
    {
    	$scope.showAdd = false;
    }
    
    $scope.changeValueType = function changeValueType(typeCd)
    {
    	$scope.currentValueTypeCd = typeCd;
    	$scope.typeValue = '';
    }
       
    $scope.addValue = function addValue()
    {
    	if ( ObservedModel.checkForDuplicate(0, $scope.valueTypeCd, $scope.typeValue) )
    	{
    		$scope.addMessage = $scope.currentValueTypeCd + ' already exists';
    		$scope.addStatus = 2;
    		return;
    	}
    	
    	ObservedModel.addABC($scope.valueTypeCd, $scope.typeValue).then(function(data)
	    {
    		if ( data.status == 'SUCCESS' )
			{
    			$scope.addStatus = 0;
    			$scope.addMessage = $scope.currentValueTypeCd + ' Added';
			}
			else
			{
				$scope.addStatus = 2;
    			$scope.addMessage = data.message;
			}
    	}
		);
    	
    }
            
    // Place at the end to ensure everything is defined
    $scope.init();
}
