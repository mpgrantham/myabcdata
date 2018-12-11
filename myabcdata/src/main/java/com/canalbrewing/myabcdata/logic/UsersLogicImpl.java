package com.canalbrewing.myabcdata.logic;

import java.util.Arrays;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.common.MyABCConstants;
import com.canalbrewing.myabcdata.dal.ObservedDAO;
import com.canalbrewing.myabcdata.dal.UsersDAO;
import com.canalbrewing.myabcdata.exception.MyABCException;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.StatusMessage;
import com.canalbrewing.myabcdata.model.User;
import com.canalbrewing.myabcdata.model.UserSession;
import com.canalbrewing.myabcdata.model.ValueObject;

@Component
public class UsersLogicImpl implements UsersLogic {
	
	@Autowired
	private UsersDAO usersDAO;
	
	@Autowired
	private ObservedDAO observedDAO;
	
	@Value("${myabcdataUrl}")
	private String myabcdataUrl;
	
	@Value("${fromEmail}")
	private String fromEmail;
	
	@Value("${smtpServer}")
	private String smtpServer;
	
	@Value("${smtpPort}")
	private String smtpPort;
	
	@Value("${mailAccount}")
	private String mailAccount;
	
	@Value("${mailPassword}")
	private String mailPassword;
	
	public UserSession getUserSession(String sessionToken) throws Exception
	{
		UserSession session = usersDAO.getUserSession(sessionToken);
		if ( session == null )
		{
			throw new MyABCException(MyABCException.INVALID_SESSION, "Invalid Session. Session Not Found.");
		}
		
		if ( session.getSessionActiveFl() == 0 )
		{
			throw new MyABCException(MyABCException.INVALID_SESSION, "Invalid Session. Session Inactive.");
		}
		
		return session;
	}
	
	public List<ValueObject> getRelationships() throws Exception
	{
		List<ValueObject> list = usersDAO.getRelationships();
		
		list.add(0, new ValueObject(0, MyABCConstants.NA));
		
		return list;
	}
	
	public User signInUser(String username, String password) throws Exception
	{
		User user = usersDAO.getUserByUsername(username);
		
		if ( user == null  )
		{
			throw new MyABCException(MyABCException.INVALID_LOGIN, "Invalid Login");
		}
		
		PasswordEncryption encrypt = new PasswordEncryption();
		
		byte[] salt = user.getSalt();
		byte[] encryptedPassword = encrypt.getEncryptedPassword(password, salt);
		
		if ( ! Arrays.equals(encryptedPassword,  user.getEncryptedPassword()))
		{
			throw new MyABCException(MyABCException.INVALID_LOGIN, "Invalid Login");
		}
		
		/*
		Map<String, String> settingsMap = usersDAO.getUserSettings(user.getId());
		if ( settingsMap != null )
		{
			user.addSetting(BlocksConstants.START_PAGE, settingsMap.get(BlocksConstants.START_PAGE));
		}
		*/
		
		//user.setPassword(password);
		
		user.setSessionKey(UUID.randomUUID().toString().replaceAll("-", ""));
		usersDAO.insertUserSession(user);
						
		return user;
	}
	
	public void signOutUser(String sessionToken) throws Exception
	{
		UserSession session = usersDAO.getUserSession(sessionToken);
		if ( session == null )
		{
			throw new MyABCException(MyABCException.INVALID_SESSION, "Invalid Session. Session Not Found.");
		}
		
		session.setSessionActiveFl(0);
		
		usersDAO.updateUserSession(session);
	}

	public User saveUser(User user) throws Exception
	{
		User u = usersDAO.getUserByUsername(user.getUserNm());
		if ( u != null && u.getId() != user.getId() )
		{
			throw new Exception("Username already exists.");
		}
		u = usersDAO.getUserByEmail(user.getEmail());
		if ( u != null && u.getId() != user.getId() )
		{
			throw new Exception("Email already exists.");
		}
		
		PasswordEncryption encrypt = new PasswordEncryption();
		byte[] salt = encrypt.generateSalt();
		byte[] encryptedPassword = encrypt.getEncryptedPassword(user.getPassword(), salt);
		user.setEncryptedPassword(encryptedPassword);
		user.setSalt(salt);
		user.setPasswordResetKey("");
		
		if ( user.getId() == 0 )
		{
			return usersDAO.insertUser(user);
		}
		else
		{
			return usersDAO.updateUser(user);
		}
	}
	
