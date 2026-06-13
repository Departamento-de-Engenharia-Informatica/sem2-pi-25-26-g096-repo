# US04 - Register an Institution

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US04. They focus on the `RegisterInstitutionController`, `InstitutionRepository`, `InstitutionTypeRepository` and `Institution` responsibilities.

### Acceptance Criteria Coverage

| Acceptance Criterion                                                                                                                         | Covered by       |
|----------------------------------------------------------------------------------------------------------------------------------------------|------------------|
| AC1 - The institution type must be selected from a predefined list of available types.                                                      | Tests 2, 3, 4    |
| AC2 - The institution name must not be null or empty; the system must reject registration with a blank name.                                | Test 6           |
| AC3 - The system must prevent duplicate institutions (same name and type); duplicates must be rejected.                                     | Test 7           |
| (integration) A valid registration with all fields correct persists the institution in the repository.                                      | Tests 1, 5       |

**Test 1:** Check that registration with a valid type and name succeeds — AC1, AC2, AC3 (positive path).

This test validates the happy path: when the Administrator selects a type from the predefined list and provides a valid name, the institution is created and returned correctly.

```java
@BeforeEach
public void setUp() {
    InstitutionRepository institutionRepository = new InstitutionRepository();
    InstitutionTypeRepository institutionTypeRepository = new InstitutionTypeRepository();
    controller = new RegisterInstitutionController(institutionRepository, institutionTypeRepository);
}

@Test
public void ensureRegistrationWithValidTypeAndNameSucceeds() {
    InstitutionDTO institution = controller.registerInstitution("My Institution", "500000001", InstitutionType.COMPANY);

    assertNotNull(institution);
    assertEquals("My Institution", institution.getName());
    assertEquals(InstitutionType.COMPANY.name(), institution.getType());
}
```

**Test 2:** Check that registration fails when the institution type is null — AC1.

This test enforces AC1: the type must come from the predefined list. Passing null is equivalent to bypassing the selection step and must be rejected.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureRegistrationFailsWithNullType() {
    controller.registerInstitution("Institution Name", "500000002", null);
}
```

**Test 3:** Check that only the five predefined institution types are available from the controller — AC1.

This test verifies that `getInstitutionTypes()` exposes exactly the Sprint 1 predefined types and that no arbitrary type can be introduced from outside the controlled list.

```java
@Test
public void ensureOnlyPredefinedTypesAreAvailableFromController() {
    List<InstitutionType> types = controller.getInstitutionTypes();

    assertTrue(types.contains(InstitutionType.COMPANY));
    assertTrue(types.contains(InstitutionType.POLITICAL_PARTY));
    assertTrue(types.contains(InstitutionType.FOUNDATION));
    assertTrue(types.contains(InstitutionType.INSTITUTE));
    assertTrue(types.contains(InstitutionType.ASSOCIATION));
    assertEquals(5, types.size());
}
```

**Test 4:** Check that registration fails when an invalid type value is provided — AC1.

This test ensures that the system rejects any attempt to use a type that is not part of the predefined list. This guards AC1 at the domain boundary.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureRegistrationFailsWithTypeNotInPredefinedList() {
    // InstitutionType.valueOf throws IllegalArgumentException for unknown names;
    // the controller must propagate or re-throw it.
    InstitutionType invalidType = InstitutionType.valueOf("INVALID_TYPE");
    controller.registerInstitution("Institution Name", "500000003", invalidType);
}
```

**Test 5:** Check that the registered institution is persisted in the repository — integration (AC1, AC2, AC3 all satisfied).

This test validates the persistence side-effect of a successful registration. After `registerInstitution()` succeeds, the institution must be retrievable from the repository through the controller.

```java
@Test
public void ensureRegisteredInstitutionIsPersisted() {
    controller.registerInstitution("Persisted Foundation", "500000004", InstitutionType.FOUNDATION);

    List<Institution> all = controller.getInstitutionRepository().getAllInstitutions();

    assertEquals(1, all.size());
    assertEquals("Persisted Foundation", all.get(0).getName());
}
```

**Test 6:** Check that institution name must not be null or empty — AC2.

This test enforces the domain invariant: an institution cannot be created without a name. The system must reject null and blank names before attempting persistence.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureInstitutionNameCannotBeNullOrEmpty() {
    controller.registerInstitution(null, "500000005", InstitutionType.INSTITUTE);
}
```

**Test 7:** Check that registering a duplicate institution is rejected — AC3.

This test validates that if an institution with the same name and type already exists in the repository, the system must refuse the second registration attempt.

```java
@Test(expected = IllegalStateException.class)
public void ensureDuplicateInstitutionIsRejected() {
    controller.registerInstitution("Duplicate Foundation", "500000006", InstitutionType.FOUNDATION);
    // Second attempt with same name and type must be rejected
    controller.registerInstitution("Duplicate Foundation", "500000007", InstitutionType.FOUNDATION);
}
```


## 5. Construction (Implementation)

The implementation should follow the design responsibilities:

* `RegisterInstitutionController` coordinates the use case: it obtains the available types from `InstitutionTypeRepository`, accepts the Administrator's input, creates the `Institution` domain object, checks for duplicates (AC3), saves it via `InstitutionRepository`, and converts it to `InstitutionDTO` before returning it to the UI.
* `InstitutionTypeRepository` provides the predefined list of types required by AC1; no type outside this list may be used.
* `Institution.validate()` enforces the domain invariants (non-null, non-empty name — AC2; non-null type — AC1) on construction, acting as the domain layer guard.
* `InstitutionRepository.isDuplicate(name, type)` checks for an existing institution with the same name and type before saving (AC3).
* `InstitutionRepository.save(institution)` persists the validated institution and makes it available for future queries (including US03 listing).
* `InstitutionMapper` maps the saved `Institution` domain entity to a simple `InstitutionDTO` data transfer object.
* `InstitutionDTO` carries the name and type back to the UI.
* `RegisterInstitutionUI` presents the type list to the Administrator, collects the institution data, and delegates all logic to the controller; it only receives and displays DTOs.


## 6. Integration and Demo

For demonstration, the Administrator must be able to access the institution registration option from the main menu. The system displays the predefined list of institution types; the Administrator selects one and enters the institution name (and tax identification number). Upon successful registration, the system confirms the creation. The new institution becomes immediately available in the US03 listing. If a duplicate is detected or the name is blank, the system must display an appropriate error message without persisting the institution.


## 7. Observations

* AC3 (duplicate prevention) was originally classified by the client as a "technical concern". It has been formalised as a business rule to ensure catalog integrity and to provide explicit test coverage.
* The alphabetical ordering of the institution catalog is the responsibility of US03 (listing), not US04 (registration).
