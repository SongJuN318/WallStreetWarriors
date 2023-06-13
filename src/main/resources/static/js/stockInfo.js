document.addEventListener("DOMContentLoaded", function () {
    let buyButton = document.getElementById("buy-button");
    buyButton.addEventListener("click", function () {
        let stockCode = /*[[${stockCode}]]*/ '';
        window.location.href = '/buy/' + stockCode;
    });
});