	public StatusMessage sendUsernameOrPassword(String email, String forgottenItem) 
	{
		// Get User by email
		String message = "";
		
		try
		{
			User user = usersDAO.getUserByEmail(email);
			if ( user == null )
			{
				return new StatusMessage("ERROR", "No User exists with that Email.");
			}
			
			if ( MyABCConstants.USERNAME.equals(forgottenItem) )
			{
				StringBuilder body = new StringBuilder();
				body.append("Your my ABC data Username: ").append(user.getUserNm());
				
				sendEmail("my ABC data Username", body, email);
				
				message = "Username email sent.  Check your email for your my ABC data Username.";
			}
			else
			{
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				
				user.setPasswordResetKey(uuid);
				usersDAO.updateUser(user);
							
				StringBuilder body = new StringBuilder();
				body.append("Please click on this link or copy it into your browser\n\n");
				body.append(myabcdataUrl + "/#!/reset/" + uuid);
				//body.append(blocksUrl + "/ResetUserPassword.do?resetPasswordKey=" + uuid);
				
				sendEmail("Reset my ABC data Password", body, email);
				
				message = "Password Reset Link email sent.  Check your email for your Password reset link.";
			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return new StatusMessage("ERROR", ex.getMessage());
		}
		
		return new StatusMessage("SUCCESS", message);
	}
	
	private void sendEmail(String subject, StringBuilder body, String toEmail) throws Exception
	{
		Properties props = System.getProperties();
        // -- Attaching to default Session, or we could start a new one --
		
		 Authenticator authenticator = new Authenticator(mailAccount, mailPassword);
		 		 
		 props.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
		 
		 //props.put("mail.transport.protocol", "smtp"); 
		 
		 props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", smtpPort);
        
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getDefaultInstance(props, authenticator);
        
        
        Message msg = new MimeMessage(session);
        // -- Set the FROM and TO fields --
        msg.setFrom(new InternetAddress(fromEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        
        msg.setSubject(subject);
        msg.setText(body.toString());
        // -- Set some other header information --
        msg.setHeader("X-Mailer", "Blocks ABC");
        
        msg.setSentDate(new Date());
        // -- Send the message --
        Transport.send(msg);
	}
	
	public User getUserByResetKey(String resetKey) throws Exception
	{
		return usersDAO.getUserByResetKey(resetKey);
	}
	
	public Observed addObserved(int userId, String observedNm, String relationshipId) throws Exception
	{
		Observed obs = new Observed();
		obs.setUserId(userId);
		obs.setObservedNm(observedNm);
		obs.setRole(Observed.ROLE_ADMIN);
		obs.setAccessStatus(Observed.STATUS_ACTIVE);
		obs.setRelationshipId(Integer.parseInt(relationshipId));
		
		Optional<ValueObject> relationship = this.getRelationships().stream().filter(r -> r.getStrId().equals(relationshipId)).findFirst();
		if ( relationship.isPresent() )
		{
			obs.setRelationship(relationship.get().getValue());
		}
		
		return usersDAO.insertObserved(obs);
	}
	
	public Observed updateObserved(int userId, String observedId, String observedNm, String role, String relationshipId) throws Exception
	{
		Observed obs = new Observed();
		obs.setUserId(userId);
		obs.setId(Integer.parseInt(observedId));
		obs.setObservedNm(observedNm);
		obs.setRole(role);
		obs.setAccessStatus(Observed.STATUS_ACTIVE);
		obs.setRelationshipId(Integer.parseInt(relationshipId));
		
		Optional<ValueObject> relationship = this.getRelationships().stream().filter(r -> r.getStrId().equals(relationshipId)).findFirst();
		if ( relationship.isPresent() )
		{
			obs.setRelationship(relationship.get().getValue());
		}
		
		return usersDAO.updateObserved(obs);
	}
	
	public Observed grantAccess(String obsId, String email, String role, String relationshipId) throws Exception
	{
		User user = usersDAO.getUserByEmail(email);
		if ( user == null )
		{
			throw new Exception("No User exists with that Email.");
		}
		
		int observedId = Integer.parseInt(obsId);
		
		List<Observed> list = observedDAO.getObservedByUser(user.getId());
		List<Observed> found = list.stream().filter(o -> o.getId() == observedId).collect(Collectors.toList());
		if ( found != null && found.size() > 0 )
		{
			Observed obs = found.get(0);
			if ( Observed.STATUS_ACTIVE.equals(obs.getAccessStatus()) )
			{
				throw new Exception("User already has access to this Observed.");
			}
		}
		
		//String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		
		Observed observed = new Observed();
		observed.setId(observedId);
		observed.setUserId(user.getId());
		observed.setRole(role);
		observed.setEmail(user.getEmail());
		observed.setRelationshipId(Integer.parseInt(relationshipId));
		observed.setAccessStatus(Observed.STATUS_ACTIVE);
		//observed.setAccessKey(uuid);
		
		Optional<ValueObject> relationship = this.getRelationships().stream().filter(r -> r.getStrId().equals(relationshipId)).findFirst();
		if ( relationship.isPresent() )
		{
			observed.setRelationship(relationship.get().getValue());
		}
		
		usersDAO.insertObservers(observed);
		
		StringBuilder body = new StringBuilder();
		body.append("You have been granted access to a my ABC data Observed.\n\n")
		.append("You will see the Observed next time you sign in to my ABC data.\n\n")
		.append("If you do not want access to this Observed you can remove access from the application.");
		//body.append(myabcdataUrl + "/#!/acceptAccess/" + uuid);
	
		sendEmail("Access granted to my ABC data Observed", body, email);
		
		return observed;
	}
	
	public List<Observed> getUsersByObserved(String observedId) throws Exception
	{
		return usersDAO.getUsersByObserved(Integer.parseInt(observedId));
	}
	
	public Observed updateObservers(String userId, String observedId, String role, String relationshipId) throws Exception
	{
		Observed observed = new Observed();
		observed.setId(Integer.parseInt(observedId));
		observed.setUserId(Integer.parseInt(userId));
		observed.setRole(role);
		observed.setRelationshipId(Integer.parseInt(relationshipId));
		observed.setAccessStatus(Observed.STATUS_ACTIVE);
		
		Optional<ValueObject> relationship = this.getRelationships().stream().filter(r -> r.getStrId().equals(relationshipId)).findFirst();
		if ( relationship.isPresent() )
		{
			observed.setRelationship(relationship.get().getValue());
		}
		
		return usersDAO.updateObservers(observed);
	}
	
	public Observed deleteObservers(String userId, String observedId) throws Exception
	{
		Observed observed = new Observed();
		observed.setId(Integer.parseInt(observedId));
		observed.setUserId(Integer.parseInt(userId));
				
		return usersDAO.deleteObservers(observed);
	}

}
