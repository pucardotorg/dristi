import 'dart:developer';
import 'dart:io';
import 'package:digit_components/digit_components.dart';
import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:geocoding/geocoding.dart';
import 'package:geolocator/geolocator.dart';
import 'package:google_places_flutter/model/place_details.dart';
import 'package:http/io_client.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:google_places_flutter/google_places_flutter.dart';
import 'package:google_places_flutter/model/prediction.dart';
import 'package:pucardpg/app/presentation/widgets/back_button.dart';
import 'package:pucardpg/app/presentation/widgets/help_button.dart';
import 'package:pucardpg/config/mixin/app_mixin.dart';
import 'package:reactive_forms/reactive_forms.dart';

class AddressScreen extends StatefulWidget with AppMixin {
  final String mobile;

  const AddressScreen({super.key, required this.mobile});

  @override
  AddressScreenState createState() => AddressScreenState();
}

const kGoogleApiKey = 'AIzaSyAASfCFja6YxwDzEAzhHFc8B-17TNTCV0g';
final homeScaffoldKey = GlobalKey<ScaffoldState>();

class AddressScreenState extends State<AddressScreen> {
  String pinLocationKey = 'pinLocation';
  String pinCodeKey = 'pinCode';
  String stateKey = 'state';
  String districtKey = 'district';
  String cityKey = 'city';
  String localityKey = 'locality';
  String doorNoKey = 'doorNo';
  // List<String> state = [];
  // List<String> district = ['District'];
  // List<String> city = ['City'];

  String stateName = '';
  String districtName = '';
  String cityName = '';
  String pinCode = '';

  late Position _currentPosition;

  // List<StatesData> statesData = [];
  // String? selectedDistrict;
  // String selectedCountryISOCode = 'IN';
  // String selectedStateISOCode = '';

  // Future<void> fetchStates(String countryISOCode) async {
  //   final response = await getStatesOfCountry(countryISOCode);
  //   setState(() {
  //     statesData =
  //         response.map((state) => StatesData.fromState(state)).toList();
  //     state = response.map((state) => state.name).toList();
  //     city = city;
  //     selectedDistrict = selectedDistrict;
  //   });
  // }
  //
  // Future<void> fetchCities(String countryISOCode, String stateISOCode) async {
  //   final cities = await getStateCities(countryISOCode, stateISOCode);
  //   setState(() {
  //     city = cities.map((city) => city.name).toList();
  //   });
  // }

  static const CameraPosition initialCameraPosition =
      CameraPosition(target: LatLng(17.4400802, 78.3489168), zoom: 15.0);

  Set<Marker> markersList = {
    const Marker(
        markerId: MarkerId(""), position: LatLng(17.4400802, 78.3489168))
  };

  late GoogleMapController googleMapController;

  // final Mode _mode = Mode.overlay;
  TextEditingController controller = TextEditingController();

  @override
  void initState() {
    // fetchStates(selectedCountryISOCode);
    super.initState();
  }

