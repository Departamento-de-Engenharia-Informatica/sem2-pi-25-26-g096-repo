# US08 - OO Analysis

## 2. Analysis

### 2.1. Domain Model Update

This user story introduces the following new conceptual classes:

- **Validation** – represents the formal act by which an Ethics Committee Member evaluates a submitted Declaration of Interests. It records the validation date and the validation verdict.
- **Annotation** – represents a comment produced during a validation when inconsistencies are found in a declaration.
- **ValidationVerdict** – describes the outcome of the validation act: approved or rejected.

The concepts **EthicsCommitteeMember**, **Declaration**, **DeclarationStatus**, **Subsidy**, **Asset**, **Position**, and **SecurityHolding** already exist in the domain and are reused here.

---

### 2.2. Identified Conceptual Classes

| Category                                 | Conceptual / Candidate Class              |
|------------------------------------------|-------------------------------------------|
| Business Transactions                    | Validation                                |
| Transaction line items                   | Annotation                                |
| Roles of People or Organizations         | EthicsCommitteeMember                     |
| Product/Service related to a Transaction | Declaration                               |
| Containers                               | Declaration                               |
| Elements of Containers                   | Subsidy, Asset, Position, SecurityHolding |
| Descriptions of Things                   | ValidationVerdict, DeclarationStatus      |

---

### 2.3. Identified Associations

| Concept A             | Association | Concept B         |
|-----------------------|-------------|-------------------|
| EthicsCommitteeMember | performs    | Validation        |
| Validation            | evaluates   | Declaration       |
| Validation            | has verdict | ValidationVerdict |
| Declaration           | has status  | DeclarationStatus |
| Validation            | contains    | Annotation        |
| Annotation            | comments on | Declaration       |
| Declaration           | contains    | Subsidy           |
| Declaration           | contains    | Asset             |
| Declaration           | contains    | Position          |
| Declaration           | contains    | SecurityHolding   |

---

### 2.4. Identified Attributes

**Validation**
- validationDate

**Annotation**
- text
- creationDate

**Declaration**
- submissionDate

**Subsidy**
- amount
- nature

**Asset**
- acquisitionValue
- marketValue

**Position**
- startDate
- endDate
- remuneration

**SecurityHolding**
- title
- marketValue
- stake
- percentage

---

### 2.5. Domain Model

![Domain Model](svg/US08-DM.svg)

---

### 2.6. Remarks

- **Validation as a concept:** `Validation` is modelled as a conceptual class because it is a recorded act with its own date and verdict, not just a simple association between `EthicsCommitteeMember` and `Declaration`.
- **ValidationVerdict vs DeclarationStatus:** `ValidationVerdict` records the Ethics Committee Member's decision in the validation act, while `DeclarationStatus` represents the lifecycle state of the declaration.
- **Annotation as part of Validation:** `Annotation` is contained by `Validation` because comments only exist in the context of a validation act.
- **Annotation target:** in the current domain model, `Annotation` is associated with `Declaration`. The specific section or item being commented is represented through the annotation text, rather than through separate associations to `Subsidy`, `Asset`, `Position`, or `SecurityHolding`.
- **Declared items:** `Subsidy`, `Asset`, `Position`, and `SecurityHolding` are included because they are the declaration items that may contain inconsistencies during validation.
- **Approved vs rejected validation:** an approved validation typically contains no annotations, while a rejected validation must contain at least one annotation/comment explaining the inconsistency found.