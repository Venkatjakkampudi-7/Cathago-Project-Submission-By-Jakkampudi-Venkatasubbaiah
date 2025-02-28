function launchmodal() {
    const modal = document.getElementById('The-modal');
    modal.classList.add('d-block');
    modal.classList.remove('fade');

    // Create backdrop
    let backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop fade show';
    backdrop.id = 'modal-backdrop';
    document.body.appendChild(backdrop);
}

function dismissmodal() {
    const modal = document.getElementById('The-modal');
    modal.classList.remove('d-block');
    modal.classList.add('fade');

    // Remove backdrop
    const backdrop = document.getElementById('modal-backdrop');
    if (backdrop) {
        backdrop.remove();
    }
}

class Docscanner{
    checkScannerFormValidity() 
    {
        const form = document.getElementById('scanfrm');
        const submitBtn = document.getElementById('scanbtn');
        const isValid = form.checkValidity();
        submitBtn.disabled = !isValid;
    }

    Scandocument(){
        const fileInput = document.getElementById("document"); // Get file input field

        if (fileInput.files.length === 0) {
            console.error("No file selected!");
            return;
        }

        const formData = new FormData();
        formData.append("file", fileInput.files[0]); // Append file

        // If there are other fields in the form
        const fd = new FormData(document.getElementById("scanfrm"));
        for (const [key, value] of fd.entries()) {
            formData.append(key, value); // Append other form fields
        }

        // Using fetch api to upload the scan
        fetch("http://localhost:8080/Text_Analyser/ScanUpload", {
            method: "POST",
            body: formData,
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // Parse JSON response
        })
        .then(data => {
            console.log('Success:', data);
    
            let content = ``;
    
            if("status" in data){
                content = `No matching files !<div class="text-center"><button type="button" class="btn btn-primary" onclick="Re_Scan(${data.sid})">ReMatch Document</button></div>`;
            }
            else{
                content += `<p><h5 class="mb-3">🔍 Similarity found in <span class="text-primary">${data.length} documents</span></h5></p>`;
                
                if(data.length > 0)
                {
                    content += `<div class="table">
                                <table class="table table-bordered table-sm">
                                    <thead class="table-dark">
                                        <thead class="text-center">
                                            <tr>
                                                <th>Document</th>
                                                <th>Similarity</th>
                                                <th>View</th>
                                            </tr>
                                        </thead>
                                        <tbody>`;
                    for(let ds of data)
                    {
                        content += `<tr><td><span class="ms-2">${ds.filename}</span></td><td class="text-center">`;

                        if(ds.similarity_score*100 == 100){
                            content += `<span class="exact-match">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        else if(ds.similarity_score*100 > 75){
                            content += `<span class="score-high">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        else if(ds.similarity_score*100 > 50){
                            content += `<span class="score-medium">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        else if(ds.similarity_score*100 > 0){
                            content += `<span class="score-low">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        content += `</td><td class="text-center"><button type="button" class="btn btn-sm btn-success" onclick="Show_Comparision(${ds.scanid},${ds.vsscanid})">View</button></td></tr>`;
                    }
                    content += `<tbody></table></div>`;
                }
                else{
                    content += `<div class="text-center"><button type="button" class="btn btn-primary" onclick="Re_Scan(${data.sid})">ReMatch Document</button></div>`;
                }
                
                content += `</div>`;
            }
            
            document.getElementById("report-details").innerHTML = content;
            
            document.getElementById("scanfrm").reset();
            
            const Doc = new Docscanner();
            Doc.checkScannerFormValidity();
            
            const pro = new Profile();
            pro.Read_scans();
            pro.Read_credits();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    
    Re_Scan(sid){
        const data = {"scanid":parseInt(sid)};
        
        console.log(JSON.stringify(data));
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/ScanUpload', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // Parse JSON response
        })
        .then(data => {
            console.log('Success:', data);
    
            let content = ``;
    
            if("status" in data){
                content = `No matching files !<div class="text-center"><button type="button" class="btn btn-primary" onclick="Re_Scan(${data.sid})">ReMatch Document</button></div>`;
            }
            else{
                content += `<p><h5 class="mb-3">🔍 Similarity found in <span class="text-primary">${data.length} documents</span></h5></p>`;
                
                if(data.length > 0)
                {
                    content += `<div class="table">
                                <table class="table table-bordered table-sm">
                                    <thead class="table-dark">
                                        <thead class="text-center">
                                            <tr>
                                                <th>Document</th>
                                                <th>Similarity</th>
                                                <th>View</th>
                                            </tr>
                                        </thead>
                                        <tbody>`;
                    for(let ds of data)
                    {
                        content += `<tr><td><span class="ms-2">${ds.filename}</span></td><td class="text-center">`;

                        if(ds.similarity_score*100 == 100){
                            content += `<span class="exact-match">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        else if(ds.similarity_score*100 > 75){
                            content += `<span class="score-high">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        else if(ds.similarity_score*100 > 50){
                            content += `<span class="score-medium">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        else if(ds.similarity_score*100 > 0){
                            content += `<span class="score-low">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                         content += `</td><td class="text-center"><button type="button" class="btn btn-sm btn-success" onclick="Show_Comparision(${ds.scanid},${ds.vsscanid})">View</button></td></tr>`;
                    }
                    content += `<tbody></table></div>`;
                }
                else{
                    if(cct > 0){
                        content += `<div class="text-center"><button type="button" class="btn btn-sm btn-primary" onclick="Re_Scan(${sid})">ReMatch Document</button></div>`;
                    }
                }
                
                content += `</div>`;
            }
            
            document.getElementById("report-details").innerHTML = content;
            
            const pro = new Profile();
            pro.Read_credits();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    
    LoadScan(sid){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/ScanUpload?scanid='+sid, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // Parse JSON response
        })
        .then(data => {
            console.log('Success:', data);
    
            let content = ``;
    
            if("status" in data){
                content = `No matching files !`;
                if(cct > 0){
                    content += `<div class="text-center"><button type="button" class="btn btn-sm btn-primary" onclick="Re_Scan(${data.sid})">ReMatch Document</button></div>`;
                }
            }
            else
            {
                content += `<p><h5 class="mb-3">🔍 Similarity found in <span class="text-primary">${data.length} documents</span></h5></p>`;
                
                if(data.length > 0)
                {
                    content += `<div class="table">
                                <table class="table table-bordered table-sm">
                                    <thead class="table-dark">
                                        <thead class="text-center">
                                            <tr>
                                                <th>Document</th>
                                                <th>Similarity</th>
                                                <th>View</th>
                                            </tr>
                                        </thead>
                                        <tbody>`;
                    for(let ds of data)
                    {
                        content += `<tr><td><span class="ms-2">${ds.filename}</span></td><td class="text-center">`;

                        if(ds.similarity_score*100 == 100){
                            content += `<span class="exact-match">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        else if(ds.similarity_score*100 > 75){
                            content += `<span class="score-high">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        else if(ds.similarity_score*100 > 50){
                            content += `<span class="score-medium">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                        else if(ds.similarity_score*100 > 0){
                            content += `<span class="score-low">${(ds.similarity_score*100).toFixed(2)}%</span>`;
                        }
                         content += `</td><td class="text-center"><button type="button" class="btn btn-sm btn-success" onclick="Show_Comparision(${ds.scanid},${ds.vsscanid})">View</button></td></tr>`;
                    }
                    content += `<tbody></table></div>`;
                }
                else{
                    if(cct > 0){
                        content += `<div class="text-center"><button type="button" class="btn btn-sm btn-primary" onclick="Re_Scan(${sid})">ReMatch Document</button></div>`;
                    }
                }
                
                content += `</div>`;
            }
            
            document.getElementById("report-details").innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    
    Show_Comparision(doc1,doc2){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Match_Comparisions?doc1=' + encodeURIComponent(doc1) + '&doc2=' + encodeURIComponent(doc2), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // Parse JSON response
        })
        .then(data => {
            console.log('Success:', data);
    
            document.getElementById("actualdoc").innerHTML = data[0].actualdoc;
            document.getElementById("vsdoc").innerHTML = data[0].vsdocs;
            
            launchmodal();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
}

const Ds = new Docscanner();

const scanfrm = document.getElementById('scanfrm');
scanfrm.addEventListener('input',Ds.checkScannerFormValidity);
scanfrm.addEventListener('change',Ds.checkScannerFormValidity);

document.getElementById('scanbtn').addEventListener('click',Ds.Scandocument);

function Show_Comparision(doc1,doc2){
    Ds.Show_Comparision(doc1,doc2);
}

function LoadScan(sid){
    Ds.LoadScan(sid);
}

function Re_Scan(sid){
    Ds.Re_Scan(sid);
}