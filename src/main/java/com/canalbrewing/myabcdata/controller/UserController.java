package com.canalbrewing.myabcdata.controller;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.myabcdata.business.UserBusiness;
import com.canalbrewing.myabcdata.exception.MyAbcDataException;
import com.canalbrewing.myabcdata.model.Relationship;
import com.canalbrewing.myabcdata.model.RequestUser;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.StatusMessage;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;
import com.canalbrewing.myabcdata.model.Verification;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	UserBusiness userBusiness;

	@GetMapping("/relationships")
	public ResponseEntity<List<Relationship>> getRelationships() throws SQLException {
		return new ResponseEntity<>(userBusiness.getRelationships(), HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<StatusMessage> register(@RequestBody RequestUser requestUser)
			throws SQLException, MyAbcDataException {
		User user = requestUser.convertToUser();

		userBusiness.register(user);

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS,
				"Confirmation email sent. Check your email for the confirm registration link."), HttpStatus.OK);
	}

	@GetMapping("/register")
	public ResponseEntity<Verification> getRegisterVerification(
			@RequestParam(value = "key", required = true) String key) throws SQLException {
		Verification verification = userBusiness.getVerification(key);

		return new ResponseEntity<>(verification, HttpStatus.OK);
	}

	@PutMapping("/register")
	public ResponseEntity<StatusMessage> confirmRegister(@RequestBody RequestUser requestUser)
			throws SQLException, MyAbcDataException {

		userBusiness.confirmRegister(requestUser.getKey());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Confirmation successful."),
				HttpStatus.OK);
	}

	@PostMapping("/signIn")
	public ResponseEntity<User> signIn(@RequestBody RequestUser requestUser) throws SQLException, MyAbcDataException {
		User user = userBusiness.signInUser(requestUser.getUsername(), requestUser.getPassword(),
				requestUser.isStaySignedIn());

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PutMapping(value = "/signOut")
	public ResponseEntity<StatusMessage> signOut(@RequestHeader("Authorization") String sessionToken)
			throws SQLException, MyAbcDataException {
		userBusiness.signOutUser(sessionToken);

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "User Signed Out"), HttpStatus.OK);
	}

	@PostMapping("/keySignIn")
	public ResponseEntity<User> signInWithKey(@RequestBody RequestUser requestUser)
			throws SQLException, MyAbcDataException {
		User user = userBusiness.signInUser(requestUser.getKey());

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/sendUsernamePassword")
	public Object sendUsernamePassword(@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "forgotType", required = true) String forgotType) throws MyAbcDataException {
		return userBusiness.sendUsernameOrPassword(email, forgotType.toUpperCase());
	}

	@GetMapping("/checkResetKey")
	public ResponseEntity<StatusMessage> checkResetKey(@RequestParam(value = "key", required = true) String key)
			throws SQLException, MyAbcDataException {
		userBusiness.getUserByResetKey(key);
		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Provide new Password"), HttpStatus.OK);
	}

	@PostMapping("/saveResetPassword")
	public ResponseEntity<StatusMessage> saveResetPassword(@RequestBody RequestUser requestUser)
			throws SQLException, MyAbcDataException {

		userBusiness.resetPassword(requestUser.getKey(), requestUser.getPassword());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Password Reset successful"),
				HttpStatus.OK);
	}

	@PutMapping(value = "/settings")
	public ResponseEntity<StatusMessage> updateSettings(@RequestHeader("Authorization") String sessionToken,
			@RequestBody RequestUser requestUser) throws SQLException, MyAbcDataException {

		UserSession userSession = userBusiness.getUserSession(sessionToken);

		User user = new User();
		user.setId(userSession.getUserId());
		user.setEmail(requestUser.getEmail());
		user.setStartPage(requestUser.getStartPage());
		user.setStatus(User.STATUS_ACTIVE);

		userBusiness.updateUser(user);

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Settings updated"), HttpStatus.OK);
	}

	@PutMapping(value = "/password")
	public ResponseEntity<StatusMessage> updatePassword(@RequestHeader("Authorization") String sessionToken,
			@RequestBody RequestUser requestUser) throws SQLException, MyAbcDataException {

		UserSession userSession = userBusiness.getUserSession(sessionToken);

		userBusiness.updatePassword(userSession.getUserId(), requestUser.getCurrentPassword(),
				requestUser.getPassword());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Password updated"), HttpStatus.OK);
	}

	@PutMapping(value = "/username")
	public ResponseEntity<StatusMessage> updateUsername(@RequestHeader("Authorization") String sessionToken,
			@RequestBody RequestUser requestUser) throws SQLException, MyAbcDataException {

		UserSession userSession = userBusiness.getUserSession(sessionToken);

		userBusiness.updateUsername(userSession.getUserId(), requestUser.getUsername());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Username updated"), HttpStatus.OK);
	}

	@PutMapping("/observed")
	public ResponseEntity<Observed> updateObserved(@RequestHeader("Authorization") String sessionToken,
			@RequestBody RequestUser requestUser) throws SQLException, MyAbcDataException {

		UserSession userSession = userBusiness.getUserSession(sessionToken);

		Observed observed = userBusiness.updateObserved(userSession.getUserId(), requestUser.getObservedId(),
				requestUser.getObservedName(), requestUser.getRole(), requestUser.getRelationship());

		return new ResponseEntity<>(observed, HttpStatus.OK);
	}

	@PostMapping("/observed")
	public ResponseEntity<Observed> addObserved(@RequestHeader("Authorization") String sessionToken,
			@RequestBody RequestUser requestUser) throws SQLException, MyAbcDataException {

		UserSession userSession = userBusiness.getUserSession(sessionToken);

		Observed observed = userBusiness.addObserved(userSession.getUserId(), requestUser.getObservedName(),
				requestUser.getRelationship());

		return new ResponseEntity<>(observed, HttpStatus.OK);
	}

	@GetMapping("/observers/{observedId}")
	public ResponseEntity<List<Observed>> getObservedUsers(@PathVariable String observedId,
			@RequestHeader("Authorization") String sessionToken) throws SQLException, MyAbcDataException {
		userBusiness.getUserSession(sessionToken);

		return new ResponseEntity<>(userBusiness.getUsersByObserved(observedId), HttpStatus.OK);
	}

	@PostMapping("/access/{observedId}")
	public ResponseEntity<StatusMessage> grantAccess(@RequestHeader("Authorization") String sessionToken,
			@PathVariable String observedId, @RequestBody RequestUser requestUser)
			throws SQLException, MyAbcDataException {
		userBusiness.getUserSession(sessionToken);

		userBusiness.grantAccess(observedId, requestUser.getEmail(), requestUser.getRole(),
				requestUser.getRelationship());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Access granted"), HttpStatus.OK);
	}

	@DeleteMapping("/{observedUserId}/access/{observedId}")
	public ResponseEntity<StatusMessage> removeAccess(@RequestHeader("Authorization") String sessionToken,
			@PathVariable String observedUserId, @PathVariable String observedId)
			throws SQLException, MyAbcDataException {
		UserSession userSession = userBusiness.getUserSession(sessionToken);

		userBusiness.deleteObservers(userSession.getUserId(), observedUserId, observedId);

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Access removed"), HttpStatus.OK);
	}

	@PostMapping("/reassign/{observedId}")
	public ResponseEntity<StatusMessage> requestReassign(@PathVariable String observedId,
			@RequestBody RequestUser requestUser, @RequestHeader("Authorization") String sessionToken)
			throws SQLException, MyAbcDataException {
		userBusiness.getUserSession(sessionToken);

		String message = userBusiness.requestReassign(sessionToken, observedId, requestUser.getEmail());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, message), HttpStatus.OK);
	}

	@GetMapping("/reassign")
	public ResponseEntity<Verification> getVerification(@RequestParam(value = "key", required = true) String key)
			throws SQLException, MyAbcDataException {
		Verification verification = userBusiness.getReassignVerification(key);

		return new ResponseEntity<>(verification, HttpStatus.OK);
	}

	@PutMapping("/reassign")
	public ResponseEntity<StatusMessage> reassignObserved(@RequestBody RequestUser requestUser)
			throws SQLException, MyAbcDataException {

		userBusiness.reassignObserved(requestUser.getKey(), requestUser.getRelationship());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Observed reassigned"), HttpStatus.OK);
	}

	@DeleteMapping("/remove")
	public ResponseEntity<StatusMessage> removeMe(@RequestHeader("Authorization") String sessionToken)
			throws SQLException, MyAbcDataException {
		UserSession userSession = userBusiness.getUserSession(sessionToken);

		userBusiness.deleteUser(userSession.getUserId());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "User removed"), HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StatusMessage> handleException(Exception ex) {
		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<StatusMessage> handleSQLException(SQLException ex) {
		ex.printStackTrace();
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
			case MyAbcDataException.DUPLICATE:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()), HttpStatus.FOUND);
			default:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}