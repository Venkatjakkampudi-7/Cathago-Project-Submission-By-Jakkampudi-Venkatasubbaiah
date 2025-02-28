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
document.getElementById('prosection').addEventListener('click',function(){
    document.getElementById('prosection').classList.add('active','bg-primary','text-light');
    document.getElementById('scansection').classList.remove('active','bg-primary','text-light');
    document.getElementById('creditsection').classList.remove('active','bg-primary','text-light');
    
    document.getElementById('the-profile').classList.remove('d-none');
    document.getElementById('the-Scans').classList.add('d-none');
    document.getElementById('the-Credits').classList.add('d-none');
});

// Setting the scan section
document.getElementById('scansection').addEventListener('click',function(){
    document.getElementById('prosection').classList.remove('active','bg-primary','text-light');
    document.getElementById('scansection').classList.add('active','bg-primary','text-light');
    document.getElementById('creditsection').classList.remove('active','bg-primary','text-light');
    
    document.getElementById('the-profile').classList.add('d-none');
    document.getElementById('the-Scans').classList.remove('d-none');
    document.getElementById('the-Credits').classList.add('d-none');
});

// Setting the credit section
document.getElementById('creditsection').addEventListener('click',function(){
    document.getElementById('prosection').classList.remove('active','bg-primary','text-light');
    document.getElementById('scansection').classList.remove('active','bg-primary','text-light');
    document.getElementById('creditsection').classList.add('active','bg-primary','text-light');
    
    document.getElementById('the-profile').classList.add('d-none');
    document.getElementById('the-Scans').classList.add('d-none');
    document.getElementById('the-Credits').classList.remove('d-none');
});


let packs = [];
let curruserid = 0;

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

