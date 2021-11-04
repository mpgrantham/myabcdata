package com.canalbrewing.myabcdata.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.canalbrewing.myabcdata.model.Abc;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Intensity;

import org.springframework.stereotype.Component;

@Component
public class IncidentDaoJdbc extends BaseDao implements IncidentDao {

    private static final String PROC_GET_INTENSITIES = "{call get_intensities()}";
	
	private static final String PROC_INSERT_INCIDENT = "{call insert_incident(?,?,?,?,?,?,?,?)}";
	private static final String PROC_UPDATE_INCIDENT = "{call update_incident(?,?,?,?,?,?,?,?)}";
	private static final String PROC_INSERT_INCIDENT_ABC = "{call insert_incident_ABC(?,?)}";
	private static final String PROC_DELETE_INCIDENT_ABCS = "{call delete_incident_abcs(?)}";
	private static final String PROC_DELETE_INCIDENT = "{call delete_incident(?)}";
	
	public static final String COL_ID = "id";
    public static final String COL_INTENSITY = "intensity";
    
    public List<Intensity> getIntensities() throws SQLException {

		List<Intensity> intensities = new ArrayList<>();
		
		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_INTENSITIES);
	    		ResultSet rs = stmt.executeQuery()
	    	) {
			while (rs.next()) {
				Intensity intensity = new Intensity();
				intensity.setId(rs.getInt(COL_ID));
				intensity.setIntensity(rs.getString(COL_INTENSITY));
				intensities.add(intensity);
	        }
		}
		
		return intensities;
    }
    
    public void insertIncident(Incident incident) throws SQLException {

		try ( Connection conn = getConnection()	) {
			conn.setAutoCommit(false);
						
			try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_INCIDENT) ) {
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
			catch ( SQLException ex ) {
				conn.rollback();
				throw ex;
			}
			
			try {
				insertIncidentAbcs(conn, incident.getId(), incident.getAntecedents());
				insertIncidentAbcs(conn, incident.getId(), incident.getBehaviors());
				insertIncidentAbcs(conn, incident.getId(), incident.getConsequences());
			}
			catch ( SQLException ex ) {
				conn.rollback();
				throw ex;
			}			
			
			conn.commit();
		}
	}
	
	public void updateIncident(Incident incident) throws SQLException {

		try ( Connection conn = getConnection()	) {

			conn.setAutoCommit(false);
						
			try (CallableStatement stmt = conn.prepareCall(PROC_UPDATE_INCIDENT) ) {
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
			catch ( SQLException ex ) {
				conn.rollback();
				throw ex;
			}
			
			try (CallableStatement stmt = conn.prepareCall(PROC_DELETE_INCIDENT_ABCS) ) {
				stmt.setInt(1, incident.getId());
				
				stmt.execute();
			}
			catch ( SQLException ex ) {
				conn.rollback();
				throw ex;
			}
			
			try {
				insertIncidentAbcs(conn, incident.getId(), incident.getAntecedents());
				insertIncidentAbcs(conn, incident.getId(), incident.getBehaviors());
				insertIncidentAbcs(conn, incident.getId(), incident.getConsequences());
			}
			catch ( SQLException ex ) {
				conn.rollback();
				throw ex;
			}			
			
			conn.commit();
		}
	}
	
	private void insertIncidentAbcs(Connection conn, int incidentId, List<Abc> abcs) throws SQLException {

		try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_INCIDENT_ABC) ) {
			for ( Abc abc : abcs )	{
				stmt.setInt(1, incidentId);
				stmt.setInt(2, abc.getValueId());
				stmt.addBatch();
			}
					
			stmt.executeBatch();
		}
	}

	public void deleteIncident(int incidentId) throws SQLException {

		try (	Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_DELETE_INCIDENT)
			) {
				stmt.setInt(1, incidentId);
				stmt.execute();
	    }

	}
    
}