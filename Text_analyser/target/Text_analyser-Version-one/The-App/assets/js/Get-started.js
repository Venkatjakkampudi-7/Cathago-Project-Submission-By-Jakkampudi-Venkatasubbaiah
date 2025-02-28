// Js for register and signin panel
document.addEventListener("DOMContentLoaded", function () {
    const registerTab = document.getElementById('registrationtab');
    const signinTab = document.getElementById('signintab');

    const registerContent = document.getElementById("register-now");
    const signinContent = document.getElementById("signin-now");

    registerTab.addEventListener("click", function () {
        registerTab.classList.add("active","bg-danger","text-light");
        signinTab.classList.remove("active","bg-danger","text-light");

        registerContent.classList.remove("d-none");
        signinContent.classList.add("d-none");
    });

    signinTab.addEventListener("click", function () {
        signinTab.classList.add("active","bg-danger","text-light");
        registerTab.classList.remove("active","bg-danger","text-light");

        registerContent.classList.add("d-none");
        signinContent.classList.remove("d-none");
    });
});