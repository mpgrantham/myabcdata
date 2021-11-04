package com.canalbrewing.myabcdata.dal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.canalbrewing.myabcdata.model.Abc;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Observed;

import org.springframework.stereotype.Component;

@Component
public interface ObservedDao {

	List<Observed> getObservedByUser(int userId) throws SQLException;

	Observed getObservedById(int observedId) throws SQLException;

	List<Abc> getObservedABCs(int observedId) throws SQLException;

	List<Incident> getIncidentsByObserved(int observedId, String incidentStartDt) throws SQLException;

	List<Date> getIncidentDatesByObserved(int observedId) throws SQLException;

	Incident getIncidentById(int observedId, int incidentId) throws SQLException;

	Abc insertObservedABC(int observedId, Abc abc) throws SQLException;

	Abc insertObservedLocation(int observedId, Abc abc) throws SQLException;

	Abc updateObservedABC(Abc abc) throws SQLException;

	Abc updateObservedLocation(Abc abc) throws SQLException;

	Observed insertObserved(Observed observed) throws SQLException;

	Observed updateObserved(Observed observed) throws SQLException;

	Observed insertObservers(Observed observed) throws SQLException;

	Observed updateObservers(Observed observed) throws SQLException;

	Observed deleteObservers(Observed observed) throws SQLException;

	Observed deleteObserved(Observed observed) throws SQLException;

	List<Observed> getUsersByObserved(int observedId) throws SQLException;

	Observed reassignObserved(Observed observed, Observed priorObserved, boolean updateObservers) throws SQLException;

	Observed insertObserved(Connection conn, Observed observed) throws SQLException;

	void insertObservers(Connection conn, Observed observed) throws SQLException;

}