angular
.module('userModel', [])
.service('UserModel', UserModel);

function UserModel($http)
{
    var service = this;
    
    service.relationships = [];
    
    service.startPages = ['HOME', 'ENTRY', 'LOG'];
       
    service.user = {
    		id: 0,
    		username: '', 
    		password: '', 
    		message: 'Sign In to My ABC Data', 
    		email: '', 
    		startPage: 'HOME',
    		observedName: '', 
    		relationship: '0', 
    		sessionKey: '',
    		signInStatus: 1
    };
        
    service.resetKey = '';
    
    service.initUser = function()
    {
    	service.user.id = 0;
        service.user.username = '';
        service.user.password = '';
        service.user.email = '';
        service.user.message = 'Sign In to My ABC Data';
        service.user.startPage = 'HOME';
        service.user.sessionKey = '';
        service.user.observedName = '';
        service.user.relationship = '0';
        service.user.signInStatus = 1;
        
        // Get Relationships
    }
    
    service.getRelationships = function ()
    {
    	return $http.get('relationships')
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('status') )
            	{
            		service.message = result.data.message;
            	}
            	else
            	{
            		service.relationships = result.data;
            	}
            })
            .catch(function(error)
            {
            	service.message = 'Unknown Error';
            });
    }
    
    service.register = function ()
    {
    	return $http.get('register?userNm=' + service.user.username 
    			+ '&password=' + service.user.password
    			+ '&email=' + service.user.email
    			+ '&startPage=' + service.user.startPage
    			+ '&observedNm=' + service.user.observedName
    			+ '&relationshipId=' + service.user.relationship
    			)
            .then(function(result)
            {
            	return result.data;
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
                                
    service.signIn = function ()
    {
    	return $http.get('signIn?username=' + service.user.username + '&password=' + service.user.password)
            .then(function(result)
            {
            	if ( result.data.hasOwnProperty('sessionKey') )
            	{
            		service.user.sessionKey = result.data.sessionKey;
            		service.user.signInStatus = 0;
            		service.user.email = result.data.email;
            		service.user.startPage = result.data.startPage;
            		service.user.id = result.data.id;
            	}
            	else
            	{
            		service.user.sessionKey = '';
            		service.user.signInStatus = 2;
            		service.user.message = result.data.message;
            	}
            })
            .catch(function(error)
            {
            	console.log("Error: " + error);
            	service.user.sessionKey = '';
            	service.user.signInStatus = 2;
        		service.user.message = 'Unknown Error';
            });
    }
    
    service.signOut = function ()
    {
    	return $http.get('signOut', {
        	    headers: {'Authorization': service.user.sessionKey}
        	})
            .then(function(result)
            {
            	service.initUser();
            })
            .catch(function(error)
            {

            });
    }
        
    service.updateSettings = function ()
    {
    	return $http.get('updateSettings?userNm=' + service.user.username 
    			+ '&password=' + service.user.password
    			+ '&email=' + service.user.email
    			+ '&startPage=' + service.user.startPage,
    			{
            	    headers: {'Authorization': service.user.sessionKey}
            	}
    			)
            .then(function(result)
            {
            	return result.data;
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
    
    service.sendForgot = function (email, forgotType)
    {
    	return $http.get('sendUsernamePassword?email=' + email
    			+ '&forgotType=' + forgotType
    			)
            .then(function(result)
            {
            	return result.data;
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
    
    service.checkResetKey = function ()
    {
    	return $http.get('checkResetKey?resetKey=' + service.resetKey )
            .then(function(result)
            {
            	return result.data;
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }
    
    service.resetPassword = function (password)
    {
    	return $http.get('saveResetPassword?resetKey=' + service.resetKey + '&password=' + password )
            .then(function(result)
            {
            	return result.data;
            })
            .catch(function(error)
            {
            	return {status: 'ERROR', message: 'Unknown Error'};
            });
    }

}

