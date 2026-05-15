# US009 - Consult Integrated Situation of a Political Agent

## 4. Tests

**Test 1:** Check that the controller returns the list of Political Agents available for consultation when the repository contains agents.

```java
@Test
public void ensureGetPoliticalAgentsReturnsData() {
  List<PoliticalAgent> agents = controller.getPoliticalAgents();

  assertFalse(agents.isEmpty());
}
```

**Test 2:** Check that the controller requests the declarations of the selected Political Agent using the provided reference date.

```java
@Test
public void ensureConsultIntegratedSituationUsesPoliticalAgentAndReferenceDate() {
  PoliticalAgent agent = new PoliticalAgent("Agent Name", "123456789");
  LocalDate referenceDate = LocalDate.of(2026, 5, 15);

  controller.consultIntegratedSituation(agent, referenceDate);

  verify(declarationRepository).getDeclarationsByPoliticalAgentUntilDate(agent, referenceDate);
}
```

**Test 3:** Check that the controller obtains the Ethics Committee member from the authenticated session before performing the consultation.

```java
@Test
public void ensureEthicsCommitteeMemberIsObtainedFromSession() {
  controller.consultIntegratedSituation(selectedAgent, referenceDate);

  verify(userRepository).getEthicsCommitteeMemberByEmail(currentUserEmail);
}
```

**Test 4:** Check that the integrated situation only includes declarations valid on the selected reference date.

```java
@Test
public void ensureIntegratedSituationUsesOnlyValidDeclarations() {
  IntegratedSituation situation = controller.consultIntegratedSituation(selectedAgent, referenceDate);

  assertEquals(referenceDate, situation.getReferenceDate());
}
```

**Test 5:** Check that access is denied when the authenticated user does not have the Ethics Committee role.

```java
@Test(expected = IllegalStateException.class)
public void ensureAccessIsDeniedWithoutEthicsCommitteeRole() {
  controller.consultIntegratedSituation(selectedAgent, referenceDate);
}
```

_It is also recommended to organize this content by subsections._


## 5. Construction (Implementation)

### Class ConsultIntegratedSituationController

```java
public List<PoliticalAgent> getPoliticalAgents() {
  return getPoliticalAgentRepository().getPoliticalAgents();
}

public IntegratedSituation consultIntegratedSituation(PoliticalAgent politicalAgent, LocalDate referenceDate) {

  EthicsCommitteeMember member = getEthicsCommitteeMemberFromSession();
  List<Declaration> declarations = getDeclarationRepository()
    .getDeclarationsByPoliticalAgentUntilDate(politicalAgent, referenceDate);

  return member.buildIntegratedSituation(politicalAgent, declarations, referenceDate);
}

private EthicsCommitteeMember getEthicsCommitteeMemberFromSession() {
  UserSession currentSession = ApplicationSession.getInstance().getCurrentSession();
  String email = currentSession.getUserEmail();

  return getUserRepository().getEthicsCommitteeMemberByEmail(email);
}
```

### Class EthicsCommitteeMember

```java
public IntegratedSituation buildIntegratedSituation(PoliticalAgent politicalAgent,
                          List<Declaration> declarations,
                          LocalDate referenceDate) {

  IntegratedSituation integratedSituation = new IntegratedSituation(politicalAgent, referenceDate);
  integratedSituation.aggregateFromDeclarations(declarations);

  return integratedSituation;
}
```


## 6. Integration and Demo

* A new option in the Ethics Committee consultation menu should allow the user to select a Political Agent and a reference date.

* For demo purposes, only declarations valid up to the selected reference date should be displayed in the integrated situation.


## 7. Observations

n/a