# US011 - Consult Assets of a Political Agent

## 4. Tests

**Test 1:** Check that the controller returns the available Political Agents for consultation.

```java
@Test
public void ensureGetPoliticalAgentsReturnsData() {
	List<PoliticalAgent> agents = controller.getPoliticalAgents();

	assertFalse(agents.isEmpty());
}
```

**Test 2:** Check that the controller requests declarations using the selected Political Agent and the provided reference date.

```java
@Test
public void ensureConsultAssetsUsesPoliticalAgentAndReferenceDate() {
	PoliticalAgent agent = new PoliticalAgent("Agent Name", "123456789");
	LocalDate referenceDate = LocalDate.of(2026, 5, 15);

	controller.consultAssets(agent, referenceDate);

	verify(declarationRepository).getDeclarationsByPoliticalAgentUntilDate(agent, referenceDate);
}
```

**Test 3:** Check that assets are extracted only from declaration sections valid on the selected date.

```java
@Test
public void ensureOnlyValidDeclarationSectionsAreUsed() {
	List<AssetView> assets = controller.consultAssets(selectedAgent, referenceDate);

	assertNotNull(assets);
}
```

**Test 4:** Check that the Citizen view masks sensitive fields according to the access policy.

```java
@Test
public void ensureCitizenViewMasksSensitiveFields() {
	List<AssetView> assets = assetVisibilityService.applyRoleMasking(citizen, rawAssets);

	assertTrue(assets.stream().anyMatch(AssetView::hasMaskedFields));
}
```

**Test 5:** Check that the Journalist view applies the expected role-specific masking.

```java
@Test
public void ensureJournalistViewAppliesRoleSpecificMasking() {
	List<AssetView> assets = assetVisibilityService.applyRoleMasking(journalist, rawAssets);

	assertNotNull(assets);
}
```

**Test 6:** Check that unauthorized roles cannot access the consultation.

```java
@Test(expected = IllegalStateException.class)
public void ensureUnauthorizedRolesCannotAccessConsultation() {
	controller.consultAssets(selectedAgent, referenceDate);
}
```

_It is also recommended to organize this content by subsections._


## 5. Construction (Implementation)

### Class ConsultAssetsController

```java
public List<PoliticalAgent> getPoliticalAgents() {
	return getPoliticalAgentRepository().getPoliticalAgents();
}

public List<AssetView> consultAssets(PoliticalAgent politicalAgent, LocalDate referenceDate) {

	User user = getAuthenticatedUserFromSession();
	List<Declaration> declarations = getDeclarationRepository()
		.getDeclarationsByPoliticalAgentUntilDate(politicalAgent, referenceDate);

	return assetVisibilityService.applyRoleMasking(user, declarations, referenceDate);
}

private User getAuthenticatedUserFromSession() {
	UserSession currentSession = ApplicationSession.getInstance().getCurrentSession();
	String email = currentSession.getUserEmail();

	return getUserRepository().getUserByEmail(email);
}
```

### Class AssetVisibilityService

```java
public List<AssetView> applyRoleMasking(User user, List<Declaration> declarations, LocalDate referenceDate) {
	List<AssetView> assets = extractAssets(declarations, referenceDate);

	if (user instanceof Citizen) {
		return maskForCitizen(assets);
	}

	if (user instanceof Journalist) {
		return maskForJournalist(assets);
	}

	throw new IllegalStateException("Unauthorized role");
}
```


## 6. Integration and Demo

* A new consultation option should be available for the user to select a Political Agent and a reference date.

* For demo purposes, the consultation should show masked asset data according to the authenticated user's role.


## 7. Observations

n/a
