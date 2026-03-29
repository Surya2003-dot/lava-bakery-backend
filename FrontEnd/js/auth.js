
function login(){

const email=document.getElementById("email").value;
const password=document.getElementById("password").value;

const emailError=document.getElementById("emailError");
const passwordError=document.getElementById("passwordError");

emailError.style.display="none";
passwordError.style.display="none";

fetch(`${API_BASE_URL}/auth/user/login`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({
email:email,
password:password
})

})

.then(res=>{

if(!res.ok){
throw new Error("Invalid");
}

return res.json();

})

.then(data=>{

localStorage.setItem("token",data.token);

window.location.href="index.html";

})

.catch(err=>{

emailError.style.display="block";
passwordError.style.display="block";

});

}
function sendOtp(){

const email = document.getElementById("email").value;

const otpSuccess = document.getElementById("otpSuccess");
const emailError = document.getElementById("emailError");
const emailExistError = document.getElementById("emailExistError");
const otpBtn = document.getElementById("registerOtpBtn");

emailError.style.display="none";
emailExistError.style.display="none";

if(!email){
emailError.style.display="block";
return;
}

 fetch("https://lava-bakery-backend.onrender.com/auth/send-register-otp",{
method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({
email:email
})

})
.then(res=>res.text())

.then(data=>{

if(data === "Email already exists"){
emailExistError.style.display="block";
return;
}

otpSuccess.style.display="block";

setTimeout(()=>{
otpSuccess.style.display="none";
},2000);

otpBtn.disabled = true;

let seconds = 30;

otpBtn.innerText = `Resend OTP (${seconds}s)`;

const timer = setInterval(()=>{

seconds--;

otpBtn.innerText = `Resend OTP (${seconds}s)`;

if(seconds <= 0){

clearInterval(timer);

otpBtn.disabled = false;

otpBtn.innerText = "Resend OTP";

}

},1000);

})

.catch(err=>{
console.error("Error:", err);
  alert("Something went wrong: " + err.message);
emailError.innerText="OTP send failed";
emailError.style.display="block";

});

}
function resetPassword(){

const email = document.getElementById("email").value;
const otp = document.getElementById("otp").value;
const newPassword = document.getElementById("password").value;

const otpError = document.getElementById("otpError");
const passwordSuccess = document.getElementById("passwordSuccess");

otpError.style.display="none";

fetch(`${API_BASE_URL}/auth/reset-password`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({
email:email,
otp:otp,
newPassword:newPassword
})

})
.then(res=>{

if(!res.ok){
throw new Error("Invalid OTP");
}

return res.text();

})
.then(data=>{

passwordSuccess.style.display="block";

setTimeout(()=>{
passwordSuccess.style.display="none";
window.location.href="login.html";
},2000);

})
.catch(err=>{

otpError.style.display="block";

});
}
function register(){

const name=document.getElementById("name").value;
const email=document.getElementById("email").value;
const password=document.getElementById("password").value;
const otp=document.getElementById("otp").value;

const emailExistError=document.getElementById("emailExistError");
const registerSuccess=document.getElementById("registerSuccess");
const otpError=document.getElementById("otpError");

emailExistError.style.display="none";
otpError.style.display="none";

fetch(`${API_BASE_URL}/auth/register`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({
name:name,
email:email,
password:password,
otp:otp
})

})

.then(res=>{

if(!res.ok){
throw new Error("Invalid OTP");
}

return res.text();

})

.then(data=>{

if(data === "Email already exists"){

emailExistError.style.display="block";
return;

}

registerSuccess.style.display="block";

setTimeout(()=>{

registerSuccess.style.display="none";
window.location.href="login.html";

},2000);

})

.catch(err=>{

otpError.style.display="block";

});

}
function sendForgotOtp(){

const email = document.getElementById("email").value;
const emailError = document.getElementById("emailError");
const otpSuccess = document.getElementById("otpSuccess");
const otpBtn = document.getElementById("otpBtn");

emailError.style.display="none";

if(!email){
emailError.style.display="block";
return;
}

fetch(`${API_BASE_URL}/auth/send-forgot-otp`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({
email:email
})

})
.then(res=>res.text())

.then(data=>{

if(data === "Email not registered"){
emailError.innerText="Email not registered";
emailError.style.display="block";
return;
}

otpSuccess.style.display="block";

setTimeout(()=>{
otpSuccess.style.display="none";
},2000);

otpBtn.disabled = true;

otpBtn.innerText = "Resend OTP (30s)";

let seconds = 30;

const timer = setInterval(()=>{

seconds--;

otpBtn.innerText = `Resend OTP (${seconds}s)`;

if(seconds <= 0){

clearInterval(timer);

otpBtn.disabled = false;

otpBtn.innerText = "Resend OTP";

}

},1000);

})

.catch(err=>{
emailError.innerText="OTP send failed";
emailError.style.display="block";
});

}