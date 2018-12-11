package com.canalbrewing.myabcdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.canalbrewing.myabcdata.logic.UsersLogic;
import com.canalbrewing.myabcdata.model.RegisterUser;
import com.canalbrewing.myabcdata.model.StatusMessage;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;

@RestController
public class UserController {
	
	@Autowired
	private UsersLogic usersLogic;
	
	@GetMapping("/relationships")
	public Object getRelationships() 
	{
		try
		{
			return usersLogic.getRelationships();
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
    @GetMapping("/register")
    public Object reqister(@ModelAttribute("registerUser") RegisterUser registerUser) 
    {
    	StatusMessage message = new StatusMessage(StatusMessage.SUCCESS, "User Created");
    	
    	try
    	{
    		User user = registerUser.convertToUser();
    		usersLogic.saveUser(user);
    	}
    	catch ( Exception ex )
    	{
    		ex.printStackTrace();
			message = new StatusMessage(StatusMessage.ERROR, ex.getMessage() == null ? "Unknown Error" : ex.getMessage());
    	}
    	
    	return message;
    }
    
    @GetMapping("/signIn")
	public Object signIn(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) 
	{
		try
		{
			return usersLogic.signInUser(username, password);
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/signOut")
	public Object signOut(@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			usersLogic.signOutUser(sessionToken);
			
			return new StatusMessage(StatusMessage.SUCCESS, "User Signed Out");
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/sendUsernamePassword")
	public Object sendUsernamePassword(@RequestParam(value = "email", required = true) String email, 
			@RequestParam(value = "forgotType", required = true) String forgotType) 
	{
		return usersLogic.sendUsernameOrPassword(email, forgotType.toUpperCase());
	}
	
	@GetMapping("/checkResetKey")
	public Object checkResetKey(@RequestParam(value = "resetKey", required = true) String resetKey) 
	{
		try
		{
			User user = usersLogic.getUserByResetKey(resetKey);
			if ( user == null )
			{
				return new StatusMessage(StatusMessage.ERROR, "Reset Link not found");
			}
			else
			{
				return new StatusMessage(StatusMessage.SUCCESS, "Provide new Password");
			}
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/saveResetPassword")
	public Object saveResetPassword(@RequestParam(value = "resetKey", required = true) String resetKey,
			@RequestParam(value = "password", required = true) String password) 
	{
		try
		{
			User user = usersLogic.getUserByResetKey(resetKey);
			if ( user == null )
			{
				return new StatusMessage(StatusMessage.ERROR, "Reset Link not found");
			}
			
			user.setPassword(password);
			usersLogic.saveUser(user);
			
			return new StatusMessage(StatusMessage.SUCCESS, "Password Reset successful");
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/updateSettings")
    public Object updateSettings(@ModelAttribute("registerUser") RegisterUser registerUser, 
    		@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			UserSession session = usersLogic.getUserSession(sessionToken);
			
			User user = registerUser.convertToUser();
			user.setId(session.getUserId());
			
			usersLogic.saveUser(user);
			
			return new StatusMessage(StatusMessage.SUCCESS, "Settings updated");
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/addObserved")
    public Object addObserved(@RequestParam(value = "observedNm", required = true) String observedNm,
    		@RequestParam(value = "relationshipId", required = true) String relationshipId, 
    		@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			UserSession session = usersLogic.getUserSession(sessionToken);
			
			return usersLogic.addObserved(session.getUserId(), observedNm, relationshipId);
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/updateObserved")
    public Object updateObserved(@RequestParam(value = "obsId", required = true) String obsId,
    		@RequestParam(value = "observedNm", required = true) String observedNm,
    		@RequestParam(value = "role", required = true) String role, 
    		@RequestParam(value = "relationshipId", required = true) String relationshipId, 
    		@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			UserSession session = usersLogic.getUserSession(sessionToken);
			
			return usersLogic.updateObserved(session.getUserId(), obsId, observedNm, role, relationshipId);
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/grantAccess")
    public Object grantAccess(@RequestParam(value = "obsId", required = true) String obsId,
    		@RequestParam(value = "email", required = true) String email,
    		@RequestParam(value = "role", required = true) String role, 
    		@RequestParam(value = "relationshipId", required = true) String relationshipId, 
    		@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			usersLogic.getUserSession(sessionToken);
			
			return usersLogic.grantAccess(obsId, email, role, relationshipId);
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/updateAccess")
    public Object updateAccess(@RequestParam(value = "userId", required = true) String userId,
    		@RequestParam(value = "obsId", required = true) String obsId,
    		@RequestParam(value = "role", required = true) String role, 
    		@RequestParam(value = "relationshipId", required = true) String relationshipId, 
    		@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			usersLogic.getUserSession(sessionToken);
			
			return usersLogic.updateObservers(userId, obsId, role, relationshipId);
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/removeAccess")
    public Object removeAccess(@RequestParam(value = "userId", required = true) String userId,
    		@RequestParam(value = "obsId", required = true) String obsId,
    		@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			usersLogic.getUserSession(sessionToken);
			
			return usersLogic.deleteObservers(userId, obsId);
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/observers")
    public Object getObservedUsers(@RequestParam(value = "obsId", required = true) String obsId,
    		@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			usersLogic.getUserSession(sessionToken);
			
			return usersLogic.getUsersByObserved(obsId);
		}
		catch ( Exception ex )
		{
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
		
} 

