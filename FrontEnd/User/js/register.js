function register(){

const name=document.getElementById("name").value.trim();
const email=document.getElementById("email").value.trim();
const password=document.getElementById("password").value.trim();

const nameError=document.getElementById("nameError");
const emailError=document.getElementById("emailError");
const passwordError=document.getElementById("passwordError");

nameError.style.display="none";
emailError.style.display="none";
passwordError.style.display="none";

let valid=true;

if(name===""){
nameError.style.display="block";
valid=false;
}

if(email===""){
emailError.style.display="block";
valid=false;
}

if(password.length<6){
passwordError.style.display="block";
valid=false;
}

if(!valid) return;

fetch(`${API_BASE_URL}/auth/register`,{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({
name:name,
email:email,
password:password
})

})

.then(res=>{

if(!res.ok){
throw new Error("Register failed");
}

return res.json();

})

.then(data=>{

window.location.href="login.html";

})

.catch(err=>{

emailError.innerText="Email already exists";
emailError.style.display="block";

});

}