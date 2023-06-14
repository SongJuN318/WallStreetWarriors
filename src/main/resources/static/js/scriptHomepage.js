document.addEventListener("DOMContentLoaded", function () {
    let subMenu = document.getElementById("subMenu");
    let profileName = document.getElementById("profileName").addEventListener("click", toggleMenu);

    let search = document.getElementById("search");
    search.addEventListener("click", function () {
        window.location.href = "/search?symbol";
    });


    let leaderboard = document.getElementById("leaderboard");
    leaderboard.addEventListener("click", function () {
        window.location.href = "/leaderboard";
    });

    let report = document.getElementById("report");
    report.addEventListener("click", function () {
        window.location.href = "https://icons8.com/icons/set/stocks";
    })

    let sell = document.getElementById("sell");
    sell.addEventListener("click", function () {
        window.location.href = ("https://icons8.com/icons/set/stocks");
    })


    function toggleMenu() {
        subMenu.classList.toggle("open-menu");
    }
})