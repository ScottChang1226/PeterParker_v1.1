
document.addEventListener("DOMContentLoaded", function () {
    // 獲取暫存的訂單資料
    const reservationDate = sessionStorage.getItem('date');
    const reservationStartTime = sessionStorage.getItem('startTime');
    const reservationEndTime = sessionStorage.getItem('endTime');
    const totalPrice = sessionStorage.getItem('totalCost'); // 注意，使用 'totalCost' 而非 'totalPrice'

    // 計算時數
    const startHour = parseInt(reservationStartTime.split(':')[0], 10);
    const endHour = parseInt(reservationEndTime.split(':')[0], 10);
    const duration = endHour - startHour;

    // 動態填充資料
    if (reservationDate) {
        document.getElementById('reservation-date').textContent = reservationDate;
    } else {
        console.error("找不到預約日期的資料");
    }

    if (reservationStartTime && reservationEndTime) {
        document.getElementById('reservation-time').textContent = `${reservationStartTime} ~ ${reservationEndTime}`;
    } else {
        console.error("找不到預約時間段的資料");
    }

    if (duration > 0) {
        document.getElementById('reservation-duration').textContent = `${duration} 小時`;
    } else {
        console.error("計算預約時數時發生錯誤");
    }

    if (totalPrice) {
        document.getElementById('total-cost').textContent = `${totalPrice} NTD`;
    } else {
        console.error("找不到總金額的資料");
    }

    // 從 sessionStorage 獲取並更新停車場相關資料
    const selectedParking = JSON.parse(localStorage.getItem("selectedParking"));

    if (selectedParking) {
        if (selectedParking.parkingName) {
            document.getElementById('parking-name').textContent = selectedParking.parkingName;
        } else {
            console.error("找不到停車場名稱資料");
        }

        if (selectedParking.parkingLocation) {
            document.getElementById('parking-address').textContent = selectedParking.parkingLocation;
        } else {
            console.error("找不到停車場地址資料");
        }

        // 更新摘要側欄
        const sidebarParkingNameElement = document.getElementById('sidebar-parking-name');
        const sidebarParkingAddressElement = document.getElementById('sidebar-parking-address');

        if (sidebarParkingNameElement) {
            sidebarParkingNameElement.textContent = selectedParking.parkingName;
        } else {
            console.error("找不到側欄停車場名稱的元素");
        }

        if (sidebarParkingAddressElement) {
            sidebarParkingAddressElement.textContent = selectedParking.parkingLocation;
        } else {
            console.error("找不到側欄停車場地址的元素");
        }
    } else {
        console.error("未找到選擇的停車場資訊");
    }
});
