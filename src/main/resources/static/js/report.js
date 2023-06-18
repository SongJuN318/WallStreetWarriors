document.addEventListener("DOMContentLoaded", function () {
        var PDF = document.getElementById('PDF');
        var TXT = document.getElementById('TXT');
        var CSV = document.getElementById('CSV');
        
        PDF.addEventListener('click', function() {
            window.location.href = '/report/pdf';
        });

        TXT.addEventListener('click', function() {
            window.location.href = '/report/txt';
        });
        
        CSV.addEventListener('click', function() {
        
            window.location.href = '/report/csv';
        });
        
        });
        