package com.canalbrewing.myabcdata.model;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class User {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_PENDING = "PENDING";

    private int id;
    private String userNm;
    private String password;
    private String email;
    private byte[] salt;
    private byte[] encryptedPassword;
    private String startPage;

    private String status;

    private String sessionKey;

    private List<Observed> observed = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(byte[] encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", encryptedPassword=" + Arrays.toString(encryptedPassword) + ", id=" + id
                + ", password=" + password + ", status=" + status + ", salt=" + Arrays.toString(salt) + ", sessionKey="
                + sessionKey + ", startPage=" + startPage + ", userNm=" + userNm + "]";
    }

    public List<Observed> getObserved() {
        return observed;
    }

    public void setObserved(List<Observed> observed) {
        this.observed = observed;
    }

}