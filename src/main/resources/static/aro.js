// Utility: escape HTML
function escapeHTML(str) {
    if (!str) return '';
    return String(str).replace(/[&<>"']/g, (char) => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;',
    }[char]));
}

function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };
}

function checkAuth() {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    
    if (!token || role !== 'ARO') {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

// Load all grades on page load
async function loadAllGrades() {
    if (!checkAuth()) return;
    
    try {
        const res = await fetch('/grades/all', {
            headers: getAuthHeaders()
        });
        
        if (res.status === 401 || res.status === 403) {
            alert('Session expired. Please login again.');
            localStorage.clear();
            window.location.href = 'index.html';
            return;
        }
        
        if (!res.ok) throw new Error('Failed to load grades');
        const grades = await res.json();
        displayGrades(grades);
    } catch (err) {
        console.error('Failed to load grades:', err);
    }
}

function displayGrades(grades) {
    const table = document.getElementById('grades_table');
    table.innerHTML = '';

    if (grades.length === 0) {
        table.innerHTML = '<tr><td colspan="5">No grades found</td></tr>';
        return;
    }

    grades.forEach(g => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${escapeHTML(g.studentId)}</td>
            <td>${escapeHTML(g.courseId)}</td>
            <td>${escapeHTML(g.term)}</td>
            <td>${escapeHTML(g.grade)}</td>
            <td>
                <button class="edit-btn" data-id="${g.id}">Edit</button>
                <button class="delete-btn" data-id="${g.id}">Delete</button>
            </td>
        `;
        table.appendChild(row);
    });
}

// Search grades
document.getElementById('searchForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const studentId = document.getElementById('search_student_id').value.trim();
    const course = document.getElementById('search_course').value.trim();

    if (!studentId && !course) {
        loadAllGrades();
        return;
    }

    try {
        let url = '/grades/all';
        const res = await fetch(url, {
            headers: getAuthHeaders()
        });
        
        if (!res.ok) throw new Error('Search failed');
        let grades = await res.json();

        // Filter client-side
        if (studentId) {
            grades = grades.filter(g => String(g.studentId) === studentId);
        }
        if (course) {
            grades = grades.filter(g => String(g.courseId).includes(course));
        }

        displayGrades(grades);
    } catch (err) {
        console.error('Failed to search grades:', err);
    }
});

// Add new grade
document.getElementById('addGradeForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const studentId = document.getElementById('new_student_id').value.trim();
    const courseId = document.getElementById('new_course_id').value.trim();
    const term = document.getElementById('new_term').value.trim();
    const grade = document.getElementById('new_grade').value.trim();
    const comments = document.getElementById('new_comments')?.value.trim() || '';

    try {
        const res = await fetch('/grades', {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({ 
                studentId: parseInt(studentId), 
                courseId: parseInt(courseId), 
                term, 
                grade,
                comments
            })
        });

        if (res.ok) {
            alert('Grade added successfully.');
            document.getElementById('addGradeForm').reset();
            loadAllGrades();
        } else {
            const error = await res.text();
            alert('Failed to add grade: ' + error);
        }
    } catch (err) {
        console.error('Failed to add grade:', err);
        alert('Failed to add grade.');
    }
});

// Delete grade
document.addEventListener('click', async (e) => {
    if (e.target.classList.contains('delete-btn')) {
        const gradeId = e.target.dataset.id;
        if (!confirm('Are you sure you want to delete this grade?')) return;

        try {
            const res = await fetch(`/grades/${gradeId}`, {
                method: 'DELETE',
                headers: getAuthHeaders()
            });

            if (res.ok) {
                alert('Grade deleted.');
                loadAllGrades();
            } else {
                alert('Failed to delete grade.');
            }
        } catch (err) {
            console.error('Delete failed:', err);
        }
    }

    if (e.target.classList.contains('edit-btn')) {
        const gradeId = e.target.dataset.id;
        const newGrade = prompt('Enter new grade (e.g., A, B+, C):');
        const newComments = prompt('Enter comments:');
        
        if (!newGrade) return;

        try {
            const res = await fetch(`/grades/${gradeId}`, {
                method: 'PUT',
                headers: getAuthHeaders(),
                body: JSON.stringify({ grade: newGrade, comments: newComments || '' })
            });

            if (res.ok) {
                alert('Grade updated.');
                loadAllGrades();
            } else {
                alert('Failed to update grade.');
            }
        } catch (err) {
            console.error('Update failed:', err);
        }
    }
});

// Logout
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = 'index.html';
});

// Load grades on page load
loadAllGrades();
