package com.canalbrewing.myabcdata.dal;

import java.util.List;
import java.sql.SQLException;

import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Intensity;

import org.springframework.stereotype.Component;

@Component
public interface IncidentDao {

    List<Intensity> getIntensities() throws SQLException;
	
	void insertIncident(Incident incident) throws SQLException;
	
	void updateIncident(Incident incident) throws SQLException;

	void deleteIncident(int incidentId) throws SQLException;
    
}