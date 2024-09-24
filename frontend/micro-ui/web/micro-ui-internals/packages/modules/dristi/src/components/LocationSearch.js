import React, { useEffect, useState } from "react";
import { SearchIconSvg } from "@egovernments/digit-ui-react-components";
import { Loader } from "@googlemaps/js-api-loader";

let defaultBounds = {};

const updateDefaultBounds = (center) => {
  if (!center.lat || !center.lng) {
    return;
  }
  defaultBounds = {
    north: center.lat + 0.1,
    south: center.lat - 0.1,
    east: center.lng + 0.1,
    west: center.lng - 0.1,
  };
};
const GetPinCode = (places) => {
  let postalCode = null;
  places?.address_components?.forEach((place) => {
    let hasPostalCode = place.types.includes("postal_code");
    postalCode = hasPostalCode ? place.long_name : null;
  });
  return postalCode;
};

const getName = (places) => {
  let name = "";
  places?.address_components?.forEach((place) => {
    let hasName = place.types.includes("sublocality_level_2") || place.types.includes("sublocality_level_1");
    if (hasName) {
      name = hasName ? place.long_name : null;
    }
  });
  return name;
};

const loadGoogleMaps = (callback) => {
  const key = window?.globalConfigs?.getConfig("GMAPS_API_KEY");
  const loader = new Loader({
    apiKey: key,
    version: "weekly",
    libraries: ["places"],
  });

  loader
    .load()
    .then(() => {
      if (callback) callback();
    })
    .catch((e) => {
      // do something
    });
};

const mapStyles = [
  {
    elementType: "geometry",
    stylers: [
      {
        color: "#f5f5f5",
      },
    ],
  },
  {
    elementType: "labels.icon",
    stylers: [
      {
        visibility: "off",
      },
    ],
  },
  {
    elementType: "labels.text.fill",
    stylers: [
      {
        color: "#616161",
      },
    ],
  },
  {
    elementType: "labels.text.stroke",
    stylers: [
      {
        color: "#f5f5f5",
      },
    ],
  },
  {
    featureType: "administrative.land_parcel",
    elementType: "labels.text.fill",
    stylers: [
      {
        color: "#bdbdbd",
      },
    ],
  },
  {
    featureType: "poi",
    elementType: "geometry",
    stylers: [
      {
        color: "#eeeeee",
      },
    ],
  },
  {
    featureType: "poi",
    elementType: "labels.text.fill",
    stylers: [
      {
        color: "#757575",
      },
    ],
  },
  {
    featureType: "poi.park",
    elementType: "geometry",
    stylers: [
      {
        color: "#e5e5e5",
      },
    ],
  },
  {
    featureType: "poi.park",
    elementType: "labels.text.fill",
    stylers: [
      {
        color: "#9e9e9e",
      },
    ],
  },
  {
    featureType: "road",
    elementType: "geometry",
    stylers: [
      {
        color: "#ffffff",
      },
    ],
  },
  {
    featureType: "road.arterial",
    elementType: "labels.text.fill",
    stylers: [
      {
        color: "#757575",
      },
    ],
  },
  {
    featureType: "road.highway",
    elementType: "geometry",
    stylers: [
      {
        color: "#dadada",
      },
    ],
  },
  {
    featureType: "road.highway",
    elementType: "labels.text.fill",
    stylers: [
      {
        color: "#616161",
      },
    ],
  },
  {
    featureType: "road.local",
    elementType: "labels.text.fill",
    stylers: [
      {
        color: "#9e9e9e",
      },
    ],
  },
  {
    featureType: "transit.line",
    elementType: "geometry",
    stylers: [
      {
        color: "#e5e5e5",
      },
    ],
  },
  {
    featureType: "transit.station",
    elementType: "geometry",
    stylers: [
      {
        color: "#eeeeee",
      },
    ],
  },
  {
    featureType: "water",
    elementType: "geometry",
    stylers: [
      {
        color: "#c9c9c9",
      },
    ],
  },
  {
    featureType: "water",
    elementType: "labels.text.fill",
    stylers: [
      {
        color: "#9e9e9e",
      },
    ],
  },
];

const setLocationText = (location, onChange, isPlaceRequired = false, index) => {
  const geocoder = new window.google.maps.Geocoder();
  geocoder.geocode(
    {
      location,
    },
    function (results, status) {
      if (status === "OK") {
        if (results[0]) {
          let pincode = GetPinCode(results[0]);
          const infoWindowContent = document.getElementById("pac-input-" + index);
          if (infoWindowContent) {
            infoWindowContent.value = getName(results[0]);
            if (onChange) {
              if (isPlaceRequired) onChange(pincode, results[0], { longitude: location.lng, latitude: location.lat }, infoWindowContent.value);
              else onChange(pincode, results[0], { longitude: location.lng, latitude: location.lat });
            }
          }
        }
      }
    }
  );
};

const onMarkerDragged = (marker, onChange, isPlaceRequired = false, index) => {
  if (!marker) return;
  const { latLng } = marker;
  const currLat = latLng.lat();
  const currLang = latLng.lng();
  const location = {
    lat: currLat,
    lng: currLang,
  };
  if (isPlaceRequired) setLocationText(location, onChange, true, index);
  else setLocationText(location, onChange, false, index);
};

const initAutocomplete = (onChange, position, isPlaceRequired = false, index) => {
  const map = new window.google.maps.Map(document.getElementById("map-" + index), {
    center: position,
    zoom: 15,
    mapTypeId: "roadmap",
    styles: mapStyles,
  }); // Create the search box and link it to the UI element.

  const input = document.getElementById("pac-input-" + index);
  updateDefaultBounds(position);
  const options = {
    bounds: defaultBounds,
    componentRestrictions: { country: "in" },
    fields: ["address_components", "geometry", "icon", "name"],
    origin: position,
    strictBounds: false,
    // types: ["address"],
  };

  const searchBox = new window.google.maps.places.Autocomplete(input, options);
  // map.controls[google.maps.ControlPosition.TOP_LEFT].push(input); // Bias the SearchBox results towards current map's viewport.

  map.addListener("bounds_changed", () => {
    searchBox.setBounds(map.getBounds());
  });

  let markers = [
    new window.google.maps.Marker({
      map,
      title: "a",
      position: position,
      draggable: true,
      clickable: true,
    }),
  ];
  if (isPlaceRequired) setLocationText(position, onChange, true, index);
  else setLocationText(position, onChange, false, index);

  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  markers[0].addListener("dragend", (marker) => onMarkerDragged(marker, onChange, isPlaceRequired, index));
  searchBox.addListener("place_changed", () => {
    const place = searchBox.getPlace();

    if (!place) {
      return;
    } // Clear out the old markers.
    let pincode = GetPinCode(place);
    if (pincode) {
      const { geometry } = place;
      const geoLocation = {
        latitude: geometry.location.lat(),
        longitude: geometry.location.lng(),
      };
      if (isPlaceRequired) onChange(pincode, place, geoLocation, place.name);
      else onChange(pincode, place, geoLocation);
    } else {
      const { geometry } = place;
      const geoLocation = {
        latitude: geometry.location.lat(),
        longitude: geometry.location.lng(),
      };
      onChange("", place, geoLocation);
    }
    markers.forEach((marker) => {
      marker.setMap(null);
    });
    markers = []; // For each place, get the icon, name and location.

    const bounds = new window.google.maps.LatLngBounds();
    if (!place.geometry) {
      return;
    }

    markers.push(
      new window.google.maps.Marker({
        map,
        title: place.name,
        position: place.geometry.location,
        draggable: true,
        clickable: true,
      })
    );
    markers[0].addListener("dragend", (marker) => onMarkerDragged(marker, onChange, isPlaceRequired, index));
    if (place.geometry.viewport) {
      // Only geocodes have viewport.
      bounds.union(place.geometry.viewport);
    } else {
      bounds.extend(place.geometry.location);
    }

    map.fitBounds(bounds);
  });
};

const LocationSearch = (props) => {
  const { setCoordinateData, isAutoFilledDisabled = false } = props;
  const [coordinates, setCoordinates] = useState({ lat: 8.898827, lng: 76.574801 });
  useEffect(() => {
    async function mapScriptCall() {
      const getLatLng = (position) => {
        initAutocomplete(props.onChange, { lat: position.coords.latitude, lng: position.coords.longitude }, props.isPlaceRequired, props?.index);
      };
      const getLatLngError = (error) => {
        let defaultLatLong = {};
        if (props?.isPTDefault) {
          defaultLatLong = props?.PTdefaultcoord?.defaultConfig || coordinates;
        } else {
          defaultLatLong = {
            lat: 8.898827,
            lng: 76.574801,
          };
        }
        initAutocomplete(props.onChange, defaultLatLong, props.isPlaceRequired, props?.index);
      };

      const initMaps = () => {
        if (props.position?.latitude && props.position?.longitude) {
          getLatLng({ coords: props.position });
        } else if (!isAutoFilledDisabled && navigator?.geolocation) {
          navigator.geolocation.getCurrentPosition(getLatLng, getLatLngError);
        } else {
          getLatLngError();
        }
      };

      loadGoogleMaps(initMaps);
    }
    mapScriptCall();
    setCoordinateData({ callbackFunc: setCoordinates });
  }, [coordinates]);

  return (
    <div className={`map-wrap${props?.disable ? "-disable" : ""}`} style={props?.locationStyle}>
      <div className="map-search-bar-wrap">
        {/* <img src={searchicon} className="map-search-bar-icon" alt=""/> */}
        <SearchIconSvg className="map-search-bar-icon" />
        <input
          id={"pac-input-" + props?.index}
          className="map-search-bar"
          type="text"
          placeholder="Search for a building, street, or area"
          autoComplete="off"
        />
      </div>
      <div id={"map-" + props?.index} className="map"></div>
    </div>
  );
};

export default LocationSearch;
