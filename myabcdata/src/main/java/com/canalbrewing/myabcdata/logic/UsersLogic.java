package com.canalbrewing.myabcdata.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.StatusMessage;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;
import com.canalbrewing.myabcdata.model.ValueObject;

@Component
public interface UsersLogic {
	
	UserSession getUserSession(String sessionToken) throws Exception;
	
	List<ValueObject> getRelationships() throws Exception;
	
	User saveUser(User user) throws Exception;
	
	User signInUser(String username, String password) throws Exception;
	
	void signOutUser(String sessionToken) throws Exception;
	
	StatusMessage sendUsernameOrPassword(String email, String forgottenItem);
	
	User getUserByResetKey(String resetKey) throws Exception;
	
	Observed addObserved(int userId, String observedNm, String relationshipId) throws Exception;
	
	Observed updateObserved(int userId, String observedId, String observedNm, String role, String relationshipId) throws Exception;
	
	Observed grantAccess(String obsId, String email, String role, String relationshipId) throws Exception;
	
	List<Observed> getUsersByObserved(String observedId) throws Exception;
	
	Observed updateObservers(String userId, String observedId, String role, String relationshipId) throws Exception;
	
	Observed deleteObservers(String userId, String observedId) throws Exception;
	

}
