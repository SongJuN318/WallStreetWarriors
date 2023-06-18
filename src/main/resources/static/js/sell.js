function calculateTotalPrice() {
    var lots = document.getElementById("lots").value * 100;
    var sellPrice = document.getElementById("sellPrice").value;
    var totalPrice = lots * sellPrice;
    document.getElementById("totalPrice").value = totalPrice;
}

document.addEventListener('DOMContentLoaded', function () {
    let urlParams = new URLSearchParams(window.location.search);
    let successParam = urlParams.get('success');

    if (successParam === 'true') {
        showPopup('Buy order executed successfully.');
    } else if (successParam === 'false') {
        showPopup('Buy order execution failed.');
    }

    calculateTotalPrice();

    function showPopup(message) {
        alert(message);
    }
});
