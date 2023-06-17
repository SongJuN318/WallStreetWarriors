// settings.js

// Function to enable notifications
function enableNotifications() {
    fetch('/enableNotifications', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                console.log('Notifications enabled.');
            } else {
                console.log('Failed to enable notifications.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// Function to disable notifications
function disableNotifications() {
    fetch('/disableNotifications', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                console.log('Notifications disabled.');
            } else {
                console.log('Failed to disable notifications.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// Function to set the profit threshold
function setProfitThreshold() {
    const profitThreshold = document.getElementById('profitThreshold').value;
    fetch('/setProfitThreshold', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ profitThreshold })
    })
        .then(response => {
            if (response.ok) {
                console.log('Profit threshold set successfully.');
            } else {
                console.log('Failed to set profit threshold.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// Function to set the loss threshold
function setLossThreshold() {
    const lossThreshold = document.getElementById('lossThreshold').value;
    fetch('/setLossThreshold', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ lossThreshold })
    })
        .then(response => {
            if (response.ok) {
                console.log('Loss threshold set successfully.');
            } else {
                console.log('Failed to set loss threshold.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
