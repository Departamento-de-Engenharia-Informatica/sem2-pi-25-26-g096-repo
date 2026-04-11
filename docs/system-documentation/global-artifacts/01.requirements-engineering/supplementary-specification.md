# Supplementary Specification (FURPS+)


## Functionality

_Specifies functionalities that: (i) are common across several US/UC; (ii) are not related to a single US/UC (for example, security, reporting, and audit)._ 

### General Functionality

- The system must enforce authentication and role-based access control.
- The system must support controlled user registration with predefined roles.
- The system must validate business rules before accepting or updating data.
- The system must restrict sensitive information according to user role.
- The system must support consultation and analysis of historical information by date/period.
- The system must support declaration validation by the Ethics Committee.
- The system must support complaint submission by authenticated citizens.

### Data and Entity Management

- The system must manage reference entities required in Sprint 1 (for example, institutions and institution types).
- Institution types must come from a predefined list.
- Institution listing must be grouped by type and ordered alphabetically by name.
- The system must store submitted declarations and related validation comments.
- The system must keep historical records needed for date-based queries.

## Usability

_Evaluates interface quality, error prevention, consistency, and user guidance._

- The interface must be clear, consistent, and easy to use.
- The system must provide clear success/error feedback for operations.
- The system must present data in a structured and readable format.
- The interface must adapt visible information to the user role.
- Documentation and user-facing texts must be in English.

## Reliability

_Refers to integrity, consistency, controlled failure behavior, and data persistence._

- The system must ensure data integrity and consistency during all operations.
- Invalid operations must be rejected without corrupting stored data.
- The system must keep stable behavior during normal usage.
- The system must support persistence between executions.

## Performance

_Evaluates response time and resource usage under expected Sprint 1 load._

- The system must provide low response times for normal Sprint 1 usage.
- List and consultation operations must execute efficiently for expected dataset sizes.
- The system must use CPU and memory efficiently on standard development machines.

## Supportability

_Covers maintainability, testability, and development support._

- The solution must follow object-oriented principles and modular design.
- The codebase must be easy to maintain and extend in future sprints.
- The team must follow recognized coding standards (for example, CamelCase).
- Unit testing must be supported for core business logic.
- Javadoc and project artifacts must be kept updated.
- Diagrams and images must be stored in SVG format.

## +

### Design Constraints

- The project must follow Scrum in iterative and incremental sprints.
- The implementation approach must follow TDD.
- The main application implementation for this repository must be in Java.

### Implementation Constraints

- Password policy: seven alphanumeric characters, including three uppercase letters and two digits.
- Unit tests must use JUnit 5.
- JaCoCo must be used for coverage reporting.
- Data persistence must be supported through object serialization.
- Unit tests are required for all methods except I/O methods.

### Interface Constraints

- Authentication and authorization must integrate with AuthLib.
- Role validation must use the session/role model provided by AuthLib.

### Physical Constraints

- No special hardware constraints are defined for Sprint 1.
- The solution must run on standard academic/development computers.
