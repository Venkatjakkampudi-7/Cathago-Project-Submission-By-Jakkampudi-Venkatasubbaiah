document.getElementById('Reportbtn').addEventListener('click', function() {
    fetch('http://localhost:8080/Text_Analyser/User_Report')
    .then(response => response.blob())
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'Scan History.txt'; // File name
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url); // Clean up
        document.body.removeChild(a);
    })
    .catch(error => {
        console.error('Error:', error);
    });
});