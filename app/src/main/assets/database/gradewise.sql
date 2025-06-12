DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS semesters;

CREATE TABLE semesters (
    id TEXT PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
	sgpa REAL DEFAULT 0.0 NOT NULL,
	created_at TEXT DEFAULT CURRENT_TIMESTAMP NOT NULL,
	UNIQUE(id)
);

CREATE TABLE courses (
    id TEXT PRIMARY KEY NOT NULL,
    semester_id TEXT NOT NULL,
    name TEXT NOT NULL,
    credit_hours INTEGER NOT NULL,
    grade TEXT NOT NULL,
    FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE CASCADE
);

INSERT INTO semesters (id, name, sgpa) VALUES
('a', 'Fall 2023', 3.81),
('b', 'Spring 2024', 3.83),
('c', 'Fall 2024', 4.00),
('d', 'Spring 2025', 3.61),
('e', 'Test Semester', 4.00);

INSERT INTO courses (id, semester_id, name, credit_hours, grade) VALUES
('aa', 'a', 'Fundamentals of Programming', 4, 'A'),
('ab', 'a', 'Calculus', 3, 'A'),
('ac', 'a', 'Applied Physics', 3, 'A'),
('ad', 'a', 'Discrete Mathematics', 3, 'A'),
('ae', 'a', 'Pakistan Studies', 2, 'C+'),
('af', 'a', 'English', 2, 'A'),
('ag', 'a', 'OHS', 1, 'B+');

INSERT INTO courses (id, semester_id, name, credit_hours, grade) VALUES
('ba', 'b', 'Object Oriented Programming', 4, 'B+'),
('bb', 'b', 'CALD', 4, 'A'),
('bc', 'b', 'LA&ODEs', 3, 'A'),
('bd', 'b', 'Communication Skills', 2, 'A'),
('be', 'b', 'Islamic Studies', 2, 'A'),
('bf', 'b', 'Engineering Drawing', 2, 'B+'),
('bg', 'b', 'Workshop Practice', 1, 'A');

INSERT INTO courses (id, semester_id, name, credit_hours, grade) VALUES
('ca', 'c', 'Data Structures & Algorithms', 4, 'A'),
('cb', 'c', 'Database Systems', 4, 'A'),
('cc', 'c', 'Computer Networks', 4, 'A'),
('cd', 'c', 'Software Engineering', 3, 'A'),
('ce', 'c', 'Probability & Statistics', 3, 'C');

INSERT INTO courses (id, semester_id, name, credit_hours, grade) VALUES
('da', 'd', 'Operating Systems', 4, 'B+'),
('db', 'd', 'Design & Analysis of Algorithms', 3, 'B+'),
('dc', 'd', 'Software Design & Architecture', 3, 'A'),
('dd', 'd', 'Complex Variables & Transforms', 3, 'A'),
('de', 'd', 'Numerical Methods', 3, 'B+'),
('df', 'd', 'Professional Ethics', 2, 'B');

INSERT INTO courses (id, semester_id, name, credit_hours, grade) VALUES
('ea', 'e', 'Test Course', 3, 'A')