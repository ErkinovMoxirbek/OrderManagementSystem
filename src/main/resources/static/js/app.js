let stompClient;
let jwt;
let myEmail;
let selectedEmail;

document.getElementById('loginBtn').addEventListener('click', () => {
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!email || !password) {
        alert("Email va password kiritilishi shart!");
        return;
    }

    axios.post('/api/auth/login', { email, password })
        .then(response => {
            jwt = response.data.token;
            const payload = JSON.parse(atob(jwt.split('.')[1]));
            myEmail = payload.email;

            if (payload.role === 'ADMIN') {
                document.getElementById('loginForm').style.display = 'none';
                document.getElementById('adminSection').style.display = 'block';
                loadActiveUsers();
            } else {
                document.getElementById('loginForm').style.display = 'none';
                openChat(myEmail); // oddiy user o‘z emailiga chat ochadi
            }
        })
        .catch(error => {
            alert("Login xato: " + error.response?.data?.message || error.message);
        });
});

function loadActiveUsers() {
    axios.get('/api/profile', { headers: { Authorization: `Bearer ${jwt}` } })
        .then(res => {
            const users = res.data;
            const list = document.getElementById('userList');
            list.innerHTML = '';
            users.forEach(user => {
                if (user.email !== myEmail) { // o‘zini chiqarma
                    const li = document.createElement('li');
                    li.className = 'list-group-item list-group-item-action';
                    li.textContent = user.email;
                    li.style.cursor = 'pointer';
                    li.onclick = () => openChat(user.email);
                    list.appendChild(li);
                }
            });
        });
}

function openChat(roomId) {
    selectedEmail = roomId;
    document.getElementById('adminSection').style.display = 'none';
    document.getElementById('chatSection').style.display = 'block';
    document.getElementById('chatWith').textContent = `Chat with ${roomId}`;
    document.getElementById('messages').innerHTML = '';
    connectWebSocket(roomId);
}

document.getElementById('backBtn').addEventListener('click', () => {
    stompClient.disconnect();
    document.getElementById('chatSection').style.display = 'none';
    document.getElementById('adminSection').style.display = 'block';
});

function connectWebSocket(roomId) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({ Authorization: `Bearer ${jwt}` }, () => {
        stompClient.subscribe(`/topic/${roomId}`, (message) => {
            const msgObj = JSON.parse(message.body);
            const msgDiv = document.createElement('div');
            msgDiv.className = 'p-2 mb-2 bg-secondary text-white rounded';
            msgDiv.textContent = `${msgObj.sender}: ${msgObj.content}`;
            document.getElementById('messages').appendChild(msgDiv);
            document.getElementById('messages').scrollTop = document.getElementById('messages').scrollHeight;
        });
    });
}

document.getElementById('sendBtn').addEventListener('click', () => {
    const input = document.getElementById('messageInput');
    const content = input.value.trim();
    if (content !== '') {
        const message = {
            sender: myEmail,
            content: content,
            roomId: selectedEmail
        };
        stompClient.send(`/app/chat/${selectedEmail}`, {}, JSON.stringify(message));
        input.value = '';
    }
});