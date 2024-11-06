// search-user.js

document.addEventListener('DOMContentLoaded', function () {
    const table = document.querySelector('.data-table tbody');  // 選擇表格主體
    let allRows = [];  // 儲存所有資料的副本，用於查詢功能

    const setupSearch = () => {
        const filterRows = () => {
            const searchValue = document.getElementById('generalSearch').value.toLowerCase();
            const filteredRows = allRows.filter(row => {
                const id = row.querySelector('td:nth-child(1)').textContent.toLowerCase();
                const name = row.querySelector('td:nth-child(2)').textContent.toLowerCase();
                const account = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
                const carNumber = row.querySelector('td:nth-child(6)').textContent.toLowerCase();
                return id.includes(searchValue) || name.includes(searchValue) || account.includes(searchValue) || carNumber.includes(searchValue);
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
    window.setUserRows = setAllRows;
});
