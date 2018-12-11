package com.canalbrewing.myabcdata.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Incident implements Serializable
{
	private static final long serialVersionUID = 280591020970526872L;
	
	public static final String INCIDENT_ID = "incident_id";
	public static final String INCIDENT_DT = "incident_dt";
	public static final String USER_ID = "user_id";
	public static final String USER_NM = "user_nm";
	public static final String LOCATION_ID = "location_id";
	public static final String LOCATION = "location";
	public static final String INTENSITY_ID = "intensity_id";
	public static final String INTENSITY = "intensity";
	public static final String DURATION = "duration";
	public static final String DESCRIPTION = "description";
	
	private static final SimpleDateFormat DATETIME = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	public static final String ABC_DELIMITER = "; ";
		
	private int id;
	private Date incidentDt;
	private int observedId;
	private int userId;
	private int locationId;
	private int intensityId;
	private int duration;
	private String description;
	
	private String userNm;
	private String location;
	private String intensity;
	
	private List<ABC> antecedents = new ArrayList<ABC>();
	private List<ABC> behaviors = new ArrayList<ABC>();
	private List<ABC> consequences = new ArrayList<ABC>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getIncidentDt() {
		return incidentDt;
	}
	public void setIncidentDt(Date incidentDt) {
		this.incidentDt = incidentDt;
	}
	
	public String getIncidentDtStr()
	{
		return DATETIME.format(incidentDt);
	}
	
	public int getObservedId() {
		return observedId;
	}
	public String getObservedIdStr() {
		return String.valueOf(observedId);
	}
	public void setObservedId(int observedId) {
		this.observedId = observedId;
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
	public int getLocationId() {
		return locationId;
	}
	public String getLocationIdStr() {
		return String.valueOf(locationId);
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public int getIntensityId() {
		return intensityId;
	}
	public String getIntensityIdStr() {
		return String.valueOf(intensityId);
	}
	public void setIntensityId(int intensityId) {
		this.intensityId = intensityId;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getDurationStr()
	{
		String desc = "";
		
		int min = duration / 60;
		int sec = duration % 60;
		
		if ( min > 0 )
		{
			if ( min == 1 )
			{
				desc = "1 minute";
			}
			else
			{
				desc = min + " minutes";
			}
		}
		
		if ( sec > 0 )
		{
			if ( desc.length() > 0 )
			{
				desc += ", ";
			}
			
			if ( sec == 1 )
			{
				desc += "1 second";
			}
			else
			{
				desc += sec + " seconds";
			}
		}
		
		return desc;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
		}
		
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getIntensity() {
		return intensity;
	}
	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	
	private String getABCString(List<ABC> list)
	{
		String desc = "";
		
		for (ABC abc : list )
		{
			if ( desc.length() > 0 )
			{
				desc += ABC_DELIMITER;
			}
			desc += abc.getTypeValue();
		}
		
		return desc;
	}
	
	public String getAntecedentStr()
	{
		return getABCString(getAntecedents());
	}
	
	public String getBehaviorStr()
	{
		return getABCString(getBehaviors());
	}
	
	public String getConsequenceStr()
	{
		return getABCString(getConsequences());
	}
}
