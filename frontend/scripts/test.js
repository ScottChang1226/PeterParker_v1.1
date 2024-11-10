document.addEventListener('DOMContentLoaded', function() {
    // 監聽選擇框變更事件，更新顯示的選擇結果
    document.querySelectorAll('select').forEach(function(select) {
        select.addEventListener('change', function() {
            updateSelectedTime();
        });
    });

    function updateSelectedTime() {
        const startTime = document.getElementById('start-time');
        const endTime = document.getElementById('end-time');
        const displayElement = document.getElementById('selected-time');
        
        if (startTime && endTime && displayElement) {
            const startTimeValue = startTime.value;
            const endTimeValue = endTime.value;

            if (startTimeValue && endTimeValue) {
                displayElement.textContent = `您選擇的時間範圍為：${startTimeValue} ~ ${endTimeValue}`;
            } else if (startTimeValue || endTimeValue) {
                displayElement.textContent = '請選擇完整的開始時間和結束時間。';
            } else {
                displayElement.textContent = '您尚未選擇開始時間和結束時間。';
            }
        }
    }

    // 點擊「下一步」按鈕時，儲存選擇的開始時間和結束時間，並跳轉到 B.html
    document.getElementById('next-button')?.addEventListener('click', function() {
        const startTime = document.getElementById('start-time');
        const endTime = document.getElementById('end-time');

        if (startTime && endTime) {
            const startTimeValue = startTime.value;
            const endTimeValue = endTime.value;

            if (startTimeValue && endTimeValue) {
                // 將開始時間和結束時間儲存在 localStorage 中
                localStorage.setItem('startTime', startTimeValue);
                localStorage.setItem('endTime', endTimeValue);
                // 跳轉到 B.html
                window.location.href = 'parking_booking_2.html';
            } else {
                alert('請選擇完整的開始時間和結束時間後再繼續。');
            }
        }
    });

    // 從 localStorage 中取得選擇的開始時間和結束時間
    const startTime = localStorage.getItem('startTime');
    const endTime = localStorage.getItem('endTime');
    const timeDisplayElement = document.getElementById('time-display');
    const displayContainer = document.getElementById('display-time');

    if (startTime && endTime) {
        // 顯示選擇的時間範圍
        if (timeDisplayElement) {
            timeDisplayElement.textContent = `${startTime} ~ ${endTime}`;
        }
        if (displayContainer) {
            displayContainer.textContent = `${startTime} ~ ${endTime}`;
        }
    } else {
        // 若無選擇時間範圍，提示使用者返回選擇頁面
        if (timeDisplayElement) {
            timeDisplayElement.textContent = '尚未選擇時間範圍，請返回上一頁進行選擇。';
        }
        if (displayContainer) {
            displayContainer.textContent = '尚未選擇任何時間範圍。';
        }
    }
});
