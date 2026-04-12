# US011 - Consult Assets of a Political Agent

## 2. Analysis

### 2.1. Relevant Domain Model Excerpt 

![Domain Model](svg/US011-DM.svg)

### 2.2. Other Remarks

The assets consultation is grounded on declarations submitted by the selected political agent and on the asset-related declaration section.

The relevant core path is Declaration -> AssetSection -> AssetField -> Property, including property type and location.

The selected date defines the temporal scope of the consultation, so only data valid on that date should be shown.

Visibility is role-dependent for Citizen and Journalist users, and sensitive information must be partially omitted before presentation.

This operation remains strictly read-only.