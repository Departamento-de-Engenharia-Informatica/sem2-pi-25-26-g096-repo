# US009 - Consult Integrated Situation of a Political Agent

## 4. Tests and Implementation

### 4.1. Tests

This user story is a read-only consultation flow.

Recommended unit tests when implementation starts:

* Controller returns non-empty Political Agent list when repository contains agents.
* Controller requests declarations using selected Political Agent and reference date.
* Controller obtains Ethics Committee member from authenticated session before consultation.
* Integrated situation aggregation only includes data valid on the selected reference date.
* Access is denied for users without Ethics Committee role.

### 4.2. Implementation Notes

Implementation should ensure:

* Temporal consistency: the integrated situation must match the selected reference date.
* Read-only behavior: no declaration or mandate data is changed by the consultation.
* Clear separation of concerns:
  * controller orchestrates,
  * repositories retrieve,
  * domain role (EthicsCommitteeMember) consolidates.

### 4.3. Status

Design artifacts are completed.

Code implementation and automated tests are pending.