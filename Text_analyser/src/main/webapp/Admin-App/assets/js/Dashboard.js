// Setting the profile section
document.getElementById('topusersection').addEventListener('click',function(){
    document.getElementById('topusersection').classList.add('active','bg-primary','text-light');
    document.getElementById('topsearchsection').classList.remove('active','bg-primary','text-light');
    document.getElementById('scanhistorysection').classList.remove('active','bg-primary','text-light');
    
    document.getElementById('top-users').classList.remove('d-none');
    document.getElementById('top-searches').classList.add('d-none');
    document.getElementById('all-scans').classList.add('d-none');
});

// Setting the scan section
document.getElementById('topsearchsection').addEventListener('click',function(){
    document.getElementById('topusersection').classList.remove('active','bg-primary','text-light');
    document.getElementById('topsearchsection').classList.add('active','bg-primary','text-light');
    document.getElementById('scanhistorysection').classList.remove('active','bg-primary','text-light');
    
    document.getElementById('top-users').classList.add('d-none');
    document.getElementById('top-searches').classList.remove('d-none');
    document.getElementById('all-scans').classList.add('d-none');
});

// Setting the scan section
document.getElementById('scanhistorysection').addEventListener('click',function(){
    document.getElementById('topusersection').classList.remove('active','bg-primary','text-light');
    document.getElementById('topsearchsection').classList.remove('active','bg-primary','text-light');
    document.getElementById('scanhistorysection').classList.add('active','bg-primary','text-light');
    
    document.getElementById('top-users').classList.add('d-none');
    document.getElementById('top-searches').classList.add('d-none');
    document.getElementById('all-scans').classList.remove('d-none');
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
class Users{
    Read_top_users(){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Analytics?req=1', {
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
                    content += `<tr><td>${ds.firstname} ${ds.lastname}</td><td>${ds.count}</td></tr>`;
                }
            }
            else{
                content = `<tr><td colspan="2" class="text-center">No results found !</td></tr>`;
            }
            
            document.getElementById("userlist").innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
        // End of Checking Session
    }
}
class Topics extends Users{
    Read_most_searched(){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Analytics?req=2', {
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
                    content += `<tr><td>${ds.keyword}</td><td>${ds.count}</td></tr>`;
                }
            }
            else{
                content = `<tr><td colspan="2" class="text-center">No results found !</td></tr>`;
            }
            
            document.getElementById("wordlist").innerHTML = content;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
}
class Scans extends Topics{
    Read_all_scans(){
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Analytics?req=3', {
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
                    content += `<tr><td>${ds.filename}</td><td>${ds.date}</td><td>${ds.time}</td></tr>`;
                }
            }
            else{
                content = `<tr><td colspan="2" class="text-center">No results found !</td></tr>`;
            }
            
            document.getElementById("scanlist").innerHTML = content;
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

document.addEventListener('DOMContentLoaded', function() {
    const se = new Session();
    se.CheckSession();
    
    setInterval(se.checkSession, 600000);
    
    const ss = new Scans();
    
    ss.Read_top_users();
    ss.Read_most_searched();
    ss.Read_all_scans();
});