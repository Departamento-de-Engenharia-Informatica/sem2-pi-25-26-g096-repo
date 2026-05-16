# US08 - Validate Declaration

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US08. They focus on the `ValidateDeclarationController`, `DeclarationRepository`, `EthicsCommitteeMember`, `Declaration`, `Validation`, `ValidationVerdict`, `DeclarationStatus` and `Annotation` responsibilities.

### Acceptance Criteria Coverage

| Acceptance Criterion                                                                | Covered by    |
|-------------------------------------------------------------------------------------|---------------|
| AC1 - When correct, the declaration is validated.                                   | Tests 3 and 4 |
| AC2 - When incorrect, the section/item with inconsistencies must be commented.      | Tests 5 and 6 |
| AC3 - When incorrect, the declaration is rejected and remains in `REJECTED` status. | Test 7        |

**Test 1:** Check that only submitted declarations are listed for validation.

This test verifies that the Ethics Committee Member is only presented with declarations that can still be validated. Declarations already validated or rejected should not be part of the submitted declarations list.

```java
@Test
public void ensureOnlySubmittedDeclarationsAreListed() {
    List<Declaration> declarations = controller.getSubmittedDeclarations();

    for (Declaration declaration : declarations) {
        assertEquals(DeclarationStatus.SUBMITTED, declaration.getStatus());
    }
}
```

**Test 2:** Check that a submitted declaration can be selected and its details consulted.

This test validates the consultation step before the decision. The selected declaration must be kept by the controller so that the Ethics Committee Member can review its subsidies, assets, positions and security holdings.

```java
@Test
public void ensureSelectedDeclarationDetailsCanBeConsulted() {
    Declaration declaration = createSubmittedDeclaration();

    controller.selectDeclaration(declaration);

    Declaration selectedDeclaration = controller.getSelectedDeclaration();

    assertEquals(declaration, selectedDeclaration);
    assertNotNull(selectedDeclaration.getSubsidies());
    assertNotNull(selectedDeclaration.getAssets());
    assertNotNull(selectedDeclaration.getPositions());
    assertNotNull(selectedDeclaration.getSecurityHoldings());
}
```

**Test 3:** Check that approving a correct declaration changes its status to `VALIDATED` - AC1.

This test verifies the expected result when the declaration is considered correct. The declaration lifecycle status must be updated from `SUBMITTED` to `VALIDATED`.

```java
@Test
public void ensureApprovingCorrectDeclarationChangesStatusToValidated() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration);

    controller.approveDeclaration();

    assertEquals(DeclarationStatus.VALIDATED, declaration.getStatus());
}
```

**Test 4:** Check that approving a correct declaration creates a `Validation` with verdict `APPROVED` - AC1.

This test confirms that validation is recorded as a domain act, not only as a status change. The `Validation` object must store the decision and the validation date.

```java
@Test
public void ensureApprovingDeclarationCreatesApprovedValidation() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration);

    Validation validation = controller.approveDeclaration();

    assertNotNull(validation);
    assertEquals(ValidationVerdict.APPROVED, validation.getVerdict());
    assertNotNull(validation.getValidationDate());
}
```

**Test 5:** Check that rejecting an incorrect declaration without target section/item and comments is not allowed - AC2.

This test enforces the requirement that inconsistencies must be commented and associated with the affected section/item. A rejection without this information must therefore be invalid.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureRejectingDeclarationRequiresComments() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration);

    controller.rejectDeclaration(null, null);
}
```

**Test 6:** Check that rejecting an incorrect declaration creates an `Annotation` with the inconsistency comments - AC2.

This test verifies that the comment is recorded inside the validation and that the target section/item is identified explicitly.

```java
@Test
public void ensureRejectingDeclarationCreatesAnnotationWithComments() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration);

    Validation validation = controller.rejectDeclaration("Asset", "Asset item has inconsistent market value.");

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
    controller.selectDeclaration(declaration);

    controller.rejectDeclaration("Position", "Position remuneration is inconsistent.");

    assertEquals(DeclarationStatus.REJECTED, declaration.getStatus());
}
```

**Test 8:** Check that the validation is associated with the Ethics Committee Member who performed it.

This test verifies the responsibility identified in the domain model: an `EthicsCommitteeMember` performs and keeps validations. It also supports traceability of validation decisions.

```java
@Test
public void ensureValidationIsAssociatedWithEthicsCommitteeMember() {
    Declaration declaration = createSubmittedDeclaration();
    controller.selectDeclaration(declaration);

    Validation validation = controller.approveDeclaration();

    assertTrue(ethicsCommitteeMember.getValidations().contains(validation));
}
```


## 5. Construction (Implementation)

The implementation should follow the design responsibilities:

* `ValidateDeclarationController` coordinates the selected declaration and obtains the current Ethics Committee Member from the session.
* `DeclarationRepository` provides declarations with status `SUBMITTED`.
* `EthicsCommitteeMember.validateDeclaration(declaration, verdict, targetReference, commentText)` creates and stores the `Validation`.
* `Declaration.approve()` changes the status to `VALIDATED`.
* `Declaration.reject()` changes the status to `REJECTED`.
* `Validation.addAnnotation(commentText, targetReference, declaration)` records comments when inconsistencies are found.


## 6. Integration and Demo

For demonstration, the Ethics Committee Member should be able to list submitted declarations, inspect one declaration and approve it when correct or reject it with comments when inconsistencies are found.


## 7. Observations

The analysis represents the section/item with inconsistencies through `Annotation.targetReference` instead of a direct association to `Subsidy`, `Asset`, `Position` or `SecurityHolding`. The rejection tests should therefore verify that both the target reference and the comment text are mandatory and persisted.
