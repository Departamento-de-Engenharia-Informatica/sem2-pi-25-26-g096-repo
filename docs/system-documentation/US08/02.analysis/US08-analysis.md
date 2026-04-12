# US08 - Validate Declaration

## 2. Analysis

### 2.1. Relevant Domain Model Excerpt 

![Domain Model](svg/US08-DM.svg)

### 2.2. Other Remarks

`Validation` is introduced as a first-class concept rather than a simple association between `EthicsCommitteeMember`
and `Declaration`. This is justified because a validation is a deliberate, recorded act with its own date and a formal
verdict — it is not merely a link between two entities, but a domain event with legal significance.

`ValidationVerdict` captures the outcome of the validation act: APPROVED or REJECTED. This is kept separate from
`DeclarationStatus` because the two serve different purposes — `DeclarationStatus` reflects the current state of the
declaration in its lifecycle (SUBMITTED, VALIDATED, REJECTED), while `ValidationVerdict` records the Ethics Committee
Member's specific decision in a given validation act. A declaration may in principle be subject to multiple validation
attempts, each with its own verdict.

`Annotation` is contained within `Validation` by composition, meaning annotations only exist in the context of a
validation act. This reflects US08 AC2: "when incorrect, the section/item with inconsistencies must be commented".
Annotations do not exist independently of the validation that produced them.

Each `Annotation` targets exactly one declared item — either a `Subsidy`, `Asset`, `Position`, or `SecurityHolding` —
with all four associations being optional (`0..1`). This allows the Ethics Committee Member to pinpoint precisely which
item in the declaration contains the inconsistency, rather than commenting on the declaration as a whole. A validation
that is APPROVED typically contains no annotations, while a REJECTED validation must contain at least one.