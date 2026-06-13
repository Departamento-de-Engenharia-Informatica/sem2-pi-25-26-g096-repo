# US03 - List Institutions

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US03. They focus on the `ListInstitutionsController`, `InstitutionRepository`, `InstitutionTypeRepository` and `Institution` responsibilities.

### Acceptance Criteria Coverage

| Acceptance Criterion                                                                                                                     | Covered by           |
|------------------------------------------------------------------------------------------------------------------------------------------|----------------------|
| AC1 - Institutions must be grouped by type and listed alphabetically by name inside each type group.                                    | Tests 2, 3           |
| AC2 - If no institutions exist or none match the selected type, the system returns an empty valid grouped result (no null/exception).    | Test 1               |
| AC3 - The institution types available for selection are restricted to the five predefined types of the enumeration.                     | Test 4               |

**Test 1:** Check that listing all institutions returns a valid grouped format when no institutions exist — AC2.

This test ensures that when no institutions are registered the system returns a well-formed but empty grouped structure, not a null reference or an exception.

```java
@BeforeEach
public void setUp() {
    InstitutionRepository institutionRepository = new InstitutionRepository();
    InstitutionTypeRepository institutionTypeRepository = new InstitutionTypeRepository();
    controller = new ListInstitutionsController(institutionRepository, institutionTypeRepository);
}

@Test
public void ensureEmptyListReturnsValidGroupedFormat() {
    Map<InstitutionType, List<Institution>> grouped = controller.getInstitutionsByType(null);

    assertNotNull(grouped);
    assertTrue(grouped.isEmpty());
}
```

**Test 2:** Check that institutions are correctly grouped by type — AC1.

This test verifies the grouping behaviour required by AC1. Institutions of different types must be placed in separate groups; no cross-group mixing is allowed.

```java
@Test
public void ensureInstitutionsAreGroupedByType() {
    controller.getInstitutionRepository().save(new Institution("Alpha Company", InstitutionType.COMPANY));
    controller.getInstitutionRepository().save(new Institution("Beta Party", InstitutionType.POLITICAL_PARTY));

    Map<InstitutionType, List<Institution>> grouped = controller.getAllInstitutionsGrouped();

    assertTrue(grouped.containsKey(InstitutionType.COMPANY));
    assertTrue(grouped.containsKey(InstitutionType.POLITICAL_PARTY));
    assertEquals(1, grouped.get(InstitutionType.COMPANY).size());
    assertEquals(1, grouped.get(InstitutionType.POLITICAL_PARTY).size());
}
```

**Test 3:** Check that institutions within the same type are sorted alphabetically by name — AC1.

This test validates the alphabetical ordering requirement inside a group. The controller must return institutions in ascending alphabetical order regardless of the insertion order.

```java
@Test
public void ensureInstitutionsWithinTypeAreSortedAlphabetically() {
    controller.getInstitutionRepository().save(new Institution("Zeta Company", InstitutionType.COMPANY));
    controller.getInstitutionRepository().save(new Institution("Alpha Company", InstitutionType.COMPANY));
    controller.getInstitutionRepository().save(new Institution("Gamma Company", InstitutionType.COMPANY));

    List<Institution> companies = controller.getInstitutionsByType(InstitutionType.COMPANY);

    assertEquals("Alpha Company", companies.get(0).getName());
    assertEquals("Gamma Company", companies.get(1).getName());
    assertEquals("Zeta Company", companies.get(2).getName());
}
```

**Test 4:** Check that all five predefined institution types are available in the system — AC3.

This test ensures that the complete set of Sprint 1 institution types (Company, Political Party, Foundation, Institute, Association) is available via the controller, as required by the acceptance criteria and referenced in the requirements remarks.

```java
@Test
public void ensureAllFivePredefinedInstitutionTypesAreAvailable() {
    List<InstitutionType> types = controller.getInstitutionTypes();

    assertTrue(types.contains(InstitutionType.COMPANY));
    assertTrue(types.contains(InstitutionType.POLITICAL_PARTY));
    assertTrue(types.contains(InstitutionType.FOUNDATION));
    assertTrue(types.contains(InstitutionType.INSTITUTE));
    assertTrue(types.contains(InstitutionType.ASSOCIATION));
}
```


## 5. Construction (Implementation)

The implementation should follow the design responsibilities:

* `ListInstitutionsController` coordinates the use case: it obtains the institution types and retrieves institutions filtered and sorted before presenting them to the UI.
* `InstitutionTypeRepository` provides the predefined list of institution types; it is the single source of truth for valid types (AC1 compliance).
* `InstitutionRepository.getInstitutionsByType(type)` retrieves the raw (unsorted) list of institutions matching a given type.
* `ListInstitutionsController.getInstitutionsByType(type)` sorts the retrieved list alphabetically by institution name before returning it to the UI layer.
* `ListInstitutionsUI` presents the grouped and ordered result to the Political Agent; it has no sorting or grouping logic of its own.


## 6. Integration and Demo

For demonstration, the Political Agent should be able to trigger the listing action from the main menu. The system must first display the available institution types; after the agent selects one (or all), the system returns the matching institutions grouped by type and ordered alphabetically by name within each group. If no institutions are registered, the system must return a valid but empty result without errors.


## 7. Observations

* The alphabetical sorting responsibility is explicitly placed on the `ListInstitutionsController`, not the repository, as stated in the design rationale (Step 5). This preserves a clean separation of concerns: the repository handles persistence, the controller handles application logic.
* US03 depends on US04 for data existence: institutions only become listable after an Administrator registers them.
