angular
.module('observedModel', [])
.service('ObservedModel', ObservedModel);

function ObservedModel($http)
{
    var service = this;
    
    service.sessionKey = '';
    
    service.observed = [];
    
    service.pageObserved = [];
    service.currentObserved = {};
    service.observedId = '0';
    
    service.entryAccess = false;
    service.logAccess = false;
    
    service.incidents = [];
    
    service.incident = {
    		id: 0,
    		incidentDate: '',
    		incidentTimeHour: '',
    		incidentTimeMinute: '',
    		incidentTimeAmPm: 'AM',
    		durationMinutes: '00',
    		durationSeconds: '00',
    		intensityId: '0',
    		locationId: '0',
    		description: ''
    };
    
    service.observers = [];
        
    service.init = function(sessionKey)
    {
    	service.sessionKey = sessionKey;
    }
    
    service.clear = function()
    {
    	service.sessionKey = '';
        service.observed = [];
        service.pageObserved = [];
        service.currentObserved = {};
        service.observedId = '0';
        service.entryAccess = false;
        service.logAccess = false;
        service.incidents = [];
    }
    
    service.initIncident = function()
    {
    	service.incident.locationId = '0';
    	
    	service.incident.durationMinutes = '00';
        service.incident.durationSeconds = '00';
        service.incident.intensityId = '0';
        service.incident.description = '';
        
        var dt = new Date();
    	
    	var dd = ("0" + dt.getDate()).slice(-2);
    	var mm = ("0"+(dt.getMonth()+1)).slice(-2);
    	var yyyy = dt.getFullYear();
    	
    	var hr = dt.getHours();
    	var min = ("0" + dt.getMinutes()).slice(-2);
    	
    	if ( hr > 12 )
    	{
    		service.incident.incidentTimeHour = (hr - 12) + "";
    		service.incident.incidentTimeAmPm = 'PM';
    	}
    	else
    	{
    		service.incident.incidentTimeHour = hr + "";
    		service.incident.incidentTimeAmPm = 'AM';
    	}
    	
    	service.incident.incidentTimeMinute = min + "";
    	
    	service.incident.incidentDate = mm + '/' + dd + '/' + yyyy;
    }
    
    service.getUserObserved = function ()
    {
    	return $http.get('userObserved', {
    	    	headers: {'Authorization': service.sessionKey}
    		})
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		service.observed = [];
            		return result.data;
            	}
            	else
            	{
            		service.observed = result.data;
            		return {status: 'SUCCESS', message: 'Observed List Returned'};
            	}
            })
            .catch(function(error)
            {
            	service.observed = [];
            	return {status: 'ERROR', message: 'Unknown Error'};
        	});
    }
    
    service.getPageObservedAll = function ()
    {
    	service.pageObserved = service.observed;
    	
    	return service.pageObserved;
    }
    
    service.getPageObserved = function (page)
    {
    	service.pageObserved = [];
    	
    	for ( var i = 0; i < service.observed.length; i++ )
    	{
    		var role = service.observed[i].role;
    		if ( role == 'ADMIN' || role.indexOf(page) > -1 )
    		{
    			service.pageObserved.push(service.observed[i]);
    		}
    	}
    	
    	return service.pageObserved;
    }
    
    service.setPageAccess = function ()
    {
    	service.entryAccess = false;
	    service.logAccess = false;
	    
    	for ( var i = 0; i < service.observed.length && (service.entryAccess == false || service.logAccess == false); i++ )
    	{
    		var role = service.observed[i].role;
    		if ( role == 'ADMIN' )
    		{
    			service.entryAccess = true;
    		    service.logAccess = true;
    		}
    		if ( role.indexOf('ENTRY') > -1 )
    		{
    			service.entryAccess = true;
    		}
    		if ( role.indexOf('LOG') > -1 )
    		{
    			service.logAccess = true;
    		}
    	}
    	
    	return false;
    }
    
    var getObservedIndex = function(observedId_Param)
    {
    	var observedIdx = 0;
    	
    	for ( var i = 0; observedId_Param > 0 && i < service.pageObserved.length; i++ )
    	{
    		if ( service.pageObserved[i].id == observedId_Param )
    		{
    			observedIdx = i;
    			break;
    		}
    	}
    	
    	return observedIdx;
    }
    
    service.initObservedLog = function (observedId_Param)
    {
    	if ( service.pageObserved.length == 0 )
    	{
    		return {status: 'ERROR', message: 'No Observed Exist'};
    	}
    	
    	var observedIdx = getObservedIndex(observedId_Param);
    	
    	service.currentObserved = service.pageObserved[observedIdx];
		service.observedId = service.currentObserved.strId;
		
		return $http.get('observedIncidents?obsId=' + service.observedId, {
	    	headers: {'Authorization': service.sessionKey}
		})
        .then(function(result)
        {
        	if ( result.data.hasOwnProperty('status') )
        	{
        		return result.data;
        	}
        	else
        	{
        		service.incidents = result.data;
        		return {status: 'SUCCESS', message: 'Observed Incidents Returned'};
        	}
        })
        .catch(function(error)
        {
        	service.observed = [];
        	return {status: 'ERROR', message: 'Unknown Error'};
    	});
    }
    
    service.initObserved = function(observedId_Param)
    {
    	if ( service.pageObserved.length == 0 )
    	{
    		return {status: 'ERROR', message: 'No Observed Exist'};
    	}
    	
    	var observedIdx = 0;
    	
    	for ( var i = 0; observedId_Param > 0 && i < service.pageObserved.length; i++ )
    	{
    		if ( service.pageObserved[i].id == observedId_Param )
    		{
    			observedIdx = i;
    			break;
    		}
    	}
    	
    	service.currentObserved = service.pageObserved[observedIdx];
		service.observedId = service.currentObserved.strId;
	}
    
    service.getObservedABCs = function ()
    {
    	return $http.get('observedABCs?obsId=' + service.observedId, {
    	    	headers: {'Authorization': service.sessionKey}
    		})
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		service.currentObserved.locations = result.data.locations;
            		service.currentObserved.antecedents = result.data.antecedents;
            		service.currentObserved.behaviors = result.data.behaviors;
            		service.currentObserved.consequences = result.data.consequences;
            		
            		service.incident.locationId = service.currentObserved.locations[0].strId;
            		
            		return {status: 'SUCCESS', message: 'Observed ABCs Returned'};
            	}
            })
            .catch(function(error)
            {
            	service.observed = [];
            	return {status: 'ERROR', message: 'Unknown Error'};
        	});
    }
    
    service.setIncident = function (incidentId_Param)
    {
    	if ( incidentId_Param == 0 )
    	{
    		return;
    	}
    	
    	var selectedIncident = {};
    	for ( var i = 0; i < service.incidents.length; i++ )
    	{
    		if ( service.incidents[i].id == incidentId_Param )
    		{
    			selectedIncident = service.incidents[i];
    			break;
    		}
    	}
    	
    	var dateParts = selectedIncident.incidentDtStr.split(' ');
    	var timeParts = dateParts[1].split(':');
    	
    	var seconds = selectedIncident.duration;
    	var min = Math.floor(seconds / 60);
		var sec = seconds % 60;
		
		service.incident.id = selectedIncident.id;
    	service.incident.incidentDate = dateParts[0];
    	service.incident.incidentTimeHour = timeParts[0];
    	service.incident.incidentTimeMinute = timeParts[1];
		service.incident.incidentTimeAmPm = dateParts[2];
    	service.incident.durationMinutes = ("0" + min).slice(-2);
    	service.incident.durationSeconds = ("0" + sec).slice(-2);
		service.incident.intensityId = selectedIncident.intensityIdStr;
		service.incident.locationId = selectedIncident.locationIdStr;
		service.incident.description = selectedIncident.description;
		
		setFilterString(selectedIncident.antecedents, service.currentObserved.antecedents);
		setFilterString(selectedIncident.behaviors, service.currentObserved.behaviors);
		setFilterString(selectedIncident.consequences, service.currentObserved.consequences);
    }
    
    var setFilterString = function(sourceArr, targetArr)
    {
    	for ( var i = 0; i < sourceArr.length; i++ ) 
		{
			var id = sourceArr[i].valueId;
			
			for ( var j = 0; j < targetArr.length; j++ )
			{
				if ( targetArr[j].valueId == id )
				{
					targetArr[j].selectedFlag = true;
					break;
				}
			}
		}
    }
    
    var getFilterString = function(arr)
    {
    	var str = '';
    	for ( var i = 0; i < arr.length; i++ ) 
    	{
    		if ( ! arr[i].selectedFlag )
    		{
    			continue;
    		}
    		if ( str != '' )
    		{
    			str += ',';
    		}
    		str += arr[i].valueId;
    	}
    	return str;
    }
    
    var isValidDate = function(value)
    {
    	var parts = value.split("/");
    	if ( parts.length != 3 )
    	{
    		return false;
    	}
    	
    	var mm = parseInt(parts[0]);  
    	var dd  = parseInt(parts[1]);  
    	var yy = parseInt(parts[2]); 
    	
    	if ( isNaN(yy) || isNaN(mm) || isNaN(dd) )
    	{
    		return false;
    	}
    	
    	if ( mm < 1 || mm > 12 )
    	{
    		return false;
    	}
    	
    	var listOfDays = [0,31,28,31,30,31,30,31,31,30,31,30,31]; 
    	
    	if ( yy > 0 && yy < 50 )
    	{
    		yy+=2000;
    	}
    	else if ( yy > 49 && yy < 100 )
    	{
    		yy+=1900;
    	}
    	
    	if ( mm == 2 ) 
    	{
    		if ( (!(yy % 4) && yy % 100) || !(yy % 400)) 
    		{
    			listofDays[2] = 29;
    		}
    	}
    	
    	if ( dd < 1 || dd > listOfDays[mm] )
    	{
    		return false;
    	}
    	    	
    	return true;
    }
   

    var isValidTimeUnit = function(timeUnit, minValue, maxValue)
    {
    	var unitValue = timeUnit.trim();
    	
    	if ( unitValue == "" )
    	{
    		return false;
    	}
    	
    	var value = parseInt(unitValue, 10);
    	
    	if ( isNaN(value) || unitValue.indexOf(".") > -1 || value < minValue || value > maxValue )
    	{
    		return false;
    	}
    	
    	return true;
    }
    
    service.validateIncident = function()
    {
    	var dt = new Date();
    	
    	var errors = [];
    	service.incident.incidentDate = service.incident.incidentDate.trim();
    	
    	if ( service.incident.incidentDate == '' )
    	{
    		errors.push("Incident Date required");
    	}
    	else if ( ! isValidDate(service.incident.incidentDate) )
    	{
    		errors.push("Invalid Incident Date");
    	}
    	
    	service.incident.incidentTimeHour = service.incident.incidentTimeHour.trim();
    	service.incident.incidentTimeMinute = service.incident.incidentTimeMinute.trim();
    	
    	if ( service.incident.incidentTimeHour == '' )
    	{
    		errors.push("Incident Time hour required");
    	}
    	else if ( ! isValidTimeUnit(service.incident.incidentTimeHour, 1, 12) )
    	{
    		errors.push("Invalid Incident Time hour");
    	}
    	
    	if ( service.incident.incidentTimeMinute == '' )
    	{
    		errors.push("Incident Time minute required");
    	}
    	else if ( ! isValidTimeUnit(service.incident.incidentTimeMinute, 0, 59) )
    	{
    		errors.push("Invalid Incident Time minute");
    	}
    	
    	// Valid date and time, now make sure it is not in the future
    	if ( errors.length == 0 )
    	{
    		var parts = service.incident.incidentDate.split("/");
        	
        	var month = parseInt(parts[0]) - 1;  
        	var day  = parseInt(parts[1]);  
        	var year = parseInt(parts[2]); 
        	
        	if ( year > 0 && year < 50 )
        	{
        		year += 2000;
        	}
        	else if ( year > 49 && year < 100 )
        	{
        		yy += 1900;
        	}
        	
        	var hour = parseInt(service.incident.incidentTimeHour);
        	var minute = parseInt(service.incident.incidentTimeMinute);
        	
        	if ( service.incident.incidentTimeAmPm == 'PM' )
        	{
        		if ( hour < 12 )
        		{
        			hour += 12;
        		}
        	}
        	else if ( hour == 12 )
        	{
        		hour = 0;
        	}
        	
    		var incidentDate = new Date(year, month, day, hour, minute, 0, 0);
    		
    		var now = new Date();
    		if ( incidentDate.getTime() > now.getTime() )
    		{
    			errors.push("Incident Date/Time in the future");
    		}
    	}
    	
    	service.incident.durationMinutes = service.incident.durationMinutes.trim();
    	if ( service.incident.durationMinutes == '' )
    	{
    		service.incident.durationMinutes = '00';
    	}
    	if ( ! isValidTimeUnit(service.incident.durationMinutes, 0, 999) )
    	{
    		errors.push("Invalid Duration minutes");
    	}
    	
    	service.incident.durationSeconds = service.incident.durationSeconds.trim();
    	if ( service.incident.durationSeconds == '' )
    	{
    		service.incident.durationSeconds = '00';
    	}
    	if ( ! isValidTimeUnit(service.incident.durationSeconds, 0, 999) )
    	{
    		errors.push("Invalid Duration seconds");
    	}
    	
    	if ( getFilterString(service.currentObserved.antecedents) == '' )
    	{
    		errors.push("At least one Antecedent required");
    	}
    	
    	if ( getFilterString(service.currentObserved.behaviors) == '' )
    	{
    		errors.push("At least one Behavior required");
    	}
    	
    	if ( getFilterString(service.currentObserved.consequences) == '' )
    	{
    		errors.push("At least one Consequence required");
    	}
    	
    	return errors;
    }
    
    service.save = function()
    {
    	return $http.get('saveIncident?id=' + service.incident.id
    			+ '&obsId=' + service.observedId
    			+ '&dt=' + service.incident.incidentDate
    			+ '&hr=' + service.incident.incidentTimeHour
    			+ '&min=' + service.incident.incidentTimeMinute
    			+ '&amPm=' + service.incident.incidentTimeAmPm
    			+ '&durMin=' + service.incident.durationMinutes
    			+ '&durSec=' + service.incident.durationSeconds
    			+ '&intensity=' + service.incident.intensityId
    			+ '&location=' + service.incident.locationId
    			+ '&desc=' + service.incident.description
    			+ '&ant=' + getFilterString(service.currentObserved.antecedents)
    			+ '&beh=' + getFilterString(service.currentObserved.behaviors)
    			+ '&con=' + getFilterString(service.currentObserved.consequences)
    			, {
    	    	headers: {'Authorization': service.sessionKey}
    			}
    			)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		var msg = service.incident.id == 0 
            				? 'Incident added for Observed' : 'Incident updated for Observed';
            		service.incident.id = result.data.id;
            		return {status: 'SUCCESS', message: msg};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
    
    service.checkForDuplicate = function(id, abcTypeCd, abcValue)
    {
    	var val = abcValue.toUpperCase();
    	
    	var abcArr = [];
    	if ( abcTypeCd == 'A' )
		{
			abcArr = service.currentObserved.antecedents;
		}
		else if ( abcTypeCd == 'B' )
		{
			abcArr = service.currentObserved.behaviors;
		}
		else if ( abcTypeCd == 'C' )
		{
			abcArr = service.currentObserved.consequences;
		}
    	else
		{
			abcArr = service.currentObserved.locations;
		}
    	
    	for ( var i = 0; i < abcArr.length; i++ )
    	{
    		if ( abcArr[i].typeValue.toUpperCase() == val && abcArr[i].id != id )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    service.addABC = function(abcTypeCd, abcValue)
    {
    	return $http.get('addObservedABC?obsId=' + service.observedId
    			+ '&typeCd=' + abcTypeCd
    			+ '&typeValue=' + abcValue
    			,{
    	    	headers: {'Authorization': service.sessionKey}
    			}
    			)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		var typeCd = result.data.typeCd;
            		
            		if ( typeCd == 'A' )
            		{
            			service.currentObserved.antecedents.push(result.data);
            		}
            		else if ( typeCd == 'B' )
            		{
            			service.currentObserved.behaviors.push(result.data);
            		}
            		else if ( typeCd == 'C' )
            		{
            			service.currentObserved.consequences.push(result.data);
            		}
            		else 
            		{
            			service.currentObserved.locations.push(result.data);
            		}
            		
            		return {status: 'SUCCESS', message: 'ABC added for Observed'};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    	
    }
    
    service.updateABC = function(id, abcTypeCd, abcValue, activeFl)
    {
    	return $http.get('updateObservedABC?id=' + id
    			+ '&typeCd=' + abcTypeCd
    			+ '&typeValue=' + abcValue
    			+ '&activeFl=' + activeFl
    			,{
    	    	headers: {'Authorization': service.sessionKey}
    			}
    			)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		var typeCd = result.data.typeCd;
            		var isActive = result.data.activeFl == 'Y';
            		var abcId = result.data.valueId;
            		var abcArr = [];
            		
            		if ( typeCd == 'A' )
            		{
            			abcArr = service.currentObserved.antecedents;
            		}
            		else if ( typeCd == 'B' )
            		{
            			abcArr = service.currentObserved.behaviors;
            		}
            		else if ( typeCd == 'C' )
            		{
            			abcArr = service.currentObserved.consequences;
            		}
            		else 
            		{
            			abcArr = service.currentObserved.locations;
            		}
            		
            		for ( var i = 0; i < abcArr.length; i++ )
                	{
                		if ( abcArr[i].valueId == abcId )
                		{
                			if ( isActive )
                			{
                				abcArr[i].typeValue = result.data.typeValue;
                			}
                			else
                			{
                				abcArr.splice(i, 1);
                			}
                			break;
                		}
                	}
            		
            		return {status: 'SUCCESS', message: 'ABC update for Observed'};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    	
    }
    
    service.checkForDuplicateObserved = function(observedNm, id)
    {
    	var val = observedNm.toUpperCase();
    	
    	for ( var i = 0; i < service.pageObserved.length; i++ )
    	{
    		var obs = service.pageObserved[i];
    		if ( obs.observedNm.toUpperCase() == val && obs.id != id )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    service.addObserved = function(observedNm, relationshipId)
    {
    	return $http.get('addObserved?observedNm=' + observedNm
    			+ '&relationshipId=' + relationshipId
    			,{
    	    	headers: {'Authorization': service.sessionKey}
    			}
    			)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		var obs = result.data;
            		
            		service.observed.push(obs);
            		service.pageObserved.push(obs);
            		
            		// If only one in array set current to this observed and get ABCS
            		if ( service.pageObserved.length == 1 )
            		{
            			service.initObserved(0);
            		}
            		            		
            		return {status: 'SUCCESS', message: 'Observed added'};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
    
    service.updateObserved = function(observedNm, role, relationshipId)
    {
    	return $http.get('updateObserved?obsId=' + service.observedId
    			+ '&observedNm=' + observedNm
    			+ '&role=' + role
    			+ '&relationshipId=' + relationshipId
    			,{
    	    	headers: {'Authorization': service.sessionKey}
    			}
    			)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		var obs = result.data;
            		
            		service.currentObserved.observedNm = obs.observedNm;
            		service.currentObserved.role = obs.role;
            		service.currentObserved.relationshipIdStr = obs.relationshipIdStr;
            		            		            		
            		return {status: 'SUCCESS', message: 'Observed added'};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    	
    }
    
    service.grantAccess = function(email, role, relationshipId)
    {
    	return $http.get('grantAccess?obsId=' + service.observedId
    			+ '&email=' + email
    			+ '&role=' + role
    			+ '&relationshipId=' + relationshipId
    			,{
    	    	headers: {'Authorization': service.sessionKey}
    			}
    			)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		var obs = result.data;
            		
            		service.observers.push(obs);
            		            		            		
            		return {status: 'SUCCESS', message: 'User access granted'};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
    
    service.updateAccess = function(userId, role, relationshipId)
    {
    	return $http.get('updateAccess?obsId=' + service.observedId
    			+ '&userId=' + userId
    			+ '&role=' + role
    			+ '&relationshipId=' + relationshipId
    			,{
    	    	headers: {'Authorization': service.sessionKey}
    			}
    			)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		var obs = result.data;
            		
            		for ( var i = 0; i < service.observers.length; i++ )
                	{
                		var observer = service.observers[i];
                		if ( observer.userIdStr == obs.userIdStr )
                		{
                			observer.relationshipIdStr = obs.relationshipIdStr;
                			observer.role = obs.role;
                			observer.relationship = obs.relationship;
                			break;
                		}
                	}
            		            		            		
            		return {status: 'SUCCESS', message: 'User access updated'};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
    
    service.removeAccess = function(userId)
    {
    	return $http.get('removeAccess?obsId=' + service.observedId
    			+ '&userId=' + userId
    			,{
    	    	headers: {'Authorization': service.sessionKey}
    			}
    			)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		var obs = result.data;
            		
            		for ( var i = 0; i < service.observers.length; i++ )
                	{
                		var observer = service.observers[i];
                		if ( observer.userIdStr == obs.userIdStr )
                		{
                			service.observers.splice(i,1);
                			break;
                		}
                	}
            		            		            		
            		return {status: 'SUCCESS', message: 'User access updated'};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
    
    service.removeObserved = function(userId)
    {
    	return $http.get('removeAccess?obsId=' + service.observedId
    			+ '&userId=' + userId
    			,{
    	    	headers: {'Authorization': service.sessionKey}
    			}
    			)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		var obs = result.data;
            		
            		for ( var i = 0; i < service.observed.length; i++ )
                	{
                		var observer = service.observed[i];
                		if ( service.observed[i].idStr == obs.idStr )
                		{
                			service.observed.splice(i,1);
                			break;
                		}
                	}
            		
            		service.pageObserved = service.observed;
            		service.currentObserved = {};
            	    service.observedId = '0';
            		            		            		
            		return {status: 'SUCCESS', message: 'User access updated'};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
    
    service.getObservers = function ()
    {
    	return $http.get('observers?obsId=' + service.observedId, {
    	    	headers: {'Authorization': service.sessionKey}
    		})
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		return result.data;
            	}
            	else
            	{
            		service.observers = result.data;
            		
            		return {status: 'SUCCESS', message: 'Observed ABCs Returned'};
            	}
            })
            .catch(function(error)
            {
            	service.observers = [];
            	return {status: 'ERROR', message: 'Unknown Error'};
        	});
    }
    
    service.clearObservers = function()
    {
    	service.observers = [];
    }
}