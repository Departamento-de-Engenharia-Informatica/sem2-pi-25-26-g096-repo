# US01 - User Registration Request

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US01. They focus on the `RegistrationRequest`, `RequestedRole`, `RequestStatus`, DTO mapping, `RequestRegistrationController` and `RegistrationRequestRepository` responsibilities identified in the design.

### Acceptance Criteria Coverage

| Acceptance Criterion                                                                                         | Covered by       |
|--------------------------------------------------------------------------------------------------------------|------------------|
| AC1 - The role must be selected from a predefined list of available roles.                                   | Test 1           |
| AC2 - The system must request the mandatory details according to the selected role.                          | Tests 3, 4 and 5 |
| AC3 - Ordinary Citizen must provide national identity card number.                                           | Test 3           |
| AC4 - Journalist must provide press card number.                                                             | Test 4           |
| AC5 - Political Agent must provide national identity card number, elected position and institution.          | Test 5           |
| AC6 - Political Agent may select existing records or provide new elected position and institution.           | Test 6           |
| AC7 - The registration request remains pending until an Administrator accepts or rejects it.                 | Tests 2 and 7    |
| AC8 - Password must have seven alphanumeric characters, at least three uppercase letters and two digits.      | Test 8           |

**Test 1:** Check that the available roles are obtained from the predefined `RequestedRole` list - AC1.

This test confirms that the registration process does not allow free-text roles. The future user must choose one of the roles defined by the system, as required by the requirements and by the `RequestedRole` enumeration in the domain model.

```java
@Test
public void ensureOnlyPredefinedRolesAreAvailable() {
    RequestedRole[] roles = controller.getRequestedRoles();

    assertTrue(Arrays.asList(roles).contains(RequestedRole.CITIZEN));
    assertTrue(Arrays.asList(roles).contains(RequestedRole.JOURNALIST));
    assertTrue(Arrays.asList(roles).contains(RequestedRole.POLITICAL_AGENT));
    assertTrue(Arrays.asList(roles).contains(RequestedRole.ETHICS_COMMITTEE_MEMBER));
}
```

**Test 2:** Check that a valid registration request is created with initial status `PENDING`.

This test validates the main outcome of US01: the system creates a registration request, not an active user account. The request must remain pending until an Administrator handles it in US02.

```java
@Test
public void ensureRegistrationRequestIsCreatedWithPendingStatus() {
    RegistrationRequestResultDTO result = controller.createRegistrationRequest(new RegistrationRequestDTO(
        "Future User", "123456789", "future.user@email.com", "ABC12de",
        RequestedRole.CITIZEN, "12345678", null, null, null));

    assertNotNull(result);
    assertEquals(RequestStatus.PENDING, result.status);
}
```

**Test 3:** Check that an Ordinary Citizen registration request requires the national identity card number - AC2 and AC3.

This test verifies the role-specific mandatory data for the Ordinary Citizen role. If the national identity card number is missing, the request is invalid and must not be created.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureCitizenRequiresNationalIdentityCardNumber() {
    controller.createRegistrationRequest(new RegistrationRequestDTO(
        "Future Citizen", "123456789", "citizen@email.com", "ABC12de",
        RequestedRole.CITIZEN, null, null, null, null));
}
```

**Test 4:** Check that a Journalist registration request requires the press card number - AC2 and AC4.

This test verifies that the system asks for, and validates, the mandatory detail associated with the Journalist role. A journalist request without press card number must be rejected.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureJournalistRequiresPressCardNumber() {
    controller.createRegistrationRequest(new RegistrationRequestDTO(
        "Future Journalist", "123456789", "journalist@email.com", "ABC12de",
        RequestedRole.JOURNALIST, null, null, null, null));
}
```

**Test 5:** Check that a Political Agent registration request requires national identity card number, elected position and institution - AC2 and AC5.

This test validates the complete mandatory data set for a Political Agent. The request must fail if any of the required political-agent-specific fields is missing.

