function escapeHTML(str) {
    return str.replace(/[&<>"']/g, (char) => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;',
    }[char]));
}

document.getElementById('searchForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const studentId = document.getElementById('search_student_id').value.trim();
    const reason = document.getElementById('search_reason').value.trim();

    const query = `/api/dro/records?student_id=${encodeURIComponent(studentId)}&reason=${encodeURIComponent(reason)}`;

    try {
        const res = await fetch(query);
        const records = await res.json();

        const table = document.getElementById('records_table');
        table.innerHTML = '';

        records.forEach(r => {
            const row = document.createElement('tr');

            ['student_id', 'date', 'reason', 'status'].forEach(key => {
                const cell = document.createElement('td');
                cell.textContent = r[key];
                row.appendChild(cell);
            });

            const actions = document.createElement('td');
            actions.innerHTML = `
        <button class="edit-btn" data-id="${escapeHTML(r.id)}">Edit</button>
        <button class="delete-btn" data-id="${escapeHTML(r.id)}">Delete</button>
      `;
            row.appendChild(actions);

            table.appendChild(row);
        });
    } catch (err) {
        console.error('Failed to fetch records:', err);
    }
});

document.getElementById('addRecordForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const student_id = document.getElementById('new_student_id').value.trim();
    const date = document.getElementById('new_date').value;
    const reason = document.getElementById('new_reason').value.trim();
    const status = document.getElementById('new_status').value.trim();

    if (!/^\d{6}$/.test(student_id) || reason.length > 100 || status.length > 50) {
        alert('Invalid input. Please check your entries.');
        return;
    }

    try {
        const res = await fetch('/api/dro/records', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ student_id, date, reason, status })
        });

        if (res.ok) {
            alert('Record added.');
            document.getElementById('addRecordForm').reset();
            document.getElementById('searchForm').dispatchEvent(new Event('submit'));
        } else {
            alert('Failed to add record.');
        }
    } catch (err) {
        console.error('Add failed:', err);
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

        fetch(`/api/dro/records/${encodeURIComponent(id)}`, {
            method: 'DELETE'
        }).then(res => {
            if (res.ok) {
                alert('Record deleted.');
                document.getElementById('searchForm').dispatchEvent(new Event('submit'));
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
    const reason = document.getElementById('edit_reason').value.trim();
    const status = document.getElementById('edit_status').value.trim();

    if (reason.length > 100 || status.length > 50) {
        alert('Invalid input. Please check your entries.');
        return;
    }

    try {
        const res = await fetch(`/api/dro/records/${encodeURIComponent(id)}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ date, reason, status })
        });

        if (res.ok) {
            alert('Record updated.');
            document.getElementById('editRecordSection').style.display = 'none';
            document.getElementById('searchForm').dispatchEvent(new Event('submit'));
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
    window.location.href = 'index.html';
});