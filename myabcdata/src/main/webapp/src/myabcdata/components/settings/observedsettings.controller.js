angular
	.module('observedSettingsController', [])
	.controller('ObservedSettingsCtrl', ['$scope', '$location', '$route', 'UserModel', 'ObservedModel', ObservedSettingsCtrl]);
    
function ObservedSettingsCtrl($scope, $location, $route, UserModel, ObservedModel)
{
	$scope.user = UserModel.user;
	
	$scope.observed = [];
	$scope.currentObserved = {};
	$scope.observedId = ObservedModel.observedId;
	
	$scope.abcTabs = ['Antecedent', 'Behavior', 'Consequence', 'Location'];
	$scope.selectedTabIdx = 0;
	
	$scope.selectedId = 0;
	$scope.selectedAId = 0;
	$scope.selectedBId = 0;
	$scope.selectedCId = 0;
	$scope.selectedLId = 0;
	$scope.typeValue = '';
	$scope.modifyAction = 'Add';
	$scope.showModifyFl = false;
	
	$scope.modifyMessage = '';
	$scope.modifyStatus = 1;
	$scope.modifyTab = 'Antecedent';
	
	// Add Observed
	$scope.relationships = UserModel.relationships;
	$scope.relationshipId = '0';
	$scope.observedNm = '';
	$scope.showAddObservedFl = false;
	$scope.addObservedStatus = 1;
	$scope.addObservedMessage = 'Add Observed';
	$scope.modifyObservedAction = '';
	$scope.selectedObservedId = '0';
	
	// Grant Access
	$scope.showGrantAccessFl = false;
	$scope.grantAction = 'Grant';
	$scope.grantAccessMessage = 'Grant Access to Observed';
	$scope.grantAccessStatus = 1;
	$scope.userEmail = '';
	$scope.accessRelationshipId = '0';
	$scope.accessRole = 'ENTRY_LOG';
	$scope.selectedUserId = '0';
	
	$scope.observers = [];
	
	$scope.roles = [
		{
    		role: 'ENTRY_LOG', 
    		desc: 'Entry and Log'
		},
		{
    		role: 'ENTRY', 
    		desc: 'Entry'
		},
		{
    		role: 'LOG', 
    		desc: 'Log'
		}
	];
			
	$scope.init = function init()
    {
		// Get Relationships
    	if ( $scope.relationships.length == 0 )
    	{
    		UserModel.getRelationships().then(function()
    		{
    			$scope.relationships = UserModel.relationships;
    		});
    	}
    	
    	$scope.observed = ObservedModel.getPageObservedAll();
    	
    	if ( $scope.observed.length == 0 )
    	{
    		return;
    	}
    	    	
    	$scope.changeObserved();
	}
	
	$scope.changeObserved = function changeObserved()
    {
    	ObservedModel.initObserved($scope.observedId);
    	
    	$scope.currentObserved = ObservedModel.currentObserved;
    	$scope.observedId = ObservedModel.observedId;
    	$scope.showAddObservedFl = false;
    	$scope.showModifyFl = false;
    	
    	if ( $scope.currentObserved.role == 'ADMIN' )
    	{
    		ObservedModel.getObservedABCs().then(function()
	        {
	    	});
	    	
	    	ObservedModel.getObservers().then(function()
	    	{
	    		$scope.observers = ObservedModel.observers;
	    	});
    	}
    	else
    	{
    		ObservedModel.clearObservers();
    		$scope.observers = ObservedModel.observers;
    	}
    	
    }
	
	$scope.switchTab = function switchTab($event, tabIdx)
    {
    	$event.preventDefault();
    	
    	$scope.modifyTab = $scope.abcTabs[tabIdx];
    	
    	$scope.selectedTabIdx = tabIdx;
    	$scope.showModifyFl = false;
    	$scope.selectedId = 0;
    }
	    
    $scope.showModify = function showModify($event, action)
    {
    	$event.preventDefault();
    	
    	if ( action != 'Add' && $scope.selectedId == 0 )
    	{
    		return;
    	}
    	
    	$scope.modifyStatus = 1;
    	    	
    	$scope.modifyAction = action;
    	
    	$scope.modifyMessage = action + ' Value';
    	
    	if ( action == 'Add' )
    	{
    		$scope.typeValue = '';
    		$scope.selectedId = 0;
    	}
    	else
    	{
    		var arr = [];
    		if ( $scope.selectedTabIdx == 0 )
    		{
    			arr = $scope.currentObserved.antecedents;
    		}
    		else if ( $scope.selectedTabIdx == 1 )
    		{
    			arr = $scope.currentObserved.behaviors;
    		}
    		else if ( $scope.selectedTabIdx == 2 )
    		{
    			arr = $scope.currentObserved.consequences;
    		}
    		else 
    		{
    			arr = $scope.currentObserved.locations;
    		}
    		
    		for ( var i = 0; i < arr.length; i++ )
    		{
    			if ( arr[i].valueId == $scope.selectedId )
    			{
    				$scope.typeValue = arr[i].typeValue;
    				break;
    			}
    		}
    	}
    	    	
    	$scope.showModifyFl = true;
    }
	       
    $scope.closeModify = function closeModify()
    {
    	$scope.showModifyFl = false;
    }
    
    var isDuplicate = function(valueTypeCd)
    {
    	if ( ObservedModel.checkForDuplicate($scope.selectedId, valueTypeCd, $scope.typeValue) )
    	{
    		$scope.modifyMessage = 'Value already exists';
    		$scope.modifyStatus = 1;
    		return true;
    	}
    	
    	return false;
    }
	        
    $scope.modify = function modify()
    {
    	var valueTypeCd = $scope.abcTabs[$scope.selectedTabIdx].substring(0,1);
    	
    	if ( $scope.modifyAction == 'Add' )
    	{
    		if ( isDuplicate(valueTypeCd) )
    		{
    			return;
    		}
    		
    		ObservedModel.addABC(valueTypeCd, $scope.typeValue).then(function(data)
		    {
    			if ( data.status == 'SUCCESS' )
				{
	    			$scope.modifyStatus = 0;
	    			$scope.modifyMessage = $scope.typeValue + ' Added';
	    		}
				else
				{
					$scope.modifyStatus = 2;
	    			$scope.modifyMessage = data.message;
				}
    		}
			);
    		
    		return;
    	}
    	
    	var activeFl = 'Y';
    	
    	if ( $scope.modifyAction == 'Update' )
    	{
    		if ( isDuplicate(valueTypeCd) )
    		{
    			return;
    		}
    	}
    	else // Delete
    	{
    		activeFl = 'N';
    	}
    	
    	ObservedModel.updateABC($scope.selectedId, valueTypeCd, $scope.typeValue, activeFl).then(function(data)
	    {
    		if ( data.status == 'SUCCESS' )
			{
    			$scope.modifyStatus = 0;
    			$scope.modifyMessage = $scope.modifyAction + ' Successful';
			}
			else
			{
				$scope.modifyStatus = 2;
    			$scope.modifyMessage = data.message;
			}
    	}
		);
    	
    }
	    
    $scope.showAddObserved = function showAddObserved($event)
    {
    	$event.preventDefault();
    	
    	$scope.relationshipId = '0';
    	$scope.observedNm = '';
    	$scope.addObservedStatus = 1;
    	$scope.addObservedMessage = 'Add Observed';
    	$scope.showAddObservedFl = true;
    	$scope.modifyObservedAction = 'Add';
    }
	    
    $scope.showEditObserved = function showEditObserved($event)
    {
    	$event.preventDefault();
    	
    	if ( $scope.observedId == '0' || $scope.currentObserved.role != 'ADMIN' )
    	{
    		return;
    	}
    	
    	
    	$scope.relationshipId = $scope.currentObserved.relationshipIdStr;
    	$scope.observedNm = $scope.currentObserved.observedNm;
    	$scope.addObservedStatus = 1;
    	$scope.addObservedMessage = 'Edit Observed';
    	$scope.showAddObservedFl = true;
    	$scope.modifyObservedAction = 'Update';
    }
	    
    $scope.showRemoveObserved = function showRemoveObserved($event)
    {
    	$event.preventDefault();
    	
    	if ( $scope.observedId == '0' || $scope.currentObserved.role == 'ADMIN' )
    	{
    		return;
    	}
    	
    	$scope.relationshipId = $scope.currentObserved.relationshipIdStr;
    	$scope.observedNm = $scope.currentObserved.observedNm;
    	$scope.addObservedStatus = 1;
    	$scope.addObservedMessage = 'Remove Access to Observed';
    	$scope.showAddObservedFl = true;
    	$scope.modifyObservedAction = 'Remove';
    }
    
    var isDuplicateObserved = function()
    {
    	if ( ObservedModel.checkForDuplicateObserved($scope.observedNm, $scope.currentObserved.id) )
    	{
    		$scope.addObservedMessage = 'Observed Name already exists';
    		$scope.addObservedStatus = 2;
    		return true;
    	}
    	
    	return false;
    }
	    
    $scope.addObserved = function addObserved()
    {
    	if ( isDuplicateObserved() )
    	{
    		return;
    	}
    	
    	ObservedModel.addObserved($scope.observedNm, $scope.relationshipId).then(function(data)
	    {
    		if ( data.status == 'SUCCESS' )
			{
    			$scope.addObservedStatus = 0;
    			$scope.addObservedMessage = 'Observed Added';
			}
			else
			{
				$scope.addObservedStatus = 2;
    			$scope.addObservedMessage = data.message;
			}
    	}
		);
    }
	    
    $scope.editObserved = function editObserved()
    {
    	if ( isDuplicateObserved() )
    	{
    		return;
    	}
    	
    	ObservedModel.updateObserved($scope.observedNm, $scope.currentObserved.role, $scope.relationshipId).then(function(data)
	    {
    		if ( data.status == 'SUCCESS' )
			{
    			$scope.addObservedStatus = 0;
    			$scope.addObservedMessage = 'Observed Updated';
			}
			else
			{
				$scope.addObservedStatus = 2;
    			$scope.addObservedMessage = data.message;
			}
    	}
		);
    }
	    
    $scope.removeObserved = function removeObserved()
    {
    	ObservedModel.removeObserved($scope.user.id).then(function(data)
    	{
    		if ( data.status == 'SUCCESS' )
			{
    			$scope.addObservedStatus = 0;
    			$scope.addObservedMessage = 'Access Removed to Observed';
    			
    			$scope.observed = ObservedModel.getPageObservedAll();
    	    	//$scope.observed = ObservedModel.getPageObserved('ADMIN');
    	    	
    	    	if ( $scope.observed.length == 0 )
    	    	{
    	    		return;
    	    	}
    	    	
    	    	$scope.changeObserved();
			}
			else
			{
				$scope.addObservedStatus = 2;
    			$scope.addObservedMessage = data.message;
			}
    	}
    	);
    }
	    
    $scope.closeAddObserved = function closeAddObserved()
    {
    	$scope.showAddObservedFl = false;
    }
	    
    $scope.showGrantAccess = function showGrantAccess($event, action)
    {
    	$event.preventDefault();
    	
    	$scope.grantAction = action;
    	
    	$scope.grantAccessStatus = 1;
    	$scope.userEmail = '';
    	$scope.accessRelationshipId = '0';
    	$scope.accessRole = 'ENTRY_LOG';
    	
    	$scope.showGrantAccessFl = true;
    	
    	if ( action == 'Grant' )
    	{
    		$scope.grantAccessMessage = 'Grant Access to ' + $scope.currentObserved.observedNm;
    		return;
    	}
    	
    	if ( action == 'Update' )
    	{
    		$scope.grantAccessMessage = 'Update Access to ' + $scope.currentObserved.observedNm;
    	}
    	else if ( action == 'Remove' )
    	{
    		$scope.grantAccessMessage = 'Remove Access to ' + $scope.currentObserved.observedNm;
    	}
    	
    	for ( var i = 0; i < $scope.observers.length; i++ )
    	{
    		var observer = $scope.observers[i];
    		if ( observer.userIdStr == $scope.selectedUserId )
    		{
    			$scope.userEmail = observer.email;
    	    	$scope.accessRelationshipId = observer.relationshipIdStr;
    	    	$scope.accessRole = observer.role;
    	    	break;
    		}
    	}
    }
	    
    $scope.closeGrantAccess = function closeGrantAccess()
    {
    	$scope.showGrantAccessFl = false;
    }
    
    var checkAccessStatus = function(status, successMessage)
    {
    	if ( status == 'SUCCESS' )
		{
			$scope.grantAccessMessage = successMessage;
	    	$scope.grantAccessStatus = 0;
		}
		else
		{
			$scope.grantAccessMessage = data.message;
	    	$scope.grantAccessStatus = 2;
		}
    }
	    
    $scope.grantAccess = function grantAccess()
    {
    	if ( $scope.grantAction == 'Grant' )
    	{
	    	ObservedModel.grantAccess($scope.userEmail, $scope.accessRole, $scope.accessRelationshipId).then(function(data)
		    {
	    		checkAccessStatus(data.status, 'Access Granted');
	    	}
	    	);
	    }
    	else if ( $scope.grantAction == 'Update' )
    	{
    		ObservedModel.updateAccess($scope.selectedUserId, $scope.accessRole, $scope.accessRelationshipId).then(function(data)
		    {
    			checkAccessStatus(data.status, 'Access Updated');
    		}
	    	);
    	}
    	else
    	{
    		ObservedModel.removeAccess($scope.selectedUserId).then(function(data)
		    {
    			checkAccessStatus(data.status, 'Access Removed');
	    	}
	    	);
    	}
    }
	    
    $scope.accessUserSelected = function accessUserSelected()
    {
    	$scope.showGrantAccessFl = false;
    }
	   
   
    $scope.init();
}