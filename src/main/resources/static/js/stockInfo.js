document.addEventListener("DOMContentLoaded", function () {
    let buyButton = document.getElementsByClassName("buy-button");
    buyButton.addEventListener("click", function () {
        window.location.href = "/buy/{symbol}(symbol=${stock.symbol})";
    });
})
