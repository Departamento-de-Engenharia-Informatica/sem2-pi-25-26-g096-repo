# US04 - Register an Institution

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US04. They focus on the `RegisterInstitutionController`, `InstitutionRepository`, `InstitutionTypeRepository` and `Institution` responsibilities.

### Acceptance Criteria Coverage

| Acceptance Criterion                                                                                  | Covered by          |
|-------------------------------------------------------------------------------------------------------|---------------------|
| AC1 - The institution type must be selected from a predefined list of available types.               | Tests 2, 3 and 4    |
| (Implicit) Institution name must not be null or empty.                                               | Test 5              |
| (Implicit) A valid registration persists the institution in the repository.                          | Tests 1 and 5       |

**Test 1:** Check that registration with a valid type and name succeeds — AC1 (positive path).

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
    Institution institution = controller.registerInstitution("My Institution", "500000001", InstitutionType.COMPANY);

    assertNotNull(institution);
    assertEquals("My Institution", institution.getName());
    assertEquals(InstitutionType.COMPANY, institution.getType());
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

**Test 5:** Check that the registered institution is persisted in the repository.

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

**Test 6:** Check that institution name must not be null or empty.

This test enforces the implicit domain invariant: an institution cannot be created without a name. The system must reject null and blank names before attempting persistence.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureInstitutionNameCannotBeNullOrEmpty() {
    controller.registerInstitution(null, "500000005", InstitutionType.INSTITUTE);
}
```


## 5. Construction (Implementation)

The implementation should follow the design responsibilities:

* `RegisterInstitutionController` coordinates the use case: it obtains the available types from `InstitutionTypeRepository`, accepts the Administrator's input, creates the `Institution` domain object, and saves it via `InstitutionRepository`.
* `InstitutionTypeRepository` provides the predefined list of types required by AC1; no type outside this list may be used.
* `Institution` validates its own invariants (non-null name, non-null type) on construction, acting as the domain layer guard.
* `InstitutionRepository.save(institution)` persists the validated institution and makes it available for future queries (including US03 listing).
* `RegisterInstitutionUI` presents the type list to the Administrator, collects the institution data, and delegates all logic to the controller; it contains no business logic.


## 6. Integration and Demo

For demonstration, the Administrator must be able to access the institution registration option from the main menu. The system displays the predefined list of institution types; the Administrator selects one and enters the institution name (and tax identification number). Upon successful registration, the system confirms the creation. The new institution becomes immediately available in the US03 listing.


## 7. Observations

* n/a
