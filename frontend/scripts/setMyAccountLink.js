document.addEventListener('DOMContentLoaded', function () {
    const adminId = localStorage.getItem('adminId');
    const myAccountLink = document.getElementById('my-account-link');

    if (adminId && myAccountLink) {
        // 設定 "我的帳號" 連結，指向管理員修改頁面並附上管理員 ID
        myAccountLink.href = `dashboard-admin-inner.html?adminId=${adminId}`;
    } else {
        console.error("無法獲取管理員 ID，或 '我的帳號' 連結元素不存在。");
    }
});
