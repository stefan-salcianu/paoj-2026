-- Ordine DROP: intai tabelele care au FK, apoi cele referite
DROP TABLE IF EXISTS loan;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS reader;
DROP TABLE IF EXISTS author;

CREATE TABLE author (
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    name    TEXT    NOT NULL,
    country TEXT
);

CREATE TABLE book (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    title     TEXT    NOT NULL,
    author_id INTEGER NOT NULL,
    available INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (author_id) REFERENCES author(id)
);

CREATE TABLE reader (
    id    INTEGER PRIMARY KEY AUTOINCREMENT,
    name  TEXT NOT NULL,
    email TEXT
);

CREATE TABLE loan (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id     INTEGER NOT NULL,
    reader_id   INTEGER NOT NULL,
    loan_date   TEXT    NOT NULL,
    return_date TEXT,
    FOREIGN KEY (book_id)   REFERENCES book(id),
    FOREIGN KEY (reader_id) REFERENCES reader(id)
);
