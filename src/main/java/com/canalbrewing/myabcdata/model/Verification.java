package com.canalbrewing.myabcdata.model;

import java.util.Date;

public class Verification {
    public static final String REGISTER = "REGISTER";
    public static final String REASSIGN = "REASSIGN";
    public static final String PASSWORD_RESET = "PASSWORD_RESET";

    private String verificationKey;
    private Date expirationDt;
    private String verificationType;
    private int verifiedUserId;
    private int requestingUserId;
    private int observedId;

    private String observedNm;

    public String getVerificationKey() {
        return verificationKey;
    }

    public void setVerificationKey(String verificationKey) {
        this.verificationKey = verificationKey;
    }

    public Date getExpirationDt() {
        return expirationDt;
    }

    public void setExpirationDt(Date expirationDt) {
        this.expirationDt = expirationDt;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public int getVerifiedUserId() {
        return verifiedUserId;
    }

    public void setVerifiedUserId(int verifiedUserId) {
        this.verifiedUserId = verifiedUserId;
    }

    public int getRequestingUserId() {
        return requestingUserId;
    }

    public void setRequestingUserId(int requestingUserId) {
        this.requestingUserId = requestingUserId;
    }

    public int getObservedId() {
        return observedId;
    }

    public void setObservedId(int observedId) {
        this.observedId = observedId;
    }

    public String getObservedNm() {
        return observedNm;
    }

    public void setObservedNm(String observedNm) {
        this.observedNm = observedNm;
    }

}
