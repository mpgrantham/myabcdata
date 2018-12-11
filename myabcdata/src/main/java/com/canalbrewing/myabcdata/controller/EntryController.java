package com.canalbrewing.myabcdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.canalbrewing.myabcdata.logic.EntryLogic;
import com.canalbrewing.myabcdata.logic.UsersLogic;
import com.canalbrewing.myabcdata.model.IncidentEntry;
import com.canalbrewing.myabcdata.model.StatusMessage;
import com.canalbrewing.myabcdata.model.UserSession;

@RestController
public class EntryController {
	
	@Autowired
	private UsersLogic usersLogic;
	
	@Autowired
	private EntryLogic entryLogic;
			
	@GetMapping("/intensities")
	public Object getIntensities() 
	{
		try
		{
			return entryLogic.getIntensities();
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
	@GetMapping("/saveIncident")
	public Object saveIncident(@ModelAttribute("incidentEntry") IncidentEntry incidentEntry,
			@RequestHeader("Authorization") String sessionToken) 
	{
		try
		{
			UserSession userSession = usersLogic.getUserSession(sessionToken);
			
			return entryLogic.saveIncident(userSession.getUserId(), incidentEntry);
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage(StatusMessage.ERROR, ex.getMessage());
		}
	}
	
}
