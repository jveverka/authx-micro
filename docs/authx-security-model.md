# Security Model
See also [internal data model](authx-data-model.md).

## Data Model Init
Data model is initialized on startup in case Global admins are not created. 
* DBD

## Global Admins
* can get access/refresh tokens
* can create new projects
* can delete existing projects

## Project Admins
* can get access/refresh tokens
* can fully manage own project (users/clients/roles/groups/permissions)
* can delete own project

## Regular Users
* can change own credentials
* can get access/refresh tokens