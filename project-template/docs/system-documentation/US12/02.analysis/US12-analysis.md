# US12 - Submit Complaint

## 2. Analysis

### 2.1. Relevant Domain Model Excerpt

[PlantUML source](puml/US12-DM.puml)

### 2.2. Other Remarks

* A `Complaint` is the core domain concept in this user story.
* Each complaint must reference exactly one `PoliticalAgent` and one `PoliticalRole` valid for the reported context.
* `Citizen` is the actor authorized to submit complaints.
* `ComplaintDate` must satisfy the invariant: `complaintDate <= today`.
* Complaint lifecycle starts with status `SUBMITTED` and can later evolve in future user stories.
