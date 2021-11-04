package com.canalbrewing.myabcdata.model;

import java.util.ArrayList;
import java.util.List;

public class Observed {
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
    
    private boolean adminRole = false;
    private boolean entryRole = false;
    private boolean logRole = false;
	
	private String accessStatus = STATUS_ACTIVE;
	private String accessKey;
	
    private String email;
    
    private List<Abc> antecedents = new ArrayList<>();
	private List<Abc> behaviors = new ArrayList<>();
	private List<Abc> consequences = new ArrayList<>();
    private List<Abc> locations = new ArrayList<>();

    private IncidentSummary incidentSummary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setRelationshipId(int relationshipId) {
        this.relationshipId = relationshipId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Abc> getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(List<Abc> antecedents) {
        this.antecedents = antecedents;
    }

    public List<Abc> getBehaviors() {
        return behaviors;
    }

    public void setBehaviors(List<Abc> behaviors) {
        this.behaviors = behaviors;
    }

    public List<Abc> getConsequences() {
        return consequences;
    }

    public void setConsequences(List<Abc> consequences) {
        this.consequences = consequences;
    }

    public List<Abc> getLocations() {
        return locations;
    }

    public void setLocations(List<Abc> locations) {
        this.locations = locations;
    }

    public void addValue(Abc abc) {
		switch ( abc.getTypeCd() )
		{
		case Abc.ANTECEDENT:
			this.antecedents.add(abc);
			break;
		case Abc.BEHAVIOR:
			this.behaviors.add(abc);
			break;
		case Abc.CONSEQUENCE:
			this.consequences.add(abc);
			break;
		case Abc.LOCATION:
			this.locations.add(abc);
            break;
        default:
		}
    }

    public IncidentSummary getIncidentSummary() {
        return incidentSummary;
    }

    public void setIncidentSummary(IncidentSummary incidentSummary) {
        this.incidentSummary = incidentSummary;
    }

    public boolean isAdminRole() {
        return adminRole;
    }

    public void setAdminRole(boolean adminRole) {
        this.adminRole = adminRole;
    }

    public boolean isEntryRole() {
        return entryRole;
    }

    public void setEntryRole(boolean entryRole) {
        this.entryRole = entryRole;
    }

    public boolean isLogRole() {
        return logRole;
    }

    public void setLogRole(boolean logRole) {
        this.logRole = logRole;
    }

    
            
}