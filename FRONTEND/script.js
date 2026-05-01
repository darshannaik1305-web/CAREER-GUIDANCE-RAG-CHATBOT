const BASE_URL = "http://localhost:6060";

// =========================
// 💬 CHAT FUNCTION
// =========================
function sendMessage() {

    const msgInput = document.getElementById("message");
    const chatBox = document.getElementById("chatBox");

    const msg = msgInput.value.trim();
    if (!msg) return;

    // Show user message
    chatBox.innerHTML += `<div class="user">${msg}</div>`;
    chatBox.scrollTop = chatBox.scrollHeight;

    // Typing indicator
    const typingDiv = document.createElement("div");
    typingDiv.className = "bot";
    typingDiv.innerText = "Typing...";
    chatBox.appendChild(typingDiv);
    chatBox.scrollTop = chatBox.scrollHeight;

    // API call
    fetch(BASE_URL + "/api/chat", {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: msg
    })
    .then(res => {
        if (!res.ok) throw new Error("Server error");
        return res.text();
    })
    .then(data => {
        typingDiv.remove();
        chatBox.innerHTML += `<div class="bot">${data}</div>`;
        chatBox.scrollTop = chatBox.scrollHeight;
    })
    .catch(err => {
        console.error(err);
        typingDiv.remove();
        chatBox.innerHTML += `<div class="bot">❌ Error connecting to server</div>`;
    });

    msgInput.value = "";
}

// =========================
// ⌨️ ENTER KEY SUPPORT
// =========================
document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("message");

    if (input) {
        input.addEventListener("keypress", function (e) {
            if (e.key === "Enter") {
                sendMessage();
            }
        });
    }
});

// =========================
// 🛠 ADD CAREER
// =========================
function addCareer() {

    const role = document.getElementById("role").value.trim();
    const skills = document.getElementById("skills").value.trim();
    const roadmap = document.getElementById("roadmap").value.trim();
    const resources = document.getElementById("resources").value.trim();

    if (!role || !skills || !roadmap) {
        alert("⚠️ Please fill all required fields");
        return;
    }

    fetch(BASE_URL + "/api/admin/career", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            role: role,
            skillsRequired: skills,
            roadmap: roadmap,
            resources: resources
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to add career");
        return res.text();
    })
    .then(data => {
        alert("✅ " + data);
        clearForm();
        loadCareers();
    })
    .catch(err => {
        console.error(err);
        alert("❌ Error adding career");
    });
}

// =========================
// 🛠 LOAD CAREERS (WITH DELETE)
// =========================
function loadCareers() {

    fetch(BASE_URL + "/api/admin/careers")
    .then(res => {
        if (!res.ok) throw new Error("Failed to load careers");
        return res.json();
    })
    .then(data => {

        const list = document.getElementById("careerList");
        if (!list) return;

        list.innerHTML = "";

        data.forEach(c => {
            list.innerHTML += `
                <div style="margin-bottom:10px; padding:10px; border:1px solid #444; border-radius:8px;">
                    <b>${c.role}</b><br>
                    <small>${c.skillsRequired}</small><br><br>

                    <button onclick="deleteCareer(${c.id})"
                        style="background:red; color:white; padding:5px 10px; border:none; border-radius:5px;">
                        Delete
                    </button>
                </div>
            `;
        });
    })
    .catch(err => {
        console.error(err);
        alert("❌ Error loading careers");
    });
}

// =========================
// 🗑 DELETE CAREER
// =========================
function deleteCareer(id) {

    if (!confirm("Are you sure you want to delete this career?")) return;

    fetch(BASE_URL + `/api/admin/career/${id}`, {
        method: "DELETE"
    })
    .then(res => {
        if (!res.ok) throw new Error("Delete failed");
        return res.text();
    })
    .then(data => {
        alert("✅ " + data);
        loadCareers();
    })
    .catch(err => {
        console.error(err);
        alert("❌ Error deleting career");
    });
}

// =========================
// 🧹 CLEAR FORM
// =========================
function clearForm() {
    document.getElementById("role").value = "";
    document.getElementById("skills").value = "";
    document.getElementById("roadmap").value = "";
    document.getElementById("resources").value = "";
}

// =========================
// 🚀 AUTO LOAD (ADMIN PAGE)
// =========================
if (window.location.pathname.includes("admin.html")) {
    loadCareers();
}