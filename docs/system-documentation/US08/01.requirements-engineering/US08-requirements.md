# US08 - Validate Declaration

## 1. Requirements Engineering

### 1.1. User Story Description

As a member of the Ethics Committee, I want to validate a submitted Declaration of Interests so that political agents 
are held accountable for the accuracy and completeness of their declarations.

---

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> The Ethics Committee is responsible for managing and supervising the Declarations of Interests of political actors.

> When correct, the declaration is validated. When incorrect, the section/item with inconsistencies must be commented.

**From the Forum:**

> **Question:** Is a distinction supposed to be made between exceptional declarations that rectify previous information 
> and exceptional declarations that significantly alter current information?
>
> **Client Answer:** Not atm.

> **Question:** What occurs when a declaration is rejected?
>
> **Client Answer:** A Declaration of Interests that has been rejected remains in the ‘Rejected’ status.

> **Question:** Is it possible for a validated declaration to be re-opened for further review?
>
> **Client Answer:** The Political Agent will have to submit a new (Extraordinary) Declaration.

---

### 1.3. Acceptance Criteria

* **AC1:** Only members of the Ethics Committee can validate Declarations of Interests.

* **AC2:** Only Declarations of Interests in the `SUBMITTED` status can be validated.

* **AC3:** When correct, the Declaration of Interests is validated.

* **AC4:** When incorrect, the section/item with inconsistencies must be commented.

* **AC5:** When incorrect, the rejection `Annotation` must have a mandatory `targetReference` identifying the inconsistent section/item.

* **AC6:** When incorrect, the Declaration of Interests is rejected and remains in the `REJECTED` status.

---

### 1.4. Found out Dependencies

* There is a dependency on **US006 - Submit Declaration of Interests**, since a submitted declaration must exist before 
it can be validated.

---

### 1.5. Input and Output Data

**Input Data:**

* Selected data:
  * a submitted Declaration of Interests

* Typed data:
  * comments on inconsistent sections/items (when applicable)

**Output Data:**

* Declaration of Interests details
* Validation outcome (validated or rejected with comments)
* (In)Success of the operation

---

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/US08-SSD.svg)

---

### 1.7. Other Relevant Remarks

* If the Political Agent needs to correct or update the 
information, a new extraordinary Declaration of Interests must be submitted.
