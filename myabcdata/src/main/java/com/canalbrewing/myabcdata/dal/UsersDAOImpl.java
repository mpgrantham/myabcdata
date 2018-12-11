package com.canalbrewing.myabcdata.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;
import com.canalbrewing.myabcdata.model.ValueObject;

@Component
public class UsersDAOImpl extends BaseDAO implements UsersDAO {
	
	private static final String PROC_GET_RELATIONSHIPS = "{call get_relationships()}";
	
	private static final String PROC_GET_USER_SESSION = "{call get_user_session(?)}";
	private static final String PROC_INSERT_USER_SESSION = "{call insert_user_session(?,?,?,?)}";
	private static final String PROC_UPDATE_USER_SESSION = "{call update_user_session(?,?,?)}";
	
	private static final String PROC_GET_USER_BY_USER_NM = "{call get_user_by_user_nm(?)}";
	private static final String PROC_GET_USER_BY_EMAIL = "{call get_user_by_email(?)}";
	private static final String PROC_GET_USER_BY_RESET_KEY = "{call get_user_by_reset_key(?)}";
	
	private static final String PROC_INSERT_USER = "{call insert_user(?,?,?,?,?,?)}";
	private static final String PROC_INSERT_OBSERVED = "{call insert_observed(?,?,?)}";
	private static final String PROC_INSERT_OBSERVERS = "{call insert_observers(?,?,?,?,?,?)}";
	
	private static final String PROC_UPDATE_USER = "{call update_user(?,?,?,?,?,?,?)}";
	private static final String PROC_UPDATE_OBSERVED = "{call update_observed(?,?)}";
	private static final String PROC_UPDATE_OBSERVERS = "{call update_observers(?,?,?,?,?,?)}";
	
	private static final String PROC_DELETE_OBSERVERS = "{call delete_observers(?,?)}";
	
	private static final String PROC_GET_USERS_BY_OBSERVED = "{call get_users_by_observed_id(?)}";
	
