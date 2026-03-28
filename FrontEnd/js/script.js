// ===============================
// GLOBAL ELEMENTS
// ===============================

const searchInput =
document.getElementById("searchInput") ||
document.getElementById("desktopSearch");
const historyContainer = document.getElementById("searchHistory");
const suggestionsBox = document.getElementById("suggestionsBox");

// ===============================
// PAGE LOAD
// ===============================

document.addEventListener("DOMContentLoaded", () => {

initSearch()          // 🔥 important
setupSearchOverlay()

updateCartCount()
updateOrderCount()
restoreCartIcons()
updateBagCount()

})
// ===============================
// SEARCH SYSTEM
// ===============================

function initSearch(){

const searchInput =
document.getElementById("searchInput") ||
document.getElementById("desktopSearch")

if(!searchInput) return

// ENTER SEARCH
searchInput.addEventListener("keypress",(e)=>{

if(e.key==="Enter"){

let value = searchInput.value.trim()

if(!value) return

saveSearchHistory(value)

window.location.href =
"search.html?flavour="+encodeURIComponent(value)

}

})

// LIVE SUGGESTIONS
searchInput.addEventListener("input",()=>{

let keyword = searchInput.value.trim()

if(keyword.length < 2){
clearSuggestions()
return
}

fetch(`${API_BASE_URL}/cakes/search?name=${keyword}`)
.then(res=>res.json())
.then(data=>{

let cakes = data.content || data

let box = document.getElementById("suggestionsBox")
if(!box) return

box.innerHTML=""

cakes.slice(0,5).forEach(cake=>{

let div=document.createElement("div")
div.className="suggestion-item"

div.innerHTML=`${cake.name}`

div.onclick=()=>{
window.location.href=`search.html?flavour=${encodeURIComponent(cake.name)}`
}

box.appendChild(div)

})

})

})

}
document.addEventListener("DOMContentLoaded",()=>{

const desktopSearch = document.getElementById("desktopSearch")

if(desktopSearch){

desktopSearch.addEventListener("keydown",(e)=>{

if(e.key === "Enter"){

let value = desktopSearch.value.trim()

if(!value) return

window.location.href =
"search.html?flavour=" + encodeURIComponent(value)

}

})

}

})

// ===============================
// SEARCH OVERLAY
// ===============================

function setupSearchOverlay(){

const searchInput =
document.getElementById("searchInput") ||
document.getElementById("desktopSearch")
const searchIcon = document.getElementById("mobileSearchIcon")
const overlay = document.getElementById("searchOverlay")
const closeBtn = document.getElementById("closeSearch")

if(searchIcon && overlay){

searchIcon.addEventListener("click",()=>{
overlay.style.display="block"
searchInput.focus()
loadSearchHistory()
})

}

if(closeBtn && overlay){

closeBtn.addEventListener("click",()=>{
overlay.style.display="none"
})

}

}

// ===============================
// SEARCH HISTORY
// ===============================

function loadSearchHistory(){

if(!historyContainer) return

historyContainer.innerHTML=""

let history = JSON.parse(localStorage.getItem("searchHistory")) || []

history.slice(0,5).forEach(item=>{

const div=document.createElement("div")
div.className="search-history-item"

div.innerHTML=`
<span class="history-text">${item}</span>
<i class="fa-solid fa-arrow-up-right-from-square history-arrow"></i>
`

div.querySelector(".history-text").onclick=()=>{
window.location.href="search.html?flavour="+encodeURIComponent(item)
}

div.querySelector(".history-arrow").onclick=(e)=>{
e.stopPropagation()
searchInput.value=item
searchInput.focus()
}

historyContainer.appendChild(div)

})

}

// ===============================
// SAVE SEARCH HISTORY
// ===============================

if(searchInput){

searchInput.addEventListener("keypress",(e)=>{

if(e.key==="Enter"){

let value=searchInput.value.trim()

if(!value) return

let history=JSON.parse(localStorage.getItem("searchHistory"))||[]

if(!history.includes(value)){
history.unshift(value)
}

localStorage.setItem("searchHistory",JSON.stringify(history))

window.location.href="search.html?flavour="+encodeURIComponent(value)

}

})

}

// ===============================
// SEARCH SUGGESTIONS
// ===============================

if(searchInput){

searchInput.addEventListener("input",()=>{

let keyword=searchInput.value.trim()

if(keyword.length<2){

suggestionsBox.innerHTML=""
if(historyContainer) historyContainer.style.display="block"
return

}

if(historyContainer) historyContainer.style.display="none"

fetch(`${API_BASE_URL}/cakes/search?name=${keyword}`)
.then(res=>res.json())
.then(data=>{

const cakes=data.content||data

suggestionsBox.innerHTML=""

cakes.slice(0,5).forEach(cake=>{

const div=document.createElement("div")
div.className="suggestion-item"

div.innerHTML=`
<span class="suggestion-text">${cake.name}</span>
<i class="fa-solid fa-arrow-up-right-from-square suggestion-arrow"></i>
`

div.querySelector(".suggestion-text").onclick=()=>{
window.location.href=`search.html?flavour=${encodeURIComponent(cake.name)}`
}

div.querySelector(".suggestion-arrow").onclick=(e)=>{
e.stopPropagation()
searchInput.value=cake.name
searchInput.focus()
}

suggestionsBox.appendChild(div)

})

})

})

}

// ===============================
// CART COUNT
// ===============================

function updateCartCount(){

let token=localStorage.getItem("token")
let badge=document.getElementById("cartCount")

if(!token){
if(badge) badge.innerText=0
return
}

fetch("https://lava-bakery-backend.onrender.com/api/cart",{
headers:{ "Authorization":"Bearer "+token }
})
.then(res=>res.json())
.then(data=>{
if(badge) badge.innerText=data.length
})
.catch(err=>console.log("Cart error",err))

}

// ===============================
// ORDER BADGE COUNT
// ===============================
function updateOrderCount(){

let token = localStorage.getItem("token")

let badge = document.getElementById("orderCount")

if(!token){
if(badge) badge.innerText = 0
return
}

fetch("https://lava-bakery-backend.onrender.com/api/orders/pending-count",{
headers:{
"Authorization":"Bearer "+token
}
})
.then(res=>res.json())
.then(data=>{

console.log(data)   // debug

let count = data.count || data.pendingCount || 0

if(badge){
badge.innerText = count
}

})
.catch(err=>{
console.log("Order count error",err)
})

}
// ===============================
// ADD TO CART
// ===============================

function addToCart(event,id){

event.stopPropagation()

let token=localStorage.getItem("token")

if(!token){
window.location.href="login.html"
return
}

fetch("https://lava-bakery-backend.onrender.com/api/cart/add",{

method:"POST",

headers:{
"Authorization":"Bearer "+token,
"Content-Type":"application/x-www-form-urlencoded"
},

body:new URLSearchParams({ cakeId:id,weight:1  })

})
.then(()=>{
updateCartCount()
restoreCartIcons()
})

}

// ===============================
// TOGGLE CART ICON
// ===============================

function toggleCart(event,id,btn){

event.stopPropagation()

let token=localStorage.getItem("token")

if(!token){
window.location.href="login.html"
return
}

if(btn.classList.contains("cart-active")){

fetch("https://lava-bakery-backend.onrender.com/api/cart",{
headers:{ "Authorization":"Bearer "+token }
})
.then(res=>res.json())
.then(cart=>{

let item=cart.find(c=>c.cakeId==id)

if(item){

fetch(`https://lava-bakery-backend.onrender.com/api/cart/${item.id}`,{
method:"DELETE",
headers:{ "Authorization":"Bearer "+token }
})
.then(()=>{
btn.classList.remove("cart-active")
updateCartCount()
})

}

})

}else{

fetch("https://lava-bakery-backend.onrender.com/api/cart/add",{

method:"POST",

headers:{
"Authorization":"Bearer "+token,
"Content-Type":"application/x-www-form-urlencoded"
},

body:new URLSearchParams({ cakeId:id })

})
.then(()=>{
btn.classList.add("cart-active")
updateCartCount()
})

}

}

// ===============================
// RESTORE CART ICONS
// ===============================

function restoreCartIcons(){

let token=localStorage.getItem("token")

if(!token) return

fetch("https://lava-bakery-backend.onrender.com/api/cart",{
headers:{ "Authorization":"Bearer "+token }
})
.then(res=>res.json())
.then(cart=>{

document.querySelectorAll(".cart-icon-btn").forEach(btn=>{

let id=btn.dataset.id

let exists=cart.find(item=>item.cakeId==id)

if(exists){
btn.classList.add("cart-active")
}else{
btn.classList.remove("cart-active")
}

})

})

}

// ===============================
// LOGIN / LOGOUT
// ===============================

document.addEventListener("DOMContentLoaded",()=>{

const loginBtn=document.getElementById("loginBtn")
const token=localStorage.getItem("token")

if(!loginBtn) return

if(token){

loginBtn.innerHTML='<i class="fa-solid fa-right-from-bracket me-1"></i> Logout'

loginBtn.onclick=(e)=>{
e.preventDefault()
localStorage.removeItem("token")
location.reload()
}

}else{

loginBtn.innerHTML='<i class="fa-solid fa-user me-1"></i> Login'
loginBtn.href="login.html"

}

})
// ===============================
// Bag count
// ===============================
function updateBagCount(){

let token = localStorage.getItem("token")

let badge = document.getElementById("bagCount")

if(!token){
if(badge) badge.innerText=0
return
}

fetch("https://lava-bakery-backend.onrender.com/api/orders/pending-count",{
headers:{ "Authorization":"Bearer "+token }
})
.then(res=>res.json())
.then(data=>{

if(badge){
badge.innerText = data.count
}

})

}
// ===============================
// category filter
// ===============================

function openCategory(flavour){

if(flavour === "ALL"){
window.location.href = "category.html"
return
}

window.location.href = "category.html?flavour=" + encodeURIComponent(flavour)

}

// ===============================
// MOBILE USER ICON
// ===============================

document.addEventListener("DOMContentLoaded",()=>{

let mobileUser = document.getElementById("mobileUserIcon")
let dropdown = document.getElementById("userDropdown")
let logoutBtn = document.getElementById("logoutBtn")
let cancelBtn = document.getElementById("cancelBtn")
let badge = document.getElementById("userStatusBadge")

let token = localStorage.getItem("token")

// login status badge
if(token && badge){
badge.style.background="green"
badge.innerHTML='<i class="fa-solid fa-check"></i>'
}else if(badge){
badge.style.background="red"
badge.innerHTML='<i class="fa-solid fa-xmark"></i>'
}

// user icon click
if(mobileUser){

mobileUser.addEventListener("click",(e)=>{

e.preventDefault()

if(!token){
window.location.href="login.html"
return
}

// show dropdown
dropdown.style.display="block"

})

}

// logout
if(logoutBtn){

logoutBtn.addEventListener("click",()=>{

localStorage.removeItem("token")
window.location.reload()

})

}

// cancel dropdown
if(cancelBtn){

cancelBtn.addEventListener("click",()=>{

dropdown.style.display="none"

})

}

// close dropdown outside click
document.addEventListener("click",(e)=>{

if(dropdown &&
!dropdown.contains(e.target) &&
!mobileUser.contains(e.target)){

dropdown.style.display="none"

}

})

})