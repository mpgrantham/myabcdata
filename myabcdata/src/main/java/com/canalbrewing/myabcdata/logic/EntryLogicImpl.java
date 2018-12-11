package com.canalbrewing.myabcdata.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.common.MyABCConstants;
import com.canalbrewing.myabcdata.dal.EntryDAO;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.IncidentEntry;
import com.canalbrewing.myabcdata.model.ValueObject;

@Component
public class EntryLogicImpl implements EntryLogic {
	
	@Autowired
	private EntryDAO entryDAO;
			
	public List<ValueObject> getIntensities() throws Exception
	{
		List<ValueObject> list = entryDAO.getIntensities();
		
		list.add(0, new ValueObject(0, MyABCConstants.NA));
		
		return list;
	}
	
	public Incident saveIncident(int userId, IncidentEntry incidentEntry) throws Exception
	{
		Incident incident = incidentEntry.convertToIncident();
		incident.setUserId(userId);
		
		if ( incident.getId() == 0 )
		{
			entryDAO.insertIncident(incident);
		}
		else
		{
			entryDAO.updateIncident(incident);
		}
		
		return incident;
	}
	
}
