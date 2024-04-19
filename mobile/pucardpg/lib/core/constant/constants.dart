
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/role-model/role.dart';

const String apiBaseURL = "https://dristi-dev.pucar.org";

const String apiId = "Rainmaker";
const String authToken = "c835932f-2ad4-4d05-83d6-49e0b8c59f8a";
const String msgId = "1712987382117|en_IN";
const int timeStamp = 1712987382117;

const String tenantId = "pg";
const String userType = "citizen";
const String login = "login";

const String register = "register";

const Role userRegisterRole = Role(
    name: "USER_REGISTER",
    code: "USER_REGISTER"
);

const Role citizenRole = Role(
    name: "Citizen",
    code: "CITIZEN"
);

const Field litigant = Field(
    key: 'userType',
    value: "LITIGANT",
);

