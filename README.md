# applications.paper-trail.financial-data-provider-service

## TODOs

- Updated POST to have "created" instead of "ok"
- Check out what the "data" type is instead of "class" for User

## Definition of Done

<details>
  <summary>This is what is needed to close out a feature branch and created/merge a PR.</summary>

- Contract created/updated
- Dependencies added to pom(s) are commented with what their usage is
- Layers are created/updated and follows naming conventions:
    - Controller
    - Service
    - Repository
    - DAO
    - DTO
- Features and tests are added/updated
- API collection (Bruno) is updated and committed to api-client repository
- Bump the version of the app in the pom
- Update the [change log](./CHANGELOG.md)

</details>

## Op Docs

| Endpoint |                                             |
|---------:|:--------------------------------------------| 
|   Health | http://localhost:8080/actuator/health       |
|     Info | http://localhost:8080/actuator/info         |
|  Swagger | http://localhost:8080/swagger-ui/index.html |