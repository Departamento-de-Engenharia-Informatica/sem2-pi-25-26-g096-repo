# US03 - List Institutions

## 1. Requirements Engineering

### 1.1. User Story Description

As a Political Agent, I want to list Institutions (Companies, Political Parties, Foundations, Institutes, Associations).

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

> Institutions must be categorized by their specific type (Company, Political Party, etc.).

> Each institution has, at minimum, a name and a type.

> The system should allow a clear view of all registered entities.

**From the client clarifications:**

> **Question:** Should the list show all institutions at once?
>
> **Answer:** Yes, but they must be grouped by their type.

> **Question:** Do institutions need to be entered manually, or will data be provided?
>
> **Answer:** Some test data will be provided, but the system should allow creating institutions through the dedicated user stories.

---

### 1.3. Acceptance Criteria

* **AC1:** The institutions must be grouped by type and listed alphabetically by institution name inside each type.

---

### 1.4. Found out Dependencies

* There is a dependency on US04, because institutions must be previously registered.
* There is a dependency on authentication and authorization, because this operation is available to Political Agent users.

---

### 1.5 Input and Output Data

**Input Data:**

* Selected data:
    * institution type(s) to filter the visualization 

* Typed data:
    * none

**Output Data:**

* Grouped list of institution types
* For each type, a list of institutions sorted alphabetically by name
* (In)Success of the operation

---

### 1.6. System Sequence Diagram (SSD)
![System Sequence Diagram](svg/US03-SSD.svg)
---

### 1.7 Other Relevant Remarks

* The supported institution types in Sprint 1 are: Companies, Political Parties, Foundations, Institutes, and Associations.
* If no institutions exist, the system should return an empty result in a valid grouped format.