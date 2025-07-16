// 🌐 Backend bazaviy URL
const backendBaseUrl = 'http://localhost:8080';

// 📥 Mahsulotlar ro'yxatini yuklash
function loadProducts() {
    fetch(`${backendBaseUrl}/api/products`)
        .then(res => res.json())
        .then(data => renderProducts(data))
        .catch(err => {
            console.error(err);
            alert("Mahsulotlar ro'yxatini yuklashda xatolik yuz berdi!");
        });
}

// 🖼️ Mahsulotlarni chiqarish
function renderProducts(products) {
    const list = document.getElementById('productList');
    list.innerHTML = '';
    if (products.length === 0) {
        list.innerHTML = '<p>Mahsulot topilmadi.</p>';
        return;
    }
    products.forEach(p => {
        const div = document.createElement('div');
        div.className = 'product-item';
        div.innerHTML = `<strong>${p.name}</strong> - ${p.price} so'm`;
        list.appendChild(div);
    });
}

// ➕ Mahsulot qo'shish
function addProduct() {
    const name = document.getElementById('nameInput').value.trim();
    const price = document.getElementById('priceInput').value.trim();

    if (!name || !price) {
        alert("Iltimos, nom va narxni to'ldiring.");
        return;
    }

    fetch(`${backendBaseUrl}/api/products`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, price })
    })
        .then(res => {
            if (res.ok) {
                loadProducts();
                document.getElementById('nameInput').value = '';
                document.getElementById('priceInput').value = '';
            } else {
                alert('Mahsulot qo‘shishda xatolik!');
            }
        })
        .catch(err => {
            console.error(err);
            alert("Mahsulot qo'shishda xatolik yuz berdi!");
        });
}

// 🔍 Qidiruv
function searchProducts() {
    const query = document.getElementById('searchInput').value.trim();

    if (!query) {
        loadProducts();
        return;
    }

    fetch(`${backendBaseUrl}/api/products/search?query=${encodeURIComponent(query)}`)
        .then(res => res.json())
        .then(data => renderProducts(data))
        .catch(err => {
            console.error(err);
            alert("Qidiruvda xatolik yuz berdi!");
        });
}

// 📥 Yuklanganda chaqirish
loadProducts();
