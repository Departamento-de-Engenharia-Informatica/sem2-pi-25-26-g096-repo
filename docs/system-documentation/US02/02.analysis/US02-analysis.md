# US02 - OO Analysis

## 2. Analysis

### 2.1. Domain Model Update

This user story reuses the concepts introduced in US01 and extends the registration workflow with the Administrator's decision.

The following concepts are relevant in this user story:

- **RegistrationRequest** – represents the request submitted by a FutureUser. In US02, its lifecycle status changes from `PENDING` to either `ACCEPTED` or `REJECTED`.
- **Administrator** – represents the user responsible for reviewing registration requests and deciding whether they should be accepted or rejected.
- **User** – represents an active platform user. A concrete user is created only when a registration request is accepted.
- **PoliticalAgent**, **Citizen**, **Journalist**, and **EthicsCommitteeMember** – represent the possible concrete user types created after an accepted registration request.
- **Annotation** – represents the rejection reason/comment provided by the Administrator when a registration request is rejected.
- **RequestStatus** – describes the lifecycle state of the registration request.
- **RequestedRole** – describes the role originally requested by the FutureUser.

---

### 2.2. Identified Conceptual Classes

| Category | Conceptual / Candidate Class |
|---|---|
| Business Transactions | RegistrationRequest |
| Transaction line items | Annotation |
| Roles of People or Organizations | FutureUser, User, Administrator, PoliticalAgent, Citizen, Journalist, EthicsCommitteeMember |
| Descriptions of Things | RequestedRole, RequestStatus |

---

### 2.3. Identified Associations

| Concept A           | Association            | Concept B             |
|---------------------|------------------------|-----------------------|
| FutureUser          | requests               | RegistrationRequest   |
| Administrator       | reviews                | RegistrationRequest   |
| RegistrationRequest | refers to              | RequestedRole         |
| RegistrationRequest | has status             | RequestStatus         |
| RegistrationRequest | creates if accepted    | User                  |
| User                | is specialised as      | PoliticalAgent        |
| User                | is specialised as      | Citizen               |
| User                | is specialised as      | Journalist            |
| User                | is specialised as      | EthicsCommitteeMember |
| User                | is specialised as      | Administrator         |
| Annotation          | justifies rejection of | RegistrationRequest   |

---

### 2.4. Identified Attributes

**RegistrationRequest**
- requestDate
- decisionDate
- name
- taxId
- email
- password
- nationalIdentityCardNumber
- pressCardNumber
- requestedPosition
- requestedInstitution

**Annotation**
- text
- creationDate

**User**
- name
- taxId
- email
- password

**Journalist**
- pressCardNumber

---

### 2.5. Domain Model

![Domain Model](svg/US02-DM.svg)

---

### 2.6. Remarks

- **Reuse of US01 concepts:** US02 continues the lifecycle of the `RegistrationRequest` created in US01. A registration request must already exist before it can be accepted or rejected.
- **FutureUser vs User:** before approval, the person is only represented through `FutureUser` and the submitted `RegistrationRequest`. A concrete `User` is created only when the request is accepted.
- **User hierarchy:** `User` is modelled as an abstract class because every active platform user must belong to a concrete role: `PoliticalAgent`, `Citizen`, `Journalist`, `EthicsCommitteeMember`, or `Administrator`.
- **Administrator as reviewer:** the `Administrator` is associated with `RegistrationRequest` because the system must record which administrator accepted or rejected each request.
- **RequestStatus:** `RequestStatus` is modelled as an enumeration because the request can only be in one of the predefined lifecycle states: `PENDING`, `ACCEPTED`, or `REJECTED`.
- **Automatic user creation:** when a request is accepted, the system creates a concrete `User` according to the `RequestedRole`.
- **Rejection reason:** when a request is rejected, an `Annotation` is associated with the `RegistrationRequest` to record the rejection reason/comment.
- **Administrator role not requested:** `Administrator` does not appear in `RequestedRole` because administrator accounts are not created through the self-registration process.