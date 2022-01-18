package com.canalbrewing.myabcdata.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.canalbrewing.myabcdata.model.Abc;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.resultsetmapper.ResultSetMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObservedDaoJdbc extends BaseDao implements ObservedDao {

	private static final String PROC_GET_OBSERVED_BY_USER = "{call get_observed_by_user_id(?)}";
	private static final String PROC_GET_OBSERVED_BY_ID = "{call get_observed_by_id(?)}";
	private static final String PROC_GET_OBSERVED_ABCS = "{call get_observed_abcs(?)}";
	private static final String PROC_GET_INCIDENTS_BY_OBSERVED = "{call get_incidents_by_observed(?, ?)}";
	private static final String PROC_GET_INCIDENT_BY_ID = "{call get_incident_by_id(?, ?)}";
	private static final String PROC_GET_INCIDENT_DATES_BY_OBSERVED = "{call get_incident_dates_by_observed(?)}";
	private static final String PROC_INSERT_OBSERVED_ABC = "{call insert_observed_abc(?,?,?,?)}";
	private static final String PROC_INSERT_OBSERVED_LOCATION = "{call insert_observed_location(?,?,?)}";
	private static final String PROC_UPDATE_OBSERVED_ABC = "{call update_observed_abc(?,?,?)}";
	private static final String PROC_UPDATE_OBSERVED_LOCATION = "{call update_observed_location(?,?,?)}";

	private static final String PROC_INSERT_OBSERVED = "{call insert_observed(?,?,?)}";
	private static final String PROC_INSERT_OBSERVERS = "{call insert_observers(?,?,?,?,?,?)}";
	private static final String PROC_UPDATE_OBSERVED = "{call update_observed(?,?,?)}";
	private static final String PROC_UPDATE_OBSERVERS = "{call update_observers(?,?,?,?,?,?)}";

	private static final String PROC_DELETE_OBSERVERS = "{call delete_observers(?,?)}";

	private static final String PROC_DELETE_OBSERVERED = "{call delete_observed(?)}";

	private static final String PROC_GET_USERS_BY_OBSERVED = "{call get_users_by_observed_id(?)}";

	private static final String COL_USER_NM = "user_nm";

	private static final String COL_USER_ID = "user_id";

	private static final String COL_VALUE_ID = "value_id";
	private static final String COL_TYPE_CD = "type_cd";
	private static final String COL_TYPE_VALUE = "type_value";

	private static final String COL_INCIDENT_ID = "incident_id";
	private static final String COL_INCIDENT_DT = "incident_dt";
	private static final String COL_LOCATION_ID = "location_id";
	private static final String COL_LOCATION = "location";
	private static final String COL_INTENSITY_ID = "intensity_id";
	private static final String COL_INTENSITY = "intensity";
	private static final String COL_DURATION = "duration";
	private static final String COL_DESCRIPTION = "description";

	@Autowired
	ResultSetMapper mapper;

	public List<Observed> getObservedByUser(int userId) throws SQLException {

		List<Observed> observedList = new ArrayList<>();

		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_GET_OBSERVED_BY_USER)) {
			stmt.setInt(1, userId);

			try (ResultSet rs = stmt.executeQuery()) {
				observedList = mapper.mapResult(rs, Observed.class);
			}
		}

		return observedList;
	}

	public Observed getObservedById(int observedId) throws SQLException {
		Observed observed = null;

		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_GET_OBSERVED_BY_ID)) {
			stmt.setInt(1, observedId);

			try (ResultSet rs = stmt.executeQuery()) {
				observed = mapper.mapOneResult(rs, Observed.class);
			}
		}

		return observed;
	}

	public List<Abc> getObservedABCs(int observedId) throws SQLException {

		List<Abc> abcs = new ArrayList<>();

		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_GET_OBSERVED_ABCS)) {
			stmt.setInt(1, observedId);

			try (ResultSet rs = stmt.executeQuery()) {
				abcs = mapper.mapResult(rs, Abc.class);
			}
		}

		return abcs;
	}

	public List<Incident> getIncidentsByObserved(int observedId, String incidentStartDt) throws SQLException {
		List<Incident> incidents = new ArrayList<>();

		try (Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_INCIDENTS_BY_OBSERVED)) {

			stmt.setInt(1, observedId);
			stmt.setString(2, incidentStartDt);

			int incidentId = 0;
			int priorIncidentId = -1;
			Incident incident = new Incident();

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					incidentId = rs.getInt(COL_INCIDENT_ID);
					if (incidentId != priorIncidentId) {
						incident = new Incident();
						incident.setId(incidentId);
						incident.setObservedId(observedId);
						incident.setIncidentDt(rs.getTimestamp(COL_INCIDENT_DT));
						incident.setUserId(rs.getInt(COL_USER_ID));
						incident.setUserNm(rs.getString(COL_USER_NM));
						incident.setLocationId(rs.getInt(COL_LOCATION_ID));
						incident.setLocation(rs.getString(COL_LOCATION));
						incident.setIntensityId(rs.getInt(COL_INTENSITY_ID));
						incident.setIntensity(rs.getString(COL_INTENSITY));
						incident.setDuration(rs.getInt(COL_DURATION));
						incident.setDescription(rs.getString(COL_DESCRIPTION));

						incidents.add(incident);
						priorIncidentId = incidentId;
					}

					Abc abc = new Abc();
					abc.setValueId(rs.getInt(COL_VALUE_ID));
					abc.setTypeCd(rs.getString(COL_TYPE_CD));
					abc.setTypeValue(rs.getString(COL_TYPE_VALUE));
					incident.addValue(abc);
				}
			}
		}

		return incidents;
	}

	public List<Date> getIncidentDatesByObserved(int observedId) throws SQLException {
		List<Date> incidentDates = new ArrayList<>();

		try (Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_INCIDENT_DATES_BY_OBSERVED)) {
			stmt.setInt(1, observedId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					incidentDates.add(rs.getTimestamp(COL_INCIDENT_DT));
				}
			}
		}

		return incidentDates;
	}

	public Incident getIncidentById(int observedId, int incidentId) throws SQLException {
		Incident incident = new Incident();

		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_GET_INCIDENT_BY_ID)) {

			stmt.setInt(1, observedId);
			stmt.setInt(2, incidentId);

			int priorIncidentId = -1;

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					incidentId = rs.getInt(COL_INCIDENT_ID);
					if (incidentId != priorIncidentId) {
						incident = new Incident();
						incident.setId(incidentId);
						incident.setObservedId(observedId);
						incident.setIncidentDt(rs.getTimestamp(COL_INCIDENT_DT));
						incident.setUserId(rs.getInt(COL_USER_ID));
						incident.setUserNm(rs.getString(COL_USER_NM));
						incident.setLocationId(rs.getInt(COL_LOCATION_ID));
						incident.setLocation(rs.getString(COL_LOCATION));
						incident.setIntensityId(rs.getInt(COL_INTENSITY_ID));
						incident.setIntensity(rs.getString(COL_INTENSITY));
						incident.setDuration(rs.getInt(COL_DURATION));
						incident.setDescription(rs.getString(COL_DESCRIPTION));

						priorIncidentId = incidentId;
					}

					Abc abc = new Abc();
					abc.setValueId(rs.getInt(COL_VALUE_ID));
					abc.setTypeCd(rs.getString(COL_TYPE_CD));
					abc.setTypeValue(rs.getString(COL_TYPE_VALUE));
					incident.addValue(abc);
				}
			}
		}

		return incident;
	}

	public Abc insertObservedABC(int observedId, Abc abc) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_INSERT_OBSERVED_ABC)) {
			stmt.setInt(1, observedId);
			stmt.setString(2, abc.getTypeCd());
			stmt.setString(3, abc.getTypeValue());

			stmt.registerOutParameter(4, Types.INTEGER);

			stmt.execute();

			abc.setValueId(stmt.getInt(4));
		}

		return abc;
	}

	public Abc updateObservedABC(Abc abc) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_UPDATE_OBSERVED_ABC)) {
			stmt.setInt(1, abc.getValueId());
			stmt.setString(2, abc.getTypeValue());
			stmt.setString(3, abc.getActiveFl());

			stmt.execute();
		}

		return abc;
	}

	public Abc insertObservedLocation(int observedId, Abc abc) throws SQLException {
		try (Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_INSERT_OBSERVED_LOCATION)) {
			stmt.setInt(1, observedId);
			stmt.setString(2, abc.getTypeValue());

			stmt.registerOutParameter(3, Types.INTEGER);

			stmt.execute();

			abc.setValueId(stmt.getInt(3));
		}

		return abc;
	}

	public Abc updateObservedLocation(Abc abc) throws SQLException {
		try (Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_UPDATE_OBSERVED_LOCATION)) {
			stmt.setInt(1, abc.getValueId());
			stmt.setString(2, abc.getTypeValue());
			stmt.setString(3, abc.getActiveFl());

			stmt.execute();
		}

		return abc;
	}

	/* Observed */

	public Observed insertObserved(Observed observed) throws SQLException {
		try (Connection conn = getConnection()) {
			conn.setAutoCommit(false);

			insertObserved(conn, observed);
			insertObservers(conn, observed);

			conn.commit();
		}

		return observed;
	}

	public Observed insertObserved(Connection conn, Observed observed) throws SQLException {
		try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_OBSERVED)) {
			stmt.setString(1, observed.getObservedNm());
			stmt.setInt(2, observed.getUserId());

			stmt.registerOutParameter(3, Types.INTEGER);

			stmt.execute();

			observed.setId(stmt.getInt(3));
		}

		return observed;
	}

	public Observed updateObserved(Observed observed) throws SQLException {
		try (Connection conn = getConnection()) {
			conn.setAutoCommit(false);

			updateObserved(conn, observed);

			try {
				updateObservers(conn, observed);
			} catch (SQLException ex) {
				conn.rollback();
				throw ex;
			}

			conn.commit();
		}

		return observed;
	}

	private void updateObserved(Connection conn, Observed observed) throws SQLException {
		try (CallableStatement stmt = conn.prepareCall(PROC_UPDATE_OBSERVED)) {
			stmt.setInt(1, observed.getId());
			stmt.setString(2, observed.getObservedNm());
			stmt.setInt(3, observed.getUserId());
			stmt.execute();
		}
	}

	/* Observers */

	public Observed insertObservers(Observed observed) throws SQLException {
		try (Connection conn = getConnection()) {
			insertObservers(conn, observed);
		}

		return observed;
	}

	public Observed updateObservers(Observed observed) throws SQLException {
		try (Connection conn = getConnection()) {
			updateObservers(conn, observed);
		}

		return observed;
	}

	public Observed deleteObservers(Observed observed) throws SQLException {
		try (Connection conn = getConnection()) {
			deleteObservers(conn, observed);
		}

		return observed;
	}

	public Observed deleteObserved(Observed observed) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_DELETE_OBSERVERED)) {
			stmt.setInt(1, observed.getId());
			stmt.execute();
		}

		return observed;
	}

	private Observed deleteObservers(Connection conn, Observed observed) throws SQLException {
		try (CallableStatement stmt = conn.prepareCall(PROC_DELETE_OBSERVERS)) {
			stmt.setInt(1, observed.getUserId());
			stmt.setInt(2, observed.getId());
			stmt.execute();
		}

		return observed;
	}

	public void insertObservers(Connection conn, Observed observed) throws SQLException {
		try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_OBSERVERS)) {
			stmt.setInt(1, observed.getUserId());
			stmt.setInt(2, observed.getId());
			stmt.setString(3, observed.getRole());
			stmt.setInt(4, observed.getRelationshipId());
			stmt.setString(5, observed.getAccessStatus());
			stmt.setString(6, observed.getAccessKey());

			stmt.execute();
		}
	}

	private void updateObservers(Connection conn, Observed observed) throws SQLException {
		try (CallableStatement stmt = conn.prepareCall(PROC_UPDATE_OBSERVERS)) {
			stmt.setInt(1, observed.getUserId());
			stmt.setInt(2, observed.getId());
			stmt.setString(3, observed.getRole());
			stmt.setInt(4, observed.getRelationshipId());
			stmt.setString(5, observed.getAccessStatus());
			stmt.setString(6, observed.getAccessKey());
			stmt.execute();
		}
	}

	public List<Observed> getUsersByObserved(int observedId) throws SQLException {
		List<Observed> userList = new ArrayList<>();

		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_GET_USERS_BY_OBSERVED)) {
			stmt.setInt(1, observedId);

			try (ResultSet rs = stmt.executeQuery()) {
				userList = mapper.mapResult(rs, Observed.class);
			}
		}

		return userList;
	}

	public Observed reassignObserved(Observed observed, Observed priorObserved, boolean updateObservers)
			throws SQLException {
		try (Connection conn = getConnection()) {
			conn.setAutoCommit(false);

			updateObserved(conn, observed);

			try {
				deleteObservers(conn, priorObserved);

				if (updateObservers) {
					updateObservers(conn, observed);
				} else {
					insertObservers(conn, observed);
				}

			} catch (Exception ex) {
				conn.rollback();
				throw ex;
			}

			conn.commit();
		}

		return observed;
	}

}