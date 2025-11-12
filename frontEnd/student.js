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

        // Load grades
        if (student.student_id) {
            loadGrades(student.student_id);
        }

        // Load disciplinary records
        if (Array.isArray(student.disciplinary_records)) {
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

async function loadGrades(studentId) {
    try {
        const res = await fetch(`/api/students/${studentId}/grades`);
        if (!res.ok) throw new Error(`HTTP error ${res.status}`);
        const grades = await res.json();

        const tableBody = document.getElementById('grades_table');
        tableBody.innerHTML = '';
        grades.forEach(({ course, semester, grade }) => {
            const row = document.createElement('tr');
            row.innerHTML = `
        <td>${escapeHTML(course)}</td>
        <td>${escapeHTML(semester)}</td>
        <td>${escapeHTML(grade)}</td>
      `;
            tableBody.appendChild(row);
        });
    } catch (err) {
        console.error('Failed to load grades:', err);
    }
}

document.getElementById('logoutBtn').addEventListener('click', () => {
    window.location.href = 'index.html';
});

loadStudentData();
