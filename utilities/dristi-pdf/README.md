# dristi-pdf service

The dristi-pdf service works between the pdf-service and the client requesting PDFs. Earlier, the client used to directly call the pdf-service with complete data as JSON, but with the introduction of this new service, one can provide just a few parameters, e.g., application number, tenant ID, to this new service to get a PDF.
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

The dristi-pdf service is a new service being added that can work between the existing pdf-service and the client requesting PDFs. Earlier, the client used to directly call the pdf-service with complete data as JSON, but with the introduction of this new service, one can provide just a few parameters, e.g., application number, tenant ID, to this new service to get a PDF. The dristi-pdf service will take responsibility for getting application data from the concerned service and will also do any enrichment if required, and then with the data, call the pdf-service to get the PDF directly. The service will return the PDF filestore id in the response, which can be fetched by calling the filestore service. With this service, the existing pdf-service endpoints need not be exposed to the frontend.

For any new pdf requirement one new endpoint with validations and logic for getting data for pdf has to be added in the code. With separate endpoint for each pdf we can define access rules per pdf basis. Currently dristi-pdf service has endpoint for order PDF templates.


#### Configurations

**Steps/guidelines for adding support for new pdf:**

- Make sure the config for the PDF is added in the PDF-Service.Refer to the PDF service [documentatiom](https://digit-discuss.atlassian.net/l/c/f3APeZPF )

- Follow the code of [existing supported PDFs](https://github.com/egovernments/DIGIT-Works/tree/master/utilities/works-pdf/src/routes) and create a new endpoint with suitable search parameters for each PDF

- Put parameters validations, module level validations e.g., application status,applicationtype and api error responses with proper error messages and error codes

- Make sure whatever service is used for preparing data for the PDF, make search call to them.

- Prepare data for the PDF by calling required services.

- Use the correct PDF key with data to call and return the PDF filestor ID (use “/_create” endpoint of PDF service)


### API Details
Currently below endpoints are in use for order PDF

| Endpoint | module | query parameter |
| -------- | ------ | --------------- |
|`/egov-pdf/order` | order | `cnrNumber, orderId, tenantId, orderType, date, qrCode, entityId, code` |

### Kafka Consumers
NA

### Kafka Producers
NA
