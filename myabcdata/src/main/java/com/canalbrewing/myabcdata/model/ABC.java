package com.canalbrewing.myabcdata.model;

import java.io.Serializable;

public class ABC implements Serializable
{
	private static final long serialVersionUID = 280591020970526872L;
	
	public static final String VALUE_ID = "value_id";
	public static final String TYPE_CD = "type_cd";
	public static final String TYPE_VALUE = "type_value";
	
	public static final String ANTECEDENT = "A";
	public static final String BEHAVIOR = "B";
	public static final String CONSEQUENCE = "C";
	public static final String LOCATION = "L";
	
	private int valueId;
	private String typeCd;
	private String typeValue;
	private int selected;
	private Boolean selectedFlag = false;
	private String activeFl;
	
	public ABC()
	{
		
	}
	
	public ABC(int valueId, String typeValue)
	{
		this.valueId = valueId;
		this.typeValue = typeValue;
	}
	
	public String getTypeCd() {
		return typeCd;
	}
	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}
	
	public String getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	public Boolean getSelectedFlag() {
		return selectedFlag;
	}
	public void setSelectedFlag(Boolean selectedFlag) {
		this.selectedFlag = selectedFlag;
	}
	public int getValueId() {
		return valueId;
	}
	public void setValueId(int valueId) {
		this.valueId = valueId;
	}
	public String getStrId() {
		return String.valueOf(valueId);
	}

	public String getActiveFl() {
		return activeFl;
	}

	public void setActiveFl(String activeFl) {
		this.activeFl = activeFl;
	}
}
