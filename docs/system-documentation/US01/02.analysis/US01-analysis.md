# US01 — User Registration Request

## 2. Analysis

### 2.1. Relevant Domain Model Excerpt 

![Domain Model](svg/US01-DM.svg)

### 2.2. Other Remarks

`User` is modeled as an abstract class because no user exists on the platform without a concrete role — every user is
a PoliticalAgent, Citizen, Journalist, EthicsCommitteeMember, or Administrator. At the registration stage, however,
the concrete subclass does not yet exist: the user is only requesting a role, not yet assigned one.

`RequestedRole` is associated with `RegistrationRequest` rather than directly with `User` because at this stage the
role is only being requested — no assignment has taken place yet. The actual creation of the concrete user subclass
occurs only upon administrator approval (US02).

`RequestedRole` is modeled as an enumeration because US01 AC1 explicitly states that "the role must be selected from
a predefined list of available roles". The Administrator role is absent from this enum because administrator accounts
are not self-registered — they are managed internally.

The multiplicity `0..*` on the `RegistrationRequest` side reflects that a user may submit multiple requests over time
— for example, after a previous request was rejected. This preserves an auditable history of all registration attempts.

`RequestStatus` tracks the lifecycle of each request (PENDING, ACCEPTED, REJECTED), which is necessary to support the
administrator's decision workflow in US02.