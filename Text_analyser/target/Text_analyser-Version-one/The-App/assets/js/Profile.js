let cct = 0;
class Session{
    // Check Session
    CheckSession(){
        
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
            return response.json();
        })
        .then(data => {
    
            console.log(data.Status);

            if (data.Status === 'Logged In !' && data.Privillage === "admin" && data.account_status === "active")
            {
                window.location.href = "/Text_Analyser/Admin-App/Dashboard.html";
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
class Credits extends Session{
    Read_credits(){
        
        fetch('http://localhost:8080/Text_Analyser/Profile?req=3', {
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
        .then(data => {
            const creditcount = data.credits;
            cct = creditcount;
            document.getElementById('creditcounter').innerHTML = `${creditcount}`;
            
            if(creditcount === 0){
                document.getElementById('scanfrm').classList.add('d-none');
                if(!data.reqstatus){
                    document.getElementById('credit-request').classList.remove('d-none');
                }
                else{
                    document.getElementById('crs').innerHTML = `Waiting for Admin to process your request !`;
                    document.getElementById('crs').classList.remove('d-none');
                }
            }
            else{
                document.getElementById('scanfrm').classList.remove('d-none');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    Request_credits(){
        const data = {"userid":2};
        
        fetch('http://localhost:8080/Text_Analyser/Credit_request', {
            method: 'POST',
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
        .then(data => {
            document.getElementById('crs').innerHTML = `${data.Status}`;
            document.getElementById('crs').classList.remove('d-none');
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
}

class Profile extends Credits {
    Read_profile(){
        
        fetch('http://localhost:8080/Text_Analyser/Profile?req=1', {
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
        .then(data => {
            if(data.accstatus === "active"){
                document.getElementById('greet').innerHTML = `Hi, ${data.firstname ?? 'Unknown'} ${data.lastname ?? 'Unknown'}`;
                document.getElementById('profile-content').innerHTML = `
                    <p><span class="fw-bold">Hi, ${data.firstname ?? 'Unknown'} ${data.lastname ?? 'Unknown'}</span></p>
                    <p>Username - <span class="text-primary">${data.username ?? 'Unknown'}</span></p>
                    <p>Emailid - <span class="text-primary">${data.emailid ?? 'Unknown'}</span></p>`;
            }
            else{
                window.location.href = "Get-started.html";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    
    Read_scans(){
        
        fetch('http://localhost:8080/Text_Analyser/Profile?req=2', {
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
        .then(data => {
            const tbody = document.getElementById('scan-list');
            
            let pastscans = 0;
            let scanstatement = ``;
            let finalstatement = ``;
            
            for (let ds of data) 
            {
                pastscans++;
                scanstatement += `<tr>
                        <td class="w-auto" style="white-space:no-wrap;width:max-content;"><span style="white-space: nowrap;width:max-content;">${ds.date}</span></td>
                        <td>${ds.filename}</td>
                        <td>
                            <button type="button" class="btn btn-sm btn-primary pt-0 pb-0" 
                                onclick="LoadScan(${ds.sid})">View</button>
                        </td>
                    </tr>
                `;
                
                scanstatement += `</td></tr>`;
            }
            
            if(pastscans == 0){
                finalstatement += `<span class="text-center fs-6 fw-semibold">No Scans until Now !</span>`;
            }
            else if(pastscans == 1){
                finalstatement += `<span class="text-center fs-6 fw-semibold">1 Scan until Now !</span>`;
            }
            else if(pastscans > 1){
                finalstatement += `<span class="text-center fs-6 fw-semibold">${pastscans} Scans until Now !</span>`;
            }
            
            document.getElementById("scan-list").innerHTML = scanstatement;
            document.getElementById("scantotal").innerHTML = finalstatement;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
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

document.getElementById("profileBtn").addEventListener("click", function () {
    let offcanvas = document.getElementById("offcanvasScrolling");
    offcanvas.classList.add("show"); 
    offcanvas.style.transform = "translateX(0)"; 
});

document.getElementById("closeCanvas").addEventListener("click", function () {
    let offcanvas = document.getElementById("offcanvasScrolling");
    offcanvas.style.transform = "translateX(100%)"; 
    setTimeout(() => {
        offcanvas.classList.remove("show"); 
    }, 300);
});

document.addEventListener("click", function (event) {
    let dropdown = document.getElementById("profile-dropdown");
    let button = document.getElementById("showprofile");

    // Checks if the clicked element is inside the dropdown or the button
    if (!dropdown.contains(event.target) && event.target !== button) {
        dropdown.classList.remove("d-block");
    }
});

// Toggle dropdown when clicking the button
document.getElementById("showprofile").addEventListener("click", function (event) {
    let dropdown = document.getElementById("profile-dropdown");
    dropdown.classList.toggle("d-block");
    event.stopPropagation();
});

document.getElementById('crequestbtn').addEventListener('click',function(){
    this.classList.add('d-none');
    const pr = new Profile();
    pr.Request_credits();
});

document.addEventListener('DOMContentLoaded', function() {
    
    const pr = new Profile();
    pr.CheckSession();
    pr.Read_scans();
    pr.Read_credits();
    pr.Read_profile();
    
    setInterval(pr.checkSession, 600000);
});