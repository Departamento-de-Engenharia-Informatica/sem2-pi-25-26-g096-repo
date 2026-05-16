# US02 - Approve/Reject Registration

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US02. They focus on the `RegistrationRequest`, `ApproveRejectRegistrationController`, `RegistrationRequestRepository`, `UserRepository`, `Administrator`, `Annotation` and concrete `User` creation responsibilities.

### Acceptance Criteria Coverage

| Acceptance Criterion                                                                        | Covered by    |
|---------------------------------------------------------------------------------------------|---------------|
| AC1 - The Administrator must select a pending registration request and consult its details. | Tests 1 and 2 |
| AC2 - The Administrator must accept or reject the selected registration request.            | Tests 3 and 4 |
| AC3 - The system must record timestamp and Administrator responsible for the decision.      | Test 5        |
| AC4 - Rejection requires a reason/comment.                                                  | Tests 6 and 7 |
| AC5 - Acceptance automatically creates the corresponding user account.                      | Test 8        |

**Test 1:** Check that only pending registration requests are listed - AC1.

This test ensures that the Administrator only sees requests that still need a decision. Requests already accepted or rejected must not appear in the pending list.

```java
@Test
public void ensureOnlyPendingRegistrationRequestsAreListed() {
    List<RegistrationRequest> pendingRequests = controller.getPendingRegistrationRequests();

    for (RegistrationRequest request : pendingRequests) {
        assertEquals(RequestStatus.PENDING, request.getStatus());
    }
}
```

**Test 2:** Check that the Administrator can select a pending request and consult its details - AC1.

This test verifies the selection step described in the requirements and design. After selecting a request, the controller must keep it and provide its submitted data for consultation.

```java
@Test
public void ensureSelectedRegistrationRequestDetailsCanBeConsulted() {
    RegistrationRequest request = createPendingCitizenRequest();

    controller.selectRegistrationRequest(request);

    assertEquals(request.getName(), controller.getSelectedRegistrationRequest().getName());
    assertEquals(request.getEmail(), controller.getSelectedRegistrationRequest().getEmail());
    assertEquals(request.getRequestedRole(), controller.getSelectedRegistrationRequest().getRequestedRole());
}
```

**Test 3:** Check that accepting a selected request changes its status to `ACCEPTED` - AC2.

This test validates one possible Administrator decision. A successfully accepted request must leave the pending state and become accepted.

```java
@Test
public void ensureAcceptingRegistrationRequestChangesStatusToAccepted() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request);

    controller.acceptRegistrationRequest();

    assertEquals(RequestStatus.ACCEPTED, request.getStatus());
}
```

**Test 4:** Check that rejecting a selected request changes its status to `REJECTED` - AC2.

This test validates the other possible Administrator decision. A rejected request must not remain pending and must not be accepted later without a new process.

```java
@Test
public void ensureRejectingRegistrationRequestChangesStatusToRejected() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request);

    controller.rejectRegistrationRequest("Identity document could not be validated.");

    assertEquals(RequestStatus.REJECTED, request.getStatus());
}
```

**Test 5:** Check that accepting or rejecting records the decision timestamp and Administrator - AC3.

This test verifies auditability. Every decision must record when it happened and which Administrator made it, as required by the client clarification.

```java
@Test
public void ensureDecisionRecordsTimestampAndAdministrator() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request);

    controller.acceptRegistrationRequest();

    assertNotNull(request.getDecisionDate());
    assertNotNull(request.getReviewedBy());
    assertEquals(administrator, request.getReviewedBy());
}
```

**Test 6:** Check that rejection without a reason/comment is not allowed - AC4.

This test enforces the mandatory rejection comment. The system must reject an attempt to reject a registration request without explaining the decision.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureRejectionReasonIsMandatory() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request);

    controller.rejectRegistrationRequest(null);
}
```

**Test 7:** Check that rejecting with a reason creates a rejection `Annotation` - AC4.

This test confirms that the rejection reason is not only validated but also recorded in the domain, using the `Annotation` class identified in the analysis and design.

```java
@Test
public void ensureRejectingRegistrationRequestCreatesAnnotation() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request);

    Annotation annotation = controller.rejectRegistrationRequest("Missing mandatory evidence.");

    assertNotNull(annotation);
    assertEquals("Missing mandatory evidence.", annotation.getText());
}
```

**Test 8:** Check that accepting a request automatically creates the corresponding active user account - AC5.

This test validates the main effect of accepting a registration request. The created user must match the requested role and must be persisted in the `UserRepository`.

```java
@Test
public void ensureAcceptingRegistrationRequestCreatesCorrespondingUser() {
    RegistrationRequest request = createPendingJournalistRequest();
    controller.selectRegistrationRequest(request);

    User user = controller.acceptRegistrationRequest();

    assertNotNull(user);
    assertTrue(user instanceof Journalist);
    assertTrue(userRepository.getUsers().contains(user));
}
```


## 5. Construction (Implementation)

The implementation should follow the design responsibilities:

* `ApproveRejectRegistrationController` coordinates the selected request and obtains the current Administrator from the session.
* `RegistrationRequestRepository` provides only pending requests.
* `RegistrationRequest.accept(administrator)` records the decision, changes the status and creates the corresponding `User`.
* `RegistrationRequest.reject(administrator, rejectionReason)` records the decision, changes the status and creates the rejection `Annotation`.
* `UserRepository` stores the created account when a request is accepted.


## 6. Integration and Demo

For demonstration, the Administrator should be able to list pending requests, open one request to consult its details, and then accept or reject it. Acceptance should create the account automatically; rejection should ask for and store a reason.


## 7. Observations

n/a
