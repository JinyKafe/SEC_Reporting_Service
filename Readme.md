# SEC Reporting Service
## Functional Specification & Project description

* [Jira SWKS-483](https://jira.scopegroup.com/browse/SWKS-483) to see the project tasks in Jira

* [Confluence: SEC Reporting Functional Specification](https://confluence.scopegroup.com/pages/editpage.action?pageId=69992746) for functional specification & requirements

* [Confluence: NRSRO Application](https://confluence.scopegroup.com/display/SCOP/Project+documents+and+resources) for general description of the NRSRO project

## Technical Specification
* [Release#1 / 16.10.2020](doc\release\1\architecture.md) 
* [Release#2 / planned](doc\release\2\architecture.md) 


## API endpoints
TODO under development
Currently we have an API to get reports for all Obligor and Issuer entries in DB

* environment details
```
# localhost 
    url : http://localhost:8080
    user: scope
    password: temp
# staging
    url: https://secreporting-staging.dev.scope-one.com
    user: scope
    password: oquaeChoov3ufie4bieV1ahbe
```
* trigger endpoint
```json
POST {url}/api/v1/xbrl/produce
Authorization: Basic {user} {password}
```
* management api (health, info, etc.)
```json
POST {url}/internal/health
Authorization: Basic {user} {password}
```
swagger-ui
```json
{url}/swagger-ui.html

```
```

## [Running locally](doc/chapters/1_running_locally.md)

## [Running in Environments](doc/chapters/5_running_in_envrironment.md)

```