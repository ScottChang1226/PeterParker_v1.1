// Default infoBox Rating Type
var infoBox_ratingType = 'star-rating';

(function($){
    "use strict";

    // Infobox Output
    function locationData(locationURL,locationImg,locationTitle, locationAddress, locationRating, locationRatingCounter) {
      return(''+
        '<a href="'+ locationURL +'" class="listing-img-container">'+
           '<div class="infoBox-close"><i class="fa fa-times"></i></div>'+
           '<img src="'+locationImg+'" alt="">'+

           '<div class="listing-item-content">'+
              '<h3>'+locationTitle+'</h3>'+
              '<span>'+locationAddress+'</span>'+
           '</div>'+

        '</a>'+

        '<div class="listing-content">'+
           '<div class="listing-title">'+
              '<div class="'+infoBox_ratingType+'" data-rating="'+locationRating+'"><div class="rating-counter">('+locationRatingCounter+' )</div></div>'+
           '</div>'+
        '</div>')
  }

  // Locations
  var locations = [
    [ locationData('listings-single-page.html','images/listing-item-01.jpg',"Tom's Restaurant",'964 School Street, New York', '3.5', '12'), 40.94401669296697, -74.16938781738281, 1, '<i class="im im-icon-Chef-Hat"></i>'],
    [ locationData('listings-single-page.html','images/listing-item-02.jpg','Sticky Band','Bishop Avenue, New York', '5.0', '23'), 40.77055783505125, -74.26002502441406,          2, '<i class="im im-icon-Electric-Guitar"></i>'],
    [ locationData('listings-single-page.html','images/listing-item-03.jpg','Hotel Govendor','778 Country Street, New York', '2.0', '17'), 40.7427837, -73.11445617675781,         3, '<i class="im im-icon-Home-2"></i>' ],
    [ locationData('listings-single-page.html','images/listing-item-04.jpg','Burger House','2726 Shinn Street, New York', '5.0', '31'), 40.70437865245596, -73.98674011230469,     4, '<i class="im im-icon-Hamburger"></i>' ],
    [ locationData('listings-single-page.html','images/listing-item-05.jpg','Airport','1512 Duncan Avenue, New York', '3.5', '46'), 40.641311, -73.778139,                         5, '<i class="im im-icon-Plane"></i>'],
    [ locationData('listings-single-page.html','images/listing-item-06.jpg','Think Coffee','215 Terry Lane, New York', '4.5', '15'), 41.080938, -73.535957,                        6, '<i class="im im-icon-Coffee"></i>'],
    [ locationData('listings-single-page.html','images/listing-item-04.jpg','Burger House','2726 Shinn Street, New York', '5.0', '31'), 41.079386, -73.519478,                     7, '<i class="im im-icon-Hamburger"></i>'],

    [ locationData('listings-single-page.html','images/listing-item-04.jpg','Burger House','2726 Shinn Street, New York', '5.0', '31'), 52.368630, 4.895782,                     7, '<i class="im im-icon-Hamburger"></i>'],
    [ locationData('listings-single-page.html','images/listing-item-04.jpg','Burger House','2726 Shinn Street, New York', '5.0', '31'), 52.350179, 4.634857,                     7, '<i class="im im-icon-Hamburger"></i>'],
  ];

  // 獲取後端資料並更新 locations
function fetchAndUpdateLocations() {
  return fetch('http://localhost:8081/order/getParkingListings', {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({
          northEast: {
              lat: 30.0800,  // 固定的北東緯度
              lng: 130.5700  // 固定的北東經度
          },
          southWest: {
              lat: 20.0200,  // 固定的南西緯度
              lng: 110.5000  // 固定的南西經度
          }
      })
  })
  .then(response => {
      if (!response.ok) {
          throw new Error('Network response was not ok');
      }
      return response.json();
  })
  .then(data => {
      // 將資料轉換為 locations 可以接受的格式
      locations = data.map((parking, index) => {
          return [
              locationData(
                  'parking_booking_1.html',
                  parking.parkingImg ? `data:image/jpeg;base64,${parking.parkingImg}` : 'images/default-image.jpg', // 如果沒有圖片則使用默認圖片
                  parking.parkingName,
                  `${parking.parkingRegion}, ${parking.parkingLocation}`,
                  '4.5', // 假設評分，實際應從後端獲取
                  `${parking.capacity} 車位`
              ),
              parking.parkingLat,
              parking.parkingLong,
              index + 1, // 用 index 作為 marker ID
              '<i class="im im-icon-Car"></i>', // 可根據需求修改圖標
              parking.parkingId // 新增 parkingId
          ];
      });
  })
  .catch(error => {
      console.error('Error fetching parking listings:', error);
      throw error; // 如果發生錯誤，向上拋出錯誤
  });
}


    function mainMap() {

      // Locations
      // ----------------------------------------------- //
      var ib = new InfoBox();

      

      // Chosen Rating Type
      google.maps.event.addListener(ib,'domready',function(){
         if (infoBox_ratingType = 'numerical-rating') {
            numericalRating('.infoBox .'+infoBox_ratingType+'');
         }
         if (infoBox_ratingType = 'star-rating') {
            starRating('.infoBox .'+infoBox_ratingType+'');
         }
      });



      // Map Attributes
      // ----------------------------------------------- //

      var mapZoomAttr = $('#map').attr('data-map-zoom');
      var mapScrollAttr = $('#map').attr('data-map-scroll');

      if (typeof mapZoomAttr !== typeof undefined && mapZoomAttr !== false) {
          var zoomLevel = parseInt(mapZoomAttr);
      } else {
          var zoomLevel = 5;
      }

      if (typeof mapScrollAttr !== typeof undefined && mapScrollAttr !== false) {
         var scrollEnabled = parseInt(mapScrollAttr);
      } else {
        var scrollEnabled = false;
      }


      // Main Map
      var map = new google.maps.Map(document.getElementById('map'), {
        zoom: zoomLevel,
        scrollwheel: scrollEnabled,
        center: new google.maps.LatLng(25.042, 121.56),
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        zoomControl: false,
        mapTypeControl: false,
        scaleControl: false,
        panControl: false,
        navigationControl: false,
        streetViewControl: false,
        gestureHandling: 'cooperative',

        // Google Map Style
        styles: [{"featureType":"poi","elementType":"labels.text.fill","stylers":[{"color":"#747474"},{"lightness":"23"}]},{"featureType":"poi.attraction","elementType":"geometry.fill","stylers":[{"color":"#f38eb0"}]},{"featureType":"poi.government","elementType":"geometry.fill","stylers":[{"color":"#ced7db"}]},{"featureType":"poi.medical","elementType":"geometry.fill","stylers":[{"color":"#ffa5a8"}]},{"featureType":"poi.park","elementType":"geometry.fill","stylers":[{"color":"#c7e5c8"}]},{"featureType":"poi.place_of_worship","elementType":"geometry.fill","stylers":[{"color":"#d6cbc7"}]},{"featureType":"poi.school","elementType":"geometry.fill","stylers":[{"color":"#c4c9e8"}]},{"featureType":"poi.sports_complex","elementType":"geometry.fill","stylers":[{"color":"#b1eaf1"}]},{"featureType":"road","elementType":"geometry","stylers":[{"lightness":"100"}]},{"featureType":"road","elementType":"labels","stylers":[{"visibility":"off"},{"lightness":"100"}]},{"featureType":"road.highway","elementType":"geometry.fill","stylers":[{"color":"#ffd4a5"}]},{"featureType":"road.arterial","elementType":"geometry.fill","stylers":[{"color":"#ffe9d2"}]},{"featureType":"road.local","elementType":"all","stylers":[{"visibility":"simplified"}]},{"featureType":"road.local","elementType":"geometry.fill","stylers":[{"weight":"3.00"}]},{"featureType":"road.local","elementType":"geometry.stroke","stylers":[{"weight":"0.30"}]},{"featureType":"road.local","elementType":"labels.text","stylers":[{"visibility":"on"}]},{"featureType":"road.local","elementType":"labels.text.fill","stylers":[{"color":"#747474"},{"lightness":"36"}]},{"featureType":"road.local","elementType":"labels.text.stroke","stylers":[{"color":"#e9e5dc"},{"lightness":"30"}]},{"featureType":"transit.line","elementType":"geometry","stylers":[{"visibility":"on"},{"lightness":"100"}]},{"featureType":"water","elementType":"all","stylers":[{"color":"#d2e7f7"}]}]

      });


      // Marker highlighting when hovering listing item
      $('.listing-item-container').on('mouseover', function(){

        var listingAttr = $(this).data('marker-id');

        if(listingAttr !== undefined) {
          var listing_id = $(this).data('marker-id') - 1;
          var marker_div = allMarkers[listing_id].div;

          $(marker_div).addClass('clicked');

          $(this).on('mouseout', function(){
              if ($(marker_div).is(":not(.infoBox-opened)")) {
                 $(marker_div).removeClass('clicked');
              }
           });
        }

      });


      // Infobox
      // ----------------------------------------------- //

      var boxText = document.createElement("div");
      boxText.className = 'map-box'

      var currentInfobox;

      var boxOptions = {
              content: boxText,
              disableAutoPan: false,
              alignBottom : true,
              maxWidth: 0,
              pixelOffset: new google.maps.Size(-134, -55),
              zIndex: null,
              boxStyle: {
                width: "270px"
              },
              closeBoxMargin: "0",
              closeBoxURL: "",
              infoBoxClearance: new google.maps.Size(25, 25),
              isHidden: false,
              pane: "floatPane",
              enableEventPropagation: false,
      };


      var markerCluster, overlay, i;
      var allMarkers = [];

      var clusterStyles = [
        {
          textColor: 'white',
          url: '',
          height: 50,
          width: 50
        }
      ];


      var markerIco;
      for (i = 0; i < locations.length; i++) {

        markerIco = locations[i][4];

        var overlaypositions = new google.maps.LatLng(locations[i][1], locations[i][2]),

        overlay = new CustomMarker(
         overlaypositions,
          map,
          {
            marker_id: i
          },
          markerIco
        );

        allMarkers.push(overlay);

        google.maps.event.addDomListener(overlay, 'click', (function(overlay, i) {
          return function() {
              ib.setOptions(boxOptions);
              boxText.innerHTML = locations[i][0]; // 設置資訊框的內容
              ib.close();
              ib.open(map, overlay);
              currentInfobox = locations[i][3];
      
              google.maps.event.addListener(ib, 'domready', function() {
                  // 當資訊框已準備好後，增加點擊處理程序
                  $('.infoBox-close').click(function(e) {
                      e.preventDefault();
                      ib.close();
                      $('.map-marker-container').removeClass('clicked infoBox-opened');
                  });
      
                  // 這裡新增停車場名稱點擊事件的處理程序
                  $('.listing-img-container').on('click', function(e) {
                      e.preventDefault();
      
                      // 保存選擇的停車場信息到 localStorage，包括 parkingId
                      const selectedParking = {
                          parkingId: locations[i][5], // 保存 parkingId
                          parkingName: locations[i][0].match(/<h3>(.*?)<\/h3>/)[1], // 從 HTML 提取停車場名稱
                          parkingAddress: locations[i][0].match(/<span>(.*?)<\/span>/)[1], // 從 HTML 提取地址
                          parkingLat: locations[i][1],
                          parkingLong: locations[i][2],
                          capacity: locations[i][0].match(/<div class="rating-counter">\((.*?)\)/)[1] // 提取容量信息
                      };
      
                      localStorage.setItem('selectedParking', JSON.stringify(selectedParking));
      
                      // 跳轉到新頁面
                      window.location.href = 'parking_booking_1.html';
                  });
              });
          };
      })(overlay, i));
      


      }


      // Marker Clusterer Init
      // ----------------------------------------------- //

      var options = {
          imagePath: 'images/',
          styles : clusterStyles,
          minClusterSize : 2
      };

      markerCluster = new MarkerClusterer(map, allMarkers, options);

      google.maps.event.addDomListener(window, "resize", function() {
          var center = map.getCenter();
          google.maps.event.trigger(map, "resize");
          map.setCenter(center);
      });



      // Custom User Interface Elements
      // ----------------------------------------------- //

      // Custom Zoom-In and Zoom-Out Buttons
        var zoomControlDiv = document.createElement('div');
        var zoomControl = new ZoomControl(zoomControlDiv, map);

        function ZoomControl(controlDiv, map) {

          zoomControlDiv.index = 1;
          map.controls[google.maps.ControlPosition.RIGHT_CENTER].push(zoomControlDiv);
          // Creating divs & styles for custom zoom control
          controlDiv.style.padding = '5px';
          controlDiv.className = "zoomControlWrapper";

          // Set CSS for the control wrapper
          var controlWrapper = document.createElement('div');
          controlDiv.appendChild(controlWrapper);

          // Set CSS for the zoomIn
          var zoomInButton = document.createElement('div');
          zoomInButton.className = "custom-zoom-in";
          controlWrapper.appendChild(zoomInButton);

          // Set CSS for the zoomOut
          var zoomOutButton = document.createElement('div');
          zoomOutButton.className = "custom-zoom-out";
          controlWrapper.appendChild(zoomOutButton);

          // Setup the click event listener - zoomIn
          google.maps.event.addDomListener(zoomInButton, 'click', function() {
            map.setZoom(map.getZoom() + 1);
          });

          // Setup the click event listener - zoomOut
          google.maps.event.addDomListener(zoomOutButton, 'click', function() {
            map.setZoom(map.getZoom() - 1);
          });

      }


      // Scroll enabling button
      var scrollEnabling = $('#scrollEnabling');

      $(scrollEnabling).click(function(e){
          e.preventDefault();
          $(this).toggleClass("enabled");

          if ( $(this).is(".enabled") ) {
             map.setOptions({'scrollwheel': true});
          } else {
             map.setOptions({'scrollwheel': false});
          }
      })


      // Geo Location Button
      $("#geoLocation, .input-with-icon.location a").click(function(e){
          e.preventDefault();
          geolocate();
      });

      function geolocate() {

          if (navigator.geolocation) {
              navigator.geolocation.getCurrentPosition(function (position) {
                  var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                  map.setCenter(pos);
                  map.setZoom(12);
              });
          }
      }

    }


    // Map Init
    var map = document.getElementById('map');
    if (typeof(map) != 'undefined' && map != null) {
        fetchAndUpdateLocations()
            .then(() => {
                // 成功更新後初始化地圖
                mainMap();
            })
            .catch(error => {
                console.error('Failed to update locations:', error);
            });
    }


    // ---------------- Main Map / End ---------------- //


    // Single Listing Map
    // ----------------------------------------------- //

    // 在頁面載入時，從 localStorage 中讀取選擇的停車場資料
document.addEventListener("DOMContentLoaded", function() {
  const selectedParkingJSON = localStorage.getItem("selectedParking");
  if (selectedParkingJSON) {
      const selectedParking = JSON.parse(selectedParkingJSON);

      // 如果成功讀取到停車場資料，設置地圖位置
      if (selectedParking.parkingLat && selectedParking.parkingLong) {
          initSingleMap(selectedParking.parkingLat, selectedParking.parkingLong);
      } else {
          console.error("Invalid parking data found in localStorage.");
      }
  } else {
      console.error("No parking data found in localStorage.");
  }
});

// 初始化單一地圖的函數，接收緯度和經度作為參數
function initSingleMap(lat, lng) {
  const myLatlng = new google.maps.LatLng(lat, lng);

  const single_map = new google.maps.Map(document.getElementById('singleListingMap'), {
      zoom: 15,
      center: myLatlng,
      scrollwheel: false,
      zoomControl: false,
      mapTypeControl: false,
      scaleControl: false,
      panControl: false,
      navigationControl: false,
      streetViewControl: false,
      styles: [/* 地圖樣式... */]
  });

  // 添加地圖標記
  new google.maps.Marker({
      map: single_map,
      position: myLatlng,
      title: "Selected Parking",
  });
}


    function singleListingMap() {

      // 確保 google 對象已定義
      if (typeof google === 'undefined' || typeof google.maps === 'undefined') {
        console.error('Google Maps API 未加載，無法初始化單一地圖。');
        return;
    }
    // 檢查是否存在選擇的停車場經緯度
    const selectedParkingJSON = localStorage.getItem("selectedParking");
    let lat, lng;

    if (selectedParkingJSON) {
        const selectedParking = JSON.parse(selectedParkingJSON);
        if (selectedParking.parkingLat && selectedParking.parkingLong) {
            lat = selectedParking.parkingLat;
            lng = selectedParking.parkingLong;
        } else {
            console.error("Invalid parking data found in localStorage.");
        }
    }

    // 如果無法從 localStorage 中獲取到有效的經緯度，則設置為預設值
    lat = lat || 25.0330; // 台北預設值
    lng = lng || 121.5654;

    // 使用上述位置進行地圖初始化
    const myLatlng = new google.maps.LatLng(lat, lng);
    

      // var myLatlng = new google.maps.LatLng({lng: $( '#singleListingMap' ).data('longitude'),lat: $( '#singleListingMap' ).data('latitude'), });

      var single_map = new google.maps.Map(document.getElementById('singleListingMap'), {
        zoom: 15,
        center: myLatlng,
        scrollwheel: false,
        zoomControl: false,
        mapTypeControl: false,
        scaleControl: false,
        panControl: false,
        navigationControl: false,
        streetViewControl: false,
        styles:  [{"featureType":"poi","elementType":"labels.text.fill","stylers":[{"color":"#747474"},{"lightness":"23"}]},{"featureType":"poi.attraction","elementType":"geometry.fill","stylers":[{"color":"#f38eb0"}]},{"featureType":"poi.government","elementType":"geometry.fill","stylers":[{"color":"#ced7db"}]},{"featureType":"poi.medical","elementType":"geometry.fill","stylers":[{"color":"#ffa5a8"}]},{"featureType":"poi.park","elementType":"geometry.fill","stylers":[{"color":"#c7e5c8"}]},{"featureType":"poi.place_of_worship","elementType":"geometry.fill","stylers":[{"color":"#d6cbc7"}]},{"featureType":"poi.school","elementType":"geometry.fill","stylers":[{"color":"#c4c9e8"}]},{"featureType":"poi.sports_complex","elementType":"geometry.fill","stylers":[{"color":"#b1eaf1"}]},{"featureType":"road","elementType":"geometry","stylers":[{"lightness":"100"}]},{"featureType":"road","elementType":"labels","stylers":[{"visibility":"off"},{"lightness":"100"}]},{"featureType":"road.highway","elementType":"geometry.fill","stylers":[{"color":"#ffd4a5"}]},{"featureType":"road.arterial","elementType":"geometry.fill","stylers":[{"color":"#ffe9d2"}]},{"featureType":"road.local","elementType":"all","stylers":[{"visibility":"simplified"}]},{"featureType":"road.local","elementType":"geometry.fill","stylers":[{"weight":"3.00"}]},{"featureType":"road.local","elementType":"geometry.stroke","stylers":[{"weight":"0.30"}]},{"featureType":"road.local","elementType":"labels.text","stylers":[{"visibility":"on"}]},{"featureType":"road.local","elementType":"labels.text.fill","stylers":[{"color":"#747474"},{"lightness":"36"}]},{"featureType":"road.local","elementType":"labels.text.stroke","stylers":[{"color":"#e9e5dc"},{"lightness":"30"}]},{"featureType":"transit.line","elementType":"geometry","stylers":[{"visibility":"on"},{"lightness":"100"}]},{"featureType":"water","elementType":"all","stylers":[{"color":"#d2e7f7"}]}]
      });

      // Steet View Button
      $('#streetView').click(function(e){
         e.preventDefault();
         single_map.getStreetView().setOptions({visible:true,position:myLatlng});
         // $(this).css('display', 'none')
      });


      // Custom zoom buttons
      var zoomControlDiv = document.createElement('div');
      var zoomControl = new ZoomControl(zoomControlDiv, single_map);

      function ZoomControl(controlDiv, single_map) {

        zoomControlDiv.index = 1;
        single_map.controls[google.maps.ControlPosition.RIGHT_CENTER].push(zoomControlDiv);

        controlDiv.style.padding = '5px';

        var controlWrapper = document.createElement('div');
        controlDiv.appendChild(controlWrapper);

        var zoomInButton = document.createElement('div');
        zoomInButton.className = "custom-zoom-in";
        controlWrapper.appendChild(zoomInButton);

        var zoomOutButton = document.createElement('div');
        zoomOutButton.className = "custom-zoom-out";
        controlWrapper.appendChild(zoomOutButton);

        google.maps.event.addDomListener(zoomInButton, 'click', function() {
          single_map.setZoom(single_map.getZoom() + 1);
        });

        google.maps.event.addDomListener(zoomOutButton, 'click', function() {
          single_map.setZoom(single_map.getZoom() - 1);
        });

      }


      // Marker
      var singleMapIco =  "<i class='"+$('#singleListingMap').data('map-icon')+"'></i>";

      new CustomMarker(
        myLatlng,
        single_map,
        {
          marker_id: '1'
        },
        singleMapIco
      );


    }

    // Single Listing Map Init
    var single_map =  document.getElementById('singleListingMap');
    if (typeof(single_map) != 'undefined' && single_map != null) {
      google.maps.event.addDomListener(window, 'load',  singleListingMap);
    }

    // -------------- Single Listing Map / End -------------- //



    // Custom Map Marker
    // ----------------------------------------------- //

    function CustomMarker(latlng, map, args, markerIco) {
      this.latlng = latlng;
      this.args = args;
      this.markerIco = markerIco;
      this.setMap(map);
    }

    CustomMarker.prototype = new google.maps.OverlayView();

    CustomMarker.prototype.draw = function() {

      var self = this;

      var div = this.div;

      if (!div) {

        div = this.div = document.createElement('div');
        div.className = 'map-marker-container';

        div.innerHTML = '<div class="marker-container">'+
                            '<div class="marker-card">'+
                               '<div class="front face">' + self.markerIco + '</div>'+
                               '<div class="back face">' + self.markerIco + '</div>'+
                               '<div class="marker-arrow"></div>'+
                            '</div>'+
                          '</div>'


        // Clicked marker highlight
        google.maps.event.addDomListener(div, "click", function(event) {
            $('.map-marker-container').removeClass('clicked infoBox-opened');
            google.maps.event.trigger(self, "click");
            $(this).addClass('clicked infoBox-opened');
        });


        if (typeof(self.args.marker_id) !== 'undefined') {
          div.dataset.marker_id = self.args.marker_id;
        }

        var panes = this.getPanes();
        panes.overlayImage.appendChild(div);
      }

      var point = this.getProjection().fromLatLngToDivPixel(this.latlng);

      if (point) {
        div.style.left = (point.x) + 'px';
        div.style.top = (point.y) + 'px';
      }
    };

    CustomMarker.prototype.remove = function() {
      if (this.div) {
        this.div.parentNode.removeChild(this.div);
        this.div = null; $(this).removeClass('clicked');
      }
    };

    CustomMarker.prototype.getPosition = function() { return this.latlng; };

    // -------------- Custom Map Marker / End -------------- //



})(this.jQuery);
