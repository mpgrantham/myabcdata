package com.canalbrewing.myabcdata.controller;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.myabcdata.business.IncidentBusiness;
import com.canalbrewing.myabcdata.business.UserBusiness;
import com.canalbrewing.myabcdata.exception.MyAbcDataException;
import com.canalbrewing.myabcdata.model.StatusMessage;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Intensity;
import com.canalbrewing.myabcdata.model.RequestIncident;
import com.canalbrewing.myabcdata.model.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IncidentController {

	@Autowired
	UserBusiness userBusiness;

	@Autowired
	IncidentBusiness incidentBusiness;

	@GetMapping("/intensities")
	public ResponseEntity<List<Intensity>> getIntensities() throws SQLException {
		List<Intensity> intensities = incidentBusiness.getIntensities();

		return new ResponseEntity<>(intensities, HttpStatus.OK);
	}

	@PostMapping("/incident")
	public ResponseEntity<Incident> addIncident(@RequestHeader("Authorization") String sessionToken,
			@RequestBody RequestIncident requestIncident) throws SQLException, MyAbcDataException {
		UserSession userSession = userBusiness.getUserSession(sessionToken);

		incidentBusiness.checkEntryPermission(userSession.getUserId(), requestIncident.getObservedId());

		Incident incident = incidentBusiness.saveIncident(userSession.getUserId(), requestIncident);

		return new ResponseEntity<>(incident, HttpStatus.OK);
	}

	@PutMapping("/incident")
	public ResponseEntity<Incident> updateIncident(@RequestHeader("Authorization") String sessionToken,
			@RequestBody RequestIncident requestIncident) throws SQLException, MyAbcDataException {
		UserSession userSession = userBusiness.getUserSession(sessionToken);

		incidentBusiness.checkEntryPermission(userSession.getUserId(), requestIncident.getObservedId());

		Incident incident = incidentBusiness.updateIncident(userSession.getUserId(), requestIncident);

		return new ResponseEntity<>(incident, HttpStatus.OK);
	}

	@DeleteMapping("/incident/{incidentId}/observed/{observedId}")
	public ResponseEntity<StatusMessage> deleteIncident(
			@PathVariable Integer observedId,
			@PathVariable Integer incidentId,
			@RequestHeader("Authorization") String sessionToken) throws SQLException, MyAbcDataException {
		UserSession userSession = userBusiness.getUserSession(sessionToken);

		incidentBusiness.checkAdminPermission(userSession.getUserId(), observedId);

		incidentBusiness.deleteIncident(incidentId);

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Incident deleted"), HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StatusMessage> handleException(Exception ex) {
		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<StatusMessage> handleSQLException(SQLException ex) {
		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MyAbcDataException.class)
	public ResponseEntity<StatusMessage> handleMyAbcDataException(MyAbcDataException ex) {
		switch (ex.getExceptionCode()) {
			case MyAbcDataException.INVALID_LOGIN:
			case MyAbcDataException.UNAUTHORIZED:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.UNAUTHORIZED);
			case MyAbcDataException.NOT_FOUND:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.NOT_FOUND);
			default:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}