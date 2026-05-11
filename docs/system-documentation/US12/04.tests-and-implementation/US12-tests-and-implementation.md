# US12 - Submit Complaint

## 4. Tests and Implementation

### 4.1. Tests

Recommended tests when implementation starts:

* Controller returns available Political Agents and Roles for complaint form.
* Incident date validation rejects future dates.
* Controller retrieves Citizen from authenticated session and blocks non-citizen users.
* Mandate retrieval by agent/role/date succeeds when contextual data exists.
* Complaint is created with unique identifier and initial status SUBMITTED.
* Complaint is persisted in repository after successful creation.

### 4.2. Implementation Notes

Implementation should ensure:

* Mandatory fields enforcement: political agent, role, incident date, and description.
* Invariant enforcement: incidentDate <= today.
* Atomic success semantics: if complaint is created, it must be stored.

### 4.3. Status

Design artifacts are completed.

Code implementation and automated tests are pending.
