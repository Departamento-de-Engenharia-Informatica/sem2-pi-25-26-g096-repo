# US12 - Submit Complaint

## 4. Tests

**Test 1:** Check that the controller returns the available Political Agents and Roles for the complaint form.

```java
@Test
public void ensureGetReferenceDataReturnsData() {
	List<PoliticalAgent> agents = controller.getPoliticalAgents();
	List<Role> roles = controller.getRoles();

	assertFalse(agents.isEmpty());
	assertFalse(roles.isEmpty());
}
```

**Test 2:** Check that incident date validation rejects future dates.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureIncidentDateCannotBeInTheFuture() {
	controller.createComplaint(selectedAgent, selectedRole, LocalDate.now().plusDays(1), "Description");
}
```

**Test 3:** Check that the controller retrieves the Citizen from the authenticated session and blocks non-citizen users.

```java
@Test(expected = IllegalStateException.class)
public void ensureOnlyCitizensCanSubmitComplaints() {
	controller.createComplaint(selectedAgent, selectedRole, incidentDate, "Description");
}
```

**Test 4:** Check that mandate retrieval by agent, role, and date succeeds when contextual data exists.

```java
@Test
public void ensureMandateIsRetrievedByAgentRoleAndDate() {
	controller.createComplaint(selectedAgent, selectedRole, incidentDate, "Description");

	verify(politicalMandateRepository).getMandateByAgentRoleAndDate(selectedAgent, selectedRole, incidentDate);
}
```

**Test 5:** Check that the complaint is created with a unique identifier and initial status SUBMITTED.

```java
@Test
public void ensureComplaintHasUniqueIdAndSubmittedStatus() {
	Complaint complaint = controller.createComplaint(selectedAgent, selectedRole, incidentDate, "Description");

	assertNotNull(complaint.getId());
	assertEquals(ComplaintStatus.SUBMITTED, complaint.getStatus());
}
```

**Test 6:** Check that the complaint is persisted in the repository after successful creation.

```java
@Test
public void ensureComplaintIsPersistedAfterCreation() {
	controller.createComplaint(selectedAgent, selectedRole, incidentDate, "Description");

	verify(complaintRepository).save(any(Complaint.class));
}
```

_It is also recommended to organize this content by subsections._


## 5. Construction (Implementation)

### Class SubmitComplaintController

```java
public Complaint createComplaint(PoliticalAgent politicalAgent, Role role, LocalDate incidentDate, String description) {

	validateIncidentDate(incidentDate);

	Citizen citizen = getCitizenFromSession();
	PoliticalMandate mandate = getPoliticalMandateRepository()
		.getMandateByAgentRoleAndDate(politicalAgent, role, incidentDate);

	Complaint complaint = new Complaint(nextIdentity(), incidentDate, description, mandate);
	citizen.addComplaint(complaint);

	getComplaintRepository().save(complaint);

	return complaint;
}

private Citizen getCitizenFromSession() {
	UserSession currentSession = ApplicationSession.getInstance().getCurrentSession();
	String email = currentSession.getUserEmail();

	return getUserRepository().getCitizenByEmail(email);
}

private void validateIncidentDate(LocalDate incidentDate) {
	if (incidentDate.isAfter(LocalDate.now())) {
		throw new IllegalArgumentException("Incident date cannot be in the future");
	}
}
```

### Class Citizen

```java
public void addComplaint(Complaint complaint) {
	complaints.add(complaint);
}
```


## 6. Integration and Demo

* A new complaint submission option should be available in the Citizen menu.

* For demo purposes, the form should load the available Political Agents and Roles before submitting a complaint.


## 7. Observations

n/a
