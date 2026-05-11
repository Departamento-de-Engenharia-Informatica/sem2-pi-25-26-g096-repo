# US011 - Consult Assets of a Political Agent

## 4. Tests and Implementation

### 4.1. Tests

This user story is a read-only consultation with role-based visibility filtering.

Recommended tests when implementation starts:

* Controller returns political agent list ordered according to expected policy.
* Consultation requests declarations with selected Political Agent and reference date.
* Assets are extracted only from declaration sections valid on selected date.
* Citizen view masks sensitive fields according to AC1.
* Journalist view applies role-specific masking according to AC1.
* Unauthorized roles cannot access this consultation.

### 4.2. Implementation Notes

Implementation should ensure:

* Temporal consistency for declaration data used in consultation.
* Strict read-only behavior for consultation operations.
* Role-based masking logic isolated in a dedicated service/component.

### 4.3. Status

Design artifacts are completed.

Code implementation and automated tests are pending.
