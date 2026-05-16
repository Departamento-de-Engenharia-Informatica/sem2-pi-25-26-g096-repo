# US06 - Submit Declaration of Interests

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US06. They focus on the `SubmitDeclarationController`, `PoliticalAgent`, `Declaration`, `Position`, `Subsidy`, `Asset`, `Location`, `SecurityHolding`, `Institution`, `Function` and `DeclarationRepository` responsibilities.

### Acceptance Criteria Coverage

| Acceptance Criterion | Covered by |
|---|---|
| AC1 - The declaration must be associated with a registered and approved Political Agent. | Tests 1 and 2 |
| AC2 - The Political Agent must select a declaration type. | Test 3 |
| AC3 - At least one entry must be provided. | Test 4 |
| AC4 - Each position must include institution, type, function, dates and remuneration. | Test 5 |
| AC5 - Each subsidy must include the providing institution, nature and amount. | Test 6 |
| AC6 - Each asset must include type, acquisition value, market value and location. | Test 7 |
| AC7 - Each security holding must include institution, title, market value, stake and percentage. | Test 8 |
| AC8 - The declaration must be stored with a submission date. | Test 9 |
| AC9 - The system must confirm successful submission. | Test 10 |

**Test 1:** Check that the authenticated Political Agent is retrieved before submitting the declaration - AC1.

This test ensures that the declaration submission process is associated with the Political Agent currently using the system. The controller must obtain the current user from the session and use the `PoliticalAgentRepository` to retrieve the corresponding Political Agent.

```java
@Test
public void ensureAuthenticatedPoliticalAgentIsRetrievedBeforeSubmission() {
    DeclarationDTO dto = createValidDeclarationDTO();

    controller.submitDeclaration(dto);

    verify(applicationSession).getCurrentSession();
    verify(userSession).getUserId();
    verify(politicalAgentRepository).getByUserId(userId);
}
```

**Test 2:** Check that a declaration cannot be submitted without a registered and approved Political Agent - AC1.

This test verifies that the system does not allow a declaration to be submitted if the authenticated user is not found as a registered and approved Political Agent.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureDeclarationRequiresRegisteredAndApprovedPoliticalAgent() {
    when(politicalAgentRepository.getByUserId(userId)).thenReturn(null);

    controller.submitDeclaration(createValidDeclarationDTO());
}
```

**Test 3:** Check that the Political Agent must select a Declaration Type - AC2.

This test verifies that the declaration type is mandatory and must be one of the predefined values: initial, regular or exceptional.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureDeclarationTypeIsMandatory() {
    DeclarationDTO dto = createValidDeclarationDTO();
    dto.type = null;

    controller.submitDeclaration(dto);
}
```

**Test 4:** Check that at least one entry must be provided - AC3.

This test verifies that a declaration without positions, subsidies, assets or security holdings is invalid.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureAtLeastOneEntryIsRequired() {
    DeclarationDTO dto = new DeclarationDTO();
    dto.type = DeclarationType.INITIAL;
    dto.positions = new ArrayList<>();
    dto.subsidies = new ArrayList<>();
    dto.assets = new ArrayList<>();
    dto.securityHoldings = new ArrayList<>();

    controller.submitDeclaration(dto);
}
```

**Test 5:** Check that each Position includes all mandatory data - AC4.

This test verifies that a position must include institution, type, function, start date, end date when applicable, and remuneration. The entry must be validated when it is added to the declaration.

```java
@Test(expected = IllegalArgumentException.class)
public void ensurePositionRequiresMandatoryData() {
    PositionDTO positionDTO = createValidPositionDTO();
    positionDTO.functionId = null;

    DeclarationDTO dto = createDeclarationDTOWithPosition(positionDTO);

    controller.submitDeclaration(dto);
}
```

**Test 6:** Check that each Subsidy includes institution, nature and amount - AC5.

This test verifies that a subsidy cannot be submitted without the providing institution, its nature or the amount. The entry must be validated when it is added to the declaration.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureSubsidyRequiresInstitutionNatureAndAmount() {
    SubsidyDTO subsidyDTO = createValidSubsidyDTO();
    subsidyDTO.nature = null;

    DeclarationDTO dto = createDeclarationDTOWithSubsidy(subsidyDTO);

    controller.submitDeclaration(dto);
}
```

**Test 7:** Check that each Asset includes type, values and location - AC6.

This test verifies that an asset must include property type, acquisition value, market value and location with parish, county and district. The entry must be validated when it is added to the declaration.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureAssetRequiresTypeValuesAndLocation() {
    AssetDTO assetDTO = createValidAssetDTO();
    assetDTO.location = null;

    DeclarationDTO dto = createDeclarationDTOWithAsset(assetDTO);

    controller.submitDeclaration(dto);
}
```

**Test 8:** Check that each Security Holding includes all mandatory data - AC7.

This test verifies that a security holding must include institution, title, market value, stake and percentage. The entry must be validated when it is added to the declaration.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureSecurityHoldingRequiresMandatoryData() {
    SecurityHoldingDTO securityHoldingDTO = createValidSecurityHoldingDTO();
    securityHoldingDTO.institutionId = null;

    DeclarationDTO dto = createDeclarationDTOWithSecurityHolding(securityHoldingDTO);

    controller.submitDeclaration(dto);
}
```

**Test 9:** Check that the Declaration is stored with a submission date - AC8.

This test verifies that a valid declaration receives a submission date when it is successfully submitted.

```java
@Test
public void ensureDeclarationHasSubmissionDate() {
    Declaration declaration = controller.submitDeclaration(createValidDeclarationDTO());

    assertNotNull(declaration.getSubmissionDate());
}
```

**Test 10:** Check that a valid Declaration is submitted successfully - AC9.

This test verifies that a valid declaration is saved by the repository and returned by the controller with status `SUBMITTED`.

```java
@Test
public void ensureValidDeclarationIsSavedWithSubmittedStatus() {
    Declaration declaration = controller.submitDeclaration(createValidDeclarationDTO());

    assertNotNull(declaration);
    assertEquals(DeclarationStatus.SUBMITTED, declaration.getStatus());
    verify(declarationRepository).save(declaration);
}
```

## 5. Construction (Implementation)

The implementation should follow the design responsibilities:

* `SubmitDeclarationController` coordinates the use case and obtains the authenticated Political Agent from the session.
* `PoliticalAgentRepository` provides the authenticated Political Agent.
* `InstitutionRepository` provides the registered institutions used in positions, subsidies and security holdings.
* `FunctionRepository` provides the registered functions used in positions.
* `PoliticalAgent.createDeclaration(declarationType)` creates the declaration.
* `Declaration` creates and validates its declared entries: positions, subsidies, assets and security holdings.
* Each entry must be validated when it is added to the declaration, ensuring that invalid entries are rejected immediately.
* `Declaration.validateData()` performs the final global validation, including the rule that at least one entry must exist.
* `DeclarationRepository.save(declaration)` stores the submitted declaration with status `SUBMITTED`.

## 6. Integration and Demo

For demonstration, the Political Agent should be able to select a declaration type, fill at least one declaration entry and submit the declaration. A successful submission should store the declaration with a submission date and status `SUBMITTED`.

## 7. Observations

The SSD describes validation at entry level. Therefore, the implementation and design should ensure that each declared item is validated when it is added to the declaration, while the final `validateData()` operation checks global declaration rules before persistence.

The tests avoid depending on `PoliticalAgent.getDeclarations()` or `DeclarationRepository.getDeclarations()` because those methods are not part of the current class diagram. Persistence is instead verified through the `DeclarationRepository.save(declaration)` responsibility.