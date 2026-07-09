USE self_healing_db_2.0;
DROP TABLE IF EXISTS records;
CREATE TABLE records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_student_name VARCHAR(255),
    original_roll_number BIGINT,
    original_grade VARCHAR(255),
    student_name VARCHAR(255),
    roll_number BIGINT,
    grade VARCHAR(255),
    status VARCHAR(255),
    hash VARCHAR(64)
);
