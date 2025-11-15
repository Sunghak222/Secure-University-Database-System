-- AI Generated

------------------------------------
-- GUARDIANS
------------------------------------
INSERT INTO guardians (last_name, first_name, email, phone)
VALUES
    ('Chan', 'Mary', 'mary.chan@example.com', '+85290001111');


------------------------------------
-- STUDENTS
------------------------------------
INSERT INTO students (
    last_name, first_name, gender,
    identification_number, address, email, phone,
    enrollment_year, guardian_id, guardian_relation
)
VALUES
    (
        'Lee', 'Jason', 'M',
        'A1234567',
        '12 Harbour Road, Wanchai',
        'jason.lee@example.com',
        '+85291234567',
        2025,
        1,
        'Mother'
    );


------------------------------------
-- STAFFS
------------------------------------
INSERT INTO staffs (
    last_name, first_name, gender,
    identification_number, address, email, phone,
    department, role
)
VALUES
    (
        'Wong', 'David', 'M',
        'S9988776',
        '88 Kowloon Bay Road, HK',
        'david.wong@example.com',
        '+85292223333',
        'IT',
        'STAFF'
    );


------------------------------------
-- COURSES
------------------------------------
INSERT INTO courses (code, course_name)
VALUES
    ('AMA1000', 'Mathematics'),
    ('COMP3335','dbsec');


------------------------------------
-- GRADES
------------------------------------
INSERT INTO grades (student_id, course_id, term, grade, comments)
VALUES
    (1, 1, '202526S1', 'A', 'Excellent performance'),
    (1, 2, '202526S1', 'B+', 'Needs improvement in writing');


------------------------------------
-- DISCIPLINARY RECORDS
------------------------------------
INSERT INTO disciplinary_records (student_id, date, staff_id, descriptions)
VALUES
    (1, '2025-11-01', 1, 'Late submission of homework');


------------------------------------
-- AUTH USERS (LOGIN ACCOUNTS)
------------------------------------
-- bcrypt hash examples are placeholder valid structures
-- NOTE: role must be ≤ 16 chars

INSERT INTO auth_users (
    email, password_hash, role,
    staff_id, student_id, guardian_id, enabled
)
VALUES
-- Admin user (no linked person)
('admin@example.com',
 '$2a$10$abcdefghabcdefghabcdefghabcdefghabcdefghabcd',
 'ADMIN',
 NULL, NULL, NULL, TRUE),

-- Staff login (linked to staff.id = 1)
('david.wong@example.com',
 '$2a$10$qwertyqwertyqwertyqwertyqwertyqwertyqwertyqw',
 'STAFF',
 1, NULL, NULL, TRUE),

-- Student login (linked to student.id = 1)
('jason.lee@example.com',
 '$2a$10$zxcvzxcvzxcvzxcvzxcvzxcvzxcvzxcvzxcvzxcvzxcv',
 'STUDENT',
 NULL, 1, NULL, TRUE),

-- Guardian login (linked to guardian.id = 1)
('mary.chan@example.com',
 '$2a$10$asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf',
 'GUARDIAN',
 NULL, NULL, 1, TRUE);
