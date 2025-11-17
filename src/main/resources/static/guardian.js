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
    
    if (!token || role !== 'GUARDIAN') {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

async function loadGuardianDashboard() {
    if (!checkAuth()) return;
    
    const guardianId = localStorage.getItem('userId');
    
    try {
        // Load guardian personal info
        const guardianRes = await fetch(`/api/guardians/me`, {
            headers: getAuthHeaders()
        });
        
        if (guardianRes.status === 401 || guardianRes.status === 403) {
            alert('Session expired. Please login again.');
            localStorage.clear();
            window.location.href = 'index.html';
            return;
        }
        
        if (!guardianRes.ok) throw new Error(`HTTP error ${guardianRes.status}`);
        const guardian = await guardianRes.json();

        // Populate guardian info
        document.getElementById('id').textContent = guardian.id || '';
        document.getElementById('first_name').textContent = guardian.firstName || '';
        document.getElementById('last_name').textContent = guardian.lastName || '';
        document.getElementById('email').textContent = guardian.email || '';
        document.getElementById('phone').textContent = guardian.phone || '';

        // Load children (students under this guardian)
        await loadChildren(guardianId);

    } catch (err) {
        console.error('Failed to load guardian dashboard:', err);
        alert('Unable to load dashboard. Please try again later.');
    }
}

async function loadChildren(guardianId) {
    try {
        const res = await fetch(`/api/guardians/${guardianId}/students`, {
            headers: getAuthHeaders()
        });
        
        if (!res.ok) {
            console.error('Failed to load children');
            return;
        }
        
        const students = await res.json();
        
        if (students.length === 0) {
            document.getElementById('grades_table').innerHTML = '<tr><td colspan="3">No children found</td></tr>';
            document.getElementById('disciplinary_table').innerHTML = '<tr><td colspan="3">No children found</td></tr>';
            return;
        }
        
        // Load grades and disciplinary records for all children
        for (const student of students) {
            await loadChildGrades(guardianId, student.id);
            await loadChildDisciplinaryRecords(guardianId, student.id);
        }

    } catch (err) {
        console.error('Failed to load children:', err);
    }
}

async function loadChildGrades(guardianId, studentId) {
    try {
        const res = await fetch(`/api/guardians/${guardianId}/students/${studentId}/grades`, {
            headers: getAuthHeaders()
        });
        
        if (!res.ok) return;
        
        const grades = await res.json();
        const gradesTable = document.getElementById('grades_table');
        
        if (gradesTable.innerHTML === '') {
            gradesTable.innerHTML = '';
        }
        
        grades.forEach(grade => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${escapeHTML(grade.courseId)}</td>
                <td>${escapeHTML(grade.term)}</td>
                <td>${escapeHTML(grade.grade)}</td>
            `;
            gradesTable.appendChild(row);
        });
        
        if (gradesTable.innerHTML === '') {
            gradesTable.innerHTML = '<tr><td colspan="3">No grades available</td></tr>';
        }

    } catch (err) {
        console.error('Failed to load child grades:', err);
    }
}

async function loadChildDisciplinaryRecords(guardianId, studentId) {
    try {
        const res = await fetch(`/api/guardians/${guardianId}/students/${studentId}/disciplinary-records`, {
            headers: getAuthHeaders()
        });
        
        if (!res.ok) return;
        
        const records = await res.json();
        const discTable = document.getElementById('disciplinary_table');
        
        records.forEach(record => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${escapeHTML(record.date)}</td>
                <td>${escapeHTML(record.descriptions)}</td>
                <td>Recorded</td>
            `;
            discTable.appendChild(row);
        });
        
        if (discTable.innerHTML === '') {
            discTable.innerHTML = '<tr><td colspan="3">No disciplinary records</td></tr>';
        }

    } catch (err) {
        console.error('Failed to load child disciplinary records:', err);
    }
}

document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = 'index.html';
});

loadGuardianDashboard();
