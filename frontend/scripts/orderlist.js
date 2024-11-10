import { config } from './config.js';

let isMapInitialized = false; // 全域變量標示地圖是否初始化完成

$(document).ready(function () {

    // 初始化地圖完成後更新標誌
    window.initMap = function () {
        isMapInitialized = true;
    };

    // 搜尋功能 AJAX 請求
    $('#search-parking').on('change', function () {
        let keyword = $(this).val();
        if (keyword) {
            $.ajax({
                url: `http://${config}/order/searchParking`,
                type: 'GET',
                data: { keyword: keyword },
                success: function (data) {
                    renderParkingList(data);
                },
                error: function (err) {
                    console.error('Error searching parking:', err);
                }
            });
        } else {
            console.warn('Please enter a keyword for searching.');
        }
    });

    // 搜索按鈕的 AJAX 請求
    $('#search-button').on('click', function (e) {
        e.preventDefault(); // 阻止默認行為
        let keyword = $('#search-parking').val();
        if (keyword) {
            $.ajax({
                url: `http://${config}/order/searchParking`,
                type: 'GET',
                data: { keyword: keyword },
                success: function (data) {
                    renderParkingList(data);
                },
                error: function (err) {
                    console.error('Error searching parking:', err);
                }
            });
        } else {
            console.warn('Please enter a keyword for searching.');
        }
    });

    // 篩選功能 AJAX 請求
    $('.distance-radius').on('input', function () {
        let radius = $(this).val();

        // 確認 googleMap 已初始化
        if (isMapInitialized && googleMap && typeof googleMap.getCenter === 'function') {
            let center = googleMap.getCenter();
            let latitude = center.lat();
            let longitude = center.lng();

            $.ajax({
                url: `http://${config}/order/nearbyParking`,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    latitude: latitude,
                    longitude: longitude,
                    radius: radius
                }),
                success: function (data) {
                    renderParkingList(data);
                },
                error: function (err) {
                    console.error('Error finding nearby parking:', err);
                }
            });
        } else {
            console.error('Google Map is not initialized properly.');
        }
    });

    // 篩選按鈕點擊事件
    $('#apply-filters').on('click', function () {
        let selectedTypes = [];
        $('.filter-option:checked').each(function () {
            selectedTypes.push($(this).val());
        });

        let distance = $('.distance-radius').val();
        fetchFilteredParkingListings(selectedTypes, distance);
    });

    // 向後端發送篩選條件並獲取停車場列表
    function fetchFilteredParkingListings(types, distance) {
        const peterParkerToken = localStorage.getItem('peterParkerToken');
        fetch(`${config}/order/filteredParkingListings`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${peterParkerToken}`,
            },
            body: JSON.stringify({
                types: types,
                distance: distance
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                renderParkingList(data);
            })
            .catch(error => {
                console.error('Error fetching filtered parking listings:', error);
            });
    }

    // 渲染停車場列表的函數
    function renderParkingList(parkingData) {
        let listingResults = $('#listing-results');
        listingResults.html(''); // 清除原有內容

        if (parkingData.length === 0) {
            listingResults.html('<p>未找到符合條件的停車場。</p>');
        } else {
            parkingData.forEach(function (parking, index) {
                let position = { lat: parking.parkingLat, lng: parking.parkingLong };

                if (googleMap) {
                    new google.maps.Marker({
                        map: googleMap,
                        position: position,
                        title: parking.parkingName,
                    });
                }

                let listingItem = `
                    <div class="col-lg-12 col-md-12">
                        <div class="listing-item-container list-layout" data-marker-id="${index + 1}">
                            <a href="parking_booking_1.html?parkingId=${parking.parkingId}" class="listing-item" onclick="handleSelectParking(${index})">
                                <!-- Image -->
                                <div class="listing-item-image">
                                    <img src="data:image/jpeg;base64,${parking.parkingImg}" alt="${parking.parkingName}">
                                </div>
                                <!-- Content -->
                                <div class="listing-item-content">
                                    <div class="listing-item-inner">
                                        <h3>${parking.parkingName} <i class="verified-icon"></i></h3>
                                        <h4>${parking.holidayHourlyRate}元 (假日) / ${parking.workdayHourlyRate}元 (平日)<span>/小時</span></h4>
                                        <p>容量：${parking.capacity} 車位</p>
                                    </div>
                                    <span class="like-icon"></span>
                                </div>
                            </a>
                        </div>
                    </div>
                `;
                listingResults.append(listingItem);
            });

            // 保存所有停車場資料，方便用戶點擊後存入 localStorage
            window.parkingList = parkingData;
        }

        $('#showing-results').text(`共找到${parkingData.length}個結果`);
    }

    // 處理用戶點擊停車場列表項目的函數
    window.handleSelectParking = function(index) {
        const selectedParking = window.parkingList[index];
        // 保存選擇的停車場資訊到 localStorage
        localStorage.setItem("selectedParking", JSON.stringify(selectedParking));
    };
});
