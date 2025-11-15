function escapeHTML(str) {
    return str.replace(/[&<>"']/g, (char) => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;',
    }[char]));
}

async function loadGuardianDashboard() {
    try {
        const res = await fetch('/api/guardian');
        if (!res.ok) throw new Error(`HTTP error ${res.status}`);
        const data = await res.json();

        const { guardian, child_grades: grades, child_disciplinary_records: records } = data;

        // Personal info
        document.getElementById('guardian_id').textContent = guardian.id;
        document.getElementById('first_name').textContent = guardian.first_name;
        document.getElementById('last_name').textContent = guardian.last_name;
        document.getElementById('email').textContent = guardian.email;
        document.getElementById('phone').textContent = guardian.phone;

        // Grades
        const gradesTable = document.getElementById('grades_table');
        gradesTable.innerHTML = '';
        grades.forEach(g => {
            const row = document.createElement('tr');
            const courseCell = document.createElement('td');
            const gradeCell = document.createElement('td');
            courseCell.textContent = g.course;
            gradeCell.textContent = g.grade;
            row.appendChild(courseCell);
            row.appendChild(gradeCell);
            gradesTable.appendChild(row);
        });

        // Disciplinary records
        const discTable = document.getElementById('disciplinary_table');
        discTable.innerHTML = '';
        records.forEach(r => {
            const row = document.createElement('tr');
            ['date', 'reason', 'status'].forEach(key => {
                const cell = document.createElement('td');
                cell.textContent = r[key];
                row.appendChild(cell);
            });
            discTable.appendChild(row);
        });

    } catch (err) {
        console.error('Failed to load guardian dashboard:', err);
        alert('Unable to load dashboard. Please try again later.');
    }
}

document.getElementById('logoutBtn').addEventListener('click', () => {
    window.location.href = 'index.html';
});

loadGuardianDashboard();
