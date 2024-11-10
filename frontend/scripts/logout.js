function logout() {
    // 清除 localStorage 中的 token 和 adminId
    localStorage.removeItem('token');
    localStorage.removeItem('adminId');
    localStorage.removeItem('profileImg');
    
    // 導向至登錄頁面（這裡假設您的登錄頁面是 dashboard-login.html）
    window.location.href = 'dashboard-login.html';
}

// 將登出功能綁定到所有的登出按鈕
document.addEventListener('DOMContentLoaded', function () {
    const logoutButtons = document.querySelectorAll('[onclick="logout()"]');
    logoutButtons.forEach(button => {
        button.addEventListener('click', logout);
    });
});
