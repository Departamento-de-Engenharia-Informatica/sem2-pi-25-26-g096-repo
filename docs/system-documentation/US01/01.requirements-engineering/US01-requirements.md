# US01 — User Registration Request

## 1. Requirements Engineering

### 1.1. User Story Description

As a future user, I want to submit a registration request to the platform, selecting the role that reflects my intended 
level of access and responsibilities.

---

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> The system shall allow any external party to initiate a registration process before gaining access to any protected 
> functionality.

> The requested role determines which permissions will be provisionally associated with the account pending administrator 
> review.

> An administrator must explicitly approve or reject the request (US002) before the account becomes operative.

**From the Forum:**

> **Question:** Can any person request any role, or are there restrictions on who can request certain roles? Is there 
> any validation required for specific roles (e.g., a journalist requiring union registration)?
>
> **Client Answer:** Yes, but specific details are required for each role. For example:          
> a) ordinary citizen – national identity card number     
> b) journalist – press card number     
> c) political representative – national identity card number, and the position/role (parish council chair, councillor, 
> member) for which they were elected, and the institution (parish council, municipal council)


> **Question:** Regarding US01, when a Political Agent is filling out their registration request, they must provide their 
> elected position/function and institution. Must the user select these from a pre-defined list of Institutions and 
> Functions that already exist in the system (created via US04 and US05)? Or, can the user manually type in a new 
> institution/function that hasn't been recorded yet? If they can type a new one, should the Administrator be responsible 
> for officially registering that new institution/function into the system at the exact moment they approve the user's 
> registration request (US02)?
>
> **Client Answer:** yes. not "or" but "and", yes. yes.


---

### 1.3. Acceptance Criteria

* **AC1:** The role must be selected from a predefined list of available roles.

* **AC2:** The system must request the mandatory details according to the selected role.

* **AC3:** When requesting the **Ordinary Citizen** role, the future user must provide their national identity card 
number.

* **AC4:** When requesting the **Journalist** role, the future user must provide their press card number.

* **AC5:** When requesting the **Political Agent** role, the future user must provide their national identity card 
number, the elected position/function, and the institution for which they were elected.

* **AC6:** When requesting the **Political Agent** role, the future user must be able to indicate the elected 
position/function and institution either by selecting existing records or by providing new ones when they are not yet registered in the system.

* **AC7:** The submitted registration request must remain in the `PENDING` status until it is accepted or rejected by an Administrator.

* **AC8:** The password must contain seven alphanumeric characters, including at least three uppercase letters and two digits.


---

### 1.4. Found-out Dependencies

* **US02 — Approve/Reject Registration:** The lifecycle of the entity created in US01 is completed by US02. Without 
this story, no request can transition from PENDING to an active account.

---

### 1.5. Input and Output Data

**Input Data:**

* Typed data:
  * Name
  * Tax identification number
  * Email
  * Password
  * National identity card number, when requesting the Ordinary Citizen role
  * Press card number, when requesting the Journalist role
  * National identity card number, when requesting the Political Agent role
  * Elected position/function, when requesting the Political Agent role and the position/function is not selected from existing records
  * Institution, when requesting the Political Agent role and the institution is not selected from existing records

* Selected data:
  * Role, from the predefined list of available roles
  * Existing elected position/function, when requesting the Political Agent role
  * Existing institution, when requesting the Political Agent role

**Output Data:**

* List of available roles for selection
* List of existing positions/functions, when requesting the Political Agent role
* List of existing institutions, when requesting the Political Agent role
* Summary of the submitted registration request
* Submission acknowledgement with the registration request status

---

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/US01-SSD.svg)

---

### 1.7. Other Relevant Remarks

* No personally identifiable information is exposed to other users during the pending phase.
* The request entity must support serialization to guarantee data persistence across application restarts.