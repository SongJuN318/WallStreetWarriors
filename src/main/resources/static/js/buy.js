document.addEventListener('DOMContentLoaded', function () {
    var button = document.getElementById('btn');
    button.addEventListener('click', executeBuyOrder);
});

function executeBuyOrder(event) {
    event.preventDefault();
    var lots = document.getElementById('lots').value;
    var buyPrice = document.getElementById('buyPrice').value;

    var buyPendingOrderDTO = {
        "lots": lots,
        "buyPrice": buyPrice
    };

    var symbol = window.location.pathname.split("/").pop(); // Extract the symbol from the URL

    fetch("/buy/" + symbol, {
        method: "POST",
        body: JSON.stringify(buyPendingOrderDTO),
        headers: {
            "Content-Type": "application/json"
        }
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showPopup(lots, buyPrice);
                } else {
                    alert("Buy order execution failed.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
}

function showPopup(lots, price) {
    alert("You have successfully bought " + lots + " lots at a price of " + price);
}