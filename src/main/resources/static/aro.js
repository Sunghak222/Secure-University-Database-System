// Utility: escape HTML
function escapeHTML(str) {
    return str.replace(/[&<>"']/g, (char) => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;',
    }[char]));
}

// Search grades
document.getElementById('searchForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const studentId = document.getElementById('search_student_id').value.trim();
    const course = document.getElementById('search_course').value.trim();

    const query = `/api/aro/grades?student_id=${encodeURIComponent(studentId)}&course=${encodeURIComponent(course)}`;

    try {
        const res = await fetch(query);
        const grades = await res.json();

        const table = document.getElementById('grades_table');
        table.innerHTML = '';

        grades.forEach(g => {
            const row = document.createElement('tr');

            ['student_id', 'course', 'grade'].forEach(key => {
                const cell = document.createElement('td');
                cell.textContent = g[key];
                row.appendChild(cell);
            });

            const actions = document.createElement('td');
            actions.innerHTML = `
        <button class="edit-btn" data-id="${escapeHTML(g.id)}">Edit</button>
        <button class="delete-btn" data-id="${escapeHTML(g.id)}">Delete</button>
      `;
            row.appendChild(actions);

            table.appendChild(row);
        });
    } catch (err) {
        console.error('Failed to fetch grades:', err);
    }
});

// Add new grade
document.getElementById('addGradeForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const student_id = document.getElementById('new_student_id').value.trim();
    const course = document.getElementById('new_course').value.trim();
    const grade = document.getElementById('new_grade').value.trim();

    const isValidId = /^\d{6}$/.test(student_id);
    const isValidCourse = /^[A-Z]{4}\d{4}$/.test(course);
    const isValidGrade = /^(A\+|A|A-|B\+|B|B-|C\+|C|C-|D\+|D|D-|E\+|E|E-|F)$/;

    if (!isValidId || !isValidCourse || !isValidGrade) {
        alert('Invalid input. Please check your entries.');
        return;
    }

    try {
        const res = await fetch('/api/aro/grades', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ student_id, course, grade })
        });

        if (res.ok) {
            alert('Grade added successfully.');
            document.getElementById('addGradeForm').reset();
            document.getElementById('searchForm').dispatchEvent(new Event('submit'));
        } else {
            alert('Failed to add grade.');
        }
    } catch (err) {
        console.error('Failed to add grade:', err);
    }
});

// Delete grade
document.addEventListener('click', async (e) => {
    if (e.target.classList.contains('delete-btn')) {
        const gradeId = e.target.dataset.id;
        if (!confirm('Are you sure you want to delete this grade?')) return;

        try {
            const res = await fetch(`/api/aro/grades/${encodeURIComponent(gradeId)}`, {
                method: 'DELETE'
            });

            if (res.ok) {
                alert('Grade deleted.');
                document.getElementById('searchForm').dispatchEvent(new Event('submit'));
            } else {
                alert('Failed to delete grade.');
            }
        } catch (err) {
            console.error('Delete failed:', err);
        }
    }
});

// Logout
document.getElementById('logoutBtn').addEventListener('click', () => {
    window.location.href = 'index.html';
});
