package com.canalbrewing.myabcdata.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Incident {

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
	
	private List<Abc> antecedents = new ArrayList<>();
	private List<Abc> behaviors = new ArrayList<>();
    private List<Abc> consequences = new ArrayList<>();

    private SimpleDateFormat dateTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

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

    public String getIncidentDtStr() {
		return dateTime.format(incidentDt);
	}

    public int getObservedId() {
        return observedId;
    }

    public void setObservedId(int observedId) {
        this.observedId = observedId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getIntensityId() {
        return intensityId;
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

    public String getDurationStr() {
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

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
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
        default:
        }
    }
    
    private String getABCString(List<Abc> list) {
		StringBuilder desc = new StringBuilder();
		
		for (Abc abc : list ) {
			if ( desc.length() > 0 )
			{
				desc.append(ABC_DELIMITER);
			}
			desc.append(abc.getTypeValue());
		}
		
		return desc.toString();
	}
	
	public String getAntecedentStr() {
		return getABCString(getAntecedents());
	}
	
	public String getBehaviorStr() {
		return getABCString(getBehaviors());
	}
	
	public String getConsequenceStr() {
		return getABCString(getConsequences());
	}
    
}