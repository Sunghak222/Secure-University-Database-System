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
    ),
    (
        'Chan', 'Leon', 'M',
        'A1234547',
        '13 Harbour Road, Wanchai',
        'leon.chan@example.com',
        '+85291256789',
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
        'Lam', 'Sarah', 'F',
        'S1122334',
        '15 Academic Building, HK',
        'sarah.lam@example.com',
        '+85293334444',
        'Academic Affairs',
        'ARO'
    ),
    (
        'Cheung', 'Michael', 'M',
        'S5566778',
        '20 Student Affairs Office, HK',
        'michael.cheung@example.com',
        '+85294445555',
        'Student Affairs',
        'DRO'
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
    (1, 2, '202526S1', 'B+', 'Needs improvement in writing'),
    (2, 1, '202526S1', 'A+', 'Outstanding performance');


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
    -- Password for all accounts: "password123"
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMye6l7dQJQnTqx6MQmN5nC3gKQJ8pQKQQS


-- Staff login (linked to staff.id = 1)
('david.wong@example.com',
 '$2a$12$iiY/q0c4K122ovmHWP2u4Ogudf.QoSsG2o0e1zXyTVSPDbEnB7WQO',
 'STAFF',
 1, NULL, NULL, TRUE),

-- ARO login (linked to staff.id = 2)
('sarah.lam@example.com',
 '$2a$12$iiY/q0c4K122ovmHWP2u4Ogudf.QoSsG2o0e1zXyTVSPDbEnB7WQO',
 'ARO',
 2, NULL, NULL, TRUE),

-- DRO login (linked to staff.id = 3)
('michael.cheung@example.com',
 '$2a$12$iiY/q0c4K122ovmHWP2u4Ogudf.QoSsG2o0e1zXyTVSPDbEnB7WQO',
 'DRO',
 3, NULL, NULL, TRUE),

-- Student login (linked to student.id = 1)
('jason.lee@example.com',
 '$2a$12$iiY/q0c4K122ovmHWP2u4Ogudf.QoSsG2o0e1zXyTVSPDbEnB7WQO',
 'STUDENT',
 NULL, 1, NULL, TRUE),

-- Student login (linked to student.id = 2)
('leon.chan@example.com',
 '$2a$12$iiY/q0c4K122ovmHWP2u4Ogudf.QoSsG2o0e1zXyTVSPDbEnB7WQO',
 'STUDENT',
 NULL, 2, NULL, TRUE),

-- Guardian login (linked to guardian.id = 1)
('mary.chan@example.com',
 '$2a$12$iiY/q0c4K122ovmHWP2u4Ogudf.QoSsG2o0e1zXyTVSPDbEnB7WQO',
 'GUARDIAN',
 NULL, NULL, 1, TRUE);
