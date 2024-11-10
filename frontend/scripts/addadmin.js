// 獲取按鈕和彈出視窗
const addAdminButton = document.getElementById("addAdminButton");
const modal = document.getElementById("addAdminModal");
const closeBtn = document.querySelector(".close");

// 點擊「新增管理員」按鈕顯示彈出視窗
addAdminButton.onclick = function () {
    modal.style.display = "flex";
};

// 點擊關閉按鈕隱藏彈出視窗
closeBtn.onclick = function () {
    modal.style.display = "none";
};

// 點擊視窗外隱藏彈出視窗
window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
};

// 表單提交事件
document.getElementById("addAdminForm").onsubmit = function (event) {
    event.preventDefault();

    // 獲取表單數據
    const adminName = document.getElementById("adminName").value;
    const adminUsername = document.getElementById("adminUsername").value;
    const adminPassword = document.getElementById("adminPassword").value;
    const adminStatus = document.getElementById("adminStatus").value;

    // 發送 POST 請求到後端
    fetch('http://localhost:8081/api/admin/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            adminName: adminName,
            adminUsername: adminUsername,
            adminPassword: adminPassword,
            adminStatus: adminStatus
        })
    })
        .then(response => response.json())
        .then(data => {
            alert("新增管理員成功！");
            modal.style.display = "none";
            location.reload(); // 刷新頁面
        })
        .catch(error => {
            console.error("新增失敗:", error);
            alert("新增失敗，請稍後再試。");
        });
};