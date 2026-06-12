# US02 - Approve/Reject Registration

## 1. Requirements Engineering

### 1.1. User Story Description

As an Administrator, I want to accept or reject registration requests so that only authorized users gain access to the 
platform.

---

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> The system must allow an Administrator to accept or reject registration requests submitted by future users.

> The System Administrator is responsible for managing the system and its access privileges.

> A notification message must be sent to the user informing them of the decision (accepted or rejected).

> The system must support multiple email service providers (e.g., Gmail, DEI service). The specific provider to be used must be defined through a configuration file, and at least two different implementations should be available.


**From the Forum:**

> **Question:** Taking into account the nature of the system, should it record the timestamp and the administrator 
> responsible for approving or rejecting a registration submission?
> 
> **Client Answer:** Yes; and in the event of rejection, a reason must be provided/recorded.

> **Question:** If a request is denied by the system administrator, does it need to have a comment explaining the 
> decision? 
> 
> **Client Answer:** yes.

> **Question:** Does the data like name, email and role get displayed in the list of pending requests or only when we 
> select one?
>
> **Client Answer:** The later makes more sense.

> **Question:** Regarding the management of registration requests (US02), after an administrator approves a request, 
> should the system automatically create and activate the user account, or is any additional action required?
>
> **Client Answer:** Automatically create.

---

### 1.3. Acceptance Criteria

* **AC1:** The Administrator must be able to select a pending registration request and consult its details.

* **AC2:** The Administrator must be able to accept or reject the selected registration request.

* **AC3:** When accepting or rejecting a registration request, the system must record the timestamp and the Administrator responsible for the decision.

* **AC4:** When rejecting a registration request, the Administrator must provide a rejection reason/comment.

* **AC5:** When accepting a registration request, the system must automatically create the corresponding user account.

* **AC6:** A notification message must be sent to the user informing them of the decision (accepted or rejected).

* **AC7:** The system must support multiple email service providers. The provider to be used must be defined through a configuration file, and at least two implementations must be available.
---

### 1.4. Found out Dependencies

* **US01 — Registration Request:** since a request must exist before it can be accepted or rejected.

---

### 1.5. Input and Output Data

**Input Data:**

* Selected data:
  * Pending registration request
  * Decision: accept or reject
  * Email service provider, indirectly selected through the configuration file

* Typed data:
  * Rejection reason/comment, when rejecting the request

**Output Data:**

* List of pending registration requests
* Details of the selected request
* Result of the operation (success or failure)
* Notification message sent to the requester with the decision outcome

---

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/US02-SSD.svg)

---

### 1.7. Other Relevant Remarks

* An accepted request results in an active user account with the role and permissions originally requested.
* A rejected request does not create any account; the requester may be informed of the outcome.
* Email provider selection is a technical configuration concern and should not be hard-coded in the use case controller.
