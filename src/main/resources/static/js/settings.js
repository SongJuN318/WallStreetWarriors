// Retrieve settings from localStorage if available
const savedSettings = JSON.parse(localStorage.getItem("userSettings"));

// Set initial values from saved settings or default values
const profitThresholdInput = document.getElementById("profitThreshold");
const lossThresholdInput = document.getElementById("lossThreshold");
const notificationSwitch = document.getElementById("notificationSwitch");

if (savedSettings) {
  profitThresholdInput.value = savedSettings.profitThreshold;
  lossThresholdInput.value = savedSettings.lossThreshold;
  notificationSwitch.checked = savedSettings.notificationsEnabled;
} else {
  notificationSwitch.checked = true; // Set the checkbox initially checked
}

// Enable/disable notifications
notificationSwitch.addEventListener("change", function () {
  if (this.checked) {
    enableNotifications();
  } else {
    disableNotifications();
  }
  saveSettings();
});

// Set profit threshold
const setProfitBtn = document.getElementById("setProfitBtn");
setProfitBtn.addEventListener("click", function () {
  const profitThreshold = profitThresholdInput.value;
  setProfitThreshold(profitThreshold);
  saveSettings();
});

// Set loss threshold
const setLossBtn = document.getElementById("setLossBtn");
setLossBtn.addEventListener("click", function () {
  const lossThreshold = lossThresholdInput.value;
  setLossThreshold(lossThreshold);
  saveSettings();
});

// Save settings to localStorage
function saveSettings() {
  const userSettings = {
    profitThreshold: parseFloat(profitThresholdInput.value),
    lossThreshold: parseFloat(lossThresholdInput.value),
    notificationsEnabled: notificationSwitch.checked,
  };
  localStorage.setItem("userSettings", JSON.stringify(userSettings));
}
