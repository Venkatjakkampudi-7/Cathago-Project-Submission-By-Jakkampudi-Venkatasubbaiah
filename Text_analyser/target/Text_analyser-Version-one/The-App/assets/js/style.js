document.getElementById('collapsebtn').addEventListener('click',function(){
    
    historysec = document.getElementById('scan-history');
    scannerarea = document.getElementById('Scan-section');
    
    if(historysec.classList.contains('collapse')){
        this.innerHTML = "Close Past Scans";
        historysec.classList.remove('collapse');
        scannerarea.classList.add("col-lg-8");
    }
    else{
        this.innerHTML = "View Past Scans";
        historysec.classList.add('collapse');
        scannerarea.classList.remove("col-lg-8");
    }
})