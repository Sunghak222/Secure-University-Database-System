document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const role = document.getElementById('role').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    const errorBox = document.getElementById('errorMessage');
    errorBox.textContent = '';

    // Basic frontend validation
    if (!role || !email || !password) {
        errorBox.textContent = 'All fields are required.';
        return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        errorBox.textContent = 'Invalid email format.';
        return;
    }

    if (password.length < 6) {
        errorBox.textContent = 'Password must be at least 6 characters.';
        return;
    }

    try {
        const res = await fetch('/api/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ role, email, password })
        });

        const data = await res.json();

        if (res.ok) {
            const safeRole = role.toLowerCase();
            window.location.href = `${safeRole}-dashboard.html`;
        } else {
            errorBox.textContent = data.error || 'Login failed.';
        }
    } catch (err) {
        console.error('Login error:', err);
        errorBox.textContent = 'Network error. Please try again.';
    }
});
