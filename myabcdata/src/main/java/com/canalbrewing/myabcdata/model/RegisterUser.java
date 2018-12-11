package com.canalbrewing.myabcdata.model;

public class RegisterUser {
	
	private String userNm;
	private String password;
	private String email;
	private String startPage;
	
	private String observedNm;
	private String relationshipId;

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

	public String getObservedNm() {
		return observedNm;
	}

	public void setObservedNm(String observedNm) {
		this.observedNm = observedNm;
	}
	
	public String getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}
	
	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

		
	public User convertToUser()
	{
		User user = new User();
		
		user.setUserNm(getUserNm());
		user.setPassword(getPassword());
		user.setEmail(getEmail());
		user.setStartPage(getStartPage());
		
		if ( this.observedNm != null && this.observedNm.trim().length() > 0 )
		{
			Observed observed = new Observed();
			observed.setObservedNm(this.observedNm);
			observed.setRole(Observed.ROLE_ADMIN);
			observed.setRelationshipId(Integer.parseInt(this.relationshipId));
			observed.setAccessStatus(Observed.STATUS_ACTIVE);
			user.addObserved(observed);
		}
				
		return user;
	}

	
	

}
