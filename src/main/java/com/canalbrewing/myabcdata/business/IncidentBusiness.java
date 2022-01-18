package com.canalbrewing.myabcdata.business;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.myabcdata.exception.MyAbcDataException;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.RequestIncident;
import com.canalbrewing.myabcdata.model.Intensity;

import org.springframework.stereotype.Component;

@Component
public interface IncidentBusiness {

    List<Intensity> getIntensities() throws SQLException;

    Incident saveIncident(int userId, RequestIncident incidentEntry) throws SQLException;

    Incident updateIncident(int userId, RequestIncident incidentEntry) throws SQLException;

    void deleteIncident(int incidentId) throws SQLException, MyAbcDataException;

    void checkEntryPermission(int userId, int observedId) throws SQLException, MyAbcDataException;

    void checkAdminPermission(int userId, int observedId) throws SQLException, MyAbcDataException;

}