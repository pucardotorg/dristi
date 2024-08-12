

# digit-ui-module-core

## Install

```bash
npm install --save @egovernments/digit-ui-module-core
```

## Limitation

```bash
This Package is more specific to DIGIT-UI's can be used across mission's
```

## Usage

After adding the dependency make sure you have this dependency in

```bash
frontend/micro-ui/web/package.json
```

```json
"@egovernments/digit-ui-module-core":"^1.5.0",
```

then navigate to App.js

```bash
 frontend/micro-ui/web/src/App.js
```

```jsx
/** add this import **/

import { DigitUI } from "@egovernments/digit-ui-module-core";


/** inside render Function add  the import for the component **/

  ReactDOM.render(<DigitUI stateCode={stateCode} enabledModules={enabledModules} moduleReducers={moduleReducers} />, document.getElementById("root"));

```

# Mandatory changes to use following version

```
from 1.5.38 add the following utility method in micro-ui-internals/packages/libraries/src/utils/index.js

const createFunction = (functionAsString) => {
  return Function("return " + functionAsString)();
};

export as createFunction;

similarly update line 76 of react-components/src/molecules/CustomDropdown.js

with  
 .filter((opt) => (opt?.hasOwnProperty("active") ? opt.active : true))

```
 *   Digit.Hooks.Utils.getDefaultLanguage()

```
from 1.8.0 beta version add the following utility method in micro-ui/web/micro-ui-internals/packages/libraries/src/utils/index.js

const getDefaultLanguage = () => {
  return  `${getLocaleDefault()}_${getLocaleRegion()}`;
};

and add its related functions

```


### Changelog

```bash
1.8.1-beta.6 Resolved duplicacy issue in the Sidebar
1.8.1-beta.5 Fixed Sidebar Path issue
1.8.1-beta.4 Added a null check for homescreen landing issue
1.8.1-beta.3 User profile back button fixes for mobile view
1.8.1-beta.2 User profile Save and change password button fixes for mobile view
1.8.1-beta.1 Republished after merging with Master due to version issues.
1.8.0-beta.16 fixed the hardcoded logout message 
1.8.0-beta.15 fixed the sidebar sort order issue 
1.8.0-beta.14
1.8.0-beta.13 
1.8.0-beta.12
1.8.0-beta.11 republished due to some version issues
1.8.0-beta.10 Constants updated for mgramsewa
1.8.0-beta.9 Updated How It works screen to take header from mdms config and show pdf card only when required
1.8.0-beta.8 redefine addtional component to render only under employee home page 
1.8.0-beta.6 added addtional component render for tqm modules
1.8.0 workbench v1.0
1.8.0-beta.5 fix for login screen alignments
1.8.0-beta.4 made the default localisation in globalconfig
1.8.0-beta workbench base version beta release
1.7.0 urban 2.9
1.6.0 urban 2.8
1.5.43 redirection issue fix incase of no roles in selected city
1.5.46 added classname for topbar options dropdown.
1.5.45 aligment issue in edit and logout
1.5.44 updated login scss and alignment issues
1.5.42 fixed the mdms call in login component for dynamic updating
1.5.41 updated the readme content
1.5.40 Updated the login componenet to handle mdms config, which can be accessed from master - commonUiConfig and module - LoginConfig
1.5.39 Show the Toast when password changed and need to logout from profile page
1.5.38 enabled the admin mode for employee login which can be accessed through route employee/user/login?mode=admin and updated to use formcomposerv2
1.5.37 fixed hiding upload drawer icons.
1.5.36 fixed after clicking on change password and then try to save profile without changing password showing error.
1.5.35 fixed user profile email was prefilled when clicking on change password
1.5.34 fixed module not found redirection issue
1.5.33 fixed payment not throwing error page for sanitation
1.5.32 fixed the localisation issue by adding translation to the keys and fixed payment response issue for  sanitation UI
1.5.31 fixed the allservices screen back button for sanitation UI
1.5.30 fixed the home routing issue in error screen
1.5.29 added the readme file
1.5.28 fixed the route issue for profile screen
```

### Contributors

[jagankumar-egov] [nipunarora-eGov] [Tulika-eGov] [Ramkrishna-egov] [nabeelmd-eGov] [anil-egov] [vamshikrishnakole-wtt-egov] 

## Documentation

Documentation Site (https://core.digit.org/guides/developer-guide/ui-developer-guide/digit-ui)

## Maintainer

- [jagankumar-egov](https://www.github.com/jagankumar-egov)


### Published from DIGIT Frontend 
DIGIT Frontend Repo (https://github.com/egovernments/Digit-Frontend/tree/develop)


![Logo](https://s3.ap-south-1.amazonaws.com/works-dev-asset/mseva-white-logo.png)
