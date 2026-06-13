# US06 - Submit Declaration of Interests

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US06. They focus on the `SubmitDeclarationController`, `PoliticalAgent`, `Declaration`, `HouseholdMember`, `Position`, `Subsidy`, `Asset`, `Location`, `SecurityHolding`, `Institution`, `Function` and `DeclarationRepository` responsibilities.

### Acceptance Criteria Coverage

| Acceptance Criterion | Covered by |
|---|---|
| AC1 - The declaration must include a household section where the Political Agent declares their partner, descendants, and any other family members. | Tests 1 and 2 |
| AC2 - When submitting a declaration, the Political Agent must be able to import data from a previous declaration. | Tests 3 and 4 |
| AC3 - Declarations of Interest must be submitted according to their type (Initial, Regular, Exceptional). | Test 5 |
| AC4 - Initial declarations can only be submitted if the Political Agent has no previous initial declaration. | Test 6 |
| AC5 - Regular declarations can only be submitted once per year. | Test 7 |
| AC6 - Exceptional declarations must include the declaration being amended or the reason for the amendment. | Test 8 |
| AC7 - At least one declaration entry (position, subsidy, asset, or security holding) must be provided. | Test 9 |
| AC8 - Each position must include institution, type, function, dates and remuneration. | Test 10 |
| AC9 - Each subsidy must include the providing institution, nature and amount. | Test 11 |
| AC10 - Each asset must include type, acquisition value, market value and location. | Test 12 |
| AC11 - Each security holding must include institution, title, market value, stake and percentage. | Test 13 |
| AC12 - The declaration must be stored with a submission date. | Test 14 |
| AC13 - The system must confirm successful submission. | Test 15 |

**Test 1:** Check that a declaration must include at least one household member - AC1.

This test verifies that a declaration without any household member is rejected, since the household section is mandatory regardless of the financial entries provided.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureDeclarationRequiresAtLeastOneHouseholdMember() {
    DeclarationDTO dto = createValidDeclarationDTO();
    dto.householdMembers = new ArrayList<>();

    controller.submitDeclaration(dto);
}
```

**Test 2:** Check that each household member includes the mandatory data - AC1.

This test verifies that a household member must include name, tax identification number, birth date and relationship to the Political Agent. The entry must be validated when it is added to the declaration.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureHouseholdMemberRequiresMandatoryData() {
    HouseholdMemberDTO householdMemberDTO = createValidHouseholdMemberDTO();
    householdMemberDTO.relationship = null;

    DeclarationDTO dto = createDeclarationDTOWithHouseholdMember(householdMemberDTO);

    controller.submitDeclaration(dto);
}
```

**Test 3:** Check that the Political Agent can retrieve their previous declaration to import data - AC2.

This test verifies that `getPreviousDeclaration()` retrieves the most recent declaration submitted by the authenticated Political Agent, via `DeclarationRepository.findLastByPoliticalAgent(politicalAgent)`.

```java
@Test
public void ensurePreviousDeclarationCanBeRetrievedForImport() {
    when(declarationRepository.findLastByPoliticalAgent(politicalAgent)).thenReturn(previousDeclaration);

    DeclarationDTO previousDeclarationDTO = controller.getPreviousDeclaration();

    assertNotNull(previousDeclarationDTO);
    verify(declarationRepository).findLastByPoliticalAgent(politicalAgent);
}
```

**Test 4:** Check that the previous declaration data is available as pre-fill and that submitting it produces a new independent declaration - AC2.

This test verifies that `getPreviousDeclaration()` returns the most recent declaration as a `DeclarationDTO` (enabling UI pre-fill), and that submitting that DTO creates a new `Declaration` that is distinct from the previous one and has its own submission date. The import is purely a UI-level convenience: the controller uses `mapToDeclarationDTO(previousDeclaration)` to expose the data, and the submitted DTO is processed through the normal creation path.

```java
@Test
public void ensurePreviousDeclarationDataIsAvailableForPreFillAndSubmission() {
    when(declarationRepository.findLastByPoliticalAgent(politicalAgent)).thenReturn(previousDeclaration);

    DeclarationDTO previousDeclarationDTO = controller.getPreviousDeclaration();
    previousDeclarationDTO.type = DeclarationType.REGULAR;

    Declaration declaration = controller.submitDeclaration(previousDeclarationDTO);

    assertNotNull(declaration);
    assertNotSame(previousDeclaration, declaration);
    assertNotNull(declaration.getSubmissionDate());
}
```

**Test 5:** Check that the Political Agent must select a Declaration Type - AC3.

