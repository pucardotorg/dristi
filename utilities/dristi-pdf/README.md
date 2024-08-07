# dristi-pdf service

dristi-pdf service work in between pdf-service and client requesting pdfs. Earlier client used to directly call pdf-service with complete data as json, but with introduction of this new service one can provide just few parameters ex:- applicationnumber, tenantId to this new service to get a pdf. 
### DB UML Diagram

- NA

### Service Dependencies

- egov-mdms-service
- Pdf-service
- egov-hrms
- individual
- case
- hearing
- order
- application
- sunbirdrc-credential-service


### Swagger API Contract
NA

## Service Details

dristi-pdf service is new service being added which can work in between existing pdf-service and client requesting pdfs. Earlier client used to directly call pdf-service with complete data as json, but with introduction of this new service one can provide just few parameters ex:- applicationnumber, tenantId to this new service to get a pdf. The dristi-pdf service will take responsibility of getting application data from concerned service and also will do any enrichment if required and then with the data call pdf service to get pdf filestore ID. The service will return pdf filestore ID in the response which can be directly downloaded by the client from filestore service. With this service the existing pdf service endpoints need not be exposed to frontend.

For any new pdf requirement one new endpoint with validations and logic for getting data for pdf has to be added in the code. With separate endpoint for each pdf we can define access rules per pdf basis. Currently dristi-pdf service has endpoint for order PDF templates.


#### Configurations

**Steps/guidelines for adding support for new pdf:**

- Make sure the config for pdf is added in the PDF-Service.Refer the PDF service [documentatiom](https://digit-discuss.atlassian.net/l/c/f3APeZPF )

- Follow code of [existing supported PDFs](https://github.com/egovernments/DIGIT-Works/tree/master/utilities/works-pdf/src/routes) and create new endpoint with suitable search parameters for each PDF

- Put parameters validations, module level validations ex:- application status,applicationtype and api error responses with proper error messages and error codes

- Make sure whatever service is used for preparing data for PDF, search call to them.

- Prepare data for pdf by calling required services.

- Use correct pdf key with data to call and return PDF filestor ID (use “/_create” endpoint of PDF service)


### API Details
Currently below endpoints are in use for order PDF

| Endpoint | module | query parameter |
| -------- | ------ | --------------- |
|`/egov-pdf/order` | order | `cnrNumber, orderId, tenantId, orderType, date, qrCode, entityId, code` |

### Kafka Consumers
NA

### Kafka Producers
NA
