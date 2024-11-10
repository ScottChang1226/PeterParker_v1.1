document.addEventListener("DOMContentLoaded", function () {
    // 取出保存的停車場資訊
    const selectedParking = JSON.parse(localStorage.getItem("selectedParking"));

    if (selectedParking) {
        // 動態填充停車場的詳細信息到頁面中
        const parkingNameElement = document.getElementById('parking-name');
        const parkingAddressElement = document.getElementById('parking-address');
        const parkingInfoElement = document.getElementById('parking-info');
        const ownerMessageElement = document.getElementById('owner-message');
        const ownerDialogTitleElement = document.querySelector('#small-dialog .small-dialog-header h3');
        const ownerTitleElement = document.querySelector('.hosted-by-title h4 a');
        const ownerEmailElement = document.querySelector('.listing-details-sidebar li i.fa-envelope-o').nextElementSibling;

        // 填充停車場資訊到對應的元素
        if (parkingNameElement) {
            parkingNameElement.textContent = selectedParking.parkingName;
        }

        if (parkingAddressElement) {
            parkingAddressElement.textContent = selectedParking.parkingAddress;
        }

        if (parkingInfoElement) {
            parkingInfoElement.textContent = `(容量: ${selectedParking.capacity} 車位)`;
        }

        if (ownerTitleElement) {
            ownerTitleElement.textContent = selectedParking.ownerName || '未知業主';
        }

        if (ownerEmailElement) {
            ownerEmailElement.textContent = selectedParking.ownerEmail || '未知';
        }

        // 填充業主名稱至對話框中的佔位符
        const ownerName = selectedParking.ownerName || "業主";
        if (ownerMessageElement) {
            ownerMessageElement.setAttribute('placeholder', `Your message to ${ownerName}`);
        }

        if (ownerDialogTitleElement) {
            ownerDialogTitleElement.textContent = `聯絡 ${ownerName}`;
        }

        // 地圖顯示
        const mapElement = document.getElementById('singleListingMap');
        if (mapElement) {
            const lat = parseFloat(selectedParking.parkingLat);
            const lng = parseFloat(selectedParking.parkingLong);

            if (!isNaN(lat) && !isNaN(lng)) {
                const map = new google.maps.Map(mapElement, {
                    center: { lat: lat, lng: lng },
                    zoom: 15
                });

                new google.maps.Marker({
                    position: { lat: lat, lng: lng },
                    map: map,
                    title: selectedParking.parkingName
                });
            } else {
                console.error('經緯度資料無效，無法顯示地圖');
            }
        } else {
            console.error("未找到地圖元素");
        }
    } else {
        console.error("未找到選擇的停車場資訊");
    }
});

function goToNextPage() {
    const selectedParking = JSON.parse(localStorage.getItem("selectedParking"));
    const date = document.getElementById('date-picker').value;
    const startTime = document.getElementById('start-time').value;
    const endTime = document.getElementById('end-time').value;

    if (selectedParking && date && startTime && endTime) {
        // 暫存選擇的資料，包括 parkingId
        if (selectedParking.parkingId) {
            sessionStorage.setItem('parkingId', selectedParking.parkingId);
        } else {
            console.error('選擇的停車場資訊中沒有找到 parkingId');
            alert('系統錯誤，請重試');
            return;
        }
        
        sessionStorage.setItem('date', date);
        sessionStorage.setItem('startTime', startTime);
        sessionStorage.setItem('endTime', endTime);

        // 跳轉至下一頁
        window.location.href = 'parking_booking_2.html';
    } else {
        alert('請選擇日期和時間段');
    }
}


// 透過連絡業主按鈕發送消息給業主
document.getElementById('contact-owner-button').addEventListener('click', function () {
    const message = document.getElementById('owner-message').value;

    if (message.trim() === "") {
        alert("請填寫您想要發給業主的訊息");
        return;
    }

    // 取出保存的停車場資訊
    const selectedParking = JSON.parse(localStorage.getItem("selectedParking"));

    if (selectedParking) {
        // 獲取 JWT token
        const peterParkerToken = localStorage.getItem('peterParkerToken');
        if (!peterParkerToken) {
            console.error('JWT token not found, redirecting to login');
            window.location.href = 'index.html'; // 重定向到登入頁面
            return;
        }

        // 傳送消息至後端
        fetch('http://localhost:8081/contactOwner', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${peterParkerToken}`,
            },
            body: JSON.stringify({
                parkingId: selectedParking.parkingId,
                message: message,
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                alert('訊息已發送成功！');
            })
            .catch(error => {
                console.error('Error sending message:', error);
                alert('發送訊息失敗，請稍後再試');
            });
    } else {
        console.error("未找到選擇的停車場資訊，無法發送訊息");
    }
});
