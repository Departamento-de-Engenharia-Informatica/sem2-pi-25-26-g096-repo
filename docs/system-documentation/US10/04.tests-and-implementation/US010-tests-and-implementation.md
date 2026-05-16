# US10 - Analyse Income Evolution of a Political Agent

## 4. Tests

The following unit tests are designed from the acceptance criteria, analysis model and design model of US10. They focus on the `AnalyseIncomeEvolutionController`, `PoliticalAgentRepository`, `DeclarationRepository`, `Declaration`, `Position`, `Subsidy`, `PoliticalAgentMapper`, `IncomeEvolutionMapper` and `IncomeEvolutionDTO` responsibilities.

### Acceptance Criteria Coverage

| Acceptance Criterion | Covered by |
|---|---|
| AC1 - The journalist must be a registered and authenticated user with the Journalist role. | Tests 1 and 2 |
| AC2 - The journalist must select a registered Political Agent. | Tests 3 and 4 |
| AC3 - The journalist must provide a valid start date and end date. | Tests 5 and 6 |
| AC4 - The system must retrieve all declarations of the selected Political Agent within the specified period. | Test 7 |
| AC5 - For each declaration, the system must present the total income, considering remunerations and subsidies. | Tests 8 and 9 |
| AC6 - Results must be presented in chronological order of submission date. | Test 10 |
| AC7 - If no declarations exist for the selected agent and period, the system must inform the journalist accordingly. | Test 11 |

**Test 1:** Check that only an authenticated Journalist can access the Political Agent list - AC1.

This test verifies that the list of Political Agents is only available to an authenticated user with the Journalist role.

```java
@Test(expected = IllegalStateException.class)
public void ensureOnlyAuthenticatedJournalistCanListPoliticalAgents() {
    when(userSession.isLoggedInAsJournalist()).thenReturn(false);

    controller.getPoliticalAgents();
}
```

**Test 2:** Check that only an authenticated Journalist can analyse income evolution - AC1.

This test verifies that the income evolution operation itself also checks authentication before returning analysis results.

```java
@Test(expected = IllegalStateException.class)
public void ensureOnlyAuthenticatedJournalistCanAnalyseIncomeEvolution() {
    when(userSession.isLoggedInAsJournalist()).thenReturn(false);

    controller.getIncomeEvolution("PA001", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
}
```

**Test 3:** Check that the Journalist can obtain the list of registered Political Agents - AC2.

This test verifies that the system provides the Journalist with a list of registered Political Agents to select from.

```java
@Test
public void ensureRegisteredPoliticalAgentsAreListed() {
    List<PoliticalAgentDTO> agents = controller.getPoliticalAgents();

    assertNotNull(agents);
    assertFalse(agents.isEmpty());
}
```

**Test 4:** Check that the selected Political Agent must exist - AC2.

