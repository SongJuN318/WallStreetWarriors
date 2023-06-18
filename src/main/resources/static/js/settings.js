document.addEventListener("DOMContentLoaded", function () {
var myButton = document.getElementById('btn1');
var myButton2 = document.getElementById('btn2');

myButton.addEventListener('click', function() {
    alert("You have successfully set your profit threshold!")
});

myButton2.addEventListener('click', function() {
    alert("You have successfully set your loss threshold!")
});

});