This test verifies that the declaration type is mandatory and must be one of the predefined values: Initial, Regular or Exceptional.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureDeclarationTypeIsMandatory() {
    DeclarationDTO dto = createValidDeclarationDTO();
    dto.type = null;

    controller.submitDeclaration(dto);
}
```

**Test 6:** Check that an Initial declaration cannot be submitted if one already exists - AC4.

This test verifies that the controller checks `DeclarationRepository.existsInitialDeclaration(politicalAgent)` before creating an Initial declaration, and rejects the submission if one already exists.

```java
@Test(expected = IllegalStateException.class)
public void ensureInitialDeclarationCannotBeSubmittedTwice() {
    DeclarationDTO dto = createValidDeclarationDTO();
    dto.type = DeclarationType.INITIAL;

    when(declarationRepository.existsInitialDeclaration(politicalAgent)).thenReturn(true);

    controller.submitDeclaration(dto);
}
```

**Test 7:** Check that a Regular declaration cannot be submitted more than once per year - AC5.

This test verifies that the controller checks `DeclarationRepository.existsRegularDeclarationForYear(politicalAgent, year)` before creating a Regular declaration, and rejects the submission if one already exists for the current year.

```java
@Test(expected = IllegalStateException.class)
public void ensureRegularDeclarationCannotBeSubmittedTwiceInSameYear() {
    DeclarationDTO dto = createValidDeclarationDTO();
    dto.type = DeclarationType.REGULAR;

    when(declarationRepository.existsRegularDeclarationForYear(politicalAgent, LocalDate.now().getYear())).thenReturn(true);

    controller.submitDeclaration(dto);
}
```

**Test 8:** Check that an Exceptional declaration requires an amendment reason or an amended declaration reference - AC6.

This test verifies that `Declaration.validateData()` rejects an Exceptional declaration when both `amendmentReason` is null/empty and no amended declaration was set.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureExceptionalDeclarationRequiresAmendmentReasonOrAmendedDeclaration() {
    DeclarationDTO dto = createValidDeclarationDTO();
    dto.type = DeclarationType.EXCEPTIONAL;
    dto.amendmentReason = null;
    dto.amendedDeclarationId = null;

    controller.submitDeclaration(dto);
}
```

**Test 9:** Check that at least one financial entry must be provided - AC7.

This test verifies that a declaration with a valid household section but without positions, subsidies, assets or security holdings is invalid.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureAtLeastOneFinancialEntryIsRequired() {
    DeclarationDTO dto = new DeclarationDTO();
    dto.type = DeclarationType.INITIAL;
    dto.householdMembers = createValidHouseholdMemberDTOList();
    dto.positions = new ArrayList<>();
    dto.subsidies = new ArrayList<>();
    dto.assets = new ArrayList<>();
    dto.securityHoldings = new ArrayList<>();

    controller.submitDeclaration(dto);
}
```

**Test 10:** Check that each Position includes all mandatory data - AC8.

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

**Test 11:** Check that each Subsidy includes institution, nature and amount - AC9.

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

**Test 12:** Check that each Asset includes type, values and location - AC10.

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

**Test 13:** Check that each Security Holding includes all mandatory data - AC11.

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

**Test 14:** Check that the Declaration is stored with a submission date - AC12.

This test verifies that a valid declaration receives a submission date when it is successfully submitted.

```java
@Test
public void ensureDeclarationHasSubmissionDate() {
    Declaration declaration = controller.submitDeclaration(createValidDeclarationDTO());

    assertNotNull(declaration.getSubmissionDate());
}
```

**Test 15:** Check that a valid Declaration is submitted successfully - AC13.

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
* `DeclarationRepository` provides the previous declaration (`findLastByPoliticalAgent`), checks Initial/Regular submission rules (`existsInitialDeclaration`, `existsRegularDeclarationForYear`), and retrieves an amended declaration by ID (`getById`).
* `SubmitDeclarationController.getPreviousDeclaration()` retrieves the Political Agent's most recent declaration through `DeclarationRepository.findLastByPoliticalAgent(politicalAgent)` and converts it into editable DTO data via `mapToDeclarationDTO(previousDeclaration)`, which the UI uses as pre-fill (AC2).
* `PoliticalAgent.createDeclaration(declarationType)` creates a new independent declaration from the final submitted DTO data. The normal creation flow is always followed, whether or not data was pre-filled from a previous declaration.
* `Declaration` creates and validates its declared entries: household members, positions, subsidies, assets and security holdings.
* Each entry must be validated when it is added to the declaration, ensuring that invalid entries are rejected immediately.
* For Exceptional declarations, the controller sets `amendmentReason` and/or the amended declaration reference via `Declaration.setAmendmentReason(...)` and `Declaration.setAmendedDeclaration(...)`.
* `Declaration.validateData()` performs the final global validation, including AC1 (at least one household member), AC6 (amendment reason/reference for Exceptional declarations), and AC7 (at least one financial entry).
* `DeclarationRepository.save(declaration)` stores the submitted declaration with status `SUBMITTED`.

## 6. Integration and Demo

For demonstration, the Political Agent should be able to select a declaration type, optionally import data from their previous declaration, declare at least one household member, fill at least one financial entry (position, subsidy, asset or security holding), provide amendment data if the declaration is Exceptional, and submit the declaration. A successful submission should store the declaration with a submission date and status `SUBMITTED`.

## 7. Observations

The SSD describes validation at entry level. Therefore, the implementation and design should ensure that each declared item (including household members) is validated when it is added to the declaration, while the final `validateData()` operation checks global declaration rules before persistence.

The Initial/Regular submission rules (AC4, AC5) are checked by the Controller against the `DeclarationRepository` before a new Declaration is created, since they depend on the Political Agent's other declarations and not solely on the data of the declaration being submitted.

Importing a previous declaration (AC2) is purely a convenience for the Political Agent: the resulting declaration is independent of the previous one (except for the optional `amends` reference used for Exceptional declarations) and is still subject to all the same validation rules (AC1, AC6, AC7).

The tests avoid depending on `PoliticalAgent.getDeclarations()` or `DeclarationRepository.getDeclarations()` because those methods are not part of the current class diagram. Persistence is instead verified through the `DeclarationRepository.save(declaration)` responsibility.