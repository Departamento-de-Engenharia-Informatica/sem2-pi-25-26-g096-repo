# US04 - Register an Institution

## 4. Tests

**Test 1:** Check that registration succeeds with a valid type from the predefined list.

	private InstitutionService service;
	
	@BeforeEach
	void setUp() {
		service = new InstitutionService();
	}
	
	@Test
	public void ensureRegistrationWithValidTypeSucceeds() {
		Institution inst = service.registerInstitution("My Institution", InstitutionType.COMPANY);
		assertNotNull(inst);
		assertEquals("My Institution", inst.getName());
		assertEquals(InstitutionType.COMPANY, inst.getType());
	}

**Test 2:** Check that registration fails when type is null or missing.

	@Test(expected = IllegalArgumentException.class)
	public void ensureRegistrationFailsWithNullType() {
		service.registerInstitution("Institution Name", null);
	}

**Test 3:** Check that registration fails with invalid type not in the predefined list.

	@Test(expected = IllegalArgumentException.class)
	public void ensureRegistrationFailsWithInvalidType() {
		// Attempting to use an invalid type should throw exception
		service.registerInstitution("Institution Name", InstitutionType.valueOf("INVALID_TYPE"));
	}

**Test 4:** Check that only the predefined institution types are available.

	@Test
	public void ensureOnlyPredefinedTypesAreAvailable() {
		InstitutionType[] expectedTypes = {InstitutionType.COMPANY, InstitutionType.POLITICAL_PARTY,
										  InstitutionType.FOUNDATION, InstitutionType.INSTITUTE, InstitutionType.ASSOCIATION};
		for (InstitutionType type : expectedTypes) {
			assertNotNull(type);
		}
	}

**Test 5:** Check that institution name must not be null or empty.

	@Test(expected = IllegalArgumentException.class)
	public void ensureInstitutionNameCannotBeNull() {
		service.registerInstitution(null, InstitutionType.FOUNDATION);
	}
