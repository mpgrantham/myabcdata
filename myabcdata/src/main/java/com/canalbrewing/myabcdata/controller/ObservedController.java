package com.canalbrewing.myabcdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.canalbrewing.myabcdata.logic.ObservedLogic;
import com.canalbrewing.myabcdata.logic.UsersLogic;
import com.canalbrewing.myabcdata.model.StatusMessage;
import com.canalbrewing.myabcdata.model.UserSession;

@RestController
public class ObservedController {
	
	@Autowired
	private UsersLogic usersLogic;
	
	@Autowired
	private ObservedLogic observedLogic;

	@GetMapping("/userObserved")
	public Object getUserObserved(@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			UserSession userSession = usersLogic.getUserSession(sessionToken);
			
			return observedLogic.getObservedByUser(userSession.getUserId());
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/observedABCs")
	public Object getObservedABCs(@RequestParam(value = "obsId", required = true) String obsId,
			@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			usersLogic.getUserSession(sessionToken);
			
			return observedLogic.getObservedABCs(obsId);
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/observedIncidents")
	public Object getObservedIncidents(@RequestParam(value = "obsId", required = true) String obsId,
			@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			usersLogic.getUserSession(sessionToken);
			
			return observedLogic.getIncidentsByObserved(obsId);
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/addObservedABC")
	public Object addObservedABC(@RequestParam(value = "obsId", required = true) String obsId,
			@RequestParam(value = "typeCd", required = true) String typeCd,
			@RequestParam(value = "typeValue", required = true) String typeValue,
			@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			usersLogic.getUserSession(sessionToken);
			
			return observedLogic.addObservedABC(obsId, typeCd, typeValue);
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/updateObservedABC")
	public Object updateObservedABC(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "typeCd", required = true) String typeCd,
			@RequestParam(value = "typeValue", required = true) String typeValue,
			@RequestParam(value = "activeFl", required = true) String activeFl,
			@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			usersLogic.getUserSession(sessionToken);
			
			return observedLogic.updateObservedABC(id, typeCd, typeValue, activeFl);
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
}
