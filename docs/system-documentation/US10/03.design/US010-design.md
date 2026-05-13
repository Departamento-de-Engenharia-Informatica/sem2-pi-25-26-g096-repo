# US10 - Analyse Income Evolution of a Political Agent

## 3. Design

### 3.1. Rationale

| Interaction ID | Question: Which class is responsible for... | Answer | Justification |
|---|---|---|---|
| Step 1 | ... interacting with the Journalist? | AnalyseIncomeEvolutionUI | Pure Fabrication: there is no reason to assign this responsibility to any existing domain class. |
| Step 1 | ... coordinating the use case? | AnalyseIncomeEvolutionController | Controller: coordinates the user story and delegates responsibilities. |
| Step 2 | ... checking the current session? | ApplicationSession | Information Expert: it provides access to the current session. |
| Step 3 | ... providing access to repositories? | Repositories | Pure Fabrication / Singleton: centralises access to system repositories. |
| Step 4 | ... listing available Political Agents? | PoliticalAgentRepository | Information Expert: it manages the collection of PoliticalAgent instances. |
| Step 5 | ... transforming PoliticalAgent objects into DTOs? | PoliticalAgentMapper | Pure Fabrication: isolates mapping logic and reduces coupling between UI and domain. |
| Step 6 | ... finding the selected Political Agent? | PoliticalAgentRepository | Information Expert: it manages PoliticalAgent instances and can retrieve one by identifier. |
| Step 7 | ... finding Declarations in a given period? | DeclarationRepository | Information Expert: it manages Declaration instances and can query them by PoliticalAgent and date interval. |
| Step 8 | ... calculating income values from a Declaration? | Declaration | Information Expert: it owns Positions and Subsidies and can calculate its own income values. |
| Step 9 | ... transforming income evolution data into DTOs? | IncomeEvolutionMapper | Pure Fabrication: isolates mapping logic and prepares data for the UI. |
| Step 10 | ... transporting income evolution data to the UI? | IncomeEvolutionDTO | DTO: decouples the UI from the domain model. |
| Step 11 | ... displaying the income evolution? | AnalyseIncomeEvolutionUI | Pure Fabrication: responsible for user interaction and output. |

### Systematization

According to the taken rationale, the conceptual classes promoted to software classes are:

* PoliticalAgent
* Declaration
* Position
* Subsidy
* DeclarationType

Other software classes identified:

* AnalyseIncomeEvolutionUI
* AnalyseIncomeEvolutionController
* ApplicationSession
* UserSession
* Repositories
* PoliticalAgentRepository
* DeclarationRepository
* PoliticalAgentMapper
* IncomeEvolutionMapper
* PoliticalAgentDTO
* IncomeEvolutionDTO

---

## 3.2. Sequence Diagram (SD)

### Full Diagram

This diagram shows the full sequence of interactions between the classes involved in the realization of this user story.

![Sequence Diagram - Full](svg/US010-SD-full.svg)

### Split Diagrams

The following diagram shows the same sequence of interactions between the classes involved in the realization of this user story, but it is split in partial diagrams to better illustrate the interactions between the classes.

It uses Interaction Occurrence.

![Sequence Diagram - Split](svg/US010-SD-split.svg)

**Get Political Agents List**

![Sequence Diagram - Partial - Get Political Agents List](svg/US010-SD-partial-get-political-agents-list.svg)

**Get Political Agent**

![Sequence Diagram - Partial - Get Political Agent](svg/US010-SD-partial-get-political-agent.svg)

**Income Evolution to DTOs**

![Sequence Diagram - Partial - Income Evolution to DTOs](svg/US010-SD-partial-income-evolution-to-dtos.svg)

---

## 3.3. Class Diagram (CD)

![Class Diagram](svg/US010-CD.svg)