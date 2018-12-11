package com.canalbrewing.myabcdata.dal;

import java.util.List;

import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.ValueObject;

@Component
public interface EntryDAO {
	
	List<ValueObject> getIntensities() throws Exception;
	
	void insertIncident(Incident incident) throws Exception;
	
	void updateIncident(Incident incident) throws Exception;
	
}
