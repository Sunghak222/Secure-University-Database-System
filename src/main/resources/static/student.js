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

// Get auth headers with JWT token
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };
}

// Check if user is logged in
function checkAuth() {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    
    if (!token || role !== 'STUDENT') {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

async function loadStudentData() {
    if (!checkAuth()) return;
    
    try {
        // Fetch student personal info
        const res = await fetch(`/api/students/me`, {
            headers: getAuthHeaders()
        });
        
        if (res.status === 401 || res.status === 403) {
            alert('Session expired. Please login again.');
            window.location.href = 'index.html';
            return;
        }
        
        if (!res.ok) throw new Error(`HTTP error ${res.status}`);
        const student = await res.json();
        
        // Store current student data for edit mode
        currentStudentData = student;
        
        console.log('Student data fetched:', student);
        console.log('Student ID:', student.id);

        // Populate personal info
        document.getElementById('id').textContent = student.id || '';
        document.getElementById('first_name').textContent = student.firstName || '';
        document.getElementById('last_name').textContent = student.lastName || '';
        document.getElementById('gender').textContent = student.gender || '';
        document.getElementById('identification_number').textContent = student.identificationNumber || '';
        document.getElementById('address').textContent = student.address || '';
        document.getElementById('email').textContent = student.email || '';
        document.getElementById('phone').textContent = student.phone || '';
        document.getElementById('enrollment_year').textContent = student.enrollmentYear || '';
        document.getElementById('guardian_id').textContent = student.guardianId || '';
        document.getElementById('guardian_relation').textContent = student.guardianRelation || '';

        // Load grades and disciplinary records using the student ID from the fetched data
        console.log('About to load grades with ID:', student.id);
        await loadGrades(student.id);
        console.log('About to load disciplinary records with ID:', student.id);
        await loadDisciplinaryRecords(student.id);

    } catch (err) {
        console.error('Failed to load student data:', err);
        alert('Unable to load dashboard. Please try again later.');
    }
}

async function loadGrades(studentId) {
    console.log('loadGrades called with studentId:', studentId);
    try {
        // Fetch courses first to map course IDs to names
        const coursesRes = await fetch('/api/courses', {
            headers: getAuthHeaders()
        });
        const courses = coursesRes.ok ? await coursesRes.json() : [];
        console.log('Courses fetched:', courses);
        const courseMap = {};
        courses.forEach(c => {
            courseMap[c.id] = c.code + ' - ' + c.courseName;
        });

        const res = await fetch(`/api/grades/${studentId}`, {
            headers: getAuthHeaders()
        });
        
        if (!res.ok) {
            console.error('Failed to load grades, status:', res.status);
            return;
        }
        
        const grades = await res.json();
        console.log('Grades fetched:', grades);
        const tableBody = document.getElementById('grades_table');
        tableBody.innerHTML = '';
        
        if (grades.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="3">No grades available</td></tr>';
            return;
        }
        
        grades.forEach(grade => {
            const row = document.createElement('tr');
            const courseName = courseMap[grade.courseId] || `Course ID: ${grade.courseId}`;
            row.innerHTML = `
                <td>${escapeHTML(courseName)}</td>
                <td>${escapeHTML(grade.term)}</td>
                <td>${escapeHTML(grade.grade)}</td>
            `;
            tableBody.appendChild(row);
        });
        console.log('Grades displayed successfully');
    } catch (err) {
        console.error('Failed to load grades:', err);
    }
}

async function loadDisciplinaryRecords(studentId) {
    console.log('loadDisciplinaryRecords called with studentId:', studentId);
    try {
        const res = await fetch(`/api/disciplinary-records/${studentId}`, {
            headers: getAuthHeaders()
        });
        
        if (!res.ok) {
            console.error('Failed to load disciplinary records, status:', res.status);
            return;
        }
        
        const records = await res.json();
        console.log('Disciplinary records fetched:', records);
        const tableBody = document.getElementById('disciplinary_table');
        tableBody.innerHTML = '';
        
        if (records.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="3">No disciplinary records</td></tr>';
            return;
        }
        
        records.forEach(record => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${escapeHTML(record.date)}</td>
                <td>${escapeHTML(record.descriptions)}</td>
                <td>Recorded</td>
            `;
            tableBody.appendChild(row);
        });
        console.log('Disciplinary records displayed successfully');
    } catch (err) {
        console.error('Failed to load disciplinary records:', err);
    }
}

document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = 'index.html';
});

// Edit mode functionality
let currentStudentData = null;

document.getElementById('editBtn').addEventListener('click', () => {
    document.getElementById('viewMode').style.display = 'none';
    document.getElementById('editMode').style.display = 'block';
    
    // Populate edit form with current data
    document.getElementById('edit_id').textContent = currentStudentData.id || '';
    document.getElementById('edit_first_name').value = currentStudentData.firstName || '';
    document.getElementById('edit_last_name').value = currentStudentData.lastName || '';
    document.getElementById('edit_gender').value = currentStudentData.gender || 'M';
    document.getElementById('edit_identification_number').value = currentStudentData.identificationNumber || '';
    document.getElementById('edit_address').value = currentStudentData.address || '';
    document.getElementById('edit_email').value = currentStudentData.email || '';
    document.getElementById('edit_phone').value = currentStudentData.phone || '';
    document.getElementById('edit_enrollment_year').value = currentStudentData.enrollmentYear || '';
    document.getElementById('edit_guardian_relation').value = currentStudentData.guardianRelation || '';
});

document.getElementById('cancelBtn').addEventListener('click', () => {
    document.getElementById('viewMode').style.display = 'block';
    document.getElementById('editMode').style.display = 'none';
});

document.getElementById('editForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const updateData = {
        firstName: document.getElementById('edit_first_name').value,
        lastName: document.getElementById('edit_last_name').value,
        gender: document.getElementById('edit_gender').value,
        identificationNumber: document.getElementById('edit_identification_number').value,
        address: document.getElementById('edit_address').value,
        email: document.getElementById('edit_email').value,
        phone: document.getElementById('edit_phone').value,
        enrollmentYear: parseInt(document.getElementById('edit_enrollment_year').value) || null,
        guardianRelation: document.getElementById('edit_guardian_relation').value
    };
    
    try {
        const res = await fetch('/api/students/me', {
            method: 'PATCH',
            headers: getAuthHeaders(),
            body: JSON.stringify(updateData)
        });
        
        if (res.ok) {
            alert('Information updated successfully!');
            // Reload the page to show updated data
            location.reload();
        } else {
            const error = await res.text();
            alert('Failed to update: ' + error);
        }
    } catch (err) {
        console.error('Failed to update student info:', err);
        alert('Failed to update information. Please try again.');
    }
});

loadStudentData();