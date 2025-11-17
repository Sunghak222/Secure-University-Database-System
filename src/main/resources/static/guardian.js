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

        // Load all courses first for mapping
        await loadAllCourses();
        
        // Load children (students under this guardian)
        await loadChildren(guardianId);

    } catch (err) {
        console.error('Failed to load guardian dashboard:', err);
        alert('Unable to load dashboard. Please try again later.');
    }
}

// Cache for course information
const courseCache = {};

async function loadAllCourses() {
    try {
        const res = await fetch(`/api/courses`, {
            headers: getAuthHeaders()
        });
        
        if (res.ok) {
            const courses = await res.json();
            courses.forEach(course => {
                courseCache[course.id] = course;
            });
        }
    } catch (err) {
        console.error('Failed to load courses:', err);
    }
}

async function loadChildren(guardianId) {
    try {
        const gradesTable = document.getElementById('grades_table');
        const discTable = document.getElementById('disciplinary_table');
        
        // Clear tables
        gradesTable.innerHTML = '';
        discTable.innerHTML = '';
        
        // Strategy: Fetch grades for potential student IDs and build student list from that
        // We'll try student IDs from the grades we can access
        const students = await discoverStudents(guardianId);
        
        if (students.length === 0) {
            gradesTable.innerHTML = '<tr><td colspan="2">No children found</td></tr>';
            discTable.innerHTML = '<tr><td colspan="3">No children found</td></tr>';
            return;
        }
        
        const showNames = students.length > 1;
        
        // Load grades and disciplinary records for all children
        for (const student of students) {
            await loadChildGrades(student, showNames);
            await loadChildDisciplinaryRecords(student, showNames);
        }
        
        // Show "No data" messages if tables are still empty
        if (gradesTable.innerHTML === '') {
            gradesTable.innerHTML = '<tr><td colspan="2">No grades available</td></tr>';
        }
        if (discTable.innerHTML === '') {
            discTable.innerHTML = '<tr><td colspan="3">No disciplinary records</td></tr>';
        }

    } catch (err) {
        console.error('Failed to load children:', err);
        document.getElementById('grades_table').innerHTML = '<tr><td colspan="2">Error loading data</td></tr>';
        document.getElementById('disciplinary_table').innerHTML = '<tr><td colspan="3">Error loading data</td></tr>';
    }
}

async function discoverStudents(guardianId) {
    // Fetch students directly from the guardian's students endpoint
    try {
        const res = await fetch(`/api/guardians/me/students`, {
            headers: getAuthHeaders()
        });
        
        if (!res.ok) {
            console.error('Failed to fetch students');
            return [];
        }
        
        const students = await res.json();
        
        // Fetch grades for each student and attach them
        for (const student of students) {
            try {
                const gradesRes = await fetch(`/api/grades/${student.id}`, {
                    headers: getAuthHeaders()
                });
                
                if (gradesRes.ok) {
                    student.grades = await gradesRes.json();
                } else {
                    student.grades = [];
                }
            } catch (err) {
                console.error(`Failed to fetch grades for student ${student.id}:`, err);
                student.grades = [];
            }
        }
        
        return students;
    } catch (err) {
        console.error('Failed to discover students:', err);
        return [];
    }
}

async function getStudentName(studentId) {
    // This function is no longer needed but kept for backwards compatibility
    return { firstName: 'Child', lastName: '#' + studentId };
}

async function getCourseInfo(courseId) {
    if (courseCache[courseId]) {
        return courseCache[courseId];
    }
    
    return { code: 'N/A', courseName: 'Unknown Course' };
}

async function loadChildGrades(student, showNames) {
    try {
        // Use cached grades if available
        let grades = student.grades;
        
        if (!grades) {
            const res = await fetch(`/api/grades/${student.id}`, {
                headers: getAuthHeaders()
            });
            
            if (!res.ok) {
                console.log(`No grades found for student ${student.id}`);
                return;
            }
            
            grades = await res.json();
        }
        
        const gradesTable = document.getElementById('grades_table');
        
        if (grades.length === 0) return;
        
        // Add student name header - always show student name
        const headerRow = document.createElement('tr');
        headerRow.style.fontWeight = 'bold';
        headerRow.style.backgroundColor = '#c6e8ffff';
        headerRow.innerHTML = `
            <td colspan="2" style="padding: 8px; text-align: left;">${escapeHTML(student.firstName)} ${escapeHTML(student.lastName)}</td>
        `;
        gradesTable.appendChild(headerRow);
        
        // Fetch course information and display
        for (const grade of grades) {
            const course = await getCourseInfo(grade.courseId);
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${escapeHTML(course.code)}-${escapeHTML(course.courseName)} </td>
                <td>${escapeHTML(grade.term)}</td>
                <td>${escapeHTML(grade.grade)}</td>
            `;
            gradesTable.appendChild(row);
        }

    } catch (err) {
        console.error('Failed to load child grades:', err);
    }
}

async function loadChildDisciplinaryRecords(student, showNames) {
    try {
        const res = await fetch(`/api/disciplinary-records/${student.id}`, {
            headers: getAuthHeaders()
        });
        
        if (!res.ok) {
            console.log(`No disciplinary records found for student ${student.id}`);
            return;
        }
        
        const records = await res.json();
        const discTable = document.getElementById('disciplinary_table');
        
        // Add student name header - always show student name
        const headerRow = document.createElement('tr');
        headerRow.style.fontWeight = 'bold';
        headerRow.style.backgroundColor = '#c6e8ffff';
        headerRow.innerHTML = `
            <td colspan="3" style="padding: 8px; text-align: left;">${escapeHTML(student.firstName)} ${escapeHTML(student.lastName)}</td>
        `;
        discTable.appendChild(headerRow);
        
        if (records.length === 0) {
            // Show "No disciplinary records" message for this student
            const noRecordsRow = document.createElement('tr');
            noRecordsRow.innerHTML = `
                <td colspan="3" style="text-align: center; font-style: italic;">No disciplinary records</td>
            `;
            discTable.appendChild(noRecordsRow);
            return;
        }
        
        records.forEach(record => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${escapeHTML(record.date)}</td>
                <td>${escapeHTML(record.descriptions)}</td>
                <td>Recorded</td>
            `;
            discTable.appendChild(row);
        });

    } catch (err) {
        console.error('Failed to load child disciplinary records:', err);
    }
}

document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = 'index.html';
});

loadGuardianDashboard();
