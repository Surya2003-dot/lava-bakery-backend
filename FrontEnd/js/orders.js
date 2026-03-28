function goToOrderPage(cakeId) {
    window.location.href = `order.html?cakeId=${cakeId}`;
}

document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("cakeImage")) {
        loadOrderPage();
    }
});

function loadOrderPage() {

    const params = new URLSearchParams(window.location.search);
    const cakeId = params.get("cakeId");

    if (!cakeId) return;

    fetch(`${API_BASE_URL}/cakes/${cakeId}`)
        .then(res => res.json())
        .then(cake => {

    const imageUrl = cake.imageUrl || "https://via.placeholder.com/300";

    const mobileImg = document.getElementById("cakeImage");
    if (mobileImg) mobileImg.src = imageUrl;

    const desktopImg = document.getElementById("cakeImageDesktop");
    if (desktopImg) desktopImg.src = imageUrl;

    document.getElementById("cakeName").innerText = cake.name;
    document.getElementById("cakePrice").innerText =
        `₹${cake.pricePerKg} per Kg`;

    localStorage.setItem("selectedCakeId", cakeId);
});
}

function placeOrder() {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login first!");
        window.location.href = "login.html";
        return;
    }

    const cakeId = localStorage.getItem("selectedCakeId");
    const kg = document.getElementById("kg").value;

    fetch(`${API_BASE_URL}/orders/place?cakeId=${cakeId}&kg=${kg}`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(data => {
        alert("Order placed successfully 🎉");
        window.location.href = "index.html";
    })
    .catch(err => {
        alert("Order failed ❌");
        console.error(err);
    });
}