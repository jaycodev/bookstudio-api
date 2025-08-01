SET search_path TO public;

BEGIN;

-- 1. NACIONALIDADES
INSERT INTO nationalities (name) VALUES 
('Peruana'),
('Española'),
('Argentina'),
('Mexicana'),
('Colombiana'),
('Chilena'),
('Estadounidense'),
('Británica'),
('Francesa'),
('Alemana'),
('Italiana'),
('Brasileña'),
('Japonesa'),
('Rusa');

-- 2. GÉNEROS LITERARIOS
INSERT INTO genres (name) VALUES 
('Novela'),
('Cuento'),
('Poesía'),
('Drama'),
('Ensayo'),
('Biografía'),
('Historia'),
('Ciencia Ficción'),
('Fantasía'),
('Misterio'),
('Romance'),
('Aventura'),
('Terror'),
('Autoayuda'),
('Técnico'),
('Educativo'),
('Infantil'),
('Juvenil');

-- 3. IDIOMAS
INSERT INTO languages (name, code) VALUES 
('Español', 'es'),
('Inglés', 'en'),
('Francés', 'fr'),
('Alemán', 'de'),
('Italiano', 'it'),
('Portugués', 'pt'),
('Japonés', 'ja'),
('Ruso', 'ru');

-- 4. CATEGORÍAS
INSERT INTO categories (name, level, description, status) VALUES 
('Literatura Clásica', 'superior', 'Obras literarias de reconocido valor universal', 'activo'),
('Literatura Contemporánea', 'superior', 'Obras literarias modernas y actuales', 'activo'),
('Historia del Perú', 'secundaria', 'Libros sobre la historia nacional', 'activo'),
('Historia Universal', 'secundaria', 'Historia mundial y civilizaciones', 'activo'),
('Matemáticas', 'secundaria', 'Libros de matemáticas nivel secundario', 'activo'),
('Física', 'secundaria', 'Textos de física para secundaria', 'activo'),
('Química', 'secundaria', 'Libros de química nivel secundario', 'activo'),
('Biología', 'secundaria', 'Textos de biología y ciencias naturales', 'activo'),
('Cuentos Infantiles', 'primaria', 'Libros para niños de educación primaria', 'activo'),
('Enciclopedias', 'general', 'Obras de consulta general', 'activo'),
('Filosofía', 'superior', 'Textos filosóficos y pensamiento', 'activo'),
('Psicología', 'superior', 'Libros de psicología y comportamiento humano', 'activo'),
('Ingeniería', 'superior', 'Textos técnicos de ingeniería', 'activo'),
('Medicina', 'superior', 'Libros de medicina y salud', 'activo'),
('Derecho', 'superior', 'Textos jurídicos y legales', 'activo');

-- 5. AUTORES
INSERT INTO authors (name, nationality_id, birth_date, biography, status, photo_url) VALUES 
('Mario Vargas Llosa', 1, '1936-03-28', 'Premio Nobel de Literatura 2010, escritor peruano considerado uno de los más importantes novelistas contemporáneos', 'activo', NULL),
('Julio Ramón Ribeyro', 1, '1929-08-31', 'Escritor peruano, maestro del cuento latinoamericano', 'activo', NULL),
('César Vallejo', 1, '1892-03-16', 'Poeta peruano, considerado uno de los mayores innovadores de la poesía del siglo XX', 'activo', NULL),
('José María Arguedas', 1, '1911-01-18', 'Escritor y antropólogo peruano, gran conocedor de la cultura andina', 'activo', NULL),
('Gabriel García Márquez', 5, '1927-03-06', 'Escritor colombiano, Premio Nobel de Literatura 1982, máximo exponente del realismo mágico', 'activo', NULL),
('Jorge Luis Borges', 3, '1899-08-24', 'Escritor argentino, maestro del cuento fantástico y la literatura universal', 'activo', NULL),
('Isabel Allende', 6, '1942-08-02', 'Escritora chilena, una de las novelistas más leídas del mundo', 'activo', NULL),
('Octavio Paz', 4, '1914-03-31', 'Poeta y ensayista mexicano, Premio Nobel de Literatura 1990', 'activo', NULL),
('Miguel de Cervantes', 2, '1547-09-29', 'Escritor español, autor de Don Quijote de la Mancha', 'activo', NULL),
('Federico García Lorca', 2, '1898-06-05', 'Poeta y dramaturgo español de la Generación del 27', 'activo', NULL),
('William Shakespeare', 8, '1564-04-26', 'Dramaturgo y poeta inglés, considerado el escritor más importante en lengua inglesa', 'activo', NULL),
('Ernest Hemingway', 7, '1899-07-21', 'Escritor estadounidense, Premio Nobel de Literatura 1954', 'activo', NULL),
('Stephen King', 7, '1947-09-21', 'Escritor estadounidense, maestro del terror y suspense', 'activo', NULL),
('J.K. Rowling', 8, '1965-07-31', 'Escritora británica, autora de la saga Harry Potter', 'activo', NULL),
('Agatha Christie', 8, '1890-09-15', 'Escritora británica, reina del misterio y la novela policíaca', 'activo', NULL);

