// search-order.js

document.addEventListener('DOMContentLoaded', function () {
    const table = document.querySelector('.data-table tbody');  // 選擇表格主體
    let allRows = [];  // 儲存所有資料的副本，用於查詢功能

    const setupSearch = () => {
        const filterRows = () => {
            const searchValue = document.getElementById('generalSearch').value.toLowerCase();
            const filteredRows = allRows.filter(row => {
                const orderId = row.querySelector('td:nth-child(1)').textContent.toLowerCase();
                const userName = row.querySelector('td:nth-child(2)').textContent.toLowerCase();
                const spaceId = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
                const status = row.querySelector('td:nth-child(4)').textContent.toLowerCase();
                const time = row.querySelector('td:nth-child(5)').textContent.toLowerCase();

                // 檢查查詢值是否出現在任何目標欄位中
                return (
                    orderId.includes(searchValue) ||
                    userName.includes(searchValue) ||
                    spaceId.includes(searchValue) ||
                    status.includes(searchValue) ||
                    time.includes(searchValue)
                );
            });
            displayRows(filteredRows);
        };

        const resetSearch = () => {
            document.getElementById('generalSearch').value = ''; // 清空搜尋框
            displayRows(allRows);  // 恢復所有資料
        };

        // 綁定查詢和重置按鈕的事件
        document.getElementById('searchButton').addEventListener('click', filterRows);
        document.getElementById('resetButton').addEventListener('click', resetSearch);
    };

    const displayRows = (rows) => {
        table.innerHTML = '';  // 清空表格顯示
        rows.forEach(row => table.appendChild(row));
    };

    // 輸出設定方法以便從其他模組呼叫
    const setAllRows = (rows) => {
        allRows = rows;
    };

    // 初始化查詢功能
    setupSearch();

    // 導出方法
    window.setOrderRows = setAllRows;
});