This test verifies that the analysis cannot be performed for a Political Agent that is not registered in the system.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureSelectedPoliticalAgentMustExist() {
    when(politicalAgentRepository.getById("invalid-id")).thenReturn(null);

    controller.getIncomeEvolution("invalid-id", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
}
```

**Test 5:** Check that start date and end date are mandatory - AC3.

This test verifies that both dates must be provided before performing the analysis.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureStartAndEndDateAreMandatory() {
    controller.getIncomeEvolution("PA001", null, LocalDate.of(2024, 12, 31));
}
```

**Test 6:** Check that the start date must be before the end date - AC3.

This test verifies the validity of the selected date interval.

```java
@Test(expected = IllegalArgumentException.class)
public void ensureStartDateMustBeBeforeEndDate() {
    controller.getIncomeEvolution("PA001", LocalDate.of(2024, 12, 31), LocalDate.of(2024, 1, 1));
}
```

**Test 7:** Check that only declarations within the selected period are retrieved - AC4.

This test verifies that the system only considers declarations submitted by the selected Political Agent within the given period.

```java
@Test
public void ensureOnlyDeclarationsWithinPeriodAreRetrieved() {
    LocalDate startDate = LocalDate.of(2024, 1, 1);
    LocalDate endDate = LocalDate.of(2024, 12, 31);

    List<IncomeEvolutionDTO> result = controller.getIncomeEvolution("PA001", startDate, endDate);

    for (IncomeEvolutionDTO dto : result) {
        assertFalse(dto.submissionDate.isBefore(startDate));
        assertFalse(dto.submissionDate.isAfter(endDate));
    }
}
```

**Test 8:** Check that position remunerations are considered in total income - AC5.

This test verifies that the system includes remuneration from professional positions when calculating income.

```java
@Test
public void ensurePositionIncomeIsIncludedInTotalIncome() {
    Declaration declaration = createDeclarationWithPositionIncome(1000.0);

    assertEquals(1000.0, declaration.getTotalPositionIncome(), 0.01);
}
```

**Test 9:** Check that subsidies are considered in total income - AC5.

This test verifies that subsidy amounts are included in the total income calculation.

```java
@Test
public void ensureSubsidyIncomeIsIncludedInTotalIncome() {
    Declaration declaration = createDeclarationWithPositionAndSubsidyIncome(1000.0, 500.0);

    assertEquals(500.0, declaration.getTotalSubsidyIncome(), 0.01);
    assertEquals(1500.0, declaration.getTotalIncome(), 0.01);
}
```

**Test 10:** Check that income evolution results are ordered chronologically - AC6.

This test verifies that the income evolution is presented according to declaration submission date. The ordering responsibility is assigned to `IncomeEvolutionMapper.toChronologicalDTOList(declarations)`, which internally calls `sortBySubmissionDate(declarations)` before building the DTO list.

```java
@Test
public void ensureIncomeEvolutionIsOrderedBySubmissionDate() {
    List<IncomeEvolutionDTO> result = controller.getIncomeEvolution(
        "PA001",
        LocalDate.of(2020, 1, 1),
        LocalDate.of(2024, 12, 31)
    );

    for (int i = 1; i < result.size(); i++) {
        assertFalse(result.get(i).submissionDate.isBefore(result.get(i - 1).submissionDate));
    }
}
```

**Test 11:** Check that the system informs when no declarations exist in the selected period - AC7.

This test verifies that the system handles the absence of declarations without producing invalid results.

```java
@Test
public void ensureEmptyResultWhenNoDeclarationsExistForPeriod() {
    List<IncomeEvolutionDTO> result = controller.getIncomeEvolution(
        "PA001",
        LocalDate.of(1990, 1, 1),
        LocalDate.of(1990, 12, 31)
    );

    assertNotNull(result);
    assertTrue(result.isEmpty());
}
```

## 5. Construction (Implementation)

The implementation should follow the design responsibilities:

* `AnalyseIncomeEvolutionController` coordinates the use case.
* `ApplicationSession` and `UserSession` verify that the user is authenticated with the Journalist role via `isLoggedInAsJournalist()`.
* The authentication check must be performed both when listing Political Agents and when analysing income evolution.
* `PoliticalAgentRepository` lists and retrieves registered Political Agents.
* `DeclarationRepository` retrieves declarations for a selected Political Agent within a selected date interval.
* `Declaration` calculates position income, subsidy income and total income.
* `PoliticalAgentMapper` converts Political Agent domain objects into DTOs.
* `IncomeEvolutionMapper.toChronologicalDTOList(declarations)` converts declarations into income evolution DTOs, sorting them chronologically by `submissionDate` via the private `sortBySubmissionDate(declarations)` method.

## 6. Integration and Demo

For demonstration, the Journalist should be able to select a Political Agent, define a valid date interval and consult the chronological evolution of that Political Agent's income. If no declarations exist in the selected period, the system should inform the Journalist accordingly.

## 7. Observations

Income is a derived value computed from position remunerations and subsidy amounts. The controller must not calculate income directly; this responsibility belongs to `Declaration`, following Information Expert.

The chronological ordering required by AC6 is the responsibility of `IncomeEvolutionMapper`, which exposes `toChronologicalDTOList(declarations)` as the public entry point and delegates ordering to the private `sortBySubmissionDate(declarations)` method. This keeps the controller focused on coordination and avoids spreading ordering logic across the UI or repository layers, following High Cohesion and Low Coupling.