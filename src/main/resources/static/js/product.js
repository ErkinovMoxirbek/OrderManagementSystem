const backendBaseUrl = 'http://localhost:8080';
const cart = []; // frontend savat
const user = JSON.parse(sessionStorage.getItem('user'));

if (!user || !user.orderId) {
    window.location.href = "index.html";
}
const orderId = user.orderId;
loadProducts();
function loadProducts() {
    fetch(`${backendBaseUrl}/api/products`)
        .then(res => res.json())
        .then(data => renderProducts(data.data || data))
        .catch(err => console.error(err));
}

function renderProducts(products) {
    const list = document.getElementById('productList');
    list.innerHTML = '';

    products.forEach(p => {
        const div = document.createElement('div');
        div.className = 'col-md-4';
        div.innerHTML = `
            <div class="card product-card" onclick="addToCart('${p.id}', '${p.name}', ${p.price})">
                <div class="card-body">
                    <h5 class="card-title">${p.name}</h5>
                    <p class="card-text">${p.price} so'm</p>
                </div>
            </div>`;
        list.appendChild(div);
    });
}

function addToCart(id, name, price) {
    const existing = cart.find(item => item.id == id);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({id, name, price, quantity: 1});
    }

    // Backendga POST order-item yuborish
    const orderItemsAdded = {
        orderId: orderId,
        productId: id,
        quantity: existing ? existing.quantity : 1
    };

    fetch(`${backendBaseUrl}/api/order-items`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderItemsAdded)

    })
        .then(res => {
            if (!res.ok) {
                throw new Error(`Xatolik: ${name}`);
            }
        })
        .catch(err => {
            console.error(err);
            alert(`Xatolik yuz berdi: ${err.message}`);
        });

    renderCart();
}

function renderCart() {
    const list = document.getElementById('cartList');
    list.innerHTML = '';

    let total = 0;
    cart.forEach(item => {
        total += item.price * item.quantity;

        const li = document.createElement('li');
        li.className = 'list-group-item d-flex justify-content-between align-items-center';
        li.innerHTML = `
            <div>
                <strong>${item.name}</strong> x${item.quantity}
            </div>
            <div>${item.price * item.quantity} so'm</div>
        `;
        list.appendChild(li);
    });

    const totalLi = document.createElement('li');
    totalLi.className = 'list-group-item d-flex justify-content-between';
    totalLi.innerHTML = `<strong>Jami:</strong> <strong>${total} so'm</strong>`;
    list.appendChild(totalLi);
}

function addProduct() {
    const name = document.getElementById('nameInput').value.trim();
    const stock = document.getElementById('stockInput').value.trim();
    const price = document.getElementById('priceInput').value.trim();

    if (!name || !stock || !price) {
        alert("Barcha maydonlarni to‘ldiring.");
        return;
    }

    fetch(`${backendBaseUrl}/api/products`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, stock, price })
    })
        .then(res => {
            if (res.ok) {
                loadProducts();
                const modal = bootstrap.Modal.getInstance(document.getElementById('addModal'));
                modal.hide();
                document.getElementById('nameInput').value = '';
                document.getElementById('stockInput').value = '';
                document.getElementById('priceInput').value = '';
            } else {
                alert('Mahsulot qo‘shishda xatolik!');
            }
        })
        .catch(err => console.error(err));
}

function cancelOrder() {
    fetch(`${backendBaseUrl}/api/orders/${orderId}/CANCELLED`, {
        method: 'PUT'
    })
        .then(res => {
            if (res.ok) {
                alert("Buyurtma bekor qilindi.");
                cart.length = 0;
                renderCart();
            } else {
                alert("Bekor qilishda xatolik.");
            }
        })
        .catch(err => console.error(err));
}

function checkoutOrder() {
    if (cart.length === 0) {
        alert("Savat bo‘sh.");
        return;
    }

    alert("Buyurtma to‘landi!");
    cart.length = 0;
    renderCart();
}
