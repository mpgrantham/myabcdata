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
import com.canalbrewing.myabcdata.model.ValueObject;

@Component
public class EntryDAOImpl extends BaseDAO implements EntryDAO {
	
	private static final String PROC_GET_INTENSITIES = "{call get_intensities()}";
	
	private static final String PROC_INSERT_INCIDENT = "{call insert_incident(?,?,?,?,?,?,?,?)}";
	private static final String PROC_UPDATE_INCIDENT = "{call update_incident(?,?,?,?,?,?,?,?)}";
	private static final String PROC_INSERT_INCIDENT_ABC = "{call insert_incident_ABC(?,?)}";
	private static final String PROC_DELETE_INCIDENT_ABCS = "{call delete_incident_abcs(?)}";
	
	public static final String ID = "id";
	public static final String INTENSITY = "intensity";
	
	public List<ValueObject> getIntensities() throws Exception
	{
		List<ValueObject> intensities = new ArrayList<ValueObject>();
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_INTENSITIES);
	    		ResultSet rs = stmt.executeQuery()
	    	) 
		{
			while (rs.next()) 
	        {
				ValueObject vo = new ValueObject();
				vo.setId(rs.getInt(ID));
				vo.setValue(rs.getString(INTENSITY));
				intensities.add(vo);
	        }
		}
		
		return intensities;
	}
	
	public void insertIncident(Incident incident) throws Exception
	{
		try ( Connection conn = getConnection()	) 
		{
			conn.setAutoCommit(false);
						
			try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_INCIDENT) )
			{
				stmt.setTimestamp(1, new java.sql.Timestamp(incident.getIncidentDt().getTime()));
				stmt.setInt(2, incident.getObservedId());
				stmt.setInt(3, incident.getUserId());
				stmt.setInt(4, incident.getLocationId());
				stmt.setInt(5, incident.getIntensityId());
				stmt.setInt(6, incident.getDuration());
				stmt.setString(7, incident.getDescription());
								
				stmt.registerOutParameter(8, Types.INTEGER);
							
				stmt.execute();
				
				incident.setId(stmt.getInt(8));
			}
			catch ( Exception ex )
			{
				conn.rollback();
				throw ex;
			}
			
			try
			{
				insertIncidentAbcs(conn, incident.getId(), incident.getAntecedents());
				insertIncidentAbcs(conn, incident.getId(), incident.getBehaviors());
				insertIncidentAbcs(conn, incident.getId(), incident.getConsequences());
			}
			catch ( Exception ex )
			{
				conn.rollback();
				throw ex;
			}			
			
			conn.commit();
		}
	}
	
	public void updateIncident(Incident incident) throws Exception
	{
		try ( Connection conn = getConnection()	) 
		{
			conn.setAutoCommit(false);
						
			try (CallableStatement stmt = conn.prepareCall(PROC_UPDATE_INCIDENT) )
			{
				stmt.setInt(1, incident.getId());
				stmt.setTimestamp(2, new java.sql.Timestamp(incident.getIncidentDt().getTime()));
				stmt.setInt(3, incident.getObservedId());
				stmt.setInt(4, incident.getUserId());
				stmt.setInt(5, incident.getLocationId());
				stmt.setInt(6, incident.getIntensityId());
				stmt.setInt(7, incident.getDuration());
				stmt.setString(8, incident.getDescription());
				
				stmt.execute();
			}
			catch ( Exception ex )
			{
				conn.rollback();
				throw ex;
			}
			
			try (CallableStatement stmt = conn.prepareCall(PROC_DELETE_INCIDENT_ABCS) )
			{
				stmt.setInt(1, incident.getId());
				
				stmt.execute();
			}
			catch ( Exception ex )
			{
				conn.rollback();
				throw ex;
			}
			
			try
			{
				insertIncidentAbcs(conn, incident.getId(), incident.getAntecedents());
				insertIncidentAbcs(conn, incident.getId(), incident.getBehaviors());
				insertIncidentAbcs(conn, incident.getId(), incident.getConsequences());
			}
			catch ( Exception ex )
			{
				conn.rollback();
				throw ex;
			}			
			
			conn.commit();
		}
	}
	
	private void insertIncidentAbcs(Connection conn, int incidentId, List<ABC> abcs) throws Exception
	{
		try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_INCIDENT_ABC) )
		{
			for ( ABC abc : abcs )
			{
				stmt.setInt(1, incidentId);
				stmt.setInt(2, abc.getValueId());
				stmt.addBatch();
			}
					
			stmt.executeBatch();
		}
	}
	
	/*
	public List<Observed> getObservedByUser(int userId) throws Exception
	{
		List<Observed> observedList = new ArrayList<Observed>();
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_OBSERVED_BY_USER)
			) 
		{
	    	stmt.setInt(1, userId);
	    	
	    	int priorObservedId = -1;
	    	Observed observed = null;
	    		    		    	
	    	try (ResultSet rs = stmt.executeQuery() )
	    	{
	    		while (rs.next()) 
	    		{
	    			int observedId = rs.getInt(Observed.OBSERVED_ID);
	    			if ( observedId != priorObservedId )
	    			{
	    				observed = new Observed();
	    				observed.setId(observedId);
	    				observed.setObservedNm(rs.getString(Observed.OBSERVED_NM));
	    				observed.setRole(rs.getString(Observed.ROLE));
	    				observed.setRelationship(rs.getString(Observed.RELATIONSHIP));
	    				
	    				observedList.add(observed);
	    				
	    				priorObservedId = observedId;
	    			}
	    			
	    			ABC abc = new ABC();
	    			abc.setValueId(rs.getInt(ABC.VALUE_ID));
	    			abc.setTypeCd(rs.getString(ABC.TYPE_CD));
	    			abc.setTypeValue(rs.getString(ABC.TYPE_VALUE));
	    			observed.addValue(abc);
	    		}
	    	}
		}
		
		return observedList;
	}
	*/
	
	

}
