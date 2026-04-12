# US02 - Approve/Reject Registration

## 2. Analysis

### 2.1. Relevant Domain Model Excerpt 

![Domain Model](svg/US02-DM.svg)

### 2.2. Other Remarks

This diagram extends US01 by showing the full user hierarchy and the Administrator's role in the registration workflow.

The concrete subclasses of `User` (PoliticalAgent, Citizen, Journalist, EthicsCommitteeMember) represent the outcome
of an accepted registration — when the Administrator approves a `RegistrationRequest`, a user of the type corresponding
to the `RequestedRole` is created. Prior to approval, only the abstract `User` and the pending `RegistrationRequest`
exist.

The `Administrator` is also a subclass of `User` but does not appear in `RequestedRole` because administrator accounts
are not created through the self-registration process — they are managed internally by the system.

The `status` attribute of `RegistrationRequest` transitions from PENDING to either ACCEPTED or REJECTED as a result of
the administrator's decision. This lifecycle is central to US02 and is what triggers the creation of the concrete user
or the permanent rejection of the request.

The multiplicity `0..*` on `RegistrationRequest` reflects that the same user may have submitted multiple requests
historically — for instance, a user whose first request was rejected may submit a new one. Each request is an
independent, auditable record.