```java
@Test(expected = IllegalArgumentException.class)
public void ensurePoliticalAgentRequiresAllMandatoryDetails() {
    controller.createRegistrationRequest(new RegistrationRequestDTO(
        "Future Political Agent", "123456789", "agent@email.com", "ABC12de",
        RequestedRole.POLITICAL_AGENT, "12345678", null, "Councillor", null));
}
```

**Test 6:** Check that a Political Agent can use existing or newly provided elected position and institution - AC6.

This test ensures that the request accepts the two alternatives stated by the client: selecting already registered records or providing new values when the records do not yet exist.

```java
@Test
public void ensurePoliticalAgentCanUseExistingOrNewPositionAndInstitution() {
    RegistrationRequestResultDTO existingDataRequest = controller.createRegistrationRequest(new RegistrationRequestDTO(
        "Political Agent One", "123456789", "agent.one@email.com", "ABC12de",
        RequestedRole.POLITICAL_AGENT, "12345678", null,
        "Councillor", "Municipal Council"));

    RegistrationRequestResultDTO newDataRequest = controller.createRegistrationRequest(new RegistrationRequestDTO(
        "Political Agent Two", "987654321", "agent.two@email.com", "ABC12de",
        RequestedRole.POLITICAL_AGENT, "87654321", null,
        "New Elected Position", "New Institution"));

    assertNotNull(existingDataRequest);
    assertNotNull(newDataRequest);
}
```

**Test 7:** Check that a valid registration request is persisted in the repository and remains pending - AC7.

This test verifies the responsibility assigned to `RegistrationRequestRepository` in the design: after successful validation, the request must be stored so that it can later be reviewed in US02.

```java
@Test
public void ensureRegistrationRequestIsPersistedAfterCreation() {
    RegistrationRequestResultDTO result = controller.createRegistrationRequest(new RegistrationRequestDTO(
        "Future User", "123456789", "future.user@email.com", "ABC12de",
        RequestedRole.CITIZEN, "12345678", null, null, null));

    RegistrationRequest request = registrationRequestRepository.getById(result.requestId);

    assertNotNull(request);
    assertEquals(RequestStatus.PENDING, request.getStatus());
}
```

**Test 8:** Check that the password policy is enforced - AC8.

This test verifies the explicit password rule: seven alphanumeric characters, including at least three uppercase letters and two digits.

```java
@Test(expected = IllegalArgumentException.class)
public void ensurePasswordMustFollowRequiredFormat() {
    controller.createRegistrationRequest(new RegistrationRequestDTO(
        "Future User", "123456789", "future.user@email.com", "AB12def",
        RequestedRole.CITIZEN, "12345678", null, null, null));
}
```

**Test 9:** Check that institutions are returned to the UI as DTOs.

This test confirms that the UI receives `InstitutionDTO` objects instead of domain objects when the selected role is Political Agent.

```java
@Test
public void ensureInstitutionsAreReturnedAsDTOs() {
    List<InstitutionDTO> institutions = controller.getInstitutions();

    assertFalse(institutions.isEmpty());
    assertTrue(institutions.get(0) instanceof InstitutionDTO);
}
```


## 5. Construction (Implementation)

The implementation should follow the design responsibilities:

* `RequestRegistrationController` coordinates the use case.
* `RegistrationRequestDTO` carries submitted data from the UI to the controller.
* `RequestRegistrationMapper` converts domain objects into DTOs for the UI.
* `RequestedRole` provides the predefined role list.
* `RegistrationRequest` validates common and role-specific mandatory data.
* `RegistrationRequestRepository` creates and stores valid requests with status `PENDING`.
* No active `User` account is created in this user story.


## 6. Integration and Demo

For demonstration, the registration UI should first present the available roles. After the future user selects a role, the UI should request the corresponding mandatory data and submit the request with status `PENDING`.


## 7. Observations

n/a
