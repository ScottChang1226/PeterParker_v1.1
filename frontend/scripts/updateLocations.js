// 用來從後端獲取資料以便傳回map.js的location
// updateLocations.js

// 引入 fetchParkingData 方法來從後端獲取停車場數據並更新 locations
export function fetchParkingData() {
  fetch('http://localhost:8081/order/getParkingListings', {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({
          // 可根據需要設定地圖範圍來過濾資料
          northEast: { lat: 25.05, lng: 121.55 },
          southWest: { lat: 25.02, lng: 121.53 }
      })
  })
  .then(response => {
      if (!response.ok) {
          throw new Error('Network response was not ok');
      }
      return response.json();
  })
  .then(data => {
      // 將獲取到的停車場資料轉換成 locations 陣列的格式
      data.forEach(parking => {
          const location = [
              locationData(
                  'listings-single-page.html',
                  'images/listing-item-default.jpg', // 你可以根據需要設置為動態圖片
                  parking.parkingName,
                  `${parking.parkingRegion}, ${parking.parkingLocation}`,
                  '4.5', // 假設的評分，可以根據需要修改
                  '20'   // 假設的評論數量，可以根據需要修改
              ),
              parking.parkingLat,
              parking.parkingLong,
              parking.parkingId,
              '<i class="im im-icon-Parking-Sign"></i>' // 使用自定義圖標
          ];

          // 添加到 locations 陣列中
          locations.push(location);
      });

      // 打印 locations 確認更新的資料
      console.log('Updated locations:', locations);
  })
  .catch(error => {
      console.error('Error fetching parking data:', error);
  });
}

function locationData(url, img, name, address, rating, reviews) {
  return `
      <a href="${url}" class="listing-img-container">
        <div class="infoBox-close"><i class="fa fa-times"></i></div>
        <img src="${img}" alt="">
        <div class="listing-item-content">
          <h3>${name}</h3>
          <span>${address}</span>
        </div>
      </a>
      <div class="listing-content">
        <div class="listing-title">
          <div class="star-rating" data-rating="${rating}">
            <div class="rating-counter">(${reviews} 評價)</div>
          </div>
        </div>
      </div>
  `;
}