-- 6. EDITORIALES
INSERT INTO publishers (name, nationality_id, foundation_year, website, address, status, photo_url) VALUES 
('Planeta', 2, 1949, 'www.planeta.es', 'Av. Diagonal 662-664, Barcelona, España', 'activo', NULL),
('Alfaguara', 2, 1964, 'www.alfaguara.com', 'Calle Torrelaguna 60, Madrid, España', 'activo', NULL),
('Penguin Random House', 7, 1927, 'www.penguinrandomhouse.com', 'New York, Estados Unidos', 'activo', NULL),
('Santillana', 2, 1960, 'www.santillana.com', 'Madrid, España', 'activo', NULL),
('Anagrama', 2, 1969, 'www.anagrama-ed.es', 'Barcelona, España', 'activo', NULL),
('Fondo de Cultura Económica', 4, 1934, 'www.fondodeculturaeconomica.com', 'Ciudad de México, México', 'activo', NULL),
('Seix Barral', 2, 1911, 'www.seixbarral.es', 'Barcelona, España', 'activo', NULL),
('Cátedra', 2, 1973, 'www.catedra.com', 'Madrid, España', 'activo', NULL),
('Tusquets', 2, 1969, 'www.tusquetseditores.com', 'Barcelona, España', 'activo', NULL),
('Peisa', 1, 1962, 'www.peisa.com.pe', 'Lima, Perú', 'activo', NULL);

-- 7. ROLES
INSERT INTO roles (name, description) VALUES 
('Administrador', 'Acceso completo al sistema, gestión de usuarios y configuración'),
('Bibliotecario', 'Gestión de préstamos, devoluciones, multas y catalogación de libros'),
('Asistente', 'Apoyo en tareas básicas de biblioteca y atención al público');

-- 8. PERMISOS
INSERT INTO permissions (code, description) VALUES 
('ADMIN_FULL', 'Acceso completo de administrador'),
('USER_CREATE', 'Crear usuarios'),
('USER_EDIT', 'Editar usuarios'),
('USER_DELETE', 'Eliminar usuarios'),
('BOOK_CREATE', 'Agregar libros'),
('BOOK_EDIT', 'Editar libros'),
('BOOK_DELETE', 'Eliminar libros'),
('LOAN_CREATE', 'Crear préstamos'),
('LOAN_EDIT', 'Editar préstamos'),
('LOAN_RETURN', 'Procesar devoluciones'),
('FINE_CREATE', 'Crear multas'),
('FINE_EDIT', 'Editar multas'),
('FINE_PAYMENT', 'Procesar pagos de multas'),
('REPORT_VIEW', 'Ver reportes'),
('CATALOG_MANAGE', 'Gestionar catálogo'),
('RESERVATION_MANAGE', 'Gestionar reservas');

