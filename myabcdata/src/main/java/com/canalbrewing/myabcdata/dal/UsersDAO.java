package com.canalbrewing.myabcdata.dal;

import java.util.List;

import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;
import com.canalbrewing.myabcdata.model.ValueObject;

@Component
public interface UsersDAO {
	
	List<ValueObject> getRelationships() throws Exception;
	
	UserSession getUserSession(String sessionToken) throws Exception;
	
	void insertUserSession(User user) throws Exception;
	
	void updateUserSession(UserSession session) throws Exception;
	
	User getUserByUsername(String username) throws Exception;
	
	User getUserByEmail(String email) throws Exception;
	
	User getUserByResetKey(String resetKey) throws Exception;
	
	User insertUser(User user) throws Exception;
	
	User updateUser(User user) throws Exception;
	
	Observed insertObserved(Observed observed) throws Exception;
		
	Observed updateObserved(Observed observed) throws Exception;
	
	Observed insertObservers(Observed observed) throws Exception;
	
	Observed updateObservers(Observed observed) throws Exception;
	
	Observed deleteObservers(Observed observed) throws Exception;
	
	List<Observed> getUsersByObserved(int observedId) throws Exception;

}