  _getCurrentLocation(FormGroup form) async {
    Position position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high);
    inspect(position);
    setState(() {
      _currentPosition = position;
    });
    _getAddressFromLatLng(form);
  }

  Future<void> _getAddressFromLatLng(FormGroup form) async {
    await placemarkFromCoordinates(
            _currentPosition.latitude, _currentPosition.longitude)
        .then((List<Placemark> placemarks) {
      Placemark place = placemarks[0];
      setState(() {
        pinCode = place.postalCode!;
        stateName = place.administrativeArea!;
        districtName = place.subAdministrativeArea!;
        cityName = place.locality!;
        form.control(pinCodeKey).value = place.postalCode!;
        form.control(stateKey).value = place.administrativeArea!;
        form.control(districtKey).value = place.subAdministrativeArea!;
        form.control(cityKey).value = place.locality!;
      });
      markersList.clear();
      markersList.add(Marker(
          markerId: MarkerId(""),
          position:
              LatLng(_currentPosition.latitude, _currentPosition.longitude)));
      googleMapController.animateCamera(CameraUpdate.newLatLngZoom(
          LatLng(_currentPosition.latitude, _currentPosition.longitude), 14.0));
      inspect(place);
    }).catchError((e) {
      debugPrint(e);
    });
  }

  // Future<void> _fetchDistrict(String pincode) async {
  //   final url = 'https://api.postalpincode.in/pincode/$pincode';
  //   final http = IOClient(HttpClient());
  //   final response = await http.get(Uri.parse(url));
  //
  //   if (response.statusCode == 200) {
  //     final body = json.decode(response.body);
  //     final postOffice = body[0]['PostOffice'] as List<dynamic>;
  //     if (postOffice.isNotEmpty) {
  //       final firstPostOffice = postOffice[0];
  //       setState(() {
  //         selectedDistrict = firstPostOffice['District'];
  //       });
  //     }
  //   } else {
  //     // Handle error
  //     print('Failed to fetch district');
  //   }
  // }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(""),
        centerTitle: true,
        actions: [
          IconButton(onPressed: () {}, icon: const Icon(Icons.notifications))
        ],
        leading: IconButton(
          onPressed: () {},
          icon: const Icon(Icons.menu),
        ),
      ),
      resizeToAvoidBottomInset: true,
      body: ReactiveFormBuilder(
        form: () => buildForm(),
        builder: (context, form, child) {
          return Column(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              Expanded(
                  child: SingleChildScrollView(
                child: Column(
                  children: [
                    const SizedBox(
                      height: 10,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        DigitBackButton(
                          onPressed: () {
                            Navigator.of(context).pop();
                          },
                        ),
                        DigitHelpButton()
                      ],
                    ),
                    DigitCard(
                      padding: const EdgeInsets.all(20.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "Registration",
                            style: widget.theme
                                .text20W400Rob()
                                ?.apply(fontStyle: FontStyle.italic),
                          ),
                          const SizedBox(
                            height: 20,
                          ),
                          Text(
                            "Enter Your Address",
                            style: widget.theme.text32W700RobCon()?.apply(),
                          ),
                          const SizedBox(
                            height: 25,
                          ),
                          Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  "Pin Location",
                                  style: DigitTheme.instance.mobileTheme
                                      .textTheme.labelSmall,
                                ),
                                const SizedBox(
                                  height: 10,
                                ),
                                GooglePlaceAutoCompleteTextField(
                                  textEditingController: controller,
                                  googleAPIKey: kGoogleApiKey,
                                  inputDecoration: const InputDecoration(
                                    hintText: "Search your location",
                                    border: InputBorder.none,
                                    enabledBorder: InputBorder.none,
                                  ),
                                  debounceTime: 400,
                                  countries: ["in"],
                                  isLatLngRequired: true,
                                  getPlaceDetailWithLatLng:
                                      (Prediction prediction) {
                                    FocusScope.of(context).unfocus();
                                    displayPrediction(prediction, form);
                                    inspect(prediction);
                                    print("placeDetails" +
                                        prediction.toString() +
                                        prediction.matchedSubstrings
                                            .toString() +
                                        prediction.types.toString() +
                                        prediction.terms!
                                            .map((e) => e.value)
                                            .toString());
                                  },
                                  itemClick: (Prediction prediction) {
                                    controller.text = prediction.description!;
                                    controller.selection =
                                        TextSelection.fromPosition(TextPosition(
                                            offset: prediction
                                                .description!.length));
                                  },
                                  boxDecoration: BoxDecoration(
                                    border: Border.all(
                                      color: Colors.black,
                                      width: 1,
                                    ),
                                    borderRadius: BorderRadius.circular(
                                        0), // Border radius
                                  ),
                                  itemBuilder:
                                      (context, index, Prediction prediction) {
                                    return Container(
                                      padding: const EdgeInsets.all(10),
                                      child: Row(
                                        children: [
                                          const Icon(Icons.location_on),
                                          const SizedBox(
                                            width: 7,
                                          ),
                                          Expanded(
                                              child: Text(
                                                  "${prediction.description ?? ""}"))
                                        ],
                                      ),
                                    );
                                  },
                                  seperatedBuilder: Divider(),
                                  isCrossBtnShown: true,
                                ),
                                const SizedBox(
                                  height: 6,
                                ),
                                Container(
                                  height: 300,
                                  child: GoogleMap(
                                    initialCameraPosition:
                                        initialCameraPosition,
                                    markers: markersList,
                                    mapType: MapType.normal,
                                    onMapCreated:
                                        (GoogleMapController controller) {
                                      _getCurrentLocation(form);
                                      googleMapController = controller;
                                    },
                                    gestureRecognizers: <Factory<
                                        OneSequenceGestureRecognizer>>{
                                      Factory<OneSequenceGestureRecognizer>(
                                        () => EagerGestureRecognizer(),
                                      ),
                                    },
                                  ),
                                ),
                                const SizedBox(
                                  height: 10,
                                ),
                                DigitTextFormField(
                                  formControlName: pinCodeKey,
                                  label: 'Pincode',
                                  keyboardType: TextInputType.number,
                                  isRequired: true,
                                  readOnly: pinCode == '' ? false : true,
                                  onChanged: (value) {
                                    // if (value.value.length == 6) {
                                    //   _fetchDistrict(value.value);
                                    // }
                                  },
                                  validationMessages: {
                                    'required': (_) => 'Pincode is required',
                                    'number': (_) => 'Pincode should contain digits 0-9',
                                    'maxLength': (_) =>
                                    'Max length should be 6'
                                  },
                                  inputFormatters: [
                                    FilteringTextInputFormatter.allow(
                                        RegExp("[0-9]")),
                                  ],
                                ),
                                DigitTextFormField(
                                  formControlName: stateKey,
                                  label: 'State',
                                  keyboardType: TextInputType.text,
                                  isRequired: true,
                                  readOnly: stateName == '' ? false : true,
                                  onChanged: (value) {
                                    // if (value.value.length == 6) {
                                    //   _fetchDistrict(value.value);
                                    // }
                                  },
                                  validationMessages: {
                                    'required': (_) => 'State is required',
                                  },
                                ),
                                DigitTextFormField(
                                  formControlName: districtKey,
                                  label: 'District',
                                  keyboardType: TextInputType.text,
                                  isRequired: true,
                                  readOnly: districtName == '' ? false : true,
                                  onChanged: (value) {
                                    // if (value.value.length == 6) {
                                    //   _fetchDistrict(value.value);
                                    // }
                                  },
                                  validationMessages: {
                                    'required': (_) => 'District is required',
                                  },
                                ),
                                DigitTextFormField(
                                  formControlName: cityKey,
                                  label: 'City',
                                  keyboardType: TextInputType.text,
                                  isRequired: true,
                                  readOnly: cityName == '' ? false : true,
                                  onChanged: (value) {
                                    // if (value.value.length == 6) {
                                    //   _fetchDistrict(value.value);
                                    // }
                                  },
                                  validationMessages: {
                                    'required': (_) => 'City is required',
                                  },
                                ),
                                // DigitReactiveDropdown<String>(
                                //   label: 'State',
                                //   menuItems: state,
                                //   initialValue: stateName,
                                //   padding: const EdgeInsets.all(0),
                                //   isRequired: true,
                                //   formControlName: stateKey,
                                //   valueMapper: (value) => value.toUpperCase(),
                                //   onChanged: (value) {
                                //     selectedStateISOCode = statesData
                                //         .firstWhere(
                                //             (state) => state.name == value)
                                //         .isoCode;
                                //     // Call fetchCities with selected state and country ISO codes
                                //     fetchCities(selectedCountryISOCode,
                                //         selectedStateISOCode);
                                //   },
                                //   validationMessages: {
                                //     'required': (_) => 'State is required',
                                //   },
                                // ),
                                // const SizedBox(
                                //   height: 12,
                                // ),
                                // DigitReactiveDropdown<String>(
                                //   label: 'District',
                                //   menuItems: selectedDistrict != null
                                //       ? [selectedDistrict!]
                                //       : ['District'],
                                //   initialValue: districtName,
                                //   isRequired: true,
                                //   formControlName: districtKey,
                                //   valueMapper: (value) => value.toUpperCase(),
                                //   onChanged: (value) {},
                                //   validationMessages: {
                                //     'required': (_) => 'District is required',
                                //   },
                                // ),
                                // const SizedBox(
                                //   height: 12,
                                // ),
                                // DigitReactiveDropdown<String>(
                                //   label: 'City / Town',
                                //   menuItems: city,
                                //   initialValue: cityName,
                                //   isRequired: true,
                                //   formControlName: cityKey,
                                //   valueMapper: (value) => value.toUpperCase(),
                                //   onChanged: (value) {},
                                //   validationMessages: {
                                //     'required': (_) => 'City / Town is required',
                                //   },
                                // ),
                                DigitTextFormField(
                                    formControlName: localityKey,
                                    label: 'Locality / Street name / Area',
                                    isRequired: true,
                                    validationMessages: {
                                      'required': (_) =>
                                          'Locality / Street name / Area is required',
                                      'minLength': (_) =>
                                      'Min length should be 2',
                                      'maxLength': (_) =>
                                          'Max length should be 128'
                                    },
                                    inputFormatters: [
                                      FilteringTextInputFormatter.allow(
                                          RegExp("[a-zA-Z0-9 .,\\/\\-_@#\\']"))
                                    ]),
                                const SizedBox(
                                  height: 12,
                                ),
                                DigitTextFormField(
                                    formControlName: doorNoKey,
                                    padding: const EdgeInsets.all(0),
                                    label: 'Door number',
                                    isRequired: true,
                                    keyboardType: TextInputType.number,
                                    validationMessages: {
                                      'required': (_) =>
                                          'Door number is required',
                                      'number': (_) => 'Door number should contain digits 0-9',
                                      'minLength': (_) =>
                                      'Min length should be 2',
                                      'maxLength': (_) =>
                                          'Max length should be 8'
                                    },
                                    inputFormatters: [
                                      FilteringTextInputFormatter.allow(
                                          RegExp("[a-zA-Z0-9 .,\\/\\-_@#\\']"))
                                    ]),
                              ]),
                        ],
                      ),
                    ),
                  ],
                ),
              )),
              DigitElevatedButton(
                  onPressed: () {
                    form.markAllAsTouched();
                    if (!form.valid) return;
                    Navigator.pushNamed(context, '/UserTypeScreen',
                        arguments: widget.mobile);
                  },
                  child: Text(
                    'Next',
                    style: widget.theme.text20W700()?.apply(
                          color: Colors.white,
                        ),
                  )),
            ],
          );
        },
      ),
    );
  }

  FormGroup buildForm() => fb.group(<String, Object>{
        pinLocationKey:
            FormControl<String>(value: ''),
        pinCodeKey: FormControl<String>(
            value: pinCode,
            validators: [Validators.required, Validators.maxLength(6),
            Validators.number]),
        stateKey: FormControl<String>(
            value: stateName, validators: [Validators.required]),
        districtKey: FormControl<String>(
            value: districtName, validators: [Validators.required]),
        cityKey: FormControl<String>(
            value: cityName, validators: [Validators.required]),
        localityKey: FormControl<String>(value: '', validators: [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(128)
        ]),
        doorNoKey: FormControl<String>(value: '', validators: [
          Validators.required,
          Validators.number,
          Validators.minLength(2),
          Validators.maxLength(8)
        ]),
      });

  Future<void> displayPrediction(Prediction p, FormGroup form) async {
    final http = IOClient(HttpClient());
    final response = await Dio().get(
        "https://maps.googleapis.com/maps/api/place/details/json?placeid=${p.placeId}&key=${kGoogleApiKey}");
    PlaceDetails details = PlaceDetails.fromJson(response.data);
    inspect(details.result!.addressComponents);
    extractAddressComponents(details.result!.addressComponents);
    markersList.clear();
    markersList.add(Marker(
        markerId: MarkerId(p.placeId!),
        position: LatLng(double.parse(p.lat!), double.parse(p.lng!)),
        infoWindow: InfoWindow(title: p.description)));

    setState(() {
      form.control(pinCodeKey).value = pinCode;
      form.control(stateKey).value = stateName;
      form.control(districtKey).value = districtName;
      form.control(cityKey).value = cityName;
      // city = city;
      // selectedDistrict = selectedDistrict;
    });
    googleMapController.animateCamera(CameraUpdate.newLatLngZoom(
        LatLng(double.parse(p.lat!), double.parse(p.lng!)), 14.0));
  }

  void extractAddressComponents(List<AddressComponents>? addressComponents) {
    String newStateName = '';
    String newDistrictName = '';
    String newCityName = '';
    String newPinCode = '';
    for (var component in addressComponents!) {
      List<String>? types = component.types;
      String? longName = component.longName;

      if (types!.contains('administrative_area_level_1')) {
        newStateName = longName!;
      } else if (types.contains('administrative_area_level_3')) {
        newDistrictName = longName!;
      } else if (types.contains('locality')) {
        newCityName = longName!;
      } else if (types.contains('postal_code')) {
        newPinCode = longName!;
      }
    }
    setState(() {
      stateName = newStateName;
      districtName = newDistrictName;
      cityName = newCityName;
      pinCode = newPinCode;
    });
  }
}
