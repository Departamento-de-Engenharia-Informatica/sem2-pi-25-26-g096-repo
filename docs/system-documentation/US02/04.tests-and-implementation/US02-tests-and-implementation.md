# US02 - Approve/Reject Registration

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and updated design model of US02. They cover registration decision behaviour, DTO mapping, decision notification, and the email provider variation point handled through the Adapter Pattern and Java Reflection.

### Acceptance Criteria Coverage

| Acceptance Criterion | Covered by |
|---|---|
| AC1 - The Administrator must select a pending registration request and consult its details. | Tests 1 and 2 |
| AC2 - The Administrator must accept or reject the selected registration request. | Tests 3 and 4 |
| AC3 - The system must record timestamp and Administrator responsible for the decision. | Test 5 |
| AC4 - Rejection requires a reason/comment. | Tests 6 and 7 |
| AC5 - Acceptance automatically creates the corresponding user account. | Test 8 |
| AC6 - A notification message must be sent to the user informing them of the decision. | Tests 9 and 10 |
| AC7 - The system must support multiple email providers configured through a configuration file. | Tests 11 and 12 |

**Test 1:** Check that only pending registration requests are listed and returned as DTOs - AC1.

This test ensures that the UI receives `PendingRegistrationRequestDTO` objects instead of domain objects. It reduces coupling between UI and domain, following the DTO guidance from the slides.

```java
@Test
public void ensureOnlyPendingRegistrationRequestsAreListedAsDTOs() {
    List<PendingRegistrationRequestDTO> pendingRequests = controller.getPendingRegistrationRequests();

    assertFalse(pendingRequests.isEmpty());
    assertTrue(pendingRequests.get(0) instanceof PendingRegistrationRequestDTO);
}
```

**Test 2:** Check that the Administrator can select a pending request and consult its details as a DTO - AC1.

This test verifies that the selected request details are exposed through `RegistrationRequestDetailsDTO`, keeping the UI decoupled from `RegistrationRequest`.

```java
@Test
public void ensureSelectedRegistrationRequestDetailsCanBeConsultedAsDTO() {
    RegistrationRequest request = createPendingCitizenRequest();

    RegistrationRequestDetailsDTO details = controller.selectRegistrationRequest(request.getId());

    assertEquals(request.getName(), details.name);
    assertEquals(request.getEmail(), details.email);
    assertEquals(request.getRequestedRole(), details.requestedRole);
}
```

**Test 3:** Check that accepting a selected request changes its status to `ACCEPTED` - AC2.

This test validates one possible Administrator decision. A successfully accepted request must leave the pending state and become accepted.

```java
@Test
public void ensureAcceptingRegistrationRequestChangesStatusToAccepted() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request.getId());

    controller.acceptRegistrationRequest(new RegistrationDecisionDTO(true, null));

    assertEquals(RequestStatus.ACCEPTED, request.getStatus());
}
```

**Test 4:** Check that rejecting a selected request changes its status to `REJECTED` - AC2.

This test validates the other possible Administrator decision. A rejected request must not remain pending.

```java
@Test
public void ensureRejectingRegistrationRequestChangesStatusToRejected() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request.getId());

    controller.rejectRegistrationRequest(new RegistrationDecisionDTO(false, "Identity document could not be validated."));

    assertEquals(RequestStatus.REJECTED, request.getStatus());
}
```

**Test 5:** Check that accepting or rejecting records the decision timestamp and Administrator - AC3.

This test verifies auditability. Every decision must record when it happened and which Administrator made it.

```java
@Test
public void ensureDecisionRecordsTimestampAndAdministrator() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request.getId());

    controller.acceptRegistrationRequest(new RegistrationDecisionDTO(true, null));

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
    controller.selectRegistrationRequest(request.getId());

    controller.rejectRegistrationRequest(new RegistrationDecisionDTO(false, null));
}
```

**Test 7:** Check that rejecting with a reason creates a rejection `Annotation` - AC4.

This test confirms that the rejection reason is recorded in the domain using the `Annotation` class identified in the analysis and design.

```java
@Test
public void ensureRejectingRegistrationRequestCreatesAnnotation() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request.getId());

    controller.rejectRegistrationRequest(new RegistrationDecisionDTO(false, "Missing mandatory evidence."));

    assertNotNull(request.getRejectionAnnotation());
    assertEquals("Missing mandatory evidence.", request.getRejectionAnnotation().getText());
}
```

