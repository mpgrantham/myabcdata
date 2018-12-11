angular
.module('logController', [])
.controller('LogCtrl', ['$rootScope', '$scope', '$window', '$location', 'uiGridConstants', 'UserModel', 'ObservedModel', LogCtrl]);

function LogCtrl($rootScope, $scope, $window, $location, uiGridConstants, UserModel, ObservedModel)
{
	$scope.observed = [];
	$scope.currentObserved = {};
	$scope.observedId = ObservedModel.observedId;
	
	$scope.incidents = [];
	
	var durationSort = function(colA, colB, rowA, rowB)
	{
		if ( rowA.entity.duration < rowB.entity.duration )
		{
			return -1;
		}
		else if ( rowA.entity.duration > rowB.entity.duration )
		{
			return 1;
		}
		return 0;
	}
	
	var dateSort = function(colA, colB, rowA, rowB)
	{
		if ( rowA.entity.incidentDt < rowB.entity.incidentDt )
		{
			return -1;
		}
		else if ( rowA.entity.incidentDt > rowB.entity.incidentDt )
		{
			return 1;
		}
		return 0;
	}
	
	$scope.incidentClicked = function(row)
	{
		if ( $scope.currentObserved.role == 'ADMIN' || $scope.currentObserved.role.indexOf('ENTRY') > -1 )
		{
			$location.path('entry/Observed/' + row.observedId + '/Incident/' + row.id);
		}
	}
	
	$scope.gridOptions = {
    		  enableColumnResizing: true,
    		  enableSorting: true,
    		  enableRowSelection: false,
    		  enableRowHeaderSelection: false,
    		  columnDefs: [
    		    { field: 'incidentDtStr', name: 'incidentDate', width: 200, maxWidth: 200, minWidth: 100, 
    		    	sortingAlgorithm: dateSort, defaultSort: { direction: uiGridConstants.DESC },
    		    	cellTemplate: '<div class="logDate ui-grid-cell-contents" ng-click="grid.appScope.incidentClicked(row.entity)">{{row.entity.incidentDtStr}}</div>'
    		    },
    		    { field: 'intensity', width: 100, maxWidth: 150, minWidth: 50},
    		    { field: 'durationStr', displayName: 'Duration', width: 200, maxWidth: 200, minWidth: 100, sortingAlgorithm: durationSort},
    		    { field: 'location', width: 150, maxWidth: 200, minWidth: 100 },
    		    { field: 'antecedentStr', displayName: 'Antecedent', width: 250, maxWidth: 350, minWidth: 100, enableSorting: false },
    		    { field: 'behaviorStr', displayName: 'Behavior', width: 250, maxWidth: 350, minWidth: 100, enableSorting: false },
    		    { field: 'consequenceStr', displayName: 'Consequence', width: 250, maxWidth: 350, minWidth: 100, enableSorting: false },
    		    { field: 'description', width: 250, maxWidth: 350, minWidth: 100, enableSorting: false }
    		  ]
    		};
	$scope.gridOptions.multiSelect = false;
	$scope.gridOptions.modifierKeysToMultiSelect = false;
	$scope.gridOptions.noUnselect = true;

    $scope.init = function init()
    {
    	if ( UserModel.user.sessionKey == '' )
    	{
    		$location.path('home');
    	}
    	
    	$scope.observed = ObservedModel.getPageObserved('LOG');
    	    	
    	$scope.changeObserved();
    	
    	$rootScope.$broadcast("tabEvent", {selectedTab: 'log' });
    }
    
    $scope.changeObserved = function changeObserved()
    {
    	if ( $scope.observed.length == 0 )
    	{
    		return;
    	}
    	
    	ObservedModel.initObservedLog($scope.observedId).then(function()
        {
    		$scope.currentObserved = ObservedModel.currentObserved;
        	$scope.observedId = ObservedModel.observedId;
        	$scope.incidents = ObservedModel.incidents;
        	
        	$scope.populateGrid();
        });
    }
    
	$scope.gridOptions.onRegisterApi = function(gridApi)
    {
		$scope.gridApi = gridApi;
    };
    
    $scope.populateGrid = function populateGrid()
    {
    	$scope.gridOptions.data = $scope.incidents;
    }
    
    $scope.addEntry = function addEntry()
    {
    	$location.path('entry');
    }
    
    $scope.exportLog = function exportLog($event)
    {
    	$event.preventDefault();
    	
    	$window.location.href = 'exportLog?obsId=' + $scope.observedId;
    }

    // Place at the end to ensure everything is defined
    $scope.init();
}
