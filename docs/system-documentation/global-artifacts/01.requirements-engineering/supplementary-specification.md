# Supplementary Specification (FURPS+)

## Scope

This supplementary specification covers only Sprint 1 of the Integrative Project MVP.
It consolidates cross-cutting and non-functional requirements that complement the Sprint 1 user stories (US01, US02, US03, US04, US06, US08, US09, US10, US11, US12).

## Functionality

_Specifies functionalities that:  
&nbsp; &nbsp; (i) are common across several US/UC;  
&nbsp; &nbsp; (ii) are not related to US/UC, namely: Audit, Reporting and Security._

### F1. Authentication and role-based authorization

The platform shall require authenticated access for protected operations and enforce role-based permissions.

- Users shall authenticate with credentials before executing role-restricted use cases.
- Roles shall be selected from predefined role sets during registration-related flows.
- Access to operations shall be restricted by role:
	- Administrator: approve/reject registration requests, register institutions.
	- Political Agent: list institutions, submit and consult declarations.
	- Ethics Committee: validate declarations, consult integrated situation.
	- Journalist: analyse income evolution, consult assets (role-limited visibility).
	- Citizen: consult assets (role-limited visibility), submit complaints.

### F2. Reference data management

The platform shall provide and maintain master/reference data required by Sprint 1 business processes.

- Institution type shall be selected from a predefined list.
- Institutions shall be listed grouped by type and sorted alphabetically by name (inside each type).
- Political agents and political roles shall be available as reference data for complaint and analysis operations.

### F3. Declaration lifecycle support

The platform shall support declaration submission, consultation, and validation lifecycle states.

- A declaration can be submitted by a political agent.
- A submitted declaration can be validated by the Ethics Committee.
- If validation fails, inconsistent section/item comments shall be stored.

### F4. Temporal analysis and date-driven queries

The platform shall support date-based and period-based consultation/analysis features.

- Integrated situation shall be consultable on a given date.
- Income evolution shall be analyzable between two dates.
- Assets shall be consultable on a specific date.
- Complaint date shall be on or before current date.

### F5. Complaint management (Sprint 1)

The platform shall allow citizens to register complaints about a political agent and role on a specific date.

- Complaint must include agent, role, date, and description.
- A successful operation shall persist complaint identifier and initial status (for example, SUBMITTED).

### F6. Data visibility and confidentiality by role

The platform shall adapt data disclosure to the consumer role.

- Sensitive data in asset consultation shall be partially omitted according to role.
- Public-facing queries shall only expose the minimum information required by the role.

## Usability

_Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards._

### U1. Clarity and consistency

- The user interface shall use consistent terminology aligned with project domain language (institution, declaration, role, complaint).
- Menus and prompts shall be consistent across operations and roles.

### U2. Error prevention and guidance

- The system shall validate mandatory fields before confirming operations.
- Date inputs shall be validated immediately (for example, complaint date cannot be in the future).
- Invalid operations (for example, unauthorized access, missing required data) shall provide clear corrective feedback.

### U3. Understandable operational feedback

- Every operation shall produce explicit success/failure feedback.
- Validation workflows shall clearly indicate whether a declaration is validated or returned with comments.

### U4. Documentation usability

- User-facing and technical documentation artifacts shall be maintained in English.
- Process artifacts shall remain understandable by all team members and evaluators.

## Reliability

_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._

### R1. Business rule integrity

- Validation of business rules shall be enforced when recording and updating data.
- Operations that violate mandatory constraints shall be rejected without committing invalid state.

### R2. Data consistency and correctness

- Declaration validation results shall remain consistent with the stored declaration content.
- Grouping and alphabetical ordering in institution listings shall be deterministic.
- Role-based visibility rules shall be applied consistently for all requests.

### R3. Controlled failure behavior

- On invalid inputs or unauthorized attempts, the system shall fail safely with informative messages and no partial corruption of state.
- The system shall preserve previously valid data when an operation fails.

### R4. Test-backed reliability

- Unit tests shall cover all non-I/O methods, as defined by project requirements.
- JUnit 5 and JaCoCo reports shall be used to monitor regression and reliability evolution.

## Performance

_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

### P1. Sprint 1 performance objective

Given Sprint 1 scope and expected academic usage, all interactive operations shall complete with perceived immediate response under normal local execution conditions.

### P2. Query performance for lists and consultations

- Institution listing (grouped/sorted) shall execute efficiently for expected Sprint 1 dataset sizes.
- Date and period consultation operations shall remain responsive for expected Sprint 1 historical data volumes.

### P3. Resource usage

- The solution shall run in standard development lab/workstation conditions without excessive CPU or memory usage.
- Data processing logic shall avoid unnecessary repeated scans when producing grouped/sorted outputs.

### P4. Availability expectations

- No 24/7 production SLA is required in Sprint 1.
- The system shall remain available during normal execution and testing sessions.

## Supportability

_The supportability requirements gathers several characteristics, such as:
testability, adaptability, maintainability, compatibility,
configurability, installability, scalability and more._

### S1. Maintainability and extensibility

- Class structure shall follow good object-oriented practices to support feature growth in Sprints 2 and 3.
- The solution shall favor separation of concerns between UI, controllers, domain, and repositories.

### S2. Testability

- Unit tests shall be written with JUnit 5.
- Coverage shall be assessed with JaCoCo to support quality control.
- Non-I/O business logic shall be designed for isolated test execution.

### S3. Documentation support

- Java source code shall include Javadoc where relevant.
- Development artifacts shall be updated incrementally per sprint.
- Images/figures produced in the process shall be stored in SVG format.

### S4. Build and execution support

- The project shall be buildable with Maven using the provided project configuration.
- Team members shall be able to run tests and generate reports through standard Maven goals.

### S5. Compatibility baseline

- The implementation baseline for this repository is Java and Maven toolchain as configured in the project.

## +

### Design Constraints

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._

- The project shall follow an iterative and incremental development process using Scrum.
- The team shall apply Test-Driven Development (TDD) as project guidance.
- Domain design shall reflect transparency-portal concepts (political actors, declarations, institutions, complaints, role-based access).
- Security/privacy concerns shall be considered in design of data visibility rules.
- Requirements engineering, OO analysis, and OO design artifacts shall be maintained and synchronized.

### Implementation Constraints

_Specifies or constraints the code or construction of a system such
such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system._

- Application implementation shall be in Java.
- Authentication shall enforce password policy: seven alphanumeric characters including three uppercase letters and two digits.
- Recognized coding standards shall be followed (for example, CamelCase).
- JUnit 5 shall be used for unit testing.
- JaCoCo plugin shall be used for coverage reporting.
- Code documentation shall be produced with Javadoc.
- Object serialization shall be adopted to support state persistence requirements.

### Interface Constraints

_Specifies or constraints the features inherent to the interaction of the
system being developed with other external systems._

- The solution shall integrate with the provided AuthLib component for authentication/authorization.
- Role checks shall rely on the AuthLib session/role model exposed to the application.
- External interactions in Sprint 1 are limited to local execution and academic tooling; no mandatory integration with third-party public APIs is defined.

### Physical Constraints

_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._

- No specific physical hardware form-factor constraints are imposed for Sprint 1.
- The system is expected to run on standard personal computers used in development and evaluation contexts.
- Repository and documentation assets shall be manageable within typical academic workstation storage and memory capacities.