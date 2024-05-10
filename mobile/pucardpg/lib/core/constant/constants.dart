
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/role-model/role.dart';

const String apiBaseURL = "https://dristi-dev.pucar.org";

const String apiId = "Rainmaker";
const String authToken = "c835932f-2ad4-4d05-83d6-49e0b8c59f8a";
const String msgId = "1712987382117|en_IN";
const int timeStamp = 1712987382117;

const String tenantId = "pg";
const String type = "CITIZEN";
const String login = "login";

const String register = "register";

const int limit = 1000;
const offset = 0;

const String individualId = "IND-2024-04-18-000063";
const String module = "DRISTI";
const Role userRegisterRole = Role(
    name: "USER_REGISTER",
    code: "USER_REGISTER"
);

const Role citizenRole = Role(
    name: "Citizen",
    code: "CITIZEN"
);

const Fields litigant = Fields(
    key: 'userType',
    value: "LITIGANT",
);

const Fields advocate = Fields(
    key: 'userType',
    value: "ADVOCATE",
);

const Fields clerk = Fields(
    key: 'userType',
    value: "ADVOCATE_CLERK",
);

const String approvalSvg = 'assets/icons/svg/approval.svg';
const String digitSvg = 'assets/icons/svg/DIGIT.png';
const String yetToRegister = 'assets/icons/svg/yet_to_register.svg';
const String govtIndia = 'assets/icons/png/govt_logo.png';
const String waitingSvg = 'assets/icons/svg/waiting.svg';
const String successSvg = 'assets/icons/svg/success.svg';
