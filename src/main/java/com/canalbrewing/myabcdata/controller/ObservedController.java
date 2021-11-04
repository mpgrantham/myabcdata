package com.canalbrewing.myabcdata.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.myabcdata.business.ObservedBusiness;
import com.canalbrewing.myabcdata.business.UserBusiness;
import com.canalbrewing.myabcdata.exception.MyAbcDataException;

import com.canalbrewing.myabcdata.model.Abc;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.RequestAbc;
import com.canalbrewing.myabcdata.model.StatusMessage;
import com.canalbrewing.myabcdata.model.UserSession;
import com.itextpdf.text.DocumentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("observed")
public class ObservedController {

	@Autowired
	UserBusiness userBusiness;

	@Autowired
	ObservedBusiness observedBusiness;

	@GetMapping("/")
	public ResponseEntity<List<Observed>> getUserObserved(@RequestHeader("Authorization") String sessionToken)
			throws SQLException {
		UserSession userSession = userBusiness.getUserSession(sessionToken);

		List<Observed> observedList = observedBusiness.getObservedByUser(userSession.getUserId());

		return new ResponseEntity<>(observedList, HttpStatus.OK);
	}

	@GetMapping("/{observedId}")
	public ResponseEntity<Observed> getObserved(@PathVariable String observedId,
			@RequestHeader("Authorization") String sessionToken) throws SQLException {
		userBusiness.getUserSession(sessionToken);

		Observed observed = observedBusiness.getObserved(observedId);

		return new ResponseEntity<>(observed, HttpStatus.OK);
	}

	@GetMapping("/{observedId}/incidents")
	public ResponseEntity<List<Incident>> getObservedIncidents(@PathVariable String observedId,
			@RequestParam(value = "start", required = false) String incidentStartDt,
			@RequestHeader("Authorization") String sessionToken) throws SQLException {
		userBusiness.getUserSession(sessionToken);

		return new ResponseEntity<>(observedBusiness.getIncidentsByObserved(observedId, incidentStartDt),
				HttpStatus.OK);
	}

	@GetMapping("/{observedId}/incidents/{incidentId}")
	public ResponseEntity<Incident> getObservedIncident(@PathVariable String observedId,
			@PathVariable String incidentId, @RequestHeader("Authorization") String sessionToken) throws SQLException {
		userBusiness.getUserSession(sessionToken);

		return new ResponseEntity<>(observedBusiness.getIncidentById(observedId, incidentId), HttpStatus.OK);
	}

	@GetMapping("/{observedId}/dataSheet")
	public ResponseEntity<byte[]> getObservedDataSheet(@PathVariable String observedId,
			@RequestHeader("Authorization") String sessionToken) throws DocumentException, IOException, SQLException {
		userBusiness.getUserSession(sessionToken);

		byte[] bytes = observedBusiness.getObservedDataSheet(observedId);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).cacheControl(CacheControl.noCache())
				.contentLength(bytes.length).header("content-Disposition", "attachment; filename=datasheet.pdf")
				.body(bytes);
	}

	@PostMapping("/{observedId}/abc")
	public ResponseEntity<Abc> addObservedABC(@PathVariable String observedId, @RequestBody RequestAbc requestAbc,
			@RequestHeader("Authorization") String sessionToken) throws SQLException {
		userBusiness.getUserSession(sessionToken);

		return new ResponseEntity<>(observedBusiness.addObservedABC(observedId, requestAbc), HttpStatus.OK);
	}

	@PutMapping("/{observedId}/abc")
	public ResponseEntity<Abc> updateObservedABC(@PathVariable String observedId, @RequestBody RequestAbc requestAbc,
			@RequestHeader("Authorization") String sessionToken) throws SQLException {
		userBusiness.getUserSession(sessionToken);

		return new ResponseEntity<>(observedBusiness.updateObservedABC(observedId, requestAbc), HttpStatus.OK);
	}

	@DeleteMapping("/{observedId}")
	public ResponseEntity<Observed> deleteObserved(@PathVariable String observedId,
			@RequestHeader("Authorization") String sessionToken) throws SQLException {
		userBusiness.getUserSession(sessionToken);

		Observed observed = observedBusiness.deleteObserved(observedId);

		return new ResponseEntity<>(observed, HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StatusMessage> handleException(Exception ex) {
		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DocumentException.class)
	public ResponseEntity<StatusMessage> handleDocumentException(DocumentException ex) {
		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<StatusMessage> handleIOException(IOException ex) {
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