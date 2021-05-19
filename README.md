# APIsParser
APIsParser allows you to generate a json representation of your Java API resources and visualise them using a customised version of [WebVOWL](https://github.com/VisualDataWeb/WebVOWL).


![2015-09-20 17 02 28](https://user-images.githubusercontent.com/5663078/99097077-065abd00-25cf-11eb-9dd3-a9c8bb9ad303.png)


----------------------------------

- [Setup](#setup)
  - [Download the repository](#download-the-repository)
  - [Generate the JAR](#generate-the-jar)
  - [Run APIsParser](#run-apisparser)
    - [Available flags](#available-flags)
  - [Build WebVOWL](#build-webvowl) 
- [Output](#output)
  - [APIs.json](#apis-fields)
    - [Example](#java-class)
- [How to visualise API Resources on WebVOWL](#how-to-visualise-api-resources-on-webvowl)
- [Strategy used by APIsParser to select java resources inside a directory](#strategy-used-by-apisparser-to-select-java-resources-inside-a-directory)
- [New Features to implement](#new-features-to-implement)


----------------------------------



### Setup

#### Download the repository
```bash
git clone git@github.com:andreaangiolillo/APIsParser.git
```
#### Generate the JAR
```bash
cd APIsParser
./gradlew build
```
#### Run APIsParser

```bash
java -jar build/libs/APIsParser-1.0.0.jar --path /Users/andrea.angiolillo/workspace/project/server/src/main
```

##### Available flags:
- ```--path```: the root directory of your api resources. You can provide different directories by using this flag multiple times.
- ```--type (Optional)```: the type of endpoints that you are interested in. Accepted values = {PUBLIC_API, WEB_API, ALL}. Default value = {PUBLIC_API}
- ```--filePath (Optional)```: the path where the json file will be created. Default: the json file will be created in the current directory with the name ```APIs.json```
- ```--networkChart (Optional)```: APIsParser creates a json file that can be used to visualise your api resources with WebVOWL
- ```--networkChartFilePath (Optional)```: the path where the WebVOWL json file will be created. Default: the WebVOWL json file will be create in ```APIsParser/WebVOWL/src/app/data/WebVOWL.json```
- ```--log (Optional)```: when set, APIsParser prints log messages

#### Build WebVOWL
- Go inside WebVOWL: ```cd WebVOWL```
- Install the dependencies and build the project: ```npm install```
- Install ```grunt-cli```: ```npm install grunt-cli -g```
- Start WebVOWL (http://localhost:8000/): ```grunt webserver```

### Output 
APIsParser generates two json files:
- **APIs.json**: json representation of api resources
- **WebVOWL.json**: json file used by WebVOWL to generate the network chart


#### APIs Fields

- ```name```: [String] 
  - The name of the resource
  - This field is extract from ```@Path``` annotation of the java class
  - Example: ```"name": "groups"```
- ```javaClass```: [String] 
  - The name of the java class
  - Example: ```"javaClass": "/api/public/v1.0/usage/groups"```
- ```baseURL```: [String] 
  - The base URL of the resource
  - This field is the value of ```@Path``` annotation of the java class
  - Example: ```"baseURL": "com.xsrc.svc.res.EmailResource"```
- ```dependencies```: Array[String] 
  - Array of dependencies
  - A dependency is a service that the resource use for its endpoints
  - APIsParser looks for variable definitions and parameters in the Constructor to find dependencies -> [ClassVisitor.java](https://github.com/10gen/APIsParser/blob/18b70dd67747dc941f93ba8107f72e16dfbbab1f/src/main/java/Parser/Visitor/ClassVisitor.java#L70)
  - Example: ```"dependencies": [ "com.xgen.svc.svc.AgentLogSvc", "com.module.svc.DbUsageTypeMappingSvc"]```
- ```endpoints```: Array[Endpoint] - Array of endpoints
  - ```endpoint.httpMethod```: [String] 
    - HTTP Verb of the endpoint
    - This field is extracted from ```@GET|@PUT|@DELETE|@POST|@PATCH``` annotation of the endpoint
    - Example: ```"httpMethod": "GET"```
  - ```endpoint.path```: [String] 
    - Path to use the endpoint
    - This field is extracted from ```@Path``` annotation of the endpoint
    - Example: ```"path": "/{groupId}/markAsBackingDatabase"```
  - ```endpoint.type``` : [String] 
    - type of the endpoint
    - Valid Values: {PROGRAMMATIC, UI}
    - This field is set by using ```@UiCall``` annotation
    - Example: ```"type": "PUBLIC_API"```
  - ```endpoint.rolesAllowed```:Array[String]
    - Roles allowed to call the endpoint
    - This field is set by using @RolesAllowed annotation
    - This field may not be present if the endpoint doesn’t have the @RolesAllowed annotation
    - Example: ```"roleAllowerd": ["oleSet.NAME.ORG_MEMBER"]```
  - ```endpoint.inputParameters```:Array[Parameter] - Array of input parameters of the endpoint
    - ```endpoint.parameter.name```: [String]
      - name of the paramenter
      - Example: ```"name": "pRequest"```
    - ```endpoint.paramenter.type```: [String] 
      - type of the paramenter 
      - Example: ```"type": HttpServletRequest```
    - ```endpoint.paramenter.annotations```: Array[String] 
      - Array of annotations related to the parameter
      - Example: ```"annotations": ["Context"]```

##### Example
###### Java class:
```java
package project.server.api.res.test;

import com.test.svc.test.svc.NDSvc;

@SubscriptionPlan(PlanTypeSet.NDS)
@PaidInFull
@Path("/api/project/v1.0/groups/{groupId}/auditLog")
@Singleton
public class ApiAuditLogResource extends ApiBaseResource {

  private final NDSvc _ndSvc;

  @Inject
  public ApiAuditLogResource(final Settings pSettings, final NDSvc pNDSUISvc) {
    super(pSettings);
    _ndSvc = NDSvc;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed({RoleSet.NAME.GROUP_ADMIN})
  public Response getAuditLog(
      @Context final Group pGroup, @QueryParam("envelope") final Boolean pEnvelope) {
    // Code
  }

  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @RolesAllowed({RoleSet.NAME.GROUP_ADMIN})
  public Response patchAuditLog(
      @Context final Group pGroup,
      @Context final AppUser pUser,
      @Context final AuditInfo pAuditInfo,
      @QueryParam("envelope") final Boolean pEnvelope,
      final ApiAuditLogView pAuditLogView) {
    // Code
}

```



###### Json Representation: 
```json
{
  "name": "auditLog",
  "javaClass": "project.server.api.res.test.ApiAuditLogResource",
  "baseURL": "/api/project/v1.0/groups/{groupId}/auditLog",
  "dependencies": [
    "com.test.svc.test.svc.NDSvc"
  ],
  "endpoints": [
    {
      "httpMethod": "GET",
      "path": "",
      "rolesAllowed": [
        "GROUP_ADMIN"
      ],
      "inputParameters": [
        {
          "name": "pGroup",
          "type": "Group",
          "annotations": [
            "Context"
          ]
        },
        {
          "name": "pEnvelope",
          "type": "Boolean",
          "annotations": [
            "QueryParam(\"envelope\")"
          ]
        }
      ]
    },
    {
      "httpMethod": "PATCH",
      "path": "",
      "rolesAllowed": [
        "GROUP_ADMIN"
      ],
      "inputParameters": [
        {
          "name": "pGroup",
          "type": "Group",
          "annotations": [
            "Context"
          ]
        },
        {
          "name": "pUser",
          "type": "AppUser",
          "annotations": [
            "Context"
          ]
        },
        {
          "name": "pAuditInfo",
          "type": "AuditInfo",
          "annotations": [
            "Context"
          ]
        },
        {
          "name": "pEnvelope",
          "type": "Boolean",
          "annotations": [
            "QueryParam(\"envelope\")"
          ]
        },
        {
          "name": "pAuditLogView",
          "type": "ApiAuditLogView",
          "annotations": []
        }
      ]
    }
  ]
}
```

### How to visualise API Resources on WebVOWL:
- Run APIsParser to generate  [**APIs.json**](#output)  and [**WebVOWL.json**](#output)
```bash
java -jar build/libs/APIsParser-1.0.0.jar --networkChart --path /Users/andrea.angiolillo/workspace/mms/server/src/main/com/xgen/svc/mms
```
- Go inside ```APIsParser/WebVOWLD```
```bash
cd APIsParser/WebVOWLD
```
- Start WebVOWL (http://localhost:8000/)
```bash
grunt webserver
```

### Strategy used by APIsParser to select java resources inside a directory:
APIsParser generates a json representation only of resources that have these two properties:
- The file name contains ```Resource.java``` such as ```ApiTestResource.java``` - > filter implemented in ```Stream.FileReader#isAJavaResource```
- The java class contains ```@Path``` such as ```@Path("/admin/test")``` -> filter implemented in ```Parser.Parser#resourceValidation```


### New Features to implement
- Run APIsParser inside an evergreen task to generate APIs.json
- Add more filter to Private api, Agent API



## License

APIsParser is released under the Apache 2.0 license.
