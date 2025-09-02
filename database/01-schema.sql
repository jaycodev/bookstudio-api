SET search_path TO public;

BEGIN;

CREATE TABLE nationalities (
    nationality_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    code VARCHAR(2) UNIQUE NOT NULL
);

CREATE TABLE genres (
    genre_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE categories (
    category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    level VARCHAR(100) CHECK (level IN ('primaria', 'secundaria', 'superior', 'general')),
    description TEXT,
    status VARCHAR(10) DEFAULT 'activo' CHECK (status IN ('activo', 'inactivo'))
);

CREATE TABLE languages (
    language_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    code VARCHAR(2) UNIQUE NOT NULL
);

CREATE TABLE authors (
    author_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    nationality_id BIGINT NOT NULL,
    birth_date DATE NOT NULL CHECK (birth_date >= '1000-01-01' AND birth_date <= CURRENT_DATE),
    biography TEXT,
    status VARCHAR(10) DEFAULT 'activo' CHECK (status IN ('activo', 'inactivo')),
    photo_url VARCHAR(512),
    FOREIGN KEY (nationality_id) REFERENCES nationalities(nationality_id)
);

CREATE TABLE publishers (
    publisher_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    nationality_id BIGINT NOT NULL,
    foundation_year INT NOT NULL CHECK (foundation_year >= 1400 AND foundation_year <= DATE_PART('year', CURRENT_DATE)),
    website VARCHAR(255),
    address VARCHAR(255),
    status VARCHAR(10) DEFAULT 'activo' CHECK (status IN ('activo', 'inactivo')),
    photo_url VARCHAR(512),
    FOREIGN KEY (nationality_id) REFERENCES nationalities(nationality_id)
);

CREATE TABLE readers (
    reader_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	code VARCHAR(50) UNIQUE,
    dni VARCHAR(8) UNIQUE NOT NULL CHECK (dni ~ '^[0-9]{8}$'),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(9) NOT NULL CHECK (phone ~ '^[0-9]{9}$'),
    email VARCHAR(100) UNIQUE NOT NULL,
    birth_date DATE NOT NULL CHECK (birth_date >= '1900-01-01' AND birth_date <= CURRENT_DATE),
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('masculino', 'femenino')),
    type VARCHAR(20) NOT NULL CHECK (type IN ('estudiante', 'docente', 'administrativo', 'externo')),
    status VARCHAR(15) DEFAULT 'activo' CHECK (status IN ('activo', 'suspendido', 'eliminado'))
);

CREATE TABLE books (
    book_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    language_id BIGINT NOT NULL,
    edition VARCHAR(50),
    pages INT CHECK (pages > 0),
    description TEXT,
    cover_url VARCHAR(300),
    publisher_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    release_date DATE NOT NULL CHECK (release_date <= CURRENT_DATE),
    status VARCHAR(10) DEFAULT 'activo' CHECK (status IN ('activo', 'inactivo')),
    FOREIGN KEY (language_id) REFERENCES languages(language_id),
    FOREIGN KEY (publisher_id) REFERENCES publishers(publisher_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE book_authors (
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE
);

CREATE TABLE book_genres (
    book_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, genre_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);

CREATE TABLE publisher_genres (
    publisher_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (publisher_id, genre_id),
    FOREIGN KEY (publisher_id) REFERENCES publishers(publisher_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);

CREATE TABLE locations (
    location_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE shelves (
    shelf_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    location_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    floor VARCHAR(50),
    description TEXT,
    UNIQUE (location_id, code),
    FOREIGN KEY (location_id) REFERENCES locations(location_id) ON DELETE CASCADE
);

CREATE TABLE copies (
    copy_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	code VARCHAR(50) UNIQUE,
    book_id BIGINT NOT NULL,
    shelf_id BIGINT NOT NULL,
    barcode VARCHAR(50) UNIQUE NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    condition VARCHAR(50) DEFAULT 'bueno' CHECK (condition IN ('nuevo', 'bueno', 'regular', 'malo', 'deteriorado')),
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (shelf_id) REFERENCES shelves(shelf_id)
);

CREATE TABLE loans (
    loan_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	code VARCHAR(50) UNIQUE,
    reader_id BIGINT NOT NULL,
    loan_date DATE NOT NULL,
    observation TEXT,
    FOREIGN KEY (reader_id) REFERENCES readers(reader_id)
);

CREATE TABLE loan_items (
    loan_id BIGINT NOT NULL,
    copy_id BIGINT NOT NULL,
    due_date DATE,
    return_date DATE,
    status VARCHAR(15) DEFAULT 'prestado',
    CHECK (status IN ('prestado', 'devuelto', 'retrasado', 'extraviado', 'cancelado')),
    PRIMARY KEY (loan_id, copy_id),
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id) ON DELETE CASCADE,
    FOREIGN KEY (copy_id) REFERENCES copies(copy_id)
);

CREATE TABLE roles (
    role_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE permissions (
    permission_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);

CREATE TABLE workers (
    worker_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    profile_photo_url VARCHAR(512),
    status VARCHAR(15) DEFAULT 'activo' CHECK (status IN ('activo', 'suspendido', 'eliminado')),
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE reservations (
    reservation_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	code VARCHAR(50) UNIQUE,
    reader_id BIGINT NOT NULL,
    copy_id BIGINT NOT NULL,
    reservation_date DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(20) DEFAULT 'pendiente' CHECK (status IN ('pendiente', 'cancelada', 'atendida')),
    FOREIGN KEY (reader_id) REFERENCES readers(reader_id),
    FOREIGN KEY (copy_id) REFERENCES copies(copy_id)
);

CREATE TABLE fines (
    fine_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	code VARCHAR(50) UNIQUE,
    loan_id BIGINT NOT NULL,
    copy_id BIGINT NOT NULL,
    amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    days_late INT NOT NULL CHECK (days_late >= 1),
    issued_at DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(20) DEFAULT 'pendiente' CHECK (status IN ('pendiente', 'pagado')),
    FOREIGN KEY (loan_id, copy_id) REFERENCES loan_items(loan_id, copy_id) ON DELETE CASCADE
);

CREATE TABLE payments (
    payment_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	code VARCHAR(50) UNIQUE,
    reader_id BIGINT NOT NULL,
    amount NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    payment_date DATE NOT NULL DEFAULT CURRENT_DATE,
    method VARCHAR(50) NOT NULL CHECK (method IN ('efectivo', 'tarjeta', 'transferencia', 'cheque', 'otros')),
    FOREIGN KEY (reader_id) REFERENCES readers(reader_id)
);

CREATE TABLE payment_fines (
    payment_id BIGINT NOT NULL,
    fine_id BIGINT NOT NULL,
    PRIMARY KEY (payment_id, fine_id),
    FOREIGN KEY (payment_id) REFERENCES payments(payment_id) ON DELETE CASCADE,
    FOREIGN KEY (fine_id) REFERENCES fines(fine_id) ON DELETE CASCADE
);

COMMIT;