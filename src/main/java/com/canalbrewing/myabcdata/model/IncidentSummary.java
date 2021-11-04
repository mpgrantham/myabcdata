package com.canalbrewing.myabcdata.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IncidentSummary {

    private static final SimpleDateFormat DATETIME = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	
	private Date latestIncidentDt;
	
	private int totalIncidentCount = 0;
	
	private int last7IncidentCount = 0;
	
    private int last30IncidentCount = 0;

    public String getLatestIncidentDtStr() {
		return latestIncidentDt == null ? "" : DATETIME.format(latestIncidentDt);
	}

    public Date getLatestIncidentDt() {
        return latestIncidentDt;
    }

    public void setLatestIncidentDt(Date latestIncidentDt) {
        this.latestIncidentDt = latestIncidentDt;
    }

    public int getTotalIncidentCount() {
        return totalIncidentCount;
    }

    public void setTotalIncidentCount(int totalIncidentCount) {
        this.totalIncidentCount = totalIncidentCount;
    }

    public int getLast7IncidentCount() {
        return last7IncidentCount;
    }

    public void setLast7IncidentCount(int last7IncidentCount) {
        this.last7IncidentCount = last7IncidentCount;
    }

    public int getLast30IncidentCount() {
        return last30IncidentCount;
    }

    public void setLast30IncidentCount(int last30IncidentCount) {
        this.last30IncidentCount = last30IncidentCount;
    }    
        
}