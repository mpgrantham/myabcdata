package com.canalbrewing.myabcdata.model;

public class RequestIncident {

    private int id;
	private int observedId;
	private long incidentDate;
	private String incidentTime;
	private String incidentAmPm;
	private String incidentDuration;
	private int intensityId;
	private int locationId;
	private String description;
	private String antecedentIds;
	private String behaviorIds;
	private String consequenceIds;
	
    public int getId() {
		return id;
    }
    
	public void setId(int id) {
		this.id = id;
	}
	
	public int getObservedId() {
		return observedId;
	}
	public void setObservedId(int observedId) {
		this.observedId = observedId;
	}
	
		
	public long getIncidentDate() {
		return incidentDate;
	}
	public void setIncidentDate(long incidentDate) {
		this.incidentDate = incidentDate;
	}
	
	public int getIntensityId() {
		return intensityId;
	}
	public void setIntensityId(int intensityId) {
		this.intensityId = intensityId;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
    }
    
	public String getAntecedentIds() {
		return antecedentIds;
    }
    
	public void setAntecedentIds(String antecedentIds) {
		this.antecedentIds = antecedentIds;
    }
    
	public String getBehaviorIds() {
		return behaviorIds;
    }
    
	public void setBehaviorIds(String behaviorIds) {
		this.behaviorIds = behaviorIds;
    }
    
	public String getConsequenceIds() {
        return consequenceIds;
    }
    
	public void setConsequenceIds(String consequenceIds) {
		this.consequenceIds = consequenceIds;
	}
	
	public String getIncidentAmPm() {
		return incidentAmPm;
    }
    
	public void setIncidentAmPm(String incidentAmPm) {
		this.incidentAmPm = incidentAmPm;
	}
		
	public Incident convertToIncident() {
		Incident incident = new Incident();
		
		incident.setId(getId());
		
		incident.setObservedId(getObservedId());
		
		incident.setIncidentDt(new java.sql.Timestamp(getIncidentDate()));
		
		incident.setIntensityId(getIntensityId());
		incident.setLocationId(getLocationId());
		
		int min = 0;
		int sec = 0;
				
		if ( getIncidentDuration().trim().length() != 0 ) {
			String[] parts = getIncidentDuration().trim().split(":");
			if ( parts.length == 2 ) {
				min = Integer.parseInt(parts[0]);
				sec = Integer.parseInt(parts[1]);
			}
		}
		
		incident.setDuration((min * 60) + sec);
		
		incident.setDescription(getDescription());
		
		addAbcs(incident, Abc.ANTECEDENT, getAntecedentIds().split(","));
		addAbcs(incident, Abc.BEHAVIOR, getBehaviorIds().split(","));
		addAbcs(incident, Abc.CONSEQUENCE, getConsequenceIds().split(","));
				
		return incident;
	}
	
	private void addAbcs(Incident incident, String typeCd, String[] ids)
	{
		for ( String abcId : ids ) {
			Abc abc = new Abc();
			abc.setTypeCd(typeCd);
			abc.setValueId(Integer.parseInt(abcId.trim()));
			incident.addValue(abc);
		}
    }
    
	public String getIncidentTime() {
		return incidentTime;
    }
    
	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
    }
    
	public String getIncidentDuration() {
		return incidentDuration;
    }
    
	public void setIncidentDuration(String incidentDuration) {
		this.incidentDuration = incidentDuration;
	}
    
}