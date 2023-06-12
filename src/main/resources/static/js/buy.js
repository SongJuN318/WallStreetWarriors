document.addEventListener('DOMContentLoaded', function () {
    let button =document.getElementById('btn');
    button.addEventListener('click', showPopup)

    function showPopup(){
        alert("You have successfully bought the stock!");
    }
});