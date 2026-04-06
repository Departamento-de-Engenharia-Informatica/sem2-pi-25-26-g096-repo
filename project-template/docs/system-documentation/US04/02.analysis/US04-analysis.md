# US04 - Register an Institution

## 2. Analysis

The analysis for this user story identifies the minimum domain concepts needed to register an institution with a predefined type. This operation creates new domain data.

### 2.1. Relevant Domain Model Excerpt

[PlantUML source](puml/US006-DM.puml)

### 2.2. Other Remarks

* The InstitutionType is modeled as a predefined enumeration, as required by AC1.
* The operation is restricted to the Administrator role.
* The platform manages registered institutions as part of the transparency domain.
* This use case creates Institution data later consumed in US03 (List Institutions).
* Business-rule validation must be enforced when recording data.