-- AI Generated

------------------------------------
-- GUARDIANS
------------------------------------
INSERT INTO guardians (last_name, first_name, email, phone)
VALUES
    (
        'Chan', 'Mary', 'mary.chan@example.com',
        AES_ENCRYPT('+85290001111', UNHEX(SHA2('YOUR_SECRET_KEY',512)))
    );


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
        AES_ENCRYPT('A1234567', UNHEX(SHA2('YOUR_SECRET_KEY',512))),   -- Changed
        AES_ENCRYPT('12 Harbour Road, Wanchai', UNHEX(SHA2('YOUR_SECRET_KEY',512))), -- Changed
        'jason.lee@example.com',
        AES_ENCRYPT('+85291234567', UNHEX(SHA2('YOUR_SECRET_KEY',512))), -- Changed
        2025, 1, 'Mother'
    ),
    (
        'abc', 'yyy', 'M',
        AES_ENCRYPT('A1234547', UNHEX(SHA2('YOUR_SECRET_KEY',512))),  -- Changed
        AES_ENCRYPT('13 Harbour Road, Wanchai', UNHEX(SHA2('YOUR_SECRET_KEY',512))), -- Changed
        'aaa.lee@example.com',
        AES_ENCRYPT('+85291256789', UNHEX(SHA2('YOUR_SECRET_KEY',512))), -- Changed
        2025, 1, 'Mother'
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
        AES_ENCRYPT('S1122334', UNHEX(SHA2('YOUR_SECRET_KEY',512))),
        AES_ENCRYPT('15 Academic Building, HK', UNHEX(SHA2('YOUR_SECRET_KEY',512))),
        'sarah.lam@example.com',
        AES_ENCRYPT('+85293334444', UNHEX(SHA2('YOUR_SECRET_KEY',512))),
        'Academic Affairs', 'ARO'
    ),
    (
        'Cheung', 'Michael', 'M',
        AES_ENCRYPT('S5566778', UNHEX(SHA2('YOUR_SECRET_KEY',512))),
        AES_ENCRYPT('20 Student Affairs Office, HK', UNHEX(SHA2('YOUR_SECRET_KEY',512))),
        'michael.cheung@example.com',
        AES_ENCRYPT('+85294445555', UNHEX(SHA2('YOUR_SECRET_KEY',512))),
        'Student Affairs', 'DRO'
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
    (
        1, 1, '202526S1',
        AES_ENCRYPT('A', UNHEX(SHA2('YOUR_SECRET_KEY',512))),
        AES_ENCRYPT('Excellent performance', UNHEX(SHA2('YOUR_SECRET_KEY',512)))
    ),
    (
        1, 2, '202526S1',
        AES_ENCRYPT('B+', UNHEX(SHA2('YOUR_SECRET_KEY',512))),
        AES_ENCRYPT('Needs improvement in writing', UNHEX(SHA2('YOUR_SECRET_KEY',512)))
    ),
    (
        2, 1, '202526S1',
        AES_ENCRYPT('A+', UNHEX(SHA2('YOUR_SECRET_KEY',512))),
        AES_ENCRYPT('Outstanding performance', UNHEX(SHA2('YOUR_SECRET_KEY',512)))
    );


------------------------------------
-- DISCIPLINARY RECORDS
------------------------------------
INSERT INTO disciplinary_records (student_id, date, staff_id, descriptions)
VALUES
    (
        1, '2025-11-01', 1,
        AES_ENCRYPT('Late submission of homework', UNHEX(SHA2('YOUR_SECRET_KEY',512)))
    );


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
('aaa.lee@example.com',
 '$2a$12$iiY/q0c4K122ovmHWP2u4Ogudf.QoSsG2o0e1zXyTVSPDbEnB7WQO',
 'STUDENT',
 NULL, 2, NULL, TRUE),

-- Guardian login (linked to guardian.id = 1)
('mary.chan@example.com',
 '$2a$12$iiY/q0c4K122ovmHWP2u4Ogudf.QoSsG2o0e1zXyTVSPDbEnB7WQO',
 'GUARDIAN',
 NULL, NULL, 1, TRUE);
