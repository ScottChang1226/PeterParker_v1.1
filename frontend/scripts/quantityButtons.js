/*----------------------------------------------------*/
/*  Quantity Buttons with Total Value Counter
/*
/*  Author: Vasterad
/*  Version: 1.0
/*----------------------------------------------------*/

// 總計數量計算函數
function qtySum() {
   var arr = document.getElementsByName('qtyInput');
   var tot = 0;

   for (var i = 0; i < arr.length; i++) {
       if (parseInt(arr[i].value)) {
           tot += parseInt(arr[i].value);
       }
   }

   var cardQty = document.querySelector(".qtyTotal");
   if (cardQty) {
       cardQty.innerHTML = tot;
   }
}

document.addEventListener('DOMContentLoaded', function() {
   qtySum();

   // 增加數量加減按鈕
   const qtyButtons = document.querySelectorAll(".qtyButtons input");

   qtyButtons.forEach(input => {
       input.insertAdjacentHTML('afterend', '<div class="qtyInc"></div>');
       input.insertAdjacentHTML('beforebegin', '<div class="qtyDec"></div>');
   });

   // 監聽加減按鈕事件
   document.querySelectorAll(".qtyDec, .qtyInc").forEach(button => {
       button.addEventListener("click", function() {
           var $button = button;
           var input = $button.parentElement.querySelector("input");
           var oldValue = parseFloat(input.value);

           var newVal;
           if ($button.classList.contains('qtyInc')) {
               newVal = oldValue + 1;
           } else {
               newVal = oldValue > 0 ? oldValue - 1 : 0;
           }

           input.value = newVal;
           qtySum();

           var qtyTotal = document.querySelector(".qtyTotal");
           if (qtyTotal) {
               qtyTotal.classList.add("rotate-x");
           }
       });
   });

   // 總計數量動畫結束時移除動畫類
   const counter = document.querySelector(".qtyTotal");
   if (counter) {
       counter.addEventListener("animationend", function() {
           counter.classList.remove("rotate-x");
       });
   }
});

// 調整 Panel Dropdown 寬度
window.addEventListener('load', adjustPanelDropdownWidth);
window.addEventListener('resize', adjustPanelDropdownWidth);

function adjustPanelDropdownWidth() {
   var panelTrigger = document.querySelector('.booking-widget .panel-dropdown a');
   var panelContent = document.querySelector('.booking-widget .panel-dropdown .panel-dropdown-content');

   if (panelTrigger && panelContent) {
       panelContent.style.width = `${panelTrigger.offsetWidth}px`;
   }
}
