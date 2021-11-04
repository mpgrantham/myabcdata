package com.canalbrewing.myabcdata.model;

public class RequestUser {

    private String observedId;

    private String username;
    private String password;
    private String currentPassword;
    private String email;
    private String startPage;

    private String observedName;
    private String relationship;
    private String role;

    private String key;
    private String userId;

    public String getObservedId() {
        return observedId;
    }

    public void setObservedId(String observedId) {
        this.observedId = observedId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public String getObservedName() {
        return observedName;
    }

    public void setObservedName(String observedName) {
        this.observedName = observedName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User convertToUser() {
        User user = new User();

        user.setUserNm(getUsername());
        user.setPassword(getPassword());
        user.setEmail(getEmail());
        user.setStartPage(getStartPage());

        if (this.observedName != null && this.observedName.trim().length() > 0) {
            Observed observed = new Observed();
            observed.setObservedNm(this.observedName);
            observed.setRole(Observed.ROLE_ADMIN);
            observed.setRelationshipId(Integer.parseInt(this.relationship));
            observed.setAccessStatus(Observed.STATUS_ACTIVE);
            user.getObserved().add(observed);
        }

        return user;
    }

    @Override
    public String toString() {
        return "RequestUser [email=" + email + ", observedName=" + observedName + ", password=" + password
                + ", relationship=" + relationship + ", startPage=" + startPage + ", username=" + username + "]";
    }

}