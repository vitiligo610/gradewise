DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS semesters;

CREATE TABLE semesters (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name TEXT NOT NULL,
	sgpa REAL DEFAULT 0.0 NOT NULL,
	UNIQUE(id)
);

CREATE TABLE courses (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    semester_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    credit_hours INTEGER NOT NULL,
    grade TEXT NOT NULL,
    FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE CASCADE
);

INSERT INTO semesters (name, sgpa) VALUES
('Fall 2023', 3.81),
('Spring 2024', 3.83),
('Fall 2024', 4.00),
('Spring 2025', 3.61),
('Test Semester', 4.00);

INSERT INTO courses (semester_id, name, credit_hours, grade) VALUES
(1, 'Fundamentals of Programming', 4, 'A'),
(1, 'Calculus', 3, 'A'),
(1, 'Applied Physics', 3, 'A'),
(1, 'Discrete Mathematics', 3, 'A'),
(1, 'Pakistan Studies', 2, 'C+'),
(1, 'English', 2, 'A'),
(1, 'OHS', 1, 'B+');

INSERT INTO courses (semester_id, name, credit_hours, grade) VALUES
(2, 'Object Oriented Programming', 4, 'B+'),
(2, 'CALD', 4, 'A'),
(2, 'LA&ODEs', 3, 'A'),
(2, 'Communication Skills', 2, 'A'),
(2, 'Islamic Studies', 2, 'A'),
(2, 'Engineering Drawing', 2, 'B+'),
(2, 'Workshop Practice', 1, 'A');

INSERT INTO courses (semester_id, name, credit_hours, grade) VALUES
(3, 'Data Structures & Algorithms', 4, 'A'),
(3, 'Database Systems', 4, 'A'),
(3, 'Computer Networks', 4, 'A'),
(3, 'Software Engineering', 3, 'A'),
(3, 'Probability & Statistics', 3, 'C');

INSERT INTO courses (semester_id, name, credit_hours, grade) VALUES
(4, 'Operating Systems', 4, 'B+'),
(4, 'Design & Analysis of Algorithms', 3, 'B+'),
(4, 'Software Design & Architecture', 3, 'A'),
(4, 'Complex Variables & Transforms', 3, 'A'),
(4, 'Numerical Methods', 3, 'B+'),
(4, 'Professional Ethics', 2, 'B');

INSERT INTO courses (semester_id, name, credit_hours, grade) VALUES
(5, 'Test Course', 3, 'A')