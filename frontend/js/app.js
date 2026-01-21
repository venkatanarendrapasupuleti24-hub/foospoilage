function addItem() {
    fetch("http://localhost:8080/addItem", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            name: document.getElementById("itemName").value,
            quantity: document.getElementById("quantity").value,
            expiry: document.getElementById("expiry").value
        })
    })
    .then(res => res.text())
    .then(alert)
    .catch(err => alert("Error: " + err));
}

function consumeItem() {
    fetch("http://localhost:8080/consume", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            name: document.getElementById("consumeName").value,
            quantity: document.getElementById("consumeQty").value
        })
    })
    .then(res => res.text())
    .then(alert)
    .catch(err => alert("Error: " + err));
}

function logWaste() {
    fetch("http://localhost:8080/logWaste", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            name: document.getElementById("wasteName").value,
            quantity: document.getElementById("wasteQty").value,
            reason: document.getElementById("reason").value
        })
    })
    .then(res => res.text())
    .then(alert)
    .catch(err => alert("Error: " + err));
}
