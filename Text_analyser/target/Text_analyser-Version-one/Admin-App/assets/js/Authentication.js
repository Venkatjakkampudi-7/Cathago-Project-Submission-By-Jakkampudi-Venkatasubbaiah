let captchaval = "";
let captchaflag = false;

// Authentication Class
class Authentication{
    
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

            if (data.Status === 'Logged In !' && data.Privillage === "admin" && data.account_status === "active")
            {
                window.location.href = "Dashboard.html";
            }
            else if (data.Status === 'Logged In !' && data.Privillage === "user" && data.account_status === "active")
            {
                window.location.href = "/Text_Analyser/The-App/Home.html";
            }
            else if (data.Status === 'Login Required !') 
            { 
                console.log("User not logged in or session expired.");
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
    
    // Validation script
    checkSigninformValidity() 
    {
        const form = document.getElementById('signinfrm');
        const submitBtn = document.getElementById('signinbtn');
        const isValid = form.checkValidity();
        let condition = false;
        if(isValid && captchaflag)
        {
            condition = true;
        }
        else
        {
            condition = false;
        }
        submitBtn.disabled = !condition;
        console.log(!condition);
    }
    // End of the validation scripts
    
    // Sign In Function : For User Signin
    Signin()
    {
        // Create a FormData object to retrieve form inputs
        const formData = new FormData(document.getElementById('signinfrm'));
        const data = {};
    
        // Convert FormData to JSON object
        formData.forEach((value, key) => {
            data[key] = value;
        });
    
        // Send the data using Fetch API
        fetch('http://localhost:8080/Text_Analyser/Login', {
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
        .then(data => {
            console.log('Success:', data);
            // Handle the JSON response from the backend
            console.log('Status: '+data.Status);
            console.log('Account status: '+data.Accountstatus);
            console.log('Privillage: '+data.Privillage);
            
            if(data.Privillage === "admin" && data.Accountstatus === "active")
            {
                window.location.href = "Dashboard.html";
            }
            else if(data.Privillage === "user"){
                window.location.href = "/Text_Analyser/The-App/Home.html";
            }
            else
            {
                if(document.getElementById("Warning").classList.contains("d-none")){
                    document.getElementById("Warning").classList.remove("d-none");
                    document.getElementById("Warning").innerHTML = data.Status;
                }
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    // End of the Sign In function

    // Captcha
    RegenerateCaptcha() 
    {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';
        for (let i = 0; i < 6; i++) 
        {
            result += characters.charAt(Math.floor(Math.random() * characters.length));
        }
        captchaval = result;
        document.getElementById('captcha').innerHTML = result;
        console.log("captcha"+result);
        
        const rc = new Authentication();
        rc.Checkcaptcha(document.getElementById('captcha').value);
        document.getElementById('Captchaelement').value = "";
    }
    Checkcaptcha()
    {
        let rs = document.getElementById('Captchaelement').value;
        if(rs == captchaval)
        {
            document.getElementById("catpchawarning").classList.add("d-none");
            captchaflag = true;
            console.log(captchaflag);
        }
        else
        {
            if(rs.length > 0){
                document.getElementById("catpchawarning").classList.remove("d-none");
            }
            else{
                document.getElementById("catpchawarning").classList.add("d-none");
            }
            captchaflag = false;
        }
        const au = new Authentication();
        au.checkSigninformValidity();
    }
    // End of Captcha
}
// End of the Authentication Class

// Authentication object
const rs = new Authentication();

// Authenticate Function : This maintains the flow of the reigstrations and the Signins
function Authenticate(req){
    switch(req){
        case "Register":
            rs.Registration();
            break;
        case "Login":
            rs.Signin();
            break;
        default:
            console.error("Invalid Request !");
            break;
    }
}
// End of the Authenticate Function


document.getElementById('Captchaelement').addEventListener('input', rs.Checkcaptcha);
document.getElementById('captchgenerator').addEventListener('click',rs.RegenerateCaptcha);

const thesignin = document.getElementById('signinfrm');
thesignin.addEventListener('input', rs.checkSigninFormValidity);
thesignin.addEventListener('change', rs.checkSigninFormValidity);

document.getElementById('signinbtn').addEventListener('click',rs.Signin);

document.addEventListener('DOMContentLoaded', function() {
    rs.CheckSession();
    rs.RegenerateCaptcha();
});