# US02 - Approve/Reject Registration

## 2. Analysis

### 2.1. Relevant Domain Model Excerpt 

![Domain Model](svg/US02-DM.svg)

### 2.2. Other Remarks

The domain model extends US001 by introducing the `User -- Role`association, which represents the role assignment that 
takes place only when the `Administrator` approves the `RegistrationRequest`. A user whose request is still pending or 
has been rejected holds no assigned role (`0..1` multiplicity).

The `status` attribute of `RegistrationRequest` is central to this user story, as it transitions from PENDING to either 
APPROVED or REJECTED as a result of the administrator's decision.