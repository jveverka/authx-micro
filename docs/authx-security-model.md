# Security Model
See also [internal data model](authx-data-model.md).

## Data Model Init
Data model is initialized on startup in case Global admins are not created. 
* if internal data model is populated, initial data model is ignored.
* if internal data model is not populated, global admin project is created with single admin user.

## Global Admins
* can get access/refresh tokens
* can create new projects
* can delete any existing project
* can modify authx global admin projects list
* can modify authx global settings

## Project Admins
* can get access/refresh tokens
* can fully manage own project (users/clients/roles/groups/permissions)
* can delete own project

## Regular Users and Clients
* can get access/refresh tokens
* can change own credentials
* can update own description
