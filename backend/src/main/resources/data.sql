-- Seed data for the default H2 profile. Ids are generated in insertion order starting at 1.
-- Tests rely on these rows, so if you change them, check the tests.

INSERT INTO students (first_name, last_name, email, active) VALUES
  ('Tendai',   'Moyo',     'tendai.moyo@smartuni.ac.zw',     TRUE),   -- 1
  ('Rutendo',  'Chikore',  'rutendo.chikore@smartuni.ac.zw', TRUE),   -- 2
  ('Farai',    'Ncube',    'farai.ncube@smartuni.ac.zw',     TRUE),   -- 3
  ('Nyasha',   'Dube',     'nyasha.dube@smartuni.ac.zw',     TRUE),   -- 4  used by batch rollback test
  ('Tafadzwa', 'Sibanda',  'tafadzwa.sibanda@smartuni.ac.zw', TRUE),  -- 5  used by enrollment test
  ('Kudzai',   'Mlambo',   'kudzai.mlambo@smartuni.ac.zw',   TRUE),   -- 6  used by cancellation test
  ('Chipo',    'Banda',    'chipo.banda@smartuni.ac.zw',     TRUE),   -- 7  enrolled in CS201 + MATH110
  ('Tatenda',  'Mutasa',   'tatenda.mutasa@smartuni.ac.zw',  FALSE);  -- 8  inactive

INSERT INTO courses (code, title, capacity) VALUES
  ('CS101',   'Introduction to Programming', 3),    -- 1  FULL (3 active enrollments)
  ('CS201',   'Data Structures',             30),   -- 2
  ('CS301',   'Databases',                   40),   -- 3
  ('MATH110', 'Discrete Mathematics',        60),   -- 4
  ('SE350',   'Software Architecture',       25);   -- 5

INSERT INTO enrollments (student_id, course_id, status, enrolled_at) VALUES
  (1, 1, 'ACTIVE',    TIMESTAMP '2026-08-01 09:00:00'),
  (2, 1, 'ACTIVE',    TIMESTAMP '2026-08-01 09:05:00'),
  (3, 1, 'ACTIVE',    TIMESTAMP '2026-08-01 09:10:00'),
  (1, 2, 'ACTIVE',    TIMESTAMP '2026-08-02 10:00:00'),
  (7, 2, 'ACTIVE',    TIMESTAMP '2026-08-02 10:30:00'),
  (7, 4, 'ACTIVE',    TIMESTAMP '2026-08-03 11:00:00'),
  (7, 5, 'CANCELLED', TIMESTAMP '2026-08-03 11:15:00');

INSERT INTO venues (name, building, room_number, capacity) VALUES
  ('Main Lecture Theatre', 'Engineering', 'E001', 200),  -- 1
  ('Computer Lab 1',       'Engineering', 'E104', 35),   -- 2
  ('Seminar Room',         'Library',     'L210', 20),   -- 3
  ('Science Hall',         'Science',     'S050', 120);  -- 4

INSERT INTO venue_bookings (venue_id, course_id, start_time, end_time, expected_attendance) VALUES
  (1, 4, TIMESTAMP '2030-02-03 08:00:00', TIMESTAMP '2030-02-03 10:00:00', 55),
  (2, 1, TIMESTAMP '2030-02-03 10:00:00', TIMESTAMP '2030-02-03 12:00:00', 3),
  (4, 3, TIMESTAMP '2030-02-04 14:00:00', TIMESTAMP '2030-02-04 16:00:00', 38),
  (3, 5, TIMESTAMP '2030-02-05 09:00:00', TIMESTAMP '2030-02-05 11:00:00', 15);

INSERT INTO announcements (title, content, pinned, created_at) VALUES
  ('Welcome to semester two', 'Registration closes on Friday. Check your enrollments.', TRUE,  TIMESTAMP '2026-08-01 08:00:00'),
  ('Library hours extended',  'The library is open until 22:00 during exam weeks.',     FALSE, TIMESTAMP '2026-08-05 12:00:00'),
  ('Lab maintenance',         'Computer Lab 1 is closed on Saturday for upgrades.',     FALSE, TIMESTAMP '2026-08-07 15:30:00');
