# US08 - Validate Declaration

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US08. They focus on the `ValidateDeclarationController`, DTO mapping, `DeclarationRepository`, `EthicsCommitteeMember`, `Declaration`, `Validation`, `ValidationVerdict`, `DeclarationStatus` and `Annotation` responsibilities.

### Acceptance Criteria Coverage

| Acceptance Criterion                                                                                         | Covered by    |
|--------------------------------------------------------------------------------------------------------------|---------------|
| AC1 - Only members of the Ethics Committee can validate Declarations of Interests.                           | Tests 8 and 9 |
| AC2 - Only Declarations of Interests in the `SUBMITTED` status can be validated.                             | Tests 1 and 10 |
| AC3 - When correct, the Declaration of Interests is validated.                                               | Tests 3 and 4 |
| AC4 - When incorrect, the section/item with inconsistencies must be commented.                               | Tests 5 and 6 |
| AC5 - Rejection `Annotation` must have a mandatory `targetReference`.                                        | Tests 5 and 6 |
| AC6 - When incorrect, the Declaration of Interests is rejected and remains in the `REJECTED` status.         | Test 7        |

**Test 1:** Check that only submitted declarations are listed for validation.

This test verifies that the Ethics Committee Member is only presented with declarations that can still be validated. Declarations already validated or rejected should not be part of the submitted declarations list.

```java
@Test
public void ensureOnlySubmittedDeclarationsAreListed() {
    List<SubmittedDeclarationDTO> declarations = controller.getSubmittedDeclarations();

    for (SubmittedDeclarationDTO declaration : declarations) {
        assertEquals(DeclarationStatus.SUBMITTED, declaration.status);
    }
}
```

**Test 2:** Check that a submitted declaration can be selected and its details consulted.

This test validates the consultation step before the decision. The selected declaration must be kept by the controller so that the Ethics Committee Member can review its subsidies, assets, positions and security holdings.

```java
@Test
public void ensureSelectedDeclarationDetailsCanBeConsulted() {
    Declaration declaration = createSubmittedDeclaration();

    DeclarationDetailsDTO details = controller.selectDeclaration(declaration.getId());

    assertEquals(declaration.getId(), details.declarationId);
    assertNotNull(details.subsidies);
    assertNotNull(details.assets);
    assertNotNull(details.positions);
    assertNotNull(details.securityHoldings);
}
```

**Test 3:** Check that approving a correct declaration changes its status to `VALIDATED` - AC1.

This test verifies the expected result when the declaration is considered correct. The declaration lifecycle status must be updated from `SUBMITTED` to `VALIDATED`.

```java
@Test
public void ensureApprovingCorrectDeclarationChangesStatusToValidated() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration.getId());

    controller.approveDeclaration(new ValidationDecisionDTO(ValidationVerdict.APPROVED, null, null));

    assertEquals(DeclarationStatus.VALIDATED, declaration.getStatus());
}
```

**Test 4:** Check that approving a correct declaration creates a `Validation` with verdict `APPROVED` - AC1.

This test confirms that validation is recorded as a domain act, not only as a status change. The `Validation` object must store the decision and the validation date.

```java
@Test
public void ensureApprovingDeclarationCreatesApprovedValidation() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration.getId());

    ValidationResultDTO result = controller.approveDeclaration(new ValidationDecisionDTO(ValidationVerdict.APPROVED, null, null));

    assertNotNull(result);
    assertEquals(ValidationVerdict.APPROVED, result.verdict);
    assertEquals(DeclarationStatus.VALIDATED, result.status);
}
```

**Test 5:** Check that rejecting an incorrect declaration without target section/item and comments is not allowed - AC2.

