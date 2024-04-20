
import 'package:pucardpg/app/data/models/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/app/data/models/role-model/role.dart';

import 'package:country_state_city/utils/state_utils.dart';
import 'package:pucardpg/app/data/models/state_model/state_model.dart';

const String apiBaseURL = "https://dristi-dev.pucar.org";

const String apiId = "Rainmaker";
const String authToken = "c835932f-2ad4-4d05-83d6-49e0b8c59f8a";
const String msgId = "1712987382117|en_IN";
const int timeStamp = 1712987382117;

const String tenantId = "pg";
const String userType = "citizen";
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

const Field litigant = Field(
    key: 'userType',
    value: "LITIGANT",
);


List<String> state = [];

Future<void> fetchStates(String countryISOCode) async {
  final response = await getStatesOfCountry(countryISOCode);
  response.map((state) => StatesData.fromState(state)).toList();
  state = response.map((state) => state.name).toList();
}

String? selectedOption;

const String approvalSvg = 'assets/icons/svg/approval.svg';
const String digitSvg = 'assets/icons/svg/DIGIT.png';
const String yetToRegister = 'assets/icons/svg/yet_to_register.svg';