**Test 8:** Check that accepting a request automatically creates the corresponding active user account - AC5.

This test validates the main effect of accepting a registration request. The created user must match the requested role and must be persisted in `UserRepository`.

```java
@Test
public void ensureAcceptingRegistrationRequestCreatesCorrespondingUser() {
    RegistrationRequest request = createPendingJournalistRequest();
    controller.selectRegistrationRequest(request.getId());

    controller.acceptRegistrationRequest(new RegistrationDecisionDTO(true, null));

    assertNotNull(request.getCreatedUser());
    assertTrue(request.getCreatedUser() instanceof Journalist);
    assertTrue(userRepository.getUsers().contains(request.getCreatedUser()));
}
```

**Test 9:** Check that an acceptance notification is sent to the requester - AC6.

This test verifies that after an accepted decision, the controller builds a `RegistrationDecisionNotificationDTO` and sends it using the stable `EmailService` interface.

```java
@Test
public void ensureAcceptanceNotificationIsSent() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request.getId());

    controller.acceptRegistrationRequest(new RegistrationDecisionDTO(true, null));

    verify(emailService).sendRegistrationDecisionNotification(any(RegistrationDecisionNotificationDTO.class));
}
```

**Test 10:** Check that a rejection notification is sent with the rejection reason - AC6.

This test ensures that the requester is informed of the rejection and that the notification message includes the rejection reason recorded in the `Annotation`.

```java
@Test
public void ensureRejectionNotificationContainsReason() {
    RegistrationRequest request = createPendingCitizenRequest();
    controller.selectRegistrationRequest(request.getId());

    controller.rejectRegistrationRequest(new RegistrationDecisionDTO(false, "Invalid press card."));

    Annotation annotation = request.getRejectionAnnotation();

    verify(emailService).sendRegistrationDecisionNotification(argThat(dto ->
        dto.recipientEmail.equals(request.getEmail()) && dto.message.contains(annotation.getText())));
}
```

**Test 11:** Check that the email provider is loaded from the configuration file - AC7.

This test verifies that the concrete provider is not hard-coded. The factory reads the adapter class name from `ApplicationProperties`.

```java
@Test
public void ensureConfiguredEmailServiceAdapterIsLoadedFromConfiguration() {
    when(applicationProperties.getProperty("email.service.class"))
        .thenReturn("pt.ipp.isep.dei.adapters.GmailEmailServiceAdapter");

    EmailService service = emailServiceFactory.createEmailService();

    assertTrue(service instanceof GmailEmailServiceAdapter);
}
```

**Test 12:** Check that at least two email provider adapters implement the stable `EmailService` interface - AC7.

This test confirms the required provider variability. Both Gmail and DEI adapters must be usable through the same stable interface.

```java
@Test
public void ensureMultipleEmailAdaptersImplementEmailService() {
    EmailService gmailService = new GmailEmailServiceAdapter();
    EmailService deiService = new DeiEmailServiceAdapter();

    assertTrue(gmailService instanceof EmailService);
    assertTrue(deiService instanceof EmailService);
}
```


## 5. Construction (Implementation)

The implementation should follow the updated design responsibilities:

* `ApproveRejectRegistrationController` coordinates the selected request and obtains the current Administrator from the session.
* `RegistrationRequestRepository` provides pending requests.
* `RegistrationRequestMapper` converts domain objects into DTOs for the UI and builds notification DTOs.
* `RegistrationRequest.accept(administrator)` records the decision, changes the status and creates the corresponding `User`.
* `RegistrationRequest.reject(administrator, rejectionReason)` records the decision, changes the status and creates the rejection `Annotation`.
* Rejection notifications use the created `Annotation` as the source of the reason included in the message.
* `EmailService` is the stable interface used by the controller to send notifications.
* `GmailEmailServiceAdapter` and `DeiEmailServiceAdapter` are concrete adapters for different providers.
* `EmailServiceFactory` reads `email.service.class` from the configuration file and instantiates the configured adapter using Java Reflection.


## 6. Integration and Demo

For demonstration, the Administrator should be able to list pending requests, open one request to consult its details, and then accept or reject it. After the decision, the requester should receive a notification using the email provider configured in the properties file.


## 7. Observations

The Adapter Pattern is used because the email provider is a variation point. The controller must not depend on Gmail or DEI directly; it only depends on the stable `EmailService` interface.
