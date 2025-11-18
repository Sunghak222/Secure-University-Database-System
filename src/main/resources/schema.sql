ALTER TABLE guardians
    MODIFY phone VARBINARY(255);

ALTER TABLE students
    MODIFY identification_number VARBINARY(255),
    MODIFY address VARBINARY(500),
    MODIFY phone VARBINARY(255);

ALTER TABLE staffs
    MODIFY identification_number VARBINARY(255),
    MODIFY address VARBINARY(500),
    MODIFY phone VARBINARY(255);

ALTER TABLE grades
    MODIFY grade VARBINARY(255),
    MODIFY comments VARBINARY(500);

ALTER TABLE disciplinary_records
    MODIFY descriptions VARBINARY(500);
