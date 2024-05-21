library i18;

import 'package:pucardpg/model/litigant-registration-model/litigant_registration_model.dart';
import 'package:pucardpg/model/role-model/role.dart';

const common = Common();
const login = Login();
const registerMobile = RegisterMobile();
const userDetails = UserDetailsText();
const address = AddressText();
const idVerification = IdVerification();
const advocateVerification = AdvocateVerification();
const success = SuccessText();
const terms = TermsAndConditionsText();

const appConstants = AppConstants();

class AppConstants {
  const AppConstants();

  String get apiBaseURL => "https://dristi-dev.pucar.org";

  String get apiId => "Rainmaker";
  String get authToken => "c835932f-2ad4-4d05-83d6-49e0b8c59f8a";
  String get msgId => "1712987382117|en_IN";
  int get timeStamp => 1712987382117;

  String get tenantId => "pg";
  String get type => "CITIZEN";
  String get login => "login";

  String get register => "register";

  int get limit => 1000;
  int get offset => 0;

  String get individualId => "IND-2024-04-18-000063";
  String get module => "DRISTI";

  Role get  userRegisterRole => const Role(
      name: "USER_REGISTER",
      code: "USER_REGISTER"
  );

  Role get getCitizenRole => const Role(
      name: "Citizen",
      code: "CITIZEN"
  );

  Fields get litigant => const Fields(
    key: 'userType',
    value: "LITIGANT",
  );

  Fields get advocate => const Fields(
    key: 'userType',
    value: "ADVOCATE",
  );

  Fields get clerk => const Fields(
    key: 'userType',
    value: "ADVOCATE_CLERK",
  );

  String get approvalSvg => 'assets/icons/svg/approval.svg';
  String get digitSvg => 'assets/icons/svg/DIGIT.png';
  String get yetToRegister => 'assets/icons/svg/yet_to_register.svg';

}

class Common {
  const Common();
  String get csCommonBack => 'CS_COMMON_BACK';
  String get csCommonChooseFile => 'CS_COMMON_CHOOSE_FILE';
  String get csLoginOtpText => 'CS_LOGIN_OTP_TEXT';
  String get csResendOtp => 'CS_RESEND_OTP';
  String get csResendAnotherOtp => 'CS_RESEND_ANOTHER_OTP';
  String get verify => 'VERIFY';
  String get coreCommonLogout => 'CORE_COMMON_LOGOUT';
  String get esCommonHome => 'ES_COMMON_HOME';
  String get csCommonChooseLanguage => 'CS_COMMON_CHOOSE_LANGUAGE';
  String get coreCommonContinue => 'CORE_COMMON_CONTINUE';
}

class Login {
  const Login();
  String get csSignInProvideMobileNumber => 'CS_SIGNIN_PROVIDE_MOBILE_NUMBER';
  String get csWelcome => 'CS_WELCOME';
  String get coreCommonPhoneNumber => 'CORE_COMMON_PHONE_NUMBER';
  String get csSignInNext => 'CS_SIGNIN_NEXT';
  String get csRegisterAccount=> 'CS_REGISTER_ACCOUNT';
  String get csRegisterLink => 'CS_REGISTER_LINK';
}

class RegisterMobile {
  const RegisterMobile();
  String get csEnterMobile => 'CS_ENTER_MOBILE';
  String get csEnterMobileSubText => 'CS_ENTER_MOBILE_SUB_TEXT';
  String get csVerifyMobile => 'CS_VERIFY_MOBILE';
}

class UserDetailsText {
  const UserDetailsText();
  String get csEnterName => 'CS_ENTER_NAME';
  String get csEnterNameSubText => 'CS_ENTER_NAME_SUB_TEXT';
  String get coreCommonFirstName => 'CORE_COMMON_FIRST_NAME';
  String get coreCommonMiddleName => 'CORE_COMMON_MIDDLE_NAME';
  String get coreCommonLastName => 'CORE_COMMON_LAST_NAME';
}

class AddressText {
  const AddressText();
  String get csEnterAddress => 'CS_ENTER_ADDRESS';
  String get pincode => 'PINCODE';
  String get state => 'STATE';
  String get district => 'DISTRICT';
  String get cityTown => 'CITY/TOWN';
}

class IdVerification {
  const IdVerification();
  String get csVerifyIdentity => 'CS_VERFIY_IDENTITY';
  String get csVerifyIdentitySubText => 'CS_VERFIY_IDENTITY_SUB_TEXT';
  String get csOther => 'CS_OTHER';
  String get csOtherSubText => 'CS_OTHER_SUB_TEXT';
  String get csEnterAadhar => 'CS_ENTER_ADHAAR';
  String get csEnterAadharText => 'CS_ENTER_ADHAAR_TEXT';
}

class AdvocateVerification {
  const AdvocateVerification();
  String get coreAdvocateVerification => 'CORE_ADVOCATE_VERFICATION';
  String get coreAdvocateVerificationText => 'CORE_ADVOCATE_VERFICATION_TEXT';
  String get barRegistrationNumber => 'BAR_REGISTRATION_NUMBER';
  String get barCouncilId => 'BAR_COUNCIL_ID';
}

class SuccessText {
  const SuccessText();
  String get csRegisterSuccess => 'CS_REGISTER_SUCCESS';
  String get csRegisterSuccessSubText => 'CS_REGISTER_SUCCESS_SUB_TEXT';
}

class TermsAndConditionsText {
  const TermsAndConditionsText();
  String get esCommonUserTermsAndConditions => 'ES_COMMON_USER_TERMS_AND_CONDITIONS';
  String get firstTermsAndConditions => 'FIRST_TERMS_AND_CONDITIONS';
}