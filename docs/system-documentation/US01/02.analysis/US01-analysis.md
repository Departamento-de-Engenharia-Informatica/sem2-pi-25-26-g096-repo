# US01 — OO Analysis

## 2. Analysis

### 2.1. Domain Model Update

This user story introduces the following conceptual classes:

- **FutureUser** – represents a person who is not yet registered in the platform but intends to request access with a specific role.
- **RegistrationRequest** – represents the formal request submitted by a FutureUser to access the platform. It stores the requested role, the request status, the request date, and the data required for the selected role.
- **RequestedRole** – describes the role requested by the FutureUser. It is selected from a predefined list of available roles.
- **RequestStatus** – describes the lifecycle state of the registration request: pending, accepted, or rejected.

The concept **User** already exists in the global domain model, but it is not created during US01. A concrete User is only created after the registration request is approved in US02.

---

### 2.2. Identified Conceptual Classes

| Category | Conceptual / Candidate Class |
|---|---|
| Business Transactions | RegistrationRequest |
| Roles of People or Organizations | FutureUser |
| Descriptions of Things | RequestedRole, RequestStatus |

---

### 2.3. Identified Associations

| Concept A | Association | Concept B |
|---|---|---|
| FutureUser | requests | RegistrationRequest |
| RegistrationRequest | refers to | RequestedRole |
| RegistrationRequest | has status | RequestStatus |

---

### 2.4. Identified Attributes

**RegistrationRequest**
- requestDate
- name
- taxId
- email
- password
- nationalIdentityCardNumber
- pressCardNumber
- requestedPosition
- requestedInstitution

---

### 2.5. Domain Model

![Domain Model](svg/US01-DM.svg)

---

### 2.6. Remarks

- **FutureUser vs User:** `FutureUser` is used because, at this stage, the person is not yet an active platform user. The creation of a concrete `User` only happens after administrator approval in US02.
- **RequestedRole as enumeration:** `RequestedRole` is modelled as an enumeration because the role must be selected from a predefined list of available roles.
- **RequestStatus as enumeration:** `RequestStatus` is modelled as an enumeration because each request has a lifecycle state: `PENDING`, `ACCEPTED`, or `REJECTED`.
- **Role-specific data:** some attributes of `RegistrationRequest` are only applicable to specific requested roles. For example, `pressCardNumber` applies to Journalist requests, while `requestedPosition` and `requestedInstitution` apply to Political Agent requests.
- **No direct creation of User in US01:** this user story only creates a registration request. The approval or rejection of that request, and the possible creation of the corresponding user account, is handled in US02.