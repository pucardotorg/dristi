# e-courts
PUCAR is a public mission by a collaborative of changemakers to transform the dispute resolution experience for citizens in India. PUCAR sees opportunity to go beyond digitising paper-based processes, and transform end-to-end processes for a digital environment in a phased manner. Digital courts can radically improve access, efficiency and predictability of the system for its users. 

This repository houses the digital platform that will act as the chassis for Digital Courts of the future. This is being built with open source software as a Digital Public Good on top of the DIGIT platform. 

Key principles behind its design and architecture are:

Modularity: Most technology systems are monolithic: they are designed end to end.  Digital platforms are developed by unbundling complex functionality into multiple mico pieces which allow their re-bundling into specific contexts - similar to LEGO building blocks. Each of these mirco pieces are interchangeable and replaceable without necessitating a redesign of the infrastructure as a whole. For example, pdf generator as a building block can be reused for generating payment receipts as well as downloadable copies of a case file. As new use cases arise, the same building blocks can be used to roll out and quickly deploy new functionality. 

Registries and Data Standards: In the past, when developing digital solutions for different stages of a case's lifecycle, each solution managed its own data, resulting in multiple versions in diverse formats. To address this, the platform adopts registries with data standards, published and available for all to use, ensuring uniformity and consistency as new digital solutions are built. Additionally, APIs enable new solutions to plug in and operate using registries of existing solutions, guaranteeing a singular, reliable version of the truth.

Separate Data and Workflows: Data related to different cases is separated from the specific workflows that manage those cases allowing for the platform to accommodate variations in the nature of cases. For example, a cheque bouncing case has different processes and workflows from civil suit for recovery or  workflows from a motor vehicle compensation cases. Separation of data from workflow ensures that the same underlying data tables can be used across both these.

Federated & decentralized allowing data to remain where it’s been collected. 

Privacy & Security by Design: Managing security and privacy of data is crucial to building and maintaining trust between ecosystem participants and thus will be a critical design principle. All data access is through API calls to only authenticated and authorized stakeholders to ensure only those with necessary permissions can see/consume data. Standard and certifications for data privacy and security are ensured and all personal information is stored in an encrypted format. 

Workflows related to advocate module:
1. [advocateregistration-workflowConfig.json](../../docs/Advocate/worfkow/advocateregistration-workflowConfig.json)
2. [advocateclerkregistration-workflowConfig - Copy.json](..%2F..%2Fdocs%2FAdvocate%2Fworfkow%2Fadvocateclerkregistration-workflowConfig%20-%20Copy.json)