-- 9. ASIGNACIÓN DE PERMISOS A ROLES
INSERT INTO role_permissions (role_id, permission_id) VALUES 
-- Administrador (todos los permisos)
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), 
(1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16),
-- Bibliotecario (permisos operativos)
(2, 5), (2, 6), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12), (2, 13), (2, 14), (2, 15), (2, 16),
-- Asistente (permisos básicos)
(3, 8), (3, 10), (3, 14), (3, 16);

-- 10. TRABAJADORES
INSERT INTO workers (username, email, first_name, last_name, password, role_id, status) VALUES 
('admin', 'admin@biblioteca.edu.pe', 'Carlos', 'Mendoza García', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewfzUf0wdG8OuJdm', 1, 'activo'), -- password: admin123
('bibliotecario1', 'ana.torres@biblioteca.edu.pe', 'Ana María', 'Torres Vásquez', '$2b$12$EixZxQz5UbrYdHqgqHl/JO4nrpxY/PnZsT0YnUGf3rGNrGNrGNrGN', 2, 'activo'), -- password: biblio123
('bibliotecario2', 'jose.ruiz@biblioteca.edu.pe', 'José Luis', 'Ruiz Morales', '$2b$12$HQqO8vTYmlnKEuTQSrToLeyH1aFPv2OfPzTgNfN.kL9vLkNrGNrGN', 2, 'activo'), -- password: biblio123
('asistente1', 'maria.lopez@biblioteca.edu.pe', 'María Elena', 'López Fernández', '$2b$12$QrNfmP5tTYoFEu5QNrToLeyH1aFPv2OfPzTgNfN.kL9vLkNrGNrGN', 3, 'activo'); -- password: asist123

-- 11. UBICACIONES
INSERT INTO locations (name, description) VALUES 
('Planta Baja - Sala General', 'Sala principal con literatura general y consulta'),
('Primer Piso - Sala de Estudio', 'Área silenciosa para estudio individual'),
('Primer Piso - Hemeroteca', 'Sección de revistas y periódicos'),
('Segundo Piso - Sala Infantil', 'Área destinada a niños y literatura infantil'),
('Segundo Piso - Sala de Referencia', 'Enciclopedias y obras de consulta'),
('Depósito', 'Almacén de libros no disponibles para préstamo');

-- 12. ESTANTES
INSERT INTO shelves (location_id, code, floor, description) VALUES 
(1, 'A01', 'Planta Baja', 'Literatura peruana'),
(1, 'A02', 'Planta Baja', 'Literatura latinoamericana'),
(1, 'A03', 'Planta Baja', 'Literatura española'),
(1, 'A04', 'Planta Baja', 'Literatura universal'),
(1, 'B01', 'Planta Baja', 'Historia y biografías'),
(1, 'B02', 'Planta Baja', 'Ciencias sociales'),
(2, 'C01', 'Primer Piso', 'Matemáticas y física'),
(2, 'C02', 'Primer Piso', 'Química y biología'),
(2, 'C03', 'Primer Piso', 'Ingeniería y tecnología'),
(4, 'D01', 'Segundo Piso', 'Cuentos infantiles'),
(4, 'D02', 'Segundo Piso', 'Literatura juvenil'),
(5, 'E01', 'Segundo Piso', 'Enciclopedias'),
(5, 'E02', 'Segundo Piso', 'Diccionarios y atlas');

-- 13. LIBROS
INSERT INTO books (title, isbn, language_id, edition, pages, description, publisher_id, category_id, release_date, status) VALUES 
('La ciudad y los perros', '978-84-204-8304-7', 1, '1ª edición', 408, 'Primera novela de Mario Vargas Llosa, ambientada en el Colegio Militar Leoncio Prado', 2, 1, '1963-10-15', 'activo'),
('Cien años de soledad', '978-84-376-0494-7', 1, '2ª edición', 432, 'Obra maestra del realismo mágico de Gabriel García Márquez', 2, 1, '1967-05-30', 'activo'),
('Los ríos profundos', '978-84-376-0495-4', 1, '3ª edición', 264, 'Novela de José María Arguedas sobre la cultura andina', 7, 1, '1958-11-20', 'activo'),
('La palabra del mudo', '978-84-204-8305-4', 1, '1ª edición', 520, 'Cuentos completos de Julio Ramón Ribeyro', 2, 2, '1973-08-15', 'activo'),
('Trilce', '978-84-376-0496-1', 1, '4ª edición', 180, 'Poemario revolucionario de César Vallejo', 8, 3, '1922-10-15', 'activo'),
('Don Quijote de la Mancha', '978-84-376-0497-8', 1, '5ª edición', 1200, 'Obra cumbre de la literatura española de Miguel de Cervantes', 8, 1, '1605-01-16', 'activo'),
('Romeo y Julieta', '978-84-376-0498-5', 1, '2ª edición', 280, 'Tragedia romántica de William Shakespeare', 8, 4, '1597-03-20', 'activo'),
('El viejo y el mar', '978-84-376-0499-2', 1, '3ª edición', 128, 'Novela corta de Ernest Hemingway, Premio Pulitzer', 2, 1, '1952-09-01', 'activo'),
('Harry Potter y la piedra filosofal', '978-84-376-0500-5', 1, '1ª edición', 320, 'Primera novela de la saga de J.K. Rowling', 4, 9, '1997-06-26', 'activo'),
('Asesinato en el Orient Express', '978-84-376-0501-2', 1, '2ª edición', 256, 'Novela de misterio de Agatha Christie con Hércules Poirot', 1, 10, '1934-01-01', 'activo'),
('Historia del Perú Contemporáneo', '978-84-376-0502-9', 1, '1ª edición', 480, 'Historia del Perú desde la independencia hasta la actualidad', 10, 3, '2020-03-15', 'activo'),
('Matemáticas 4to Secundaria', '978-84-376-0503-6', 1, '2ª edición', 360, 'Texto escolar de matemáticas para cuarto año de secundaria', 4, 5, '2023-02-01', 'activo'),
('Física General', '978-84-376-0504-3', 1, '3ª edición', 420, 'Principios fundamentales de física para educación secundaria', 4, 6, '2022-08-20', 'activo'),
('El Principito', '978-84-376-0505-0', 1, '10ª edición', 96, 'Clásico de la literatura infantil y juvenil', 4, 9, '1943-04-06', 'activo'),
('Enciclopedia Juvenil Oceano', '978-84-376-0506-7', 1, '1ª edición', 800, 'Enciclopedia temática para jóvenes estudiantes', 4, 10, '2021-01-15', 'activo');

-- 14. RELACIÓN LIBROS-AUTORES
INSERT INTO book_authors (book_id, author_id) VALUES 
(1, 1),  -- La ciudad y los perros - Mario Vargas Llosa
(2, 5),  -- Cien años de soledad - Gabriel García Márquez
(3, 4),  -- Los ríos profundos - José María Arguedas
(4, 2),  -- La palabra del mudo - Julio Ramón Ribeyro
(5, 3),  -- Trilce - César Vallejo
(6, 9),  -- Don Quijote - Miguel de Cervantes
(7, 11), -- Romeo y Julieta - William Shakespeare
(8, 12), -- El viejo y el mar - Ernest Hemingway
(9, 14), -- Harry Potter - J.K. Rowling
(10, 15); -- Asesinato en el Orient Express - Agatha Christie

-- 15. RELACIÓN LIBROS-GÉNEROS
INSERT INTO book_genres (book_id, genre_id) VALUES 
(1, 1),   -- La ciudad y los perros - Novela
(2, 1), (2, 8),  -- Cien años de soledad - Novela, Fantasía
(3, 1),   -- Los ríos profundos - Novela
(4, 2),   -- La palabra del mudo - Cuento
(5, 3),   -- Trilce - Poesía
(6, 1), (6, 12),  -- Don Quijote - Novela, Aventura
(7, 4), (7, 11),  -- Romeo y Julieta - Drama, Romance
(8, 1),   -- El viejo y el mar - Novela
(9, 9), (9, 18),  -- Harry Potter - Fantasía, Juvenil
(10, 10), -- Asesinato en el Orient Express - Misterio
(11, 7),  -- Historia del Perú - Historia
(12, 16), -- Matemáticas - Educativo
(13, 16), -- Física - Educativo
(14, 17), -- El Principito - Infantil
(15, 16); -- Enciclopedia - Educativo

-- 16. EJEMPLARES (COPIAS)
INSERT INTO copies (book_id, shelf_id, barcode, is_available, condition) VALUES 
-- La ciudad y los perros (3 copias)
(1, 1, '100001001', TRUE, 'bueno'),
(1, 1, '100001002', TRUE, 'bueno'),
(1, 1, '100001003', FALSE, 'regular'),
-- Cien años de soledad (2 copias)
(2, 2, '100002001', TRUE, 'bueno'),
(2, 2, '100002002', TRUE, 'nuevo'),
-- Los ríos profundos (2 copias)
(3, 1, '100003001', TRUE, 'bueno'),
(3, 1, '100003002', FALSE, 'bueno'),
-- La palabra del mudo (1 copia)
(4, 1, '100004001', TRUE, 'regular'),
-- Trilce (1 copia)
(5, 1, '100005001', TRUE, 'bueno'),
-- Don Quijote (3 copias)
(6, 3, '100006001', TRUE, 'bueno'),
(6, 3, '100006002', TRUE, 'regular'),
(6, 3, '100006003', FALSE, 'bueno'),
-- Romeo y Julieta (2 copias)
(7, 4, '100007001', TRUE, 'bueno'),
(7, 4, '100007002', TRUE, 'nuevo'),
-- El viejo y el mar (2 copias)
(8, 4, '100008001', TRUE, 'bueno'),
(8, 4, '100008002', FALSE, 'bueno'),
-- Harry Potter (4 copias)
(9, 11, '100009001', TRUE, 'nuevo'),
(9, 11, '100009002', TRUE, 'bueno'),
(9, 11, '100009003', FALSE, 'bueno'),
(9, 11, '100009004', TRUE, 'bueno'),
-- Asesinato en el Orient Express (1 copia)
(10, 4, '100010001', TRUE, 'bueno'),
-- Historia del Perú (2 copias)
(11, 5, '100011001', TRUE, 'nuevo'),
(11, 5, '100011002', TRUE, 'bueno'),
-- Matemáticas (3 copias)
(12, 7, '100012001', TRUE, 'bueno'),
(12, 7, '100012002', FALSE, 'bueno'),
(12, 7, '100012003', TRUE, 'regular'),
-- Física (2 copias)
(13, 7, '100013001', TRUE, 'bueno'),
(13, 7, '100013002', TRUE, 'nuevo'),
-- El Principito (3 copias)
(14, 10, '100014001', TRUE, 'bueno'),
(14, 10, '100014002', TRUE, 'bueno'),
(14, 10, '100014003', FALSE, 'regular'),
-- Enciclopedia (1 copia)
(15, 12, '100015001', TRUE, 'nuevo');

-- 17. LECTORES
INSERT INTO readers (dni, first_name, last_name, address, phone, email, birth_date, gender, type, status) VALUES 
('12345678', 'Ana María', 'García López', 'Av. Universitaria 1234, Lima', '987654321', 'ana.garcia@email.com', '1998-05-15', 'femenino', 'estudiante', 'activo'),
('23456789', 'Carlos Andrés', 'Mendoza Silva', 'Jr. Los Olivos 567, Lima', '976543210', 'carlos.mendoza@email.com', '1997-08-22', 'masculino', 'estudiante', 'activo'),
('34567890', 'María Elena', 'Rodríguez Torres', 'Av. Arequipa 890, Lima', '965432109', 'maria.rodriguez@email.com', '1985-03-10', 'femenino', 'docente', 'activo'),
('45678901', 'José Luis', 'Fernández Huamán', 'Calle Las Flores 123, Lima', '954321098', 'jose.fernandez@email.com', '1990-12-03', 'masculino', 'administrativo', 'activo'),
('56789012', 'Patricia Rosa', 'Vargas Chávez', 'Av. Brasil 456, Lima', '943210987', 'patricia.vargas@email.com', '1999-07-18', 'femenino', 'estudiante', 'activo'),
('67890123', 'Roberto Miguel', 'Castro Quispe', 'Jr. Junín 789, Lima', '932109876', 'roberto.castro@email.com', '2000-01-25', 'masculino', 'estudiante', 'activo'),
('78901234', 'Carmen Luz', 'Herrera Morales', 'Av. Colonial 321, Lima', '921098765', 'carmen.herrera@email.com', '1988-11-14', 'femenino', 'docente', 'activo'),
('89012345', 'Manuel Antonio', 'Sánchez Vega', 'Calle San Martín 654, Lima', '910987654', 'manuel.sanchez@email.com', '1995-04-07', 'masculino', 'externo', 'activo'),
('90123456', 'Lucía Isabel', 'Paredes Gutiérrez', 'Av. Tacna 987, Lima', '901876543', 'lucia.paredes@email.com', '2001-09-30', 'femenino', 'estudiante', 'activo'),
('01234567', 'Diego Alejandro', 'Moreno Ramos', 'Jr. Huancayo 147, Lima', '890765432', 'diego.moreno@email.com', '1996-06-12', 'masculino', 'estudiante', 'activo');

-- 18. PRÉSTAMOS
INSERT INTO loans (reader_id, loan_date, observation) VALUES 
(1, '2024-07-20', 'Préstamo regular'),
(2, '2024-07-21', 'Estudiante de literatura'),
(3, '2024-07-22', 'Material para clase'),
(4, '2024-07-23', 'Préstamo administrativo'),
(5, '2024-07-24', 'Préstamo regular'),
(1, '2024-07-25', 'Segundo préstamo del mes'),
(6, '2024-07-26', 'Nuevo usuario');

-- 19. ITEMS DE PRÉSTAMO
INSERT INTO loan_items (loan_id, copy_id, due_date, return_date, status) VALUES 
-- Préstamo 1 (Ana García) - Devuelto
(1, 3, '2024-08-03', '2024-08-01', 'devuelto'),
(1, 7, '2024-08-03', '2024-08-01', 'devuelto'),
-- Préstamo 2 (Carlos Mendoza) - Activo
(2, 13, '2024-08-04', NULL, 'prestado'),
(2, 15, '2024-08-04', NULL, 'prestado'),
-- Préstamo 3 (María Rodríguez) - Retrasado
(3, 12, '2024-08-05', NULL, 'retrasado'),
-- Préstamo 4 (José Fernández) - Devuelto
(4, 2, '2024-08-06', '2024-08-04', 'devuelto'),
-- Préstamo 5 (Patricia Vargas) - Activo
(5, 17, '2024-08-07', NULL, 'prestado'),
(5, 23, '2024-08-07', NULL, 'prestado'),
-- Préstamo 6 (Ana García - segundo) - Activo
(6, 8, '2024-08-08', NULL, 'prestado'),
-- Préstamo 7 (Roberto Castro) - Activo
(7, 19, '2024-08-09', NULL, 'prestado');

-- 20. RESERVAS
INSERT INTO reservations (reader_id, copy_id, reservation_date, status) VALUES 
(8, 3, '2024-07-28', 'pendiente'),
(9, 13, '2024-07-28', 'pendiente'),
(10, 8, '2024-07-27', 'pendiente');

-- 21. MULTAS
INSERT INTO fines (loan_id, copy_id, amount, days_late, status) VALUES 
-- Multa por retraso en devolución
(3, 12, 5.00, 2, 'pendiente'); -- María Rodríguez debe 5 soles por 2 días de retraso

-- 22. PAGOS
INSERT INTO payments (reader_id, amount, payment_date, method) VALUES 
(1, 10.00, '2024-07-15', 'efectivo'); -- Ana García pagó multas anteriores

-- 23. RELACIÓN PAGOS-MULTAS (ejemplo de pago anterior)
INSERT INTO payment_fines (payment_id, fine_id) VALUES (1, 1);

COMMIT;