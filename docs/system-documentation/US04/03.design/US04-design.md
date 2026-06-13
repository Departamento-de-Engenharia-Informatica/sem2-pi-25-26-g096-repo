# US04 - Register an Institution

## 3. Design

### 3.1. Rationale

| Interaction ID | Question: Which class is responsible for...                 | Answer                          | Justification (with patterns)                                                |
|:---------------|:------------------------------------------------------------|:--------------------------------|:-----------------------------------------------------------------------------|
| Step 1         | ... interacting with the actor?                             | RegisterInstitutionUI           | Pure Fabrication: responsible for handling user interaction.                 |
|                | ... coordinating the US?                                    | RegisterInstitutionController   | Controller pattern: coordinates the use case execution.                      |
|                | ... knowing the user using the system?                      | UserSession                     | Information Expert: knows authenticated user information.                    |
| Step 2         | ... requesting institution type selection?                  | RegisterInstitutionUI           | IE: manages interaction with the actor.                                      |
|                | ... obtaining institution types?                            | InstitutionTypeRepository       | Information Expert: provides predefined InstitutionType list (AC1).          |
| Step 3         | ... saving selected institution type?                       | RegisterInstitutionUI           | IE: temporarily stores user input data.                                      |
| Step 4         | ... requesting institution data (name, taxId)?              | RegisterInstitutionUI           | Pure Fabrication: responsible for user interaction.                          |
| Step 5         | ... validating that name is not null or empty (AC2)?        | Institution                     | Information Expert: owns its data and enforces its own invariants.           |
|                | ... checking for duplicate institutions (AC3)?              | InstitutionRepository           | Information Expert: has access to all persisted institutions.                |
|                | ... creating the Institution object?                        | RegisterInstitutionController   | Creator (delegated): has all data needed to initialise Institution.          |
|                | ... saving the new institution?                             | InstitutionRepository           | Information Expert: manages persistence of Institution objects.              |
| Step 6         | ... informing operation success or failure?                 | RegisterInstitutionUI           | Pure Fabrication: presents result to the actor.                              |

### 3.2. Systematization

According to the taken rationale, the conceptual classes promoted to software classes are:

* Administrator
* Institution
* InstitutionType

> **Note:** `Address` is **not** promoted for this US. Institution address is not part of the data collected during registration in this sprint (the SSD and AC do not reference it), and it does not appear in any step of the rationale. Promoting it without a corresponding interaction would break traceability between requirements and design.

Other software classes (i.e. Pure Fabrication) identified:

* RegisterInstitutionUI
* RegisterInstitutionController
* Repositories
* InstitutionRepository
* InstitutionTypeRepository
* ApplicationSession
* UserSession


## 3.3. Sequence Diagram (SD)

### Full Diagram

![Sequence Diagram - Full](svg/US004-SD-full.svg)

### Split Diagrams

![Sequence Diagram - split](svg/US004-SD-split.svg)


## 3.4. Class Diagram (CD)

![Class Diagram](svg/US004-CD.svg)
