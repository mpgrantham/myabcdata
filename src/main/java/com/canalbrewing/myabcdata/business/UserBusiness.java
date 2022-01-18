package com.canalbrewing.myabcdata.business;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.myabcdata.model.Relationship;
import com.canalbrewing.myabcdata.exception.MyAbcDataException;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;
import com.canalbrewing.myabcdata.model.Verification;

import org.springframework.stereotype.Component;

@Component
public interface UserBusiness {

	List<Relationship> getRelationships() throws SQLException;

	UserSession getUserSession(String sessionToken) throws SQLException, MyAbcDataException;

	User signInUser(String username, String password, boolean staySignedIn) throws SQLException, MyAbcDataException;

	void signOutUser(String sessionToken) throws SQLException, MyAbcDataException;

	User signInUser(String signedInKey) throws SQLException, MyAbcDataException;

	User register(User user) throws SQLException, MyAbcDataException;

	User confirmRegister(String verificationKey) throws SQLException, MyAbcDataException;

	User saveUser(User user) throws SQLException, MyAbcDataException;

	User updateUser(User user) throws SQLException, MyAbcDataException;

	User updatePassword(int userId, String currentPassword, String password) throws SQLException, MyAbcDataException;

	User updateUsername(int userId, String userName) throws SQLException, MyAbcDataException;

	String sendUsernameOrPassword(String email, String forgottenItem) throws MyAbcDataException;

	User getUserByResetKey(String resetKey) throws SQLException, MyAbcDataException;

	User resetPassword(String resetKey, String password) throws SQLException, MyAbcDataException;

	Observed addObserved(int userId, String observedNm, String relationshipId) throws SQLException, MyAbcDataException;

	Observed updateObserved(int userId, String observedId, String observedNm, String role, String relationshipId)
			throws SQLException;

	Observed grantAccess(String obsId, String email, String role, String relationshipId)
			throws SQLException, MyAbcDataException;

	List<Observed> getUsersByObserved(String observedId) throws SQLException;

	Observed updateObservers(String userId, String observedId, String role, String relationshipId) throws SQLException;

	Observed deleteObservers(int userId, String observedUserId, String observedId) throws SQLException;

	String requestReassign(String sessionToken, String observedId, String email)
			throws SQLException, MyAbcDataException;

	Observed reassignObserved(String verificationKey, String relationshipId) throws SQLException, MyAbcDataException;

	void deleteUser(int userId) throws SQLException, MyAbcDataException;

	Verification getVerification(String verificationKey) throws SQLException;

	Verification getReassignVerification(String verificationKey) throws SQLException, MyAbcDataException;

}