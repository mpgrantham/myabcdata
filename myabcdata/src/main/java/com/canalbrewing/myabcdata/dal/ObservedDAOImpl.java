package com.canalbrewing.myabcdata.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.model.ABC;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Observed;

@Component
public class ObservedDAOImpl extends BaseDAO implements ObservedDAO {
	
	private static final String PROC_GET_OBSERVED_BY_USER = "{call get_observed_by_user_id(?)}";
	private static final String PROC_GET_OBSERVED_ABCS = "{call get_observed_abcs(?)}";
	private static final String PROC_GET_INCIDENTS_BY_OBSERVED = "{call get_incidents_by_observed(?)}";
	
	private static final String PROC_INSERT_OBSERVED_ABC = "{call insert_observed_abc(?,?,?,?)}";
	private static final String PROC_INSERT_OBSERVED_LOCATION = "{call insert_observed_location(?,?,?)}";
	private static final String PROC_UPDATE_OBSERVED_ABC = "{call update_observed_abc(?,?,?)}";
	private static final String PROC_UPDATE_OBSERVED_LOCATION = "{call update_observed_location(?,?,?)}";
	
		
	public List<Observed> getObservedByUser(int userId) throws Exception
	{
		List<Observed> observedList = new ArrayList<Observed>();
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_OBSERVED_BY_USER)
			) 
		{
	    	stmt.setInt(1, userId);
	    	
	    	try (ResultSet rs = stmt.executeQuery() )
	    	{
	    		while (rs.next()) 
	    		{
	    			Observed observed = new Observed();
    				observed.setId(rs.getInt(Observed.OBSERVED_ID));
    				observed.setObservedNm(rs.getString(Observed.OBSERVED_NM));
    				observed.setRole(rs.getString(Observed.ROLE));
    				observed.setRelationshipId(rs.getInt(Observed.RELATIONSHIP_ID));
    				observed.setRelationship(rs.getString(Observed.RELATIONSHIP));
    				observed.setAccessStatus(rs.getString(Observed.ACCESS_STATUS));
    				observedList.add(observed);
	    		}
	    	}
		}
		
		return observedList;
	}
	
	public Observed getObservedABCs(int observedId) throws Exception
	{
		Observed observed = new Observed();
		observed.setId(observedId);
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_OBSERVED_ABCS)
			) 
		{
			stmt.setInt(1, observedId);
	    	   		    	
	    	try (ResultSet rs = stmt.executeQuery() )
	    	{
	    		while (rs.next()) 
	    		{
	    			ABC abc = new ABC();
	    			abc.setValueId(rs.getInt(ABC.VALUE_ID));
	    			abc.setTypeCd(rs.getString(ABC.TYPE_CD));
	    			abc.setTypeValue(rs.getString(ABC.TYPE_VALUE));
	    			observed.addValue(abc);
	    		}
	    	}
		}
		
		return observed;
	}
	
	public List<Incident> getIncidentsByObserved(int observedId) throws Exception
	{
		List<Incident> incidents = new ArrayList<Incident>();
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_INCIDENTS_BY_OBSERVED)
			) 
		{
	    	stmt.setInt(1, observedId);
	    	
	    	int incidentId = 0;
	    	int priorIncidentId = -1;
	    	Incident incident = null;
	    	   		    	
	    	try (ResultSet rs = stmt.executeQuery() )
	    	{
	    		while (rs.next()) 
	    		{
	    			incidentId = rs.getInt(Incident.INCIDENT_ID);
	    			if ( incidentId != priorIncidentId )
	    			{
	    				incident = new Incident();
	    				incident.setId(incidentId);
	    				incident.setObservedId(observedId);
	    				incident.setIncidentDt(rs.getTimestamp(Incident.INCIDENT_DT));
	    				incident.setUserId(rs.getInt(Incident.USER_ID));
	    				incident.setUserNm(rs.getString(Incident.USER_NM));
	    				incident.setLocationId(rs.getInt(Incident.LOCATION_ID));
	    				incident.setLocation(rs.getString(Incident.LOCATION));
	    				incident.setIntensityId(rs.getInt(Incident.INTENSITY_ID));
	    				incident.setIntensity(rs.getString(Incident.INTENSITY));
	    				incident.setDuration(rs.getInt(Incident.DURATION));
	    				incident.setDescription(rs.getString(Incident.DESCRIPTION));
	    				
	    				incidents.add(incident);
	    				priorIncidentId = incidentId;
	    			}
	    				    			
	    			ABC abc = new ABC();
	    			abc.setValueId(rs.getInt(ABC.VALUE_ID));
	    			abc.setTypeCd(rs.getString(ABC.TYPE_CD));
	    			abc.setTypeValue(rs.getString(ABC.TYPE_VALUE));
	    			incident.addValue(abc);
	    		}
	    	}
		}
		
		return incidents;
	}
	
	public ABC insertObservedABC(int observedId, ABC abc) throws Exception
	{
		try ( Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_INSERT_OBSERVED_ABC)  ) 
		{
			stmt.setInt(1, observedId);
			stmt.setString(2, abc.getTypeCd());
			stmt.setString(3, abc.getTypeValue());
			
			stmt.registerOutParameter(4, Types.INTEGER);
							
			stmt.execute();
				
			abc.setValueId(stmt.getInt(4));
		}
				
		return abc;
	}
	
	public ABC updateObservedABC(ABC abc) throws Exception
	{
		try ( Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_UPDATE_OBSERVED_ABC)  ) 
		{
			stmt.setInt(1, abc.getValueId());
			stmt.setString(2, abc.getTypeValue());
			stmt.setString(3, abc.getActiveFl());
			
			stmt.execute();
		}
				
		return abc;
	}
	
	public ABC insertObservedLocation(int observedId, ABC abc) throws Exception
	{
		try ( Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_INSERT_OBSERVED_LOCATION)  ) 
		{
			stmt.setInt(1, observedId);
			stmt.setString(2, abc.getTypeValue());
			
			stmt.registerOutParameter(3, Types.INTEGER);
							
			stmt.execute();
				
			abc.setValueId(stmt.getInt(3));
		}
				
		return abc;
	}
	
	public ABC updateObservedLocation(ABC abc) throws Exception
	{
		try ( Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_UPDATE_OBSERVED_LOCATION)  ) 
		{
			stmt.setInt(1, abc.getValueId());
			stmt.setString(2, abc.getTypeValue());
			stmt.setString(3, abc.getActiveFl());
			
			stmt.execute();
		}
				
		return abc;
	}
	
}
