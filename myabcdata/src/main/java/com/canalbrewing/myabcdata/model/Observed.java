package com.canalbrewing.myabcdata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Observed implements Serializable
{
	private static final long serialVersionUID = 280591020970526872L;
	
	public static final String ID = "id";
	public static final String OBSERVED_ID = "observed_id";
	public static final String OBSERVED_NM = "observed_nm";
	public static final String USER_ID = "user_id";
	
	public static final String ROLE = "role";
	public static final String RELATIONSHIP_ID = "relationship_id";
	public static final String RELATIONSHIP = "relationship";
	
	public static final String ACCESS_STATUS = "access_status";
	public static final String ACCESS_KEY = "access_key";
	
	public static final String EMAIL = "email";
	
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_ENTRY = "ENTRY";
	public static final String ROLE_LOG = "LOG";
	public static final String ROLE_ENTRY_LOG = "ENTRY_LOG";
	
	public static final String STATUS_ACTIVE = "ACTIVE";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_INACTIVE = "INACTIVE";
	public static final String STATUS_HIDDEN = "HIDDEN";
	
	private int id;
	private String observedNm;
	private int userId;
	
	private String role;
	private int relationshipId;
	private String relationship;
	
	private String accessStatus = STATUS_ACTIVE;
	private String accessKey;
	
	private String email;
	
	private List<ABC> antecedents = new ArrayList<ABC>();
	private List<ABC> behaviors = new ArrayList<ABC>();
	private List<ABC> consequences = new ArrayList<ABC>();
	private List<ABC> locations = new ArrayList<ABC>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getStrId() {
		return String.valueOf(id);
	}
	
	public String getObservedNm() {
		return observedNm;
	}
	public void setObservedNm(String observedNm) {
		this.observedNm = observedNm;
	}
	
	public int getUserId() {
		return userId;
	}
	public String getUserIdStr() {
		return String.valueOf(userId);
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public int getRelationshipId() {
		return relationshipId;
	}
	public String getRelationshipIdStr() {
		return String.valueOf(relationshipId);
	}
	public void setRelationshipId(int relationshipId) {
		this.relationshipId = relationshipId;
	}
	
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	public List<ABC> getAntecedents() {
		return antecedents;
	}
	public void setAntecedents(List<ABC> antecedents) {
		this.antecedents = antecedents;
	}
	public List<ABC> getBehaviors() {
		return behaviors;
	}
	public void setBehaviors(List<ABC> behaviors) {
		this.behaviors = behaviors;
	}
	public List<ABC> getConsequences() {
		return consequences;
	}
	public void setConsequences(List<ABC> consequences) {
		this.consequences = consequences;
	}
	
	public List<ABC> getLocations() {
		return locations;
	}
	public void setLocations(List<ABC> locations) {
		this.locations = locations;
	}
	
	public void addValue(ABC abc)
	{
		switch ( abc.getTypeCd() )
		{
		case ABC.ANTECEDENT:
			this.antecedents.add(abc);
			break;
		case ABC.BEHAVIOR:
			this.behaviors.add(abc);
			break;
		case ABC.CONSEQUENCE:
			this.consequences.add(abc);
			break;
		case ABC.LOCATION:
			this.locations.add(abc);
			break;
			
		}
		
	}
	
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getAccessStatus() {
		return accessStatus;
	}
	public void setAccessStatus(String accessStatus) {
		this.accessStatus = accessStatus;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
