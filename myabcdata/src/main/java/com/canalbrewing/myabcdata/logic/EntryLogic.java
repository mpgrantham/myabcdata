package com.canalbrewing.myabcdata.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.IncidentEntry;
import com.canalbrewing.myabcdata.model.ValueObject;

@Component
public interface EntryLogic {
	
	List<ValueObject> getIntensities() throws Exception;
	
	Incident saveIncident(int userId, IncidentEntry incidentEntry) throws Exception;
}