This test enforces the requirement that inconsistencies must be commented and associated with the affected section/item. A rejection without this information must therefore be invalid.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureRejectingDeclarationRequiresComments() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration.getId());

    controller.rejectDeclaration(new ValidationDecisionDTO(ValidationVerdict.REJECTED, null, null));
}
```

**Test 6:** Check that rejecting an incorrect declaration creates an `Annotation` with the inconsistency comments - AC2.

This test verifies that the comment is recorded inside the validation and that the target section/item is identified explicitly.

```java
@Test
public void ensureRejectingDeclarationCreatesAnnotationWithComments() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration.getId());

    controller.rejectDeclaration(new ValidationDecisionDTO(
        ValidationVerdict.REJECTED, "Asset", "Asset item has inconsistent market value."));

    Validation validation = ethicsCommitteeMember.getLastValidation();

    assertFalse(validation.getAnnotations().isEmpty());
    assertEquals("Asset", validation.getAnnotations().get(0).getTargetReference());
    assertEquals("Asset item has inconsistent market value.", validation.getAnnotations().get(0).getText());
}
```

**Test 7:** Check that rejecting an incorrect declaration changes and keeps its status as `REJECTED` - AC3.

This test validates the lifecycle rule clarified by the client: a rejected declaration remains rejected. If the Political Agent needs to correct it, a new extraordinary declaration must be submitted instead.

```java
@Test
public void ensureRejectedDeclarationRemainsRejected() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration.getId());

    controller.rejectDeclaration(new ValidationDecisionDTO(
        ValidationVerdict.REJECTED, "Position", "Position remuneration is inconsistent."));

    assertEquals(DeclarationStatus.REJECTED, declaration.getStatus());
}
```

**Test 8:** Check that the validation is associated with the Ethics Committee Member who performed it.

This test verifies the responsibility identified in the domain model: an `EthicsCommitteeMember` performs and keeps validations. It also supports traceability of validation decisions.

```java
@Test
public void ensureValidationIsAssociatedWithEthicsCommitteeMember() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration.getId());

    controller.approveDeclaration(new ValidationDecisionDTO(ValidationVerdict.APPROVED, null, null));
    Validation validation = ethicsCommitteeMember.getLastValidation();

    assertTrue(ethicsCommitteeMember.getValidations().contains(validation));
}
```

**Test 9:** Check that a non-Ethics Committee user cannot validate declarations - AC1.

This test verifies the actor restriction explicitly stated in the acceptance criteria.

```java
@Test(expected = IllegalStateException.class)
public void ensureOnlyEthicsCommitteeMembersCanValidateDeclarations() {
    authenticateAsCitizen();
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration.getId());

    controller.approveDeclaration(new ValidationDecisionDTO(ValidationVerdict.APPROVED, null, null));
}
```

**Test 10:** Check that a declaration not in `SUBMITTED` status cannot be validated - AC2.

This test enforces the lifecycle precondition: already validated or rejected declarations cannot be validated again.

```java
@Test(expected = IllegalStateException.class)
public void ensureOnlySubmittedDeclarationsCanBeValidated() {
    Declaration declaration = createValidatedDeclaration();
    controller.selectDeclaration(declaration.getId());

    controller.approveDeclaration(new ValidationDecisionDTO(ValidationVerdict.APPROVED, null, null));
}
```

**Test 11:** Check that submitted declarations are returned to the UI as DTOs.

This test verifies the DTO boundary introduced in the design.

```java
@Test
public void ensureSubmittedDeclarationsAreReturnedAsDTOs() {
    List<SubmittedDeclarationDTO> declarations = controller.getSubmittedDeclarations();

    assertFalse(declarations.isEmpty());
    assertTrue(declarations.get(0) instanceof SubmittedDeclarationDTO);
}
```


## 5. Construction (Implementation)

The implementation should follow the design responsibilities:

* `ValidateDeclarationController` coordinates the selected declaration and obtains the current Ethics Committee Member from the session.
* `SubmittedDeclarationDTO`, `DeclarationDetailsDTO`, `ValidationDecisionDTO` and `ValidationResultDTO` are used at the UI/controller boundary.
* `DeclarationMapper` converts domain objects and validation results into DTOs.
* `DeclarationRepository` provides declarations with status `SUBMITTED`.
* `EthicsCommitteeMember.validateDeclaration(declaration, verdict, targetReference, commentText)` creates and stores the `Validation`.
* `Declaration.approve()` changes the status to `VALIDATED`.
* `Declaration.reject()` changes the status to `REJECTED`.
* `Validation.addAnnotation(commentText, targetReference, declaration)` records comments when inconsistencies are found.


## 6. Integration and Demo

For demonstration, the Ethics Committee Member should be able to list submitted declarations, inspect one declaration and approve it when correct or reject it with comments when inconsistencies are found.


## 7. Observations

The analysis represents the section/item with inconsistencies through `Annotation.targetReference` instead of a direct association to `Subsidy`, `Asset`, `Position` or `SecurityHolding`. The rejection tests should therefore verify that both the target reference and the comment text are mandatory and persisted.
