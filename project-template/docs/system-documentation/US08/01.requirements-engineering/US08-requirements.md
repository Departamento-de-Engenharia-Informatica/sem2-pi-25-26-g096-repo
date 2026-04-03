# US08 - Validate Declaration of Interests

## 1. Requirements Engineering

### 1.1. User Story Description

As a member of the Ethics Committee, I want to validate a  submitted Declaration of Interests.

---

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> A member of the Ethics Committee must be able to validate a submitted Declaration of Interests.

**From the client clarifications:**

*(No clarifications available.)*

---

### 1.3. Acceptance Criteria

* **AC1:**  When correct, it is validated.
* **AC2:** When incorrect, the section/item with inconsistencies must be commented.

---

### 1.4. Found out Dependencies

* There is a dependency on **US06 - Submit Declaration of Interests**, since there must be a submitted Declaration of Interests to be validated.

---

### 1.5 Input and Output Data

**Input Data:**

* Selected data:
  * a submitted Declaration of Interests

* Typed data:
  * comments on inconsistent sections/items

**Output Data:**

* submitted Declaration of Interests data
* validation result
* (In)Success of the operation

---

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/US008-SSD.svg)

---

### 1.7 Other Relevant Remarks

* The validation of a Declaration of Interests is performed by a member of the Ethics Committee.
* If the Declaration of Interests is incorrect, the inconsistent section/item must be commented.