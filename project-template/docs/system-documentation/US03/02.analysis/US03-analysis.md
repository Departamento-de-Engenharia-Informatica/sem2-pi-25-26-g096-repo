# US03 - List Institutions

## 2. Analysis
### 2.1. Relevant Domain Model Excerpt

[PlantUML source](puml/US006-DM.puml)

### 2.2. Other Remarks

* To support grouping and ordering, `Institution` must have at least a `name` and an associated `InstitutionType`.
* Institution types are predefined in Sprint 1: Company, Political Party, Foundation, Institute, and Association.
* The system must enforce role-based access: only authenticated users with the Political Agent role can execute this use case.
* This use case depends on prior institution registration (US04).