	private static final String ID = "id";
	private static final String RELATIONSHIP = "relationship";
	
	
	public List<ValueObject> getRelationships() throws Exception
	{
		List<ValueObject> relationships = new ArrayList<ValueObject>();
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_RELATIONSHIPS);
	    		ResultSet rs = stmt.executeQuery()
	    	) 
		{
			while (rs.next()) 
	        {
				ValueObject relationship = new ValueObject();
				relationship.setId(rs.getInt(ID));
				relationship.setValue(rs.getString(RELATIONSHIP));
				relationships.add(relationship);
	        }
		}
		
		return relationships;
	}
	
	/* Manage User Session */
	public UserSession getUserSession(String sessionToken) throws Exception
	{
		UserSession session = null;
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_USER_SESSION)
			) 
		{
	    	stmt.setString(1, sessionToken);
	    	
	    	try (ResultSet rs = stmt.executeQuery() )
	    	{
	    		while (rs.next()) 
		        {
		    		session = new UserSession();
					session.setSessionToken(sessionToken);
					session.setUserId(rs.getInt(UserSession.USER_ID));
					session.setSessionDate(rs.getDate(UserSession.SESSION_DATETIME));
					session.setSessionActiveFl(rs.getInt(UserSession.SESSION_ACTIVE_FL));
		        }
	    	}
		 }
					
		return session;
	}
	
	public void insertUserSession(User user) throws Exception
	{
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_INSERT_USER_SESSION)
			)
		{
			stmt.setString(1, user.getSessionKey());
			stmt.setInt(2, user.getId());
			stmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
			stmt.setInt(4, 1);
					
			stmt.execute();
		}
	}
	
	public void updateUserSession(UserSession session) throws Exception
	{
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_UPDATE_USER_SESSION)
			)
		{
			stmt.setString(1, session.getSessionToken());
			stmt.setDate(2, new java.sql.Date(session.getSessionDate().getTime()));
			stmt.setInt(3, session.getSessionActiveFl());
					
			stmt.execute();
		}
	}
	
	public User getUserByUsername(String username) throws Exception
	{
		return getUser(PROC_GET_USER_BY_USER_NM, username);
	}
	
	public User getUserByEmail(String email) throws Exception
	{
		return getUser(PROC_GET_USER_BY_EMAIL, email);
	}
	
	public User getUserByResetKey(String resetKey) throws Exception
	{
		return getUser(PROC_GET_USER_BY_RESET_KEY, resetKey);
	}
	
	private User getUser(String storedProc, String value) throws Exception
	{
		User user = null;
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(storedProc)
			) 
		{
		    	stmt.setString(1, value);
		    	
		    	try (ResultSet rs = stmt.executeQuery() )
		    	{
		    		while (rs.next()) 
			        {
		    			user = new User();
						user.setId(rs.getInt(User.ID));
						user.setUserNm(rs.getString(User.USER_NM));
						user.setEncryptedPassword(rs.getBytes(User.PASSWORD));
						user.setSalt(rs.getBytes(User.SALT));
						user.setEmail(rs.getString(User.EMAIL));
						user.setStartPage(rs.getString(User.START_PAGE));
					}
		    	}
		 }
					
		return user;
	}
	
	public User insertUser(User user) throws Exception
	{
		try ( Connection conn = getConnection()	) 
		{
			conn.setAutoCommit(false);
						
			try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_USER) )
			{
				stmt.setString(1, user.getUserNm());
				stmt.setString(2, user.getEmail());
				stmt.setBytes(3, user.getEncryptedPassword());
				stmt.setBytes(4, user.getSalt());
				stmt.setString(5, user.getStartPage());
								
				stmt.registerOutParameter(6, Types.INTEGER);
							
				stmt.execute();
				
				user.setId(stmt.getInt(6));
			}
			catch ( Exception ex )
			{
				conn.rollback();
				throw ex;
			}
			
			for ( Observed observed : user.getObserved() )
			{
				observed.setUserId(user.getId());
				insertObserved(conn, observed);
				insertObservers(conn, observed);
			}
			
			conn.commit();
		}
		
		return user;
	}
	
	public Observed insertObserved(Observed observed) throws Exception
	{
		try ( Connection conn = getConnection()	) 
		{
			conn.setAutoCommit(false);
			
			insertObserved(conn, observed);
			insertObservers(conn, observed);
			
			conn.commit();
		}
		
		return observed;
	}
	
	public Observed insertObservers(Observed observed) throws Exception
	{
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_INSERT_OBSERVERS)	)
		{
			stmt.setInt(1, observed.getUserId());
			stmt.setInt(2, observed.getId());
			stmt.setString(3, observed.getRole());
			stmt.setInt(4, observed.getRelationshipId());
			stmt.setString(5, observed.getAccessStatus());
			stmt.setString(6, observed.getAccessKey());
						
			stmt.execute();
		}
		
		return observed;
	}
	
	public Observed updateObservers(Observed observed) throws Exception
	{
		try (	Connection conn = getConnection())
		{
			updateObservers(conn, observed);
		}
		
		return observed;
	}
	
	public Observed deleteObservers(Observed observed) throws Exception
	{
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_DELETE_OBSERVERS)	)
		{
			stmt.setInt(1, observed.getUserId());
			stmt.setInt(2, observed.getId());
			stmt.execute();
		}
		
		return observed;
	}
	
	private Observed insertObserved(Connection conn, Observed observed) throws Exception
	{
		try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_OBSERVED) )
		{
			stmt.setString(1, observed.getObservedNm());
			stmt.setInt(2, observed.getUserId());
							
			stmt.registerOutParameter(3, Types.INTEGER);
						
			stmt.execute();
			
			observed.setId(stmt.getInt(3));
		}
		
		return observed;
	}
	
	private void insertObservers(Connection conn, Observed observed) throws Exception
	{
		try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_OBSERVERS) )
		{
			stmt.setInt(1, observed.getUserId());
			stmt.setInt(2, observed.getId());
			stmt.setString(3, observed.getRole());
			stmt.setInt(4, observed.getRelationshipId());
			stmt.setString(5, observed.getAccessStatus());
			stmt.setString(6, observed.getAccessKey());
						
			stmt.execute();
		}
	}
	
	public User updateUser(User user) throws Exception
	{
		try ( 	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_UPDATE_USER)	) 
		{
			stmt.setInt(1, user.getId());
			stmt.setString(2, user.getUserNm());
			stmt.setString(3, user.getEmail());
			stmt.setBytes(4, user.getEncryptedPassword());
			stmt.setBytes(5, user.getSalt());
			stmt.setString(6, user.getStartPage());
			stmt.setString(7, user.getPasswordResetKey());
			stmt.execute();
		}
		
		return user;
	}
	
	public Observed updateObserved(Observed observed) throws Exception
	{
		try ( Connection conn = getConnection()	) 
		{
			conn.setAutoCommit(false);
			
			try (CallableStatement stmt = conn.prepareCall(PROC_UPDATE_OBSERVED) )
			{
				stmt.setInt(1, observed.getId());
				stmt.setString(2, observed.getObservedNm());
				stmt.execute();
			}
			
			try 
			{
				updateObservers(conn, observed);
			}
			catch ( Exception ex )
			{
				conn.rollback();
				throw ex;
			}
			
			conn.commit();
		}
		
		return observed;
	}
	
	private void updateObservers(Connection conn, Observed observed) throws Exception
	{
		try (CallableStatement stmt = conn.prepareCall(PROC_UPDATE_OBSERVERS) )
		{
			stmt.setInt(1, observed.getUserId());
			stmt.setInt(2, observed.getId());
			stmt.setString(3, observed.getRole());
			stmt.setInt(4, observed.getRelationshipId());
			stmt.setString(5, observed.getAccessStatus());
			stmt.setString(6, observed.getAccessKey());
			stmt.execute();
		}
	}
	
	public List<Observed> getUsersByObserved(int observedId) throws Exception
	{
		List<Observed> userList = new ArrayList<Observed>();
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_USERS_BY_OBSERVED)
			) 
		{
	    	stmt.setInt(1, observedId);
	    	
	    	try (ResultSet rs = stmt.executeQuery() )
	    	{
	    		while (rs.next()) 
	    		{
	    			Observed observed = new Observed();
    				observed.setId(observedId);
    				observed.setUserId(rs.getInt(Observed.USER_ID));
    				observed.setRole(rs.getString(Observed.ROLE));
    				observed.setRelationshipId(rs.getInt(Observed.RELATIONSHIP_ID));
    				observed.setRelationship(rs.getString(Observed.RELATIONSHIP));
    				observed.setAccessStatus(rs.getString(Observed.ACCESS_STATUS));
    				observed.setEmail(rs.getString(Observed.EMAIL));
    				userList.add(observed);
	    		}
	    	}
		}
		
		return userList;
	}

}
