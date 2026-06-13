# US06 - OO Analysis

## 2.1. Domain Model Update

This user story introduces the following new conceptual classes:

- **Declaration** – represents a formal declaration of interests submitted
  by a Political Agent at a specific point in time. Its lifecycle state
  is tracked via DeclarationStatus, and its nature is described by
  DeclarationType. When `type` is `EXCEPTIONAL`, the Declaration also
  records the reason for the amendment and/or a reference to the
  declaration being amended.
- **Position** – represents a professional role (public, private, or
  social) held or previously held, performed at an Institution, with a
  designated Function.
- **Subsidy** – represents financial support received by the Political
  Agent from an Institution.
- **Asset** – represents real estate property (urban or rural) owned by
  the Political Agent, located at a specific Location.
- **SecurityHolding** – represents a quota, share, or holding in a
  company (modelled as an Institution), with a market value, stake and
  percentage.
- **Location** – represents the geographic location of an Asset,
  described by parish, county and district.
- **HouseholdMember** – represents a member of the Political Agent's
  household declared for transparency purposes (partner, descendant, or
  other family member), identified by name, tax identification number,
  birth date, and relationship to the Political Agent.

The concepts **PoliticalAgent**, **Institution**, and **Function** already
exist in the domain (introduced in US01/US02, US03/US04, and US05
respectively) and are reused here.

---

## 2.2. Identified Conceptual Classes

| Category | Conceptual / Candidate Class |
|---|---|
| (Business) Transactions | Declaration |
| Transaction line items | Position, Subsidy, Asset, SecurityHolding, HouseholdMember |
| Roles of People or Organizations | PoliticalAgent (existing) |
| (Other) Organizations | Institution (existing) |
| Catalogs | Function (existing) |
| Places | Location |
| Descriptions of Things | DeclarationType, DeclarationStatus, PositionType, PropertyType, HouseholdRelationship |

---

## 2.3. Identified Associations

| Concept A | Association | Concept B |
|---|---|---|
| PoliticalAgent | submits | Declaration |
| Declaration | typed as | DeclarationType |
| Declaration | has status | DeclarationStatus |
| Declaration | declares | HouseholdMember |
| Declaration | contains | Position |
| Declaration | contains | Subsidy |
| Declaration | contains | Asset |
| Declaration | contains | SecurityHolding |
| Declaration | amends | Declaration |
| Position | classified as | PositionType |
| Position | held at | Institution |
| Position | designated as | Function |
| Subsidy | received from | Institution |
| Asset | classified as | PropertyType |
| Asset | located in | Location |
| SecurityHolding | held in | Institution |
| HouseholdMember | classified as | HouseholdRelationship |

---

## 2.4. Identified Attributes

**Declaration**
- submissionDate
- amendmentReason *(applicable when type is EXCEPTIONAL)*

**HouseholdMember**
- name
- taxId
- birthDate

**Position**
- startDate
- endDate
- remuneration

**Subsidy**
- amount
- nature

**Asset**
- acquisitionValue
- marketValue

**SecurityHolding**
- title
- marketValue
- stake
- percentage

**Location**
- parish
- county
- district

---

## 2.5. Domain Model

![Domain Model](svg/US06-DM.svg)

---

## 2.6. Remarks

- **SecurityHolding vs Participation:** The concept previously referred to
  as Participation is modelled in the global domain as SecurityHolding,
  reflecting the financial instrument nature of quotas, shares and
  holdings more precisely.
- **Attribute vs. Concept:** Declaration, Position, Subsidy, Asset,
  SecurityHolding, HouseholdMember and Location all have internal
  structure and multiple attributes, confirming they must be modelled as
  conceptual classes rather than simple attributes of PoliticalAgent.
- **Attribute vs. Association:** Institution and Function are domain
  concepts (not numbers or plain text), so the relationships between
  Position/Subsidy/SecurityHolding and these concepts are modelled as
  associations, not as attributes.
- **Enumerations:** DeclarationType, DeclarationStatus, PositionType,
  PropertyType and HouseholdRelationship are modelled as enumerations
  because their values are fixed and predefined by the specification or
  acceptance criteria.
- **Subsidy nature:** The `nature` attribute of Subsidy captures the
  descriptive information associated with the declared support or subsidy,
  as required by the specification.
- **No generalisation applied to Asset:** although urban and rural real
  estate could be subclasses, no additional associations or distinct
  behaviours are identified at this stage; the distinction is captured
  via the PropertyType enumeration.
- **HouseholdMember as a domain concept:** HouseholdMember is modelled as
  its own conceptual class, rather than a list of names on PoliticalAgent
  or Declaration, because it has internal structure (name, tax
  identification number, birth date and relationship) and represents
  information that the system must store as part of the declaration
  (AC1).
- **HouseholdRelationship as an enumeration:** HouseholdRelationship is
  modelled as an enumeration because the relationship of a household
  member to the Political Agent is selected from a limited, predefined
  set of values (PARTNER, DESCENDANT, OTHER).
- **HouseholdMember has no income/assets in this US:** following the
  specification, only the identity, tax identification number, birth date
  and relationship of household members are recorded in this user story;
  no financial attributes are associated with HouseholdMember.
- **Mandatory household section:** `Declaration "1" -- "1..*"
  HouseholdMember : declares >` reflects AC1 — a declaration must declare
  at least one household member. This multiplicity is independent of the
  AC7 rule on financial entries (Position, Subsidy, Asset,
  SecurityHolding).
- **amendmentReason as a conditional attribute:** `amendmentReason` is an
  attribute of Declaration that is only meaningful when
  `type == EXCEPTIONAL` (AC6). It is modelled as a single optional
  attribute on Declaration rather than as a separate concept, since it is
  simple descriptive text directly tied to the declaration's lifecycle and
  has no independent identity or behaviour.
- **Self-association for amended declarations:** the `Declaration "0..*"
  -- "0..1" Declaration : amends >` association represents the optional
  reference, for Exceptional declarations, to the previously submitted
  Declaration being corrected (AC6). A Declaration may amend at most one
  other Declaration, and a Declaration may be amended by zero or more
  later Exceptional declarations.
- **Importing from a previous declaration is behavioural, not
  structural:** AC2 (import data from the previous declaration) does not
  introduce new conceptual classes or associations. It is realised at
  design level by retrieving the previous Declaration and converting it
  into a `DeclarationDTO` used to pre-fill the UI; the final submitted
  declaration is still created normally from the edited DTO data. This
  is therefore addressed at the design level (see US006-design.md)
  rather than in the domain model.
- **Initial/Regular submission rules (AC4, AC5) are behavioural:** the
  rules that a Political Agent may submit only one Initial declaration,
  and at most one Regular declaration per calendar year, are business
  rules enforced when a new Declaration is created. They do not require
  additional domain concepts; they are addressed at the design level
  through repository queries (see US006-design.md).
- **Multiplicity justification:** A Declaration must include at least one
  HouseholdMember (AC1). A Declaration may include zero or more instances
  of each financial entry type (Position, Subsidy, Asset,
  SecurityHolding); the constraint that at least one of these must exist
  overall (AC7) is enforced at the application level, not in the domain
  model multiplicities, since the combination of those four collections
  satisfies the rule.