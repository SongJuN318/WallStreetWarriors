document.addEventListener('DOMContentLoaded', function () {
    let background = document.getElementById('background');
    let moon = document.getElementById('moon');
    let mountains = document.getElementById('mountains');
    let text = document.getElementById('text');
    let btn = document.getElementById('btn');
    let mountains2 = document.getElementById('mountains2');
    let header = document.querySelector('header');
});


window.addEventListener('scroll', function () {
    let value = window.scrollY;
    background.style.top = value * 0 + 'px';
    icon.style.marginLeft = value * 2 + 'px'
    text.style.marginRight = value * 4.5 + 'px';
    btn.style.marginTop = value * 0.9 + 'px';
    header.style.top = value * 0.5 + 'px';
})
