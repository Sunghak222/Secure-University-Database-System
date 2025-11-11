function escapeHTML(str) {
    return str.replace(/[&<>"']/g, (char) => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;',
    }[char]));
}

async function loadStudentData() {
    try {
        const res = await fetch('/api/student');
        if (!res.ok) throw new Error(`HTTP error ${res.status}`);
        const student = await res.json();

        const fields = [
            'student_id', 'first_name', 'last_name', 'gender',
            'identification_number', 'address', 'email',
            'phone', 'enrollment_year', 'guardian_relation'
        ];

        fields.forEach(field => {
            const el = document.getElementById(field);
            if (el) el.textContent = student[field] ?? '';
        });

        // Optional: If disciplinary records are returned
        if (student.disciplinary_records && Array.isArray(student.disciplinary_records)) {
            const table = document.getElementById('disciplinary_table');
            table.innerHTML = '';
            student.disciplinary_records.forEach(record => {
                const row = document.createElement('tr');
                ['date', 'reason', 'status'].forEach(key => {
                    const cell = document.createElement('td');
                    cell.textContent = record[key] ?? '';
                    row.appendChild(cell);
                });
                table.appendChild(row);
            });
        }

    } catch (err) {
        console.error('Failed to load student data:', err);
        alert('Unable to load dashboard. Please try again later.');
    }
}

document.getElementById('logoutBtn').addEventListener('click', () => {
    window.location.href = 'index.html';
});

loadStudentData();
