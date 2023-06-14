document.addEventListener('DOMContentLoaded', function () {
    let urlParams = new URLSearchParams(window.location.search);
    let successParam = urlParams.get('success');

    if (successParam === 'true') {
        showPopup('Buy order executed successfully.');
    } else if (successParam === 'false') {
        showPopup('Buy order execution failed.');
    }

    function showPopup(message) {
        alert(message);
    }
});
