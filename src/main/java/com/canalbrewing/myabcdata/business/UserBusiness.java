package com.canalbrewing.myabcdata.business;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.myabcdata.model.Relationship;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;
import com.canalbrewing.myabcdata.model.Verification;

import org.springframework.stereotype.Component;

@Component
public interface UserBusiness {

	List<Relationship> getRelationships() throws SQLException;

	UserSession getUserSession(String sessionToken) throws SQLException;

	User signInUser(String username, String password) throws SQLException;

	void signOutUser(String sessionToken) throws SQLException;

	User register(User user) throws SQLException;

	User confirmRegister(String verificationKey) throws SQLException;

	User saveUser(User user) throws SQLException;

	User updateUser(User user) throws SQLException;

	User updatePassword(int userId, String currentPassword, String password) throws SQLException;

	User updateUsername(int userId, String userName) throws SQLException;

	String sendUsernameOrPassword(String email, String forgottenItem);

	User getUserByResetKey(String resetKey) throws SQLException;

	User resetPassword(String resetKey, String password) throws SQLException;

	Observed addObserved(int userId, String observedNm, String relationshipId) throws SQLException;

	Observed updateObserved(int userId, String observedId, String observedNm, String role, String relationshipId)
			throws SQLException;

	Observed grantAccess(String obsId, String email, String role, String relationshipId) throws SQLException;

	List<Observed> getUsersByObserved(String observedId) throws SQLException;

	Observed updateObservers(String userId, String observedId, String role, String relationshipId) throws SQLException;

	Observed deleteObservers(int userId, String observedUserId, String observedId) throws SQLException;

	String requestReassign(String sessionToken, String observedId, String email) throws SQLException;

	Observed reassignObserved(String verificationKey, String relationshipId) throws SQLException;

	void deleteUser(int userId) throws SQLException;

	Verification getVerification(String verificationKey) throws SQLException;

	Verification getReassignVerification(String verificationKey) throws SQLException;

}