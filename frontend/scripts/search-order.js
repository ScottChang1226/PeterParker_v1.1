document.addEventListener('DOMContentLoaded', function () {
    const table = document.querySelector('.data-table tbody'); // 選擇表格主體
    let allRows = []; // 儲存所有資料的副本，用於查詢功能

    // 設置所有的資料行（從外部呼叫該方法以初始化 allRows）
    const setAllRows = (rows) => {
        allRows = rows;
        displayRows(allRows); // 顯示所有資料行
        console.log("All rows have been set:", allRows); // 除錯訊息，確認 allRows 被正確設置
    };

    // 過濾資料行的函數
    const filterRows = () => {
        const searchValue = document.getElementById('generalSearch').value.toLowerCase();
        const filteredRows = allRows.filter(row => {
            // 獲取每個欄位的內容，並進行查詢值比對
            const orderId = row.querySelector('td:nth-child(1)').textContent.toLowerCase();
            const userName = row.querySelector('td:nth-child(2)').textContent.toLowerCase();
            const status = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
            const userComment = row.querySelector('td:nth-child(4)').textContent.toLowerCase();
            const orderTotalIncome = row.querySelector('td:nth-child(5)').textContent.toLowerCase();
            const orderModified = row.querySelector('td:nth-child(6)').textContent.toLowerCase();

            // 檢查查詢值是否出現在任何目標欄位中
            return (
                orderId.includes(searchValue) ||
                userName.includes(searchValue) ||
                status.includes(searchValue) ||
                userComment.includes(searchValue) ||
                orderTotalIncome.includes(searchValue) ||
                orderModified.includes(searchValue)
            );
        });
        console.log("Filtered rows:", filteredRows); // 除錯訊息，確認過濾後的行
        displayRows(filteredRows); // 顯示過濾後的資料
    };

    // 顯示資料行
    const displayRows = (rows) => {
        table.innerHTML = ''; // 清空表格顯示
        rows.forEach(row => table.appendChild(row));
    };

    // 重置查詢
    const resetSearch = () => {
        document.getElementById('generalSearch').value = ''; // 清空搜尋框
        displayRows(allRows); // 恢復顯示所有資料
        console.log("Search reset, displaying all rows."); // 除錯訊息
    };

    // 綁定查詢和重置按鈕的事件
    document.getElementById('searchButton').addEventListener('click', filterRows);
    document.getElementById('resetButton').addEventListener('click', resetSearch);

    // 導出 setAllRows 方法，以便從其他模組呼叫
    window.setOrderRows = setAllRows;
});
