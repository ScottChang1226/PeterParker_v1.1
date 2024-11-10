document.addEventListener('DOMContentLoaded', function () {
    const profileAvatar = document.getElementById('profile-avatar');
    const jwtToken = localStorage.getItem('token');
    const adminId = localStorage.getItem('adminId');

    if (!adminId) {
        console.error('adminId 未定義，無法載入頭像圖片。');
        profileAvatar.src = 'images/dashboard-avatar.jpg'; // 預設圖片
        return;
    }

    // 從伺服器加載圖片
    fetch(`http://localhost:8081/api/admin/image/${adminId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`
        }
    })
    .then(response => {
        if (response.ok) {
            return response.blob();
        } else {
            throw new Error('無法獲取頭像圖片');
        }
    })
    .then(blob => {
        const profileImgUrl = URL.createObjectURL(blob);
        profileAvatar.src = profileImgUrl;

        // 清理過時的 blob URL
        profileAvatar.onload = function () {
            URL.revokeObjectURL(profileImgUrl);
        };
    })
    .catch(error => {
        console.error('無法加載頭像:', error);
        profileAvatar.src = 'images/dashboard-avatar.jpg'; // 預設圖片
    });
});
