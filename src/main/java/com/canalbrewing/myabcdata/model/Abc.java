package com.canalbrewing.myabcdata.model;

import com.canalbrewing.myabcdata.resultsetmapper.annotation.DbColumn;

public class Abc {
    public static final String VALUE_ID = "value_id";
    public static final String TYPE_CD = "type_cd";
    public static final String TYPE_VALUE = "type_value";

    public static final String ANTECEDENT = "A";
    public static final String BEHAVIOR = "B";
    public static final String CONSEQUENCE = "C";
    public static final String LOCATION = "L";

    @DbColumn(name = "value_id")
    private int valueId;

    @DbColumn(name = "type_cd")
    private String typeCd;

    @DbColumn(name = "type_value")
    private String typeValue;

    private int selected;
    private Boolean selectedFlag = false;
    private String activeFl;

    public Abc() {

    }

    public Abc(int valueId, String typeValue) {
        this.valueId = valueId;
        this.typeValue = typeValue;
    }

    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
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

    public String getActiveFl() {
        return activeFl;
    }

    public void setActiveFl(String activeFl) {
        this.activeFl = activeFl;
    }

    @Override
    public String toString() {
        return "Abc [activeFl=" + activeFl + ", selected=" + selected + ", selectedFlag=" + selectedFlag + ", typeCd="
                + typeCd + ", typeValue=" + typeValue + ", valueId=" + valueId + "]";
    }

}