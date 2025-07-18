    const backendBaseUrl = 'http://localhost:8080';

    async function login() {
        const name = document.getElementById('userName').value.trim();
        const email = document.getElementById('userEmail').value.trim();

        if (!name || !email) {
            alert("Ism va emailni to‘ldiring.");
            return;
        }

        try {
            const res = await fetch(`${backendBaseUrl}/api/orders`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    customerName: name,
                    customerEmail: email
                })
            });

            if (!res.ok) {
                throw new Error("Order yaratishda xatolik.");
            }

            const order = await res.json();

            if (!order) {
                throw new Error("Order ID qaytmadi.");
            }

            sessionStorage.setItem('user', JSON.stringify({
                name: order.customerName,
                email: order.customerEmail,
                orderId: order.id
            }));

            window.location.href = "../static/product.html";

        } catch (err) {
            alert(err.message);
        }
    }
