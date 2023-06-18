// Enable/disable notifications
const notificationSwitch = document.getElementById('notificationSwitch');
notificationSwitch.addEventListener('change', function() {
    if (this.checked) {
        enableNotifications();
    } else {
        disableNotifications();
    }
});

// Set profit threshold
const setProfitBtn = document.getElementById('setProfitBtn');
setProfitBtn.addEventListener('click', function() {
    const profitThreshold = document.getElementById('profitThreshold').value;
    setProfitThreshold(profitThreshold);
});

// Set loss threshold
const setLossBtn = document.getElementById('setLossBtn');
setLossBtn.addEventListener('click', function() {
    const lossThreshold = document.getElementById('lossThreshold').value;
    setLossThreshold(lossThreshold);
});
