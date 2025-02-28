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

// Setting the profile section
document.getElementById('requestsection').addEventListener('click',function(){
    document.getElementById('requestsection').classList.add('active','bg-primary','text-light');
    document.getElementById('packsection').classList.remove('active','bg-primary','text-light');
    
    document.getElementById('the-request').classList.remove('d-none');
    document.getElementById('the-packs').classList.add('d-none');
});

// Setting the scan section
document.getElementById('packsection').addEventListener('click',function(){
    document.getElementById('requestsection').classList.remove('active','bg-primary','text-light');
    document.getElementById('packsection').classList.add('active','bg-primary','text-light');
    
    document.getElementById('the-request').classList.add('d-none');
    document.getElementById('the-packs').classList.remove('d-none');
});

class Session{
    // Check Session
    CheckSession(){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Login', {
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
    
            console.log(data.Status);

            if (data.Status === 'Logged In !' && data.Privillage === "user" && data.account_status === "active")
            {
                window.location.href = "/Text_Analyser/The-App/Home.html";
            }
            else if (data.Status === 'Login Required !') 
            { 
                window.location.href = "Get-started.html";
            }
            else 
            {
                console.log("Unknown status:", data.Status);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of Checking Session
}

class Pack {
    Read_packs(){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Pack_Manager?req=1', {
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
            
            if(data.length > 0){
            
                for (let ds of data) 
                {
                    content += `<td>${ds.slabname}</td>
                        <td>
                            <input type="number" class="form-control form-control-sm text-center" min="0" id="creditlimit${ds.packid}" name="thecredit" value="${ds.creditcount}" placeholder="Enter Credit count" required>
                        </td>
                        <td>${ds.uscount}</td>
                        <td>
                            <div class="form-check form-switch d-flex justify-content-center">
                                <input class="form-check-input" type="checkbox" role="switch" id="aipower${ds.packid}" ${ds.ai_assisted === 'yes' ? 'checked' : ''}>
                            </div>
                        </td>
                        <td>
                            <div class="form-check form-switch d-flex justify-content-center">
                                <input class="form-check-input" type="checkbox" role="switch" id="packstatus${ds.packid}" ${ds.slabstatus === 'active' ? 'checked' : ''}>
                            </div>
                        </td>
                        <td class="text-success">
                            <button type="button" class="btn btn-sm btn-success" onclick="packcontrollerfour(${ds.packid},document.getElementById('creditlimit${ds.packid}').value,document.getElementById('aipower${ds.packid}'),document.getElementById('packstatus${ds.packid}'))">Modify</button>
                        </td>
                    </tr>`;
                }
            }
            else{
                content = `<tr><td colspan="5" class="text-center">No Packs found !</td></tr>`;
            }
            
            document.getElementById('packlist').innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    Fetch_packs(skey){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Pack_Manager?req=2&skey='+skey, {
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
            
            if(data.length > 0){
            
                for (let ds of data) 
                {
                    content += `<td>${ds.slabname}</td>
                        <td>
                            <input type="number" class="form-control form-control-sm text-center" min="0" id="thecredit" name="thecredit" value="${ds.creditcount}" placeholder="Enter Credit count" required>
                        </td>
                        <td>${ds.uscount}</td>
                        <td>
                            <div class="form-check form-switch d-flex justify-content-center">
                                <input class="form-check-input" type="checkbox" role="switch" id="aipower${ds.packid}" ${ds.ai_assisted === 'yes' ? 'checked' : ''}>
                            </div>
                        </td>
                        <td>
                            <div class="form-check form-switch d-flex justify-content-center">
                                <input class="form-check-input" type="checkbox" role="switch" id="packstatus${ds.packid}" ${ds.slabstatus === 'active' ? 'checked' : ''}>
                            </div>
                        </td>
                        <td class="text-success">
                            <button type="button" class="btn btn-sm btn-success" onclick="packcontrollerfour(${ds.packid},document.getElementById('creditlimit${ds.packid}').value,document.getElementById('aipower${ds.packid}'),document.getElementById('packstatus${ds.packid}'))">Modify</button>
                        </td>
                    </tr>`;
                }
            }
            else{
                content = `<tr><td colspan="5" class="text-center">No Packs found !</td></tr>`;
            }
            
            document.getElementById('packlist').innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    Create_pack(){
        // Create a FormData object to retrieve form inputs
        const formData = new FormData(document.getElementById('packfrm'));
        const data = {};
        
        // Convert FormData to JSON object
        formData.forEach((value, key) => {
            data[key] = value;
        });
        
        console.log(JSON.stringify(data));
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Pack_Manager', {
            method: 'POST',
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
        .then(function() {
            const cres = new Credit();
            cres.Read_packs();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    Update_pack(packid,climit,aipower,status){
        const data = {"packid":parseInt(packid),"creditlimit":parseInt(climit),"aipower":aipower,"packstatus":status};
        
        console.log(JSON.stringify(data));
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Pack_Manager', {
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
            alert(data.Status);
    
            const cres = new Credit();
            cres.Read_packs();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
}
class Credit extends Pack {
    Read_credit_requests(){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Credit_Manager?req=1', {
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
            
            if(data.length > 0){
            
                for (let ds of data) 
                {
                    content += `<tr>
                            <td>${ds.firstname} ${ds.lastname}</td>
                            <td>${ds.usage}</td>
                            <td>
                                <input type="number" class="form-control form-control-sm text-center" min="1" id="creditcount${ds.requestid}" name="creditcount" value="5" placeholder="Set credits" required>
                            </td>
                            <td>
                                <div class="d-flex justify-content-center">
                                    <button type="button" class="btn btn-sm btn-success ms-2 me-2" onclick="cccontrollerfour(${ds.userid},${ds.requestid},document.getElementById('creditcount${ds.requestid}').value,'granted')">Grant</button>
                                    <button type="button" class="btn btn-sm btn-danger ms-2 me-2" onclick="cccontrollerfour(${ds.userid},${ds.requestid},document.getElementById('creditcount${ds.requestid}').value,'rejected')">Reject</button>
                                </div>
                            </td>
                        </tr>`;
                }
            }
            else{
                content = `<tr><td colspan="4" class="text-center">No Requests found !</td></tr>`;
            }
            
            document.getElementById('creditrequests').innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    Fetch_credit_requests(skey){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Credit_Manager?req=2&skey='+skey, {
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
            
            if(data.length > 0){
            
                for (let ds of data) 
                {
                    content += `<tr>
                            <td>${ds.firstname} ${ds.lastname}</td>
                            <td>${ds.usage}</td>
                            <td>
                                <input type="number" class="form-control form-control-sm text-center" min="1" id="creditcount${ds.requestid}" name="creditcount" value="5" placeholder="Set credits" required>
                            </td>
                            <td>
                                <div class="d-flex justify-content-center">
                                    <button type="button" class="btn btn-sm btn-success ms-2 me-2" onclick="cccontrollerfour(${ds.userid},${ds.requestid},document.getElementById('creditcount${ds.requestid}').value,'granted')">Grant</button>
                                    <button type="button" class="btn btn-sm btn-danger ms-2 me-2" onclick="cccontrollerfour(${ds.userid},${ds.requestid},document.getElementById('creditcount${ds.requestid}').value,'rejected')">Reject</button>
                                </div>
                            </td>
                        </tr>`;
                }
            }
            else{
                content = `<tr><td colspan="4" class="text-center">No Requests found !</td></tr>`;
            }
            
            document.getElementById('creditrequests').innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of fetching the credit requests
    
    // Processing the admin decision
    Process_credit_request(usid,reqid,ccount,status){
        const data = {"userid":parseInt(usid),"requestid":parseInt(reqid),"creditcount":parseInt(ccount),"reqstatus":status};
        
        console.log(JSON.stringify(data));
        
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Credit_Manager', {
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
            alert(data.Status);
    
            const cres = new Credit();
            cres.Read_credit_requests();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of Processing the credit request
}

function cccontrollerone(type)
{
    const cre = new Credit();
    switch(type){
        // Cases for the packs
        case 'readpacks':
            cre.Read_packs();
            break;
        case 'createpack':
            cre.Create_pack();
            dismissmodal();
            break;
        // End of the packs
        // Cases for the Credits
        case 'readcres':
            cre.Read_credit_requests();
            break;
        // End of the credit
        default:
            console.log("Invalid request !");
            break;
    }
}

function cccontrollertwo(type,skey)
{
    const cre = new Credit();
    switch(type){
        // Cases for the packs
        case 'fetchpacks':
            cre.Fetch_packs(skey);
            break;
        // End of the packs
        // Cases for the Credits
        case 'fetchcres':
            cre.Fetch_credit_requests(skey);
            break; 
        // End of the credit
        default:
            console.log("Invalid request !");
            break;
    }
}

function cccontrollerfour(usid,reqid,ccount,status)
{
    const cre = new Credit();
    cre.Process_credit_request(usid,reqid,ccount,status);
}

function packcontrollerfour(packid,climit,aipow,ele){
    let aipower = "no";
    if(aipow.checked){
        aipower = "yes";
    }
    
    let status = "deactive";
    if(ele.checked){
        status = "active";
    }
    
    const cre = new Credit();
    cre.Update_pack(packid,climit,aipower,status);
}

// Credit search
const skeyreq = document.getElementById('skeyreq');
skeyreq.addEventListener('input', function() {
    cccontrollertwo('fetchcres', skeyreq.value);
});

skeyreq.addEventListener('change', () => {
    cccontrollertwo('fetchcres', skeyreq.value);
});

function clearrequestsearch(){
    document.getElementById("skeyreq").value = "";
    cccontrollerone("readcres");
}
// End of credit search

const skeypack = document.getElementById('skeypack');
skeypack.addEventListener('input', function() {
    cccontrollertwo('fetchpacks', skeypack.value);
});

skeypack.addEventListener('change', () => {
    cccontrollertwo('fetchpacks', skeypack.value);
});

function clearpacksearch(){
    document.getElementById("skeypack").value = "";
    cccontrollerone("readpacks");
}

function signout(){
    fetch('http://localhost:8080/Text_Analyser/Signout', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json(); 
    })
    .then(() => { 
        const se = new Session();
        se.CheckSession();
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const se = new Session();
    se.CheckSession();
    
    setInterval(se.checkSession, 600000);
    
    const cre = new Credit();
    cre.Read_packs();
    cre.Read_credit_requests();
});