package com.canalbrewing.myabcdata.business;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.myabcdata.model.Abc;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.IncidentSummary;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.RequestAbc;

import org.springframework.stereotype.Component;

import com.itextpdf.text.DocumentException;

@Component
public interface ObservedBusiness {

	List<Observed> getObservedByUser(int userId) throws SQLException;

	Observed getObserved(String observedId) throws SQLException;

	List<Incident> getIncidentsByObserved(String observedId, String incidentStartDt) throws SQLException;

	Incident getIncidentById(String observedId, String incidentId) throws SQLException;

	IncidentSummary getIncidentSummaryByObserved(String observedId) throws SQLException;

	Abc addObservedABC(String observedId, RequestAbc requestAbc) throws SQLException;

	Abc updateObservedABC(String observedId, RequestAbc requestAbc) throws SQLException;

	byte[] getObservedDataSheet(String observedId) throws DocumentException, IOException;

	Observed deleteObserved(String observedId) throws SQLException;

}