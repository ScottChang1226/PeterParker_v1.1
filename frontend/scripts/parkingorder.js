
var googleMap;
var markers = [];
var infoWindow;

function initMap() {
    // Initialize the Google Map
    googleMap = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 25.0330, lng: 121.5654 }, // Taipei default center
        zoom: 13,
        scrollwheel: true,
        mapId:'DEMO_MAP_ID'
    });

    // console.log('asdasdasd');
    // let marker = new google.maps.marker.AdvancedMarkerElement({
    // 	map:googleMap,
    // 	position: { lat: 25.042, lng: 121.56 },
    // 	title: "asdasdasd",
    // });
    // console.log('asdasdasd');

    infoWindow = new google.maps.InfoWindow();

    // Initialize autocomplete
    initAutocomplete();

    // Fetch initial parking data and place markers
    fetchAndDisplayParking();

    // Listen to map changes and fetch parking data accordingly
    googleMap.addListener('bounds_changed', function() {
        fetchAndDisplayParking();
    });

}

function initAutocomplete() {
    var input = document.getElementById('autocomplete-input');
    var autocomplete = new google.maps.places.Autocomplete(input);

    autocomplete.addListener('place_changed', function() {
        var place = autocomplete.getPlace();
        if (!place.geometry) {
            return;
        }

        // Center the map to the selected place
        googleMap.setCenter(place.geometry.location);
        googleMap.setZoom(15);

        // Fetch and display nearby parking
        fetchAndDisplayParking();
    });

    if ($('.main-search-input-item').length > 0) {
        setTimeout(function() {
            $(".pac-container").prependTo("#autocomplete-container");
        }, 300);
    }
}

function fetchAndDisplayParking() {
    // Assuming the back-end API provides filtered parking listings based on current map bounds
    var bounds = googleMap.getBounds();
    if (!bounds) return;

    var ne = bounds.getNorthEast();
    var sw = bounds.getSouthWest();

    //暫時設定連接位址
    fetch('http://localhost:8081/order/getParkingListings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            northEast: { lat: ne.lat(), lng: ne.lng() },
            southWest: { lat: sw.lat(), lng: sw.lng() },
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            clearMarkers(); // Clear previous markers
            data.forEach(parking => {
                createMarker(parking);
            });
        })
        .catch(error => {
            console.error('Error fetching parking listings:', error);
        });
}

function createMarker(parking) {
    var marker = new google.maps.Marker({
        map: googleMap,
        position: { lat: parking.parkingLat, lng: parking.parkingLong },
        title: parking.parkingName,
    });

    marker.addListener('click', function() {
        infoWindow.setContent(`
                <div class="infobox-content">
                    <h5>${parking.parkingName}</h5>
                    <p>${parking.parkingLocation}</p>
                    <p>Hourly Rate: $${parking.workdayHourlyRate}</p>
                    <a href="parking_booking_1.html?parkingId=${parking.parkingId}">Book Now</a>
                </div>
            `);
        infoWindow.open(googleMap, marker);
    });

    markers.push(marker);
}

function clearMarkers() {
    markers.forEach(marker => {
        marker.setMap(null);
    });
    markers = [];
}