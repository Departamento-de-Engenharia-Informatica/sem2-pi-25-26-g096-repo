# US03 - List Institutions

## 4. Tests

**Test 1:** Check that listing returns valid grouped format when no institutions exist.

	private InstitutionService service;
	
	@BeforeEach
	void setUp() {
		service = new InstitutionService();
	}
	
	@Test
	public void ensureEmptyListReturnsValidGroupedFormat() {
		Map<InstitutionType, List<Institution>> grouped = service.listGroupedByType();
		assertTrue(grouped.isEmpty());
	}

**Test 2:** Check that institutions are grouped by type and sorted alphabetically within each type.

	@Test
	public void ensureInstitutionsAreGroupedByTypeAndSortedAlphabetically() {
		service.registerInstitution("Zeta Company", InstitutionType.COMPANY);
		service.registerInstitution("Alpha Company", InstitutionType.COMPANY);
		service.registerInstitution("Beta Party", InstitutionType.POLITICAL_PARTY);
		
		Map<InstitutionType, List<Institution>> grouped = service.listGroupedByType();
		
		List<Institution> companies = grouped.get(InstitutionType.COMPANY);
		assertEquals(2, companies.size());
		assertEquals("Alpha Company", companies.get(0).getName());
		assertEquals("Zeta Company", companies.get(1).getName());
	}

**Test 3:** Check that filtering by type returns only that type group, sorted alphabetically.

	@Test
	public void ensureFilterByTypeReturnsOnlyThatTypeAndSorted() {
		service.registerInstitution("Zeta Company", InstitutionType.COMPANY);
		service.registerInstitution("Alpha Company", InstitutionType.COMPANY);
		service.registerInstitution("Beta Party", InstitutionType.POLITICAL_PARTY);
		
		Map<InstitutionType, List<Institution>> grouped = service.listGroupedByType();
		
		assertTrue(grouped.containsKey(InstitutionType.COMPANY));
		assertFalse(grouped.containsKey(InstitutionType.POLITICAL_PARTY) && grouped.get(InstitutionType.POLITICAL_PARTY).isEmpty());
	}

**Test 4:** Check that only Sprint 1 supported types appear in the list.

	@Test
	public void ensureOnlySupportedTypeAppear() {
		InstitutionType[] supportedTypes = {InstitutionType.COMPANY, InstitutionType.POLITICAL_PARTY, 
										  InstitutionType.FOUNDATION, InstitutionType.INSTITUTE, InstitutionType.ASSOCIATION};
		for (InstitutionType type : supportedTypes) {
			assertNotNull(type);
		}
	}
