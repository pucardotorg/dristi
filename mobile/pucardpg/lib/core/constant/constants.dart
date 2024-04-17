
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

List<String> state = [];

Future<void> fetchStates(String countryISOCode) async {
  final response = await getStatesOfCountry(countryISOCode);
  response.map((state) => StatesData.fromState(state)).toList();
  state = response.map((state) => state.name).toList();
}

String? selectedOption;

const String noResultSvg = 'assets/svg/no_result.svg';
