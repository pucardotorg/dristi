# Local Setup

To set up the dristi-pdf service in your local system, clone the dristi repository.

## Dependencies

### Infra Dependency

- [ ] Postgres DB
- [ ] Redis
- [ ] Elasticsearch
- [ ] Kafka
  - [ ] Consumer
  - [ ] Producer

## Running Locally

- To run the services in local system, you need to port forward below services.

```bash
 function kgpt(){kubectl get pods -n egov --selector=app=$1 --no-headers=true | head -n1 | awk '{print $1}'}
 kubectl port-forward -n egov $(kgpt egov-mdms-service) 8081:8080 &
 kubectl port-forward -n egov $(kgpt egov-hrms) 8082:8080 &
 kubectl port-forward -n egov $(kgpt individual) 8085:8080 &
 kubectl port-forward -n egov $(kgpt case) 8091:8080 &
 kubectl port-forward -n egov $(kgpt order) 8092:8080 &
 kubectl port-forward -n egov $(kgpt hearing) 8093:8080 &
 kubectl port-forward -n egov $(kgpt application) 8094:8080 &
 kubectl port-forward -n egov $(kgpt sunbirdrc-credential-service) 8095:8080
``` 

- Update below listed properties in `config.js` before running the project:

```ini
mdms: process.env.EGOV_MDMS_HOST || 'http://localhost:8081',
    pdf: process.env.EGOV_PDF_HOST || 'http://localhost:8070',
    case: process.env.DRISTI_CASE_HOST || 'http://localhost:8091',
    order: process.env.DRISTI_ORDER_HOST || 'http://localhost:8092',
    hrms: process.env.EGOV_HRMS_HOST || 'http://localhost:8082',
    individual: process.env.EGOV_INDIVIDUAL_HOST || 'http://localhost:8085',
    hearing: process.env.DRISTI_HEARING_HOST || 'http://localhost:8093',
    sunbirdrc_credential_service: process.env.EGOV_SUNBIRDRC_CREDENTIAL_HOST || 'http://localhost:8095',
    application: process.env.DRISTI_APPLICATION_HOST || 'http://localhost:8094',
```
- Open the terminal and run the following command
    - `cd [filepath to dristi-pdf service]`
    - `npm install`
    - `npm start`