class Packhandler{
    constructor(){
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
            packs = data;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
}

class Profile{
    // Function for reading the profile
    Read_Profile(uid){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/User_Manager?req=3&userid='+uid, {
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
            let content = `
                <h5 class="badge mb-1 text-start">
                    <span class="badge text-bg-danger fs-6">Details</span>
                </h5>
                <div class="col-xl-4 col-lg-6 col-sm-12">
                    <span class="fw-semibold">Name - </span>${data.firstname} ${data.lastname}
                </div>
                <div class="col-xl-4 col-lg-6 col-sm-12">
                    <span class="fw-semibold">Email id - </span>${data.emailid}
                </div>
                <div class="col-xl-4 col-lg-6 col-sm-12">
                    <span class="fw-semibold">User since - </span>${data.joined}
                </div>
                <h5 class="badge mb-1 text-start">
                    <span class="badge text-bg-danger fs-6">Subscription</span>
                </h5>
                <div class="col-xl-4 col-lg-6 col-sm-12">
                    <span class="fw-semibold">Subscription - </span>${data.packname}
                </div>
                <div class="col-xl-4 col-lg-6 col-sm-12">
                    <span class="fw-semibold">Credit limit - </span>${data.creditlimit}
                </div>
                <div class="col-xl-4 col-lg-6 col-sm-12">
                    <span class="fw-semibold">Credits used - </span>${data.scancount}
                </div>`;
            
            document.getElementById('the-profile').innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of the function for reading the profile
    
    // Function for reading the previous scans
    Read_Scans(uid){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/User_Manager?req=4&userid='+uid, {
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
            
            let content = ``;
            
            if(data.length > 0){
                for(let ds of data){
                    content += `<tr>
                            <td>${ds.filename}</td>
                            <td>${ds.date}</td>
                            <td>${ds.time}</td>
                        </tr>`;
                }
            }
            else{
                content += `<tr><td colspan="3" class="text-center">No Scans until now !</td></tr>`;
            }
            
            document.getElementById('the-scanlist').innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of the function for reading the previous scans
    
    // Function for reading the previous requests
    Read_Requests(uid){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/User_Manager?req=5&userid='+uid, {
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
            let content = ``;
            
            if(data.length > 0){
                for(let ds of data){
                    content += `<tr>
                            <td>${ds.date}</td>
                            <td>${ds.time}</td>
                            <td>${ds.credits}</td>
                            <td>${ds.status}</td>
                        </tr>`;
                }
            }
            else{
                content += `<tr><td colspan="4" class="text-center">No Requests authorised until now !</td></tr>`;
            }
            
            document.getElementById('the-requestlist').innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of the function for reading the requests
}

class Users extends Profile {
    
    // Function for reading users
    Read_users(){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/User_Manager?req=1', {
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
                    const firstname = ds.firstname;
                    const lastname = ds.lastname;
                    const creditsleft = ds.creditsleft;
                    const userid = ds.userid;
                    const accountstatus = ds.accountstatus;
                    
                    content += `<tr>
                        <td>${firstname} ${lastname}</td>
                        <td>
                            ${creditsleft}
                        </td>
                        <td>
                            <input type="number" class="form-control form-control-sm text-center" id="ccount${userid}" name="ccount" value="0" placeholder="Set Credit count" required>
                        </td>
                        <td>`;
                    if(packs.length > 1){
                        content += `<select class="form-select form-select-sm text-center" id="pack${ds.userid}">`;
                        for(let ps of packs){

                            let pkid = ps.packid;
                            let pkname = ps.slabname;
                            let pstatus = ps.slabstatus;

                            if(pkid == ds.slabid){
                                content += `<option value="${pkid}" selected>${pkname}</option>`;
                            }
                            else{
                                content += `<option value="${pkid}" ${pstatus === 'deactive' ? 'disabled' : ''}>${pkname}</option>`;
                            }
                        }
                        content += `</select>`;
                    }
                    else{
                        console.log(packs);
                        content += `${packs[0].slabname}`;
                    }
                    content += `</td>
                        <td>
                            <select class="form-select form-select-sm text-center" id="account${userid}">
                                <option value="active" ${accountstatus  === 'active' ? 'selected' : ''}>Active</option>
                                <option value="blocked" ${accountstatus  === 'blocked' ? 'selected' : ''}>Block</option>
                            </select>
                        </td>
                        <td>
                            <button type="button" class="btn btn-sm btn-success" onclick="updateprofile(${userid},document.getElementById('ccount${userid}').value,document.getElementById('pack${userid}').value,document.getElementById('account${userid}').value)">Update</button>
                        </td>
                        <td>
                            <button type="button" class="btn btn-sm btn-primary" onclick="showprofile(${userid})">View</button>
                        </td>
                    </tr>`;
                }
            }
            else{
                content = `<tr><td colspan="5" class="text-center">No Packs found !</td></tr>`;
            }
            
            document.getElementById('user-list').innerHTML = content;
            
            document.getElementById('totalcount').innerHTML = data.length+" results";
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of the function for reading the users
    
    // Function for fetching users
    Fetch_users(skey){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/User_Manager?req=2&skey='+skey, {
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
                    const firstname = ds.firstname;
                    const lastname = ds.lastname;
                    const creditsleft = ds.creditsleft;
                    const userid = ds.userid;
                    const accountstatus = ds.accountstatus;
                    
                    content += `<tr>
                        <td>${firstname} ${lastname}</td>
                        <td>
                            ${creditsleft}
                        </td>
                        <td>
                            <input type="number" class="form-control form-control-sm text-center" id="ccount${userid}" name="ccount" value="0" placeholder="Set Credit count" required>
                        </td>
                        <td>`;
                    if(packs.length > 1){
                        content += `<select class="form-select form-select-sm text-center" id="pack${ds.userid}">`;
                        for(let ps of packs){

                            let pkid = ps.packid;
                            let pkname = ps.slabname;
                            let pstatus = ps.slabstatus;

                            if(pkid == ds.slabid){
                                content += `<option value="${pkid}" selected>${pkname}</option>`;
                            }
                            else{
                                content += `<option value="${pkid}" ${pstatus === 'deactive' ? 'disabled' : ''}>${pkname}</option>`;
                            }
                        }
                        content += `</select>`;
                    }
                    else{
                        console.log(packs);
                        content += `${packs[0].slabname}`;
                    }
                    content += `</td>
                        <td>
                            <select class="form-select form-select-sm text-center" id="account${userid}">
                                <option value="active" ${accountstatus  === 'active' ? 'selected' : ''}>Active</option>
                                <option value="blocked" ${accountstatus  === 'blocked' ? 'selected' : ''}>Block</option>
                            </select>
                        </td>
                        <td>
                            <button type="button" class="btn btn-sm btn-success" onclick="updateprofile(${userid},document.getElementById('ccount${userid}').value,document.getElementById('pack${userid}').value,document.getElementById('account${userid}').value)">Update</button>
                        </td>
                        <td>
                            <button type="button" class="btn btn-sm btn-primary" onclick="showprofile(${userid})">View</button>
                        </td>
                    </tr>`;
                }
            }
            else{
                content = `<tr><td colspan="5" class="text-center">No Packs found !</td></tr>`;
            }
            
            document.getElementById('user-list').innerHTML = content;
            
            document.getElementById('totalcount').innerHTML = data.length+" results";
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of the function for fetching the user
    
    // Function for updating the users
    Update_User(uid,ccount,sid,status){
        const data = {"userid":uid,"creditcount":parseInt(ccount),"slabid":parseInt(sid),"accstatus":status};
        console.log(JSON.stringify(data));
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/User_Manager', {
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
        .then(function(){
            const Us = new Users();
            Us.Read_users();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of the function for updating the user
    
    // Load user
    Load_user(uid){
        const us = new Users();
        us.Read_profile(uid);
        us.Read_scans(uid);
        us.Read_Requests(uid);
        launchmodal();
    }
    // End of the function for loading the profile
}

function Read_users(){
    Us = new Users();
    Us.Read_users();
}

function Fetch_users(skey){
    const us = new Users();
    us.Fetch_users(skey);
}

const skey = document.getElementById('skey');
skey.addEventListener('input', function() {
    Fetch_users(skey.value);
});

skey.addEventListener('change', () => {
    Fetch_users(skey.value);
});

function clearsearch(){
    document.getElementById("skey").value = "";
    Read_users();
}

function showprofile(uid){
    
    if(uid != curruserid){
        curruserid = uid;
        const us = new Users();
        us.Read_Profile(uid);
        us.Read_Scans(uid);
        us.Read_Requests(uid);
    }
    
    launchmodal();
}

function updateprofile(uid,ccount,sid,status){
    const uss = new Users();
    uss.Update_User(uid,ccount,sid,status);
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
    
    phr = new Packhandler();
    
    Us = new Users();
    Us.Read_users();
});