document.addEventListener("DOMContentLoaded", () => {
loadCakes();
updateCartCount();
});

function logout(){
localStorage.removeItem("token");
location.reload();
}

// ==============================
// LOAD CAKES
// ==============================

function loadCakes(){

// fetch(`${API_BASE_URL}/cakes/search?page=0&size=20`)
fetch(`${API_BASE_URL}/cakes/featured`)
.then(res=>res.json())
.then(data=>{

const container=document.getElementById("cakeContainer")
container.innerHTML=""

data.forEach(cake=>{

container.innerHTML+=`
<div class="col-6 col-md-4 col-lg-3 p-1">

<div class="mobile-product-card"
onclick="window.location='details.html?id=${cake.id}&weight=${parseFloat(cake.weights)}'"
style="cursor:pointer;">

<div class="mobile-product-img">

${cake.badge ? `<span class="badge-label ${cake.badge === 'Hot Seller' ? 'hot-seller' : 'best-seller'}">${cake.badge}</span>` : ''}

<img src="${cake.imageUrl}" alt="${cake.name}">

<span class="prep-time-badge">
<i class="fa-solid fa-clock"></i>
${cake.preparationTimeHours} hrs
</span>

<div class="wishlist">

<button class="cart-icon-btn"
data-id="${cake.id}"
onclick="toggleCart(event,${cake.id},this)">

<i class="fa fa-shopping-cart cart-icon"></i>

</button>

</div>

</div>

<div class="p-2">

<div class="product-title">
${cake.name}
</div>

<div class="price-row">

<span class="main-price">₹${cake.pricePerKg}</span>

${cake.oldPrice ? `<span class="old-price">₹${cake.oldPrice}</span>` : ''}

${cake.offerPercentage ? `<span class="discount">${cake.offerPercentage}% OFF</span>` : ''}

<!-- Weight moved here -->
<span class="cake-weight">
<i class="fa-solid fa-scale-balanced"></i>
${cake.weights}
</span>
<!-- Shape moved here -->
<span class="cake-shape">
<i class="fa-solid fa-shapes"></i>
${cake.shapes}
</span>
</div>

<p class="cake-desc">${cake.description}</p>

<!-- Rating -->
<div class="rating-row">
<div>
<span class="rating-badge">${cake.rating} ★</span>
<span class="review-count">${cake.reviewCount} Reviews</span>
</div>



</div>

</div>

</div>

</div>
`

})

restoreCartIcons()

})
.catch(err=>console.log("Cake load error",err))

}


// ==============================
// TOGGLE CART
// ==============================

function toggleCart(event,id,btn){

event.stopPropagation()

let token = localStorage.getItem("token")

if(!token){
window.location.href="login.html"
return
}

if(btn.classList.contains("cart-active")){

// REMOVE

fetch("https://lava-bakery-backend.onrender.com/api/cart",{
headers:{ "Authorization":"Bearer "+token }
})
.then(res=>res.json())
.then(cart=>{

let item = cart.find(c=>c.cakeId==id || c.cake?.id==id)

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

// ADD

fetch(`https://lava-bakery-backend.onrender.com/api/cart/add?cakeId=${id}`,{
method:"POST",
headers:{ "Authorization":"Bearer "+token }
})
.then(()=>{
btn.classList.add("cart-active")
updateCartCount()
})

}

}


// ==============================
// RESTORE ICON AFTER REFRESH
// ==============================

function restoreCartIcons(){

let token = localStorage.getItem("token")

if(!token) return

fetch("https://lava-bakery-backend.onrender.com/api/cart",{
headers:{ "Authorization":"Bearer "+token }
})
.then(res=>res.json())
.then(cart=>{

document.querySelectorAll(".cart-icon-btn").forEach(btn=>{

let id = btn.dataset.id

let exists = cart.find(item=>item.cakeId==id)

if(exists){
btn.classList.add("cart-active")
}

})

})

}


// ==============================
// CART COUNT
// ==============================

function updateCartCount(){

let token = localStorage.getItem("token")
let badge = document.getElementById("cartCount")

if(!token){
if(badge) badge.innerText=0
return
}

fetch("https://lava-bakery-backend.onrender.com/api/cart",{

headers:{"Authorization":"Bearer "+token}
})
.then(res=>res.json())
.then(data=>{
if(badge){
badge.innerText=data.length
}
})

}