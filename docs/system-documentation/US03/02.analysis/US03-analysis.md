# US03 - List Institutions

## 2. Analysis

### 2.1. Relevant Domain Model Excerpt

![Domain Model](svg/US03-DM.svg)

### 2.2. Other Remarks

`Institution` is modeled as a catalog entity classified by `InstitutionType`, which directly supports AC1 of US03: institutions must be grouped by type. The type is not free text; it is selected from a predefined enumeration to guarantee consistent grouping semantics.

The alphabetical ordering requirement inside each group is supported by the `name` attribute of `Institution`. Since ordering is a presentation/query concern, no additional domain concept is required beyond the institution identity and classification.

US03 is a read operation over previously registered catalog data. For that reason, the excerpt focuses on the stable catalog structure (`Institution` + `InstitutionType`) and on the fact that institutions are administratively maintained before being listed.

This use case depends on US04 for data existence, because institutions only become listable after registration. Access restriction to Political Agent users is an application authorization rule and therefore is enforced at service/interface level rather than by introducing extra domain associations.