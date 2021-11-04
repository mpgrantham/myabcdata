package com.canalbrewing.myabcdata.business;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.canalbrewing.myabcdata.dal.ObservedDao;
import com.canalbrewing.myabcdata.dal.UserDao;
import com.canalbrewing.myabcdata.exception.MyAbcDataException;
import com.canalbrewing.myabcdata.model.Relationship;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;
import com.canalbrewing.myabcdata.model.Verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserBusinessImpl implements UserBusiness {

	private static final String MSG_USERNAME_EXISTS = "Username already exists";
	private static final String MSG_EMAIL_EXISTS = "Email already exists";
	private static final String MSG_INVALID_LOGIN = "Invalid Login";
	private static final String MSG_NO_USER_FOR_EMAIL = "No User exists with that Email. Please have the person to whom you are granting access register as a my ABC data user.";

	@Autowired
	ObservedDao observedDao;

	@Autowired
	UserDao userDao;

	@Value("${myabcdata.url}")
	private String myabcdataUrl;

	@Value("${myabcdata.fromEmail}")
	private String fromEmail;

	@Value("${myabcdata.smtpServer}")
	private String smtpServer;

	@Value("${myabcdata.smtpPort}")
	private String smtpPort;

	@Value("${myabcdata.mailAccount}")
	private String mailAccount;

	@Value("${myabcdata.mailPassword}")
	private String mailPassword;

	public List<Relationship> getRelationships() throws SQLException {

		List<Relationship> relationships = userDao.getRelationships();
		relationships.add(0, new Relationship(0, "N/A"));

		return relationships;
	}

	public UserSession getUserSession(String sessionToken) throws SQLException {
		UserSession session = userDao.getUserSession(sessionToken);
		if (session == null) {
			throw new MyAbcDataException(MyAbcDataException.INVALID_SESSION, "Invalid Session. Session Not Found.");
		}

		if (session.getSessionActiveFl() == 0) {
			throw new MyAbcDataException(MyAbcDataException.INVALID_SESSION, "Invalid Session. Session Inactive.");
		}

		return session;
	}

	public User signInUser(String username, String password) throws SQLException {
		User user = userDao.getUserByUsername(username);

		if (user == null || User.STATUS_PENDING.equals(user.getStatus())) {
			throw new MyAbcDataException(MyAbcDataException.INVALID_LOGIN, MSG_INVALID_LOGIN);
		}

		byte[] encryptedPassword = null;

		try {
			PasswordEncryption encrypt = new PasswordEncryption();

			byte[] salt = user.getSalt();
			encryptedPassword = encrypt.getEncryptedPassword(password, salt);
		} catch (Exception ex) {
			throw new MyAbcDataException(MyAbcDataException.OTHER, ex.getMessage());
		}

		if (!Arrays.equals(encryptedPassword, user.getEncryptedPassword())) {
			throw new MyAbcDataException(MyAbcDataException.INVALID_LOGIN, MSG_INVALID_LOGIN);
		}

		user.setSessionKey(UUID.randomUUID().toString().replace("-", ""));

		userDao.insertUserSession(user);

		return user;
	}

	public void signOutUser(String sessionToken) throws SQLException {

		UserSession session = userDao.getUserSession(sessionToken);
		if (session == null) {
			throw new MyAbcDataException(MyAbcDataException.INVALID_SESSION, "Invalid Session. Session Not Found.");
		}

		session.setSessionActiveFl(0);

		userDao.updateUserSession(session);
	}

	public User register(User user) throws SQLException {

		if (userDao.getUserByUsername(user.getUserNm()) != null) {
			throw new MyAbcDataException(MyAbcDataException.DUPLICATE, MSG_USERNAME_EXISTS);
		}

		if (userDao.getUserByEmail(user.getEmail()) != null) {
			throw new MyAbcDataException(MyAbcDataException.DUPLICATE, MSG_EMAIL_EXISTS);
		}

		try {
			PasswordEncryption encrypt = new PasswordEncryption();
			byte[] salt = encrypt.generateSalt();
			byte[] encryptedPassword = encrypt.getEncryptedPassword(user.getPassword(), salt);
			user.setEncryptedPassword(encryptedPassword);
			user.setSalt(salt);
		} catch (Exception ex) {
			throw new MyAbcDataException(MyAbcDataException.OTHER, ex.getMessage());
		}

		user.setStatus(User.STATUS_PENDING);

		User insertUser = userDao.insertUser(user);

		String uuid = UUID.randomUUID().toString().replace("-", "");

		Verification verification = new Verification();
		verification.setVerificationKey(uuid);
		verification.setVerificationType(Verification.REGISTER);
		verification.setVerifiedUserId(insertUser.getId());
		verification.setExpirationDt(getExpirationDate(24));

		userDao.insertVerification(verification);

		StringBuilder body = new StringBuilder();
		body.append("To complete My ABC Data registration, please click on this link or copy it into your browser\n\n");
		body.append(myabcdataUrl + "/confirmRegister/" + uuid);

		sendEmail("Verify My ABC Data Registration", body, user.getEmail());

		return insertUser;
	}

	public User confirmRegister(String verificationKey) throws SQLException {

		Verification verification = getVerification(verificationKey);

		User user = userDao.getUserById(verification.getVerifiedUserId());
		if (user == null) {
			throw new MyAbcDataException(MyAbcDataException.NOT_FOUND, MSG_NO_USER_FOR_EMAIL);
		}

		user.setStatus(User.STATUS_ACTIVE);

		userDao.updateUser(user);

		userDao.updateVerification(verificationKey, new Date());

		return user;
	}

	public User saveUser(User user) throws SQLException {

		User u = userDao.getUserByUsername(user.getUserNm());
		if (u != null && u.getId() != user.getId()) {
			throw new MyAbcDataException(MyAbcDataException.DUPLICATE, "Username already exists.");
		}

		u = userDao.getUserByEmail(user.getEmail());
		if (u != null && u.getId() != user.getId()) {
			throw new MyAbcDataException(MyAbcDataException.DUPLICATE, "Email already exists.");
		}

		try {
			PasswordEncryption encrypt = new PasswordEncryption();
			byte[] salt = encrypt.generateSalt();
			byte[] encryptedPassword = encrypt.getEncryptedPassword(user.getPassword(), salt);
			user.setEncryptedPassword(encryptedPassword);
			user.setSalt(salt);
		} catch (Exception ex) {
			throw new MyAbcDataException(MyAbcDataException.OTHER, ex.getMessage());
		}

		if (user.getId() == 0) {
			user.setStatus(User.STATUS_PENDING);
			return userDao.insertUser(user);
		}

		return userDao.updateUser(user);

	}

	public User updateUser(User user) throws SQLException {

		User u = userDao.getUserByEmail(user.getEmail());
		if (u != null && u.getId() != user.getId()) {
			throw new MyAbcDataException(MyAbcDataException.DUPLICATE, "Email already exists.");
		}

		return userDao.updateUser(user);
	}

	public User updateUsername(int userId, String userName) throws SQLException {

		User u = userDao.getUserByUsername(userName);
		if (u != null && u.getId() != userId) {
			throw new MyAbcDataException(MyAbcDataException.DUPLICATE, "Username already exists.");
		}

		User user = new User();
		user.setId(userId);
		user.setUserNm(userName);

		return userDao.updateUsername(user);
	}

	public User resetPassword(String resetKey, String password) throws SQLException {

		User user = getUserByResetKey(resetKey);

		try {
			PasswordEncryption encrypt = new PasswordEncryption();

			byte[] salt = encrypt.generateSalt();

			user.setEncryptedPassword(encrypt.getEncryptedPassword(password, salt));
			user.setSalt(salt);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new MyAbcDataException(MyAbcDataException.OTHER, ex.getMessage());
		}

		userDao.updatePassword(user);

		userDao.updateVerification(resetKey, new Date());

		return user;
	}

	public User updatePassword(int userId, String currentPassword, String password) throws SQLException {

		User existingUser = userDao.getUserById(userId);

		User user = new User();

		try {
			PasswordEncryption encrypt = new PasswordEncryption();

			byte[] salt = existingUser.getSalt();

			byte[] encryptedCurrentPassword = encrypt.getEncryptedPassword(currentPassword, salt);

			if (!Arrays.equals(encryptedCurrentPassword, existingUser.getEncryptedPassword())) {
				throw new MyAbcDataException(MyAbcDataException.INVALID_LOGIN, MSG_INVALID_LOGIN);
			}

			salt = encrypt.generateSalt();

			user.setId(userId);
			user.setEncryptedPassword(encrypt.getEncryptedPassword(password, salt));
			user.setSalt(salt);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new MyAbcDataException(MyAbcDataException.OTHER, ex.getMessage());
		}

		return userDao.updatePassword(user);
	}

	public User getUserByResetKey(String resetKey) throws SQLException {
		Verification verification = userDao.getVerification(resetKey);

		if (verification == null) {
			throw new MyAbcDataException(MyAbcDataException.NOT_FOUND, "Reset Link not found");
		}

		return userDao.getUserById(verification.getVerifiedUserId());
	}

	public void deleteUser(int userId) throws SQLException {

		List<Observed> observedList = observedDao.getObservedByUser(userId);
		Optional<Observed> found = observedList.stream().filter(o -> o.getRole().equals(Observed.ROLE_ADMIN)).findAny();
		if (found.isPresent()) {
			throw new MyAbcDataException(MyAbcDataException.INVALID_DELETE, "User is ADMIN for Observed");
		}

		userDao.deleteUser(userId);
	}

	public Observed addObserved(int userId, String observedNm, String relationshipId) throws SQLException {

		List<Observed> observedList = observedDao.getObservedByUser(userId);
		Optional<Observed> found = observedList.stream().filter(o -> o.getObservedNm().equals(observedNm)).findAny();
		if (found.isPresent()) {
			throw new MyAbcDataException(MyAbcDataException.DUPLICATE, "Observed name exists for User");
		}

		Observed observed = new Observed();
		observed.setUserId(userId);
		observed.setObservedNm(observedNm);
		observed.setRole(Observed.ROLE_ADMIN);
		observed.setAccessStatus(Observed.STATUS_ACTIVE);
		observed.setRelationshipId(Integer.parseInt(relationshipId));

		setRelationship(observed, relationshipId);

		return observedDao.insertObserved(observed);
	}

	public Observed updateObserved(int userId, String observedId, String observedNm, String role, String relationshipId)
			throws SQLException {
		Observed observed = new Observed();
		observed.setUserId(userId);
		observed.setId(Integer.parseInt(observedId));
		observed.setObservedNm(observedNm);
		observed.setRole(role);
		observed.setAccessStatus(Observed.STATUS_ACTIVE);
		observed.setRelationshipId(Integer.parseInt(relationshipId));

		setRelationship(observed, relationshipId);

		return observedDao.updateObserved(observed);
	}

	public Observed grantAccess(String obsId, String email, String role, String relationshipId) throws SQLException {
		User user = userDao.getUserByEmail(email);
		if (user == null || User.STATUS_PENDING.equals(user.getStatus())) {
			throw new MyAbcDataException(MyAbcDataException.NOT_FOUND, MSG_NO_USER_FOR_EMAIL);
		}

		int observedId = Integer.parseInt(obsId);

		List<Observed> list = observedDao.getObservedByUser(user.getId());

		List<Observed> found = list.stream().filter(o -> o.getId() == observedId).collect(Collectors.toList());
		if (found != null && !found.isEmpty()) {
			Observed obs = found.get(0);
			if (Observed.STATUS_ACTIVE.equals(obs.getAccessStatus())) {
				throw new MyAbcDataException(MyAbcDataException.DUPLICATE, "User already has access to this Observed.");
			}
		}

		Observed observed = new Observed();
		observed.setId(observedId);
		observed.setUserId(user.getId());
		observed.setRole(role);
		observed.setEmail(user.getEmail());
		observed.setRelationshipId(Integer.parseInt(relationshipId));
		observed.setAccessStatus(Observed.STATUS_ACTIVE);

		setRelationship(observed, relationshipId);

		observedDao.insertObservers(observed);

		StringBuilder body = new StringBuilder();
		body.append("You have been granted access to a my ABC data Observed.\n\n")
				.append("You will see the Observed next time you sign in to my ABC data.\n\n")
				.append("If you do not want access to this Observed you can remove access.\n\n")
				.append("To remove access, navigate to Settings. Choose the observed in the Observed section.  Click 'Remove Access', then confirm.");

		sendEmail("Access granted to My ABC Data Observed", body, email);

		return observed;
	}

	public List<Observed> getUsersByObserved(String observedId) throws SQLException {
		return observedDao.getUsersByObserved(Integer.parseInt(observedId));
	}

	public Observed updateObservers(String userId, String observedId, String role, String relationshipId)
			throws SQLException {
		Observed observed = new Observed();
		observed.setId(Integer.parseInt(observedId));
		observed.setUserId(Integer.parseInt(userId));
		observed.setRole(role);
		observed.setRelationshipId(Integer.parseInt(relationshipId));
		observed.setAccessStatus(Observed.STATUS_ACTIVE);

		setRelationship(observed, relationshipId);

		return observedDao.updateObservers(observed);
	}

	public Observed deleteObservers(int userId, String observedUserId, String observedId) throws SQLException {

		int id = Integer.parseInt(observedUserId);

		if (userId != id) {

			// todo - make sure userId is the ADMIN for that observedId

		}

		Observed observed = new Observed();
		observed.setId(Integer.parseInt(observedId));
		observed.setUserId(id);

		return observedDao.deleteObservers(observed);
	}

	public String requestReassign(String sessionToken, String observedId, String email) throws SQLException {

		UserSession session = getUserSession(sessionToken);

		User user = userDao.getUserByEmail(email);
		if (user == null || User.STATUS_PENDING.equals(user.getStatus())) {
			throw new MyAbcDataException(MyAbcDataException.NOT_FOUND, MSG_NO_USER_FOR_EMAIL);
		}

		String observedNm = "Unknown Observed";

		Observed observed = observedDao.getObservedById(Integer.parseInt(observedId));
		if (observed != null) {
			observedNm = observed.getObservedNm();
		}

		String uuid = UUID.randomUUID().toString().replace("-", "");

		Verification verification = new Verification();
		verification.setVerificationKey(uuid);
		verification.setVerificationType(Verification.REASSIGN);
		verification.setVerifiedUserId(user.getId());
		verification.setRequestingUserId(session.getUserId());
		verification.setObservedId(Integer.parseInt(observedId));
		verification.setExpirationDt(getExpirationDate(72));

		userDao.insertVerification(verification);

		StringBuilder body = new StringBuilder();
		body.append("The following Observed has been reassigned to you.\n\n");
		body.append("Observed: ").append(observedNm).append("\n\n");
		body.append("To accept this reassignment, please click on this link or copy it into your browser\n\n");
		body.append(myabcdataUrl + "/reassign/" + uuid);

		sendEmail("Reassign My ABC Data Observed", body, email);

		return "Reassign request email sent.  The requested user must accept by clicking verification link.";
	}

	public Observed reassignObserved(String verificationKey, String relationshipId) throws SQLException {

		Verification verification = getVerification(verificationKey);

		User user = userDao.getUserById(verification.getVerifiedUserId());
		if (user == null || User.STATUS_PENDING.equals(user.getStatus())) {
			throw new MyAbcDataException(MyAbcDataException.NOT_FOUND, MSG_NO_USER_FOR_EMAIL);
		}

		int observedId = verification.getObservedId();

		boolean updateObservers = false;

		List<Observed> list = observedDao.getObservedByUser(user.getId());
		List<Observed> found = list.stream().filter(o -> o.getId() == observedId).collect(Collectors.toList());
		if (found != null && !found.isEmpty()) {
			updateObservers = true;
		}

		Observed observed = observedDao.getObservedById(observedId);
		observed.setUserId(user.getId());
		observed.setRole(Observed.ROLE_ADMIN);
		observed.setEmail(user.getEmail());
		observed.setRelationshipId(Integer.parseInt(relationshipId));
		observed.setAccessStatus(Observed.STATUS_ACTIVE);

		setRelationship(observed, relationshipId);

		Observed priorObserved = new Observed();
		priorObserved.setUserId(verification.getRequestingUserId());
		priorObserved.setId(observedId);

		observedDao.reassignObserved(observed, priorObserved, updateObservers);

		userDao.updateVerification(verificationKey, new Date());

		return observed;
	}

	public Verification getVerification(String verificationKey) throws SQLException {
		return userDao.getVerification(verificationKey);
	}

	public Verification getReassignVerification(String verificationKey) throws SQLException {
		Verification verification = userDao.getVerification(verificationKey);
		if (verification == null) {
			throw new MyAbcDataException(MyAbcDataException.NOT_FOUND, "Reassign Link not found");
		}

		Observed observed = observedDao.getObservedById(verification.getObservedId());
		if (observed != null) {
			verification.setObservedNm(observed.getObservedNm());
		}

		return verification;
	}

	private void setRelationship(Observed observed, String relationshipId) throws SQLException {
		Optional<Relationship> relationship = this.getRelationships().stream()
				.filter(r -> String.valueOf(r.getId()).equals(relationshipId)).findFirst();
		if (relationship.isPresent()) {
			observed.setRelationship(relationship.get().getName());
		}
	}

	public String sendUsernameOrPassword(String email, String forgottenItem) {
		String message = "";

		try {
			User user = userDao.getUserByEmail(email);
			if (user == null || User.STATUS_PENDING.equals(user.getStatus())) {
				throw new MyAbcDataException(MyAbcDataException.NOT_FOUND, MSG_NO_USER_FOR_EMAIL);
			}

			if ("USERNAME".equals(forgottenItem)) {
				StringBuilder body = new StringBuilder();
				body.append("Your My ABC Data Username: ").append(user.getUserNm());

				sendEmail("My ABC Data Username", body, email);

				message = "Username email sent.  Check your email for your My ABC Data Username.";
			} else {
				String uuid = UUID.randomUUID().toString().replace("-", "");

				Verification verification = new Verification();
				verification.setVerificationKey(uuid);
				verification.setVerificationType(Verification.PASSWORD_RESET);
				verification.setVerifiedUserId(user.getId());
				verification.setExpirationDt(getExpirationDate(24));

				userDao.insertVerification(verification);

				StringBuilder body = new StringBuilder();
				body.append("Please click on this link or copy it into your browser\n\n");
				body.append(myabcdataUrl + "/reset/" + uuid);

				sendEmail("Reset My ABC Data Password", body, email);

				message = "Password Reset Link email sent.  Check your email for your Password reset link.";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new MyAbcDataException(MyAbcDataException.OTHER, ex.getMessage());
		}

		return message;
	}

	private Date getExpirationDate(int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, hours);

		return calendar.getTime();
	}

	private void sendEmail(String subject, StringBuilder body, String toEmail) {
		Properties props = System.getProperties();
		// -- Attaching to default Session, or we could start a new one --

		MyAbcAuthenticator authenticator = new MyAbcAuthenticator(mailAccount, mailPassword);

		props.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());

		props.setProperty("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.port", smtpPort);

		props.put("mail.smtp.socketFactory.port", smtpPort);
		props.put("mail.smtp.ssl.checkserveridentity", "true");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getDefaultInstance(props, authenticator);

		try {
			Message msg = new MimeMessage(session);
			// -- Set the FROM and TO fields --
			msg.setFrom(new InternetAddress(fromEmail));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

			msg.setSubject(subject);
			msg.setText(body.toString());
			// -- Set some other header information --
			msg.setHeader("X-Mailer", "My ABC Data");

			msg.setSentDate(new Date());
			// -- Send the message --
			Transport.send(msg);
		} catch (Exception ex) {
			throw new MyAbcDataException(MyAbcDataException.OTHER, ex.getMessage());
		}

	}

}