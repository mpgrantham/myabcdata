package com.canalbrewing.myabcdata.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.canalbrewing.myabcdata.model.Relationship;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;
import com.canalbrewing.myabcdata.model.Verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDaoJdbc extends BaseDao implements UserDao {

	private static final String PROC_GET_RELATIONSHIPS = "{call get_relationships()}";

	private static final String PROC_GET_USER_SESSION = "{call get_user_session(?)}";
	private static final String PROC_INSERT_USER_SESSION = "{call insert_user_session(?,?,?,?)}";
	private static final String PROC_UPDATE_USER_SESSION = "{call update_user_session(?,?,?)}";

	private static final String PROC_GET_USER_BY_USER_NM = "{call get_user_by_user_nm(?)}";
	private static final String PROC_GET_USER_BY_ID = "{call get_user_by_id(?)}";
	private static final String PROC_GET_USER_BY_EMAIL = "{call get_user_by_email(?)}";

	private static final String PROC_INSERT_USER = "{call insert_user(?,?,?,?,?,?,?)}";

	private static final String PROC_DELETE_USER = "{call delete_user(?)}";

	private static final String PROC_UPDATE_USER = "{call update_user(?,?,?, ?)}";
	private static final String PROC_UPDATE_USER_PASSWORD = "{call update_user_password(?,?,?)}";
	private static final String PROC_UPDATE_USER_USER_NM = "{call update_user_user_nm(?,?)}";

	private static final String PROC_INSERT_VERIFICATION = "{call insert_verification(?,?,?,?,?,?)}";
	private static final String PROC_UPDATE_VERIFICATION = "{call update_verification(?, ?)}";
	private static final String PROC_GET_VERIFICATION = "{call get_verification(?)}";

	private static final String COL_ID = "id";
	private static final String COL_RELATIONSHIP = "relationship";
	private static final String COL_USER_NM = "user_nm";
	private static final String COL_PASSWORD = "password";
	private static final String COL_SALT = "salt";
	private static final String COL_EMAIL = "email";
	private static final String COL_START_PAGE = "start_page";
	private static final String COL_STATUS = "status";

	private static final String COL_USER_ID = "user_id";
	private static final String COL_SESSION_DATETIME = "session_datetime";
	private static final String COL_SESSION_ACTIVE_FL = "session_active_fl";

	private static final String COL_EXPIRATION_DT = "expiration_dt";
	private static final String COL_VERIFICATION_TYPE = "verification_type";
	private static final String COL_VERIFIED_USER_ID = "verified_user_id";
	private static final String COL_REQUESTING_USER_ID = "requesting_user_id";
	private static final String COL_OBSERVED_ID = "observed_id";

	@Autowired
	ObservedDao observedDao;

	public List<Relationship> getRelationships() throws SQLException {

		List<Relationship> relationships = new ArrayList<>();

		// Try-With Resopurces closes Connection, etc.
		try (Connection conn = getConnection();
				CallableStatement stmt = conn.prepareCall(PROC_GET_RELATIONSHIPS);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Relationship relationship = new Relationship();
				relationship.setId(rs.getInt(COL_ID));
				relationship.setName(rs.getString(COL_RELATIONSHIP));
				relationships.add(relationship);
			}
		}

		return relationships;
	}

	/* UserSession */

	public UserSession getUserSession(String sessionToken) throws SQLException {
		UserSession session = null;

		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_GET_USER_SESSION)) {
			stmt.setString(1, sessionToken);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					session = new UserSession();
					session.setSessionToken(sessionToken);
					session.setUserId(rs.getInt(COL_USER_ID));
					session.setSessionDate(rs.getDate(COL_SESSION_DATETIME));
					session.setSessionActiveFl(rs.getInt(COL_SESSION_ACTIVE_FL));
				}
			}
		}

		return session;
	}

	public void insertUserSession(User user) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_INSERT_USER_SESSION)) {
			stmt.setString(1, user.getSessionKey());
			stmt.setInt(2, user.getId());
			stmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
			stmt.setInt(4, 1);

			stmt.execute();
		}
	}

	public void updateUserSession(UserSession session) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_UPDATE_USER_SESSION)) {
			stmt.setString(1, session.getSessionToken());
			stmt.setDate(2, new java.sql.Date(session.getSessionDate().getTime()));
			stmt.setInt(3, session.getSessionActiveFl());

			stmt.execute();
		}
	}

	/* User */

	public User getUserByUsername(String username) throws SQLException {
		return getUser(PROC_GET_USER_BY_USER_NM, username);
	}

	public User getUserByEmail(String email) throws SQLException {
		return getUser(PROC_GET_USER_BY_EMAIL, email);
	}

	private User getUser(String storedProc, String value) throws SQLException {
		User user = null;

		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(storedProc)) {
			stmt.setString(1, value);

			try (ResultSet rs = stmt.executeQuery()) {
				user = mapUser(rs);
			}
		}

		return user;
	}

	public User getUserById(int userId) throws SQLException {
		User user = null;

		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_GET_USER_BY_ID)) {
			stmt.setInt(1, userId);

			try (ResultSet rs = stmt.executeQuery()) {
				user = mapUser(rs);
			}
		}

		return user;
	}

	private User mapUser(ResultSet rs) throws SQLException {
		User user = null;

		while (rs.next()) {
			user = new User();
			user.setId(rs.getInt(COL_ID));
			user.setUserNm(rs.getString(COL_USER_NM));
			user.setEncryptedPassword(rs.getBytes(COL_PASSWORD));
			user.setSalt(rs.getBytes(COL_SALT));
			user.setEmail(rs.getString(COL_EMAIL));
			user.setStartPage(rs.getString(COL_START_PAGE));
			user.setStatus(rs.getString(COL_STATUS));
		}

		return user;
	}

	public User insertUser(User user) throws SQLException {
		try (Connection conn = getConnection()) {
			conn.setAutoCommit(false);

			try (CallableStatement stmt = conn.prepareCall(PROC_INSERT_USER)) {
				stmt.setString(1, user.getUserNm());
				stmt.setString(2, user.getEmail());
				stmt.setBytes(3, user.getEncryptedPassword());
				stmt.setBytes(4, user.getSalt());
				stmt.setString(5, user.getStartPage());
				stmt.setString(6, user.getStatus());

				stmt.registerOutParameter(7, Types.INTEGER);

				stmt.execute();

				user.setId(stmt.getInt(7));
			} catch (Exception ex) {
				conn.rollback();
				throw ex;
			}

			for (Observed observed : user.getObserved()) {
				observed.setUserId(user.getId());
				observedDao.insertObserved(conn, observed);
				observedDao.insertObservers(conn, observed);
			}

			conn.commit();
		}

		return user;
	}

	public User updateUser(User user) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_UPDATE_USER)) {
			stmt.setInt(1, user.getId());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getStartPage());
			stmt.setString(4, user.getStatus());
			stmt.execute();
		}

		return user;
	}

	public void deleteUser(int userId) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_DELETE_USER)) {
			stmt.setInt(1, userId);
			stmt.execute();
		}
	}

	public User updatePassword(User user) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_UPDATE_USER_PASSWORD)) {
			stmt.setInt(1, user.getId());
			stmt.setBytes(2, user.getEncryptedPassword());
			stmt.setBytes(3, user.getSalt());
			stmt.execute();
		}

		return user;
	}

	public User updateUsername(User user) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_UPDATE_USER_USER_NM)) {
			stmt.setInt(1, user.getId());
			stmt.setString(2, user.getUserNm());
			stmt.execute();
		}

		return user;
	}

	public void insertVerification(Verification verification) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_INSERT_VERIFICATION)) {
			stmt.setString(1, verification.getVerificationKey());
			stmt.setTimestamp(2, new java.sql.Timestamp(verification.getExpirationDt().getTime()));
			stmt.setString(3, verification.getVerificationType());
			stmt.setInt(4, verification.getVerifiedUserId());
			stmt.setInt(5, verification.getRequestingUserId());
			stmt.setInt(6, verification.getObservedId());

			stmt.execute();
		}
	}

	public void updateVerification(String verificationKey, Date expirationDt) throws SQLException {
		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_UPDATE_VERIFICATION)) {
			stmt.setString(1, verificationKey);
			stmt.setTimestamp(2, new java.sql.Timestamp(expirationDt.getTime()));

			stmt.execute();
		}
	}

	public Verification getVerification(String verificationKey) throws SQLException {
		Verification verification = null;

		try (Connection conn = getConnection(); CallableStatement stmt = conn.prepareCall(PROC_GET_VERIFICATION)) {
			stmt.setString(1, verificationKey);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					verification = new Verification();
					verification.setVerificationKey(verificationKey);
					verification.setExpirationDt(rs.getDate(COL_EXPIRATION_DT));
					verification.setVerificationType(rs.getString(COL_VERIFICATION_TYPE));
					verification.setVerifiedUserId(rs.getInt(COL_VERIFIED_USER_ID));
					verification.setRequestingUserId(rs.getInt(COL_REQUESTING_USER_ID));
					verification.setVerifiedUserId(rs.getInt(COL_VERIFIED_USER_ID));
					verification.setObservedId(rs.getInt(COL_OBSERVED_ID));
				}
			}
		}

		return verification;
	}

}
