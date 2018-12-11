angular
.module('entryModel', [])
.service('EntryModel', EntryModel);

function EntryModel($http)
{
    var service = this;
    
    service.sessionKey = '';
       
    
    service.intensities = [];
    
    
    service.message = '';
    
   
    service.incident = {
    		id: 0,
    		observedId: '0',
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
    

    service.initEntry = function(sessionKey)
    {
    	service.sessionKey = sessionKey;
    	    	
        service.message = '';
        
        service.entry.id = 0;
        //service.entry.observedId = '0';
        service.entry.durationMinutes = '00';
        service.entry.durationSeconds = '00';
        service.entry.intensityId = '0';
        service.entry.locationId = '0';
        service.entry.description = '';
        
        var dt = new Date();
    	
    	var dd = ("0" + dt.getDate()).slice(-2);
    	var mm = ("0"+(dt.getMonth()+1)).slice(-2);
    	var yyyy = dt.getFullYear();
    	
    	var hr = dt.getHours();
    	var min = ("0" + dt.getMinutes()).slice(-2);
    	
    	if ( hr > 12 )
    	{
    		service.entry.incidentTimeHour = (hr - 12) + "";
    		service.entry.incidentTimeAmPm = 'PM';
    	}
    	else
    	{
    		service.entry.incidentTimeHour = hr + "";
    		service.entry.incidentTimeAmPm = 'AM';
    	}
    	
    	service.entry.incidentTimeMinute = min + "";
    	
    	service.entry.incidentDate = mm + '/' + dd + '/' + yyyy;
    	
    	if ( service.observed.length > 0 )
		{
			service.currentObserved = service.observed[0];
    		service.entry.observedId = service.currentObserved.strId;
    		service.entry.locationId = service.currentObserved.locations[0].strId;
		}
    }
        
    service.getIntensities = function ()
    {
    	return $http.get('intensities')
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		service.message = result.data.message;
            	}
            	else
            	{
            		service.intensities = result.data;
            	}
            })
            .catch(function(error)
            {
            	service.message = 'Unknown Error';
            });
    }
    
    service.getFilterString = function(arr)
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
    
    service.save = function()
    {
    	var a = service.getFilterString(service.currentObserved.antecedents);
    	var b = service.getFilterString(service.currentObserved.behaviors);
    	var c = service.getFilterString(service.currentObserved.consequences);
    	
    	return $http.get('saveIncident?id=' + service.entry.id
    			+ '&obsId=' + service.entry.observedId
    			+ '&dt=' + service.entry.incidentDate
    			+ '&hr=' + service.entry.incidentTimeHour
    			+ '&min=' + service.entry.incidentTimeMinute
    			+ '&amPm=' + service.entry.incidentTimeAmPm
    			+ '&durMin=' + service.entry.durationMinutes
    			+ '&durSec=' + service.entry.durationSeconds
    			+ '&intensity=' + service.entry.intensityId
    			+ '&location=' + service.entry.locationId
    			+ '&desc=' + service.entry.description
    			+ '&ant=' + a
    			+ '&beh=' + b
    			+ '&con=' + c
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
            		service.entry.id = result.data.id;
            		return {status: 'SUCCESS', message: 'Incident added for Observed'};
            	}
            	
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
}