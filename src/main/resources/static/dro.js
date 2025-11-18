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
    
    if (!token || role !== 'DRO') {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

async function loadAllRecords() {
    if (!checkAuth()) return;
    
    try {
        const res = await fetch('/api/disciplinary-records/all', {
            headers: getAuthHeaders()
        });
        
        if (res.status === 401 || res.status === 403) {
            alert('Session expired. Please login again.');
            localStorage.clear();
            window.location.href = 'index.html';
            return;
        }
        
        if (!res.ok) throw new Error('Failed to load records');
        const records = await res.json();
        displayRecords(records);
    } catch (err) {
        console.error('Failed to load records:', err);
    }
}

function displayRecords(records) {
    const table = document.getElementById('records_table');
    table.innerHTML = '';

    if (records.length === 0) {
        table.innerHTML = '<tr><td colspan="5">No records found</td></tr>';
        return;
    }

    records.forEach(r => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${escapeHTML(r.studentId)}</td>
            <td>${escapeHTML(r.date)}</td>
            <td>${escapeHTML(r.descriptions)}</td>
            <td>Recorded</td>
            <td>
                <button class="edit-btn" data-id="${r.id}">Edit</button>
                <button class="delete-btn" data-id="${r.id}">Delete</button>
            </td>
        `;
        table.appendChild(row);
    });
}

document.getElementById('searchForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const studentId = document.getElementById('search_student_id').value.trim();
    const reason = document.getElementById('search_reason').value.trim().toLowerCase();

    try {
        const res = await fetch('/api/disciplinary-records/all', {
            headers: getAuthHeaders()
        });
        
        if (!res.ok) throw new Error('Search failed');
        let records = await res.json();
        console.log("Fetched Records:", records);

        if (studentId) {
            records = records.filter(r => String(r.studentId) === studentId);
        }
        if (reason) {
            records = records.filter(r =>
                r.descriptions?.toLowerCase().includes(reason)
            );
        }

        displayRecords(records);
    } catch (err) {
        console.error('Failed to search records:', err);
    }
});

document.getElementById('addRecordForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const studentId = document.getElementById('new_student_id').value.trim();
    const staffId = localStorage.getItem('userId'); // Current DRO staff ID
    const date = document.getElementById('new_date').value;
    const description = document.getElementById('new_reason').value.trim();

    try {
        const res = await fetch('/api/disciplinary-records', {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify({ 
                studentId: parseInt(studentId),
                staffId: parseInt(staffId),
                date,
                description
            })
        });

        if (res.ok) {
            alert('Record added.');
            document.getElementById('addRecordForm').reset();
            loadAllRecords();
        } else {
            const error = await res.text();
            alert('Failed to add record: ' + error);
        }
    } catch (err) {
        console.error('Add failed:', err);
        alert('Failed to add record.');
    }
});

document.addEventListener('click', (e) => {
    if (e.target.classList.contains('edit-btn')) {
        const row = e.target.closest('tr');
        document.getElementById('edit_record_id').value = e.target.dataset.id;
        document.getElementById('edit_student_id').value = row.children[0].textContent;
        document.getElementById('edit_date').value = row.children[1].textContent;
        document.getElementById('edit_reason').value = row.children[2].textContent;
        document.getElementById('edit_status').value = row.children[3].textContent;
        document.getElementById('editRecordSection').style.display = 'block';
    }

    if (e.target.classList.contains('delete-btn')) {
        const id = e.target.dataset.id;
        if (!confirm('Delete this record?')) return;

        fetch(`/api/disciplinary-records/${id}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        }).then(res => {
            if (res.ok) {
                alert('Record deleted.');
                loadAllRecords();
            } else {
                alert('Failed to delete record.');
            }
        }).catch(err => {
            console.error('Delete failed:', err);
        });
    }
});

document.getElementById('editRecordForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('edit_record_id').value;
    const date = document.getElementById('edit_date').value;
    const description = document.getElementById('edit_reason').value.trim();

    try {
        const res = await fetch(`/api/disciplinary-records/${id}`, {
            method: 'PUT',
            headers: getAuthHeaders(),
            body: JSON.stringify({ date, description })
        });

        if (res.ok) {
            alert('Record updated.');
            document.getElementById('editRecordSection').style.display = 'none';
            loadAllRecords();
        } else {
            alert('Failed to update record.');
        }
    } catch (err) {
        console.error('Edit failed:', err);
    }
});

document.getElementById('cancelEditBtn').addEventListener('click', () => {
    document.getElementById('editRecordSection').style.display = 'none';
});

// Logout
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = 'index.html';
});

// Load all records on page load
loadAllRecords();