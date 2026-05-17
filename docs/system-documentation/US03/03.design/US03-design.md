# US03 - List Institutions

## 3. Design

### 3.1. Rationale

| Interaction ID | Question: Which class is responsible for...                     | Answer                        | Justification (with patterns)                                          |
|:---------------|:----------------------------------------------------------------|:------------------------------|:-----------------------------------------------------------------------|
| Step 1         | ... interacting with the actor?                                 | ListInstitutionsUI            | Pure Fabrication: handles all user interaction.                        |
|                | ... coordinating the US?                                        | ListInstitutionsController    | Controller pattern: coordinates system operations.                     |
|                | ... knowing the user using the system?                          | UserSession                   | Information Expert: knows authenticated user information.              |
| Step 2         | ... obtaining available institution types?                      | InstitutionTypeRepository     | Information Expert: manages InstitutionType persistence.               |
|                | ... requesting institution type selection?                      | ListInstitutionsUI            | IE: responsible for interaction with the actor.                        |
| Step 3         | ... saving selected institution type?                           | ListInstitutionsUI            | IE: temporarily keeps user input data.                                 |
| Step 4         | ... retrieving institutions by selected type?                   | InstitutionRepository         | IE: maintains Institution persistence and retrieval logic.             |
| Step 5         | ... ordering institutions alphabetically?                       | ListInstitutionsController    | Controller applies application logic before presentation.              |
| Step 6         | ... presenting institutions grouped by type and success?        | ListInstitutionsUI            | Pure Fabrication: responsible for displaying results.                  |

### Systematization ##

According to the taken rationale, the conceptual classes promoted to software classes are:

* Institution
* InstitutionType
* NatureOfInstitution
* Address

Other software classes (i.e. Pure Fabrication) identified:

* ListInstitutionsUI
* ListInstitutionsController
* Repositories
* InstitutionRepository
* InstitutionTypeRepository
* ApplicationSession
* UserSession


## 3.2. Sequence Diagram (SD)

### Full Diagram

![Sequence Diagram - Full](svg/US003-SD-full.svg)

### Split Diagrams

![Sequence Diagram - split](svg/US003-SD-split.svg)


## 3.3. Class Diagram (CD)

![Class Diagram](svg/US003-CD.svg)
