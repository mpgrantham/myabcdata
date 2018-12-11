package com.canalbrewing.myabcdata.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.canalbrewing.myabcdata.util.MyABCUtil;

public class IncidentEntry {
	
	private String id;
	private String obsId;
	private String dt;
	private String hr;
	private String min;
	private String amPm;
	private String durMin;
	private String durSec;
	private String intensity;
	private String location;
	private String desc;
	private String ant;
	private String beh;
	private String con;
	
	private static final DateTimeFormatter MMDDYYYY_FMT = DateTimeFormatter.ofPattern("MM/dd/yyyy h:m a");
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getObsId() {
		return obsId;
	}
	public void setObsId(String obsId) {
		this.obsId = obsId;
	}
	
		
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public String getHr() {
		return hr;
	}
	public void setHr(String hr) {
		this.hr = hr;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getDurMin() {
		return durMin;
	}
	public void setDurMin(String durMin) {
		this.durMin = durMin;
	}
	public String getDurSec() {
		return durSec;
	}
	public void setDurSec(String durSec) {
		this.durSec = durSec;
	}
	public String getIntensity() {
		return intensity;
	}
	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getAnt() {
		return ant;
	}
	public void setAnt(String ant) {
		this.ant = ant;
	}
	public String getBeh() {
		return beh;
	}
	public void setBeh(String beh) {
		this.beh = beh;
	}
	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
	}
	
	public String getAmPm() {
		return amPm;
	}
	public void setAmPm(String amPm) {
		this.amPm = amPm;
	}
		
	public Incident convertToIncident()
	{
		Incident incident = new Incident();
		
		incident.setId(MyABCUtil.atoi(getId()));
		
		incident.setObservedId(Integer.parseInt(getObsId()));
		
		String dtTm = getDt() + " " + getHr() + ":" + getMin() + " " + getAmPm();
				
		LocalDateTime localDate = LocalDateTime.parse(dtTm, MMDDYYYY_FMT);
		incident.setIncidentDt(java.sql.Timestamp.valueOf(localDate));
		
		incident.setIntensityId(Integer.parseInt(getIntensity()));
		incident.setLocationId(Integer.parseInt(getLocation()));
		
		int min = Integer.parseInt(getDurMin());
		int sec = Integer.parseInt(getDurSec());
		
		incident.setDuration((min * 60) + sec);
		incident.setDescription(getDesc());
		
		addAbcs(incident, ABC.ANTECEDENT, getAnt().split(","));
		addAbcs(incident, ABC.BEHAVIOR, getBeh().split(","));
		addAbcs(incident, ABC.CONSEQUENCE, getCon().split(","));
				
		return incident;
	}
	
	private void addAbcs(Incident incident, String typeCd, String[] ids)
	{
		for ( String id : ids )
		{
			ABC abc = new ABC();
			abc.setTypeCd(typeCd);
			abc.setValueId(Integer.parseInt(id.trim()));
			incident.addValue(abc);
		}
	}
	

}
