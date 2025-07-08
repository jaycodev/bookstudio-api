DROP DATABASE IF EXISTS bookstudio_db;
CREATE DATABASE bookstudio_db;
USE bookstudio_db;

CREATE TABLE Faculties (
    FacultyID BIGINT AUTO_INCREMENT PRIMARY KEY,
    FacultyName VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE LiteraryGenres (
    LiteraryGenreID BIGINT AUTO_INCREMENT PRIMARY KEY,
    GenreName VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE Genres (
    GenreID BIGINT AUTO_INCREMENT PRIMARY KEY,
    GenreName VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE Nationalities (
    NationalityID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NationalityName VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE Authors (
    AuthorID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    NationalityID BIGINT NOT NULL,
    LiteraryGenreID BIGINT NOT NULL,
    BirthDate DATE NOT NULL,
    Biography TEXT,
    Status ENUM('activo', 'inactivo') DEFAULT 'activo',
    Photo LONGBLOB,
    FOREIGN KEY (NationalityID) REFERENCES Nationalities(NationalityID),
    FOREIGN KEY (LiteraryGenreID) REFERENCES LiteraryGenres(LiteraryGenreID)
);

CREATE TABLE Publishers (
    PublisherID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    NationalityID BIGINT NOT NULL,
    LiteraryGenreID BIGINT NOT NULL,
    FoundationYear INT NOT NULL,
    Website VARCHAR(255),
    Address VARCHAR(255),
    Status ENUM('activo', 'inactivo') DEFAULT 'activo',
    Photo LONGBLOB,
    FOREIGN KEY (NationalityID) REFERENCES Nationalities(NationalityID),
    FOREIGN KEY (LiteraryGenreID) REFERENCES LiteraryGenres(LiteraryGenreID)
);

CREATE TABLE Courses (
    CourseID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Level VARCHAR(100) NOT NULL,
    Description TEXT,
    Status ENUM('activo', 'inactivo') DEFAULT 'activo'
);

CREATE TABLE Students (
    StudentID BIGINT AUTO_INCREMENT PRIMARY KEY,
    DNI VARCHAR(8) UNIQUE NOT NULL,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    Address VARCHAR(255) NOT NULL,
    Phone VARCHAR(9) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    BirthDate DATE NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    FacultyID BIGINT NOT NULL,
    Status ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (FacultyID) REFERENCES Faculties(FacultyID)
);

CREATE TABLE Books (
    BookID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR(255) NOT NULL,
    TotalCopies INT NOT NULL,
    LoanedCopies INT NOT NULL,
    AuthorID BIGINT NOT NULL,
    PublisherID BIGINT NOT NULL,
    CourseID BIGINT NOT NULL,
    ReleaseDate DATE NOT NULL,
    GenreID BIGINT NOT NULL,
    Status ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (AuthorID) REFERENCES Authors(AuthorID),
    FOREIGN KEY (PublisherID) REFERENCES Publishers(PublisherID),
    FOREIGN KEY (CourseID) REFERENCES Courses(CourseID),
    FOREIGN KEY (GenreID) REFERENCES Genres(GenreID)
);

CREATE TABLE Loans (
    LoanID BIGINT AUTO_INCREMENT PRIMARY KEY,
    BookID BIGINT NOT NULL,
    StudentID BIGINT NOT NULL,
    LoanDate DATE NOT NULL,
    ReturnDate DATE NOT NULL,
    Quantity INT NOT NULL,
    Status ENUM('prestado', 'devuelto') DEFAULT 'prestado',
    Observation TEXT,
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID)
);

CREATE TABLE Users (
    UserID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin UNIQUE NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    Password VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    Role ENUM('administrador', 'bibliotecario') NOT NULL,
    ProfilePhoto LONGBLOB
);

CREATE TABLE PasswordResetTokens (
    TokenID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Email VARCHAR(100) NOT NULL,
    Token VARCHAR(255) UNIQUE NOT NULL,
    ExpiryTime DATETIME NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Used BOOLEAN DEFAULT FALSE,
    CONSTRAINT FK_Email FOREIGN KEY (Email) REFERENCES Users(Email) ON DELETE CASCADE
);