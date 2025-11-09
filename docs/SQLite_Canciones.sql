-- Base de datos para Reproductor de Canciones - SQLite
-- Creación de tablas
PRAGMA foreign_keys = ON;

-- Tabla canciones
CREATE TABLE canciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    artista TEXT NOT NULL,
    album TEXT,
    duracion INTEGER NOT NULL, -- duración en segundos
    ruta_archivo TEXT
);

-- Insertar datos de ejemplo
INSERT INTO canciones (titulo, artista, album, duracion, ruta_archivo) VALUES
('Bohemian Rhapsody', 'Queen', 'A Night at the Opera', 354, '/music/queen/bohemian_rhapsody.mp3'),
('Stairway to Heaven', 'Led Zeppelin', 'Led Zeppelin IV', 482, '/music/led_zeppelin/stairway_to_heaven.mp3'),
('Hotel California', 'Eagles', 'Hotel California', 391, '/music/eagles/hotel_california.mp3'),
('Imagine', 'John Lennon', 'Imagine', 183, '/music/john_lennon/imagine.mp3'),
('Sweet Child O Mine', 'Guns N Roses', 'Appetite for Destruction', 356, '/music/guns_n_roses/sweet_child.mp3'),
('Smells Like Teen Spirit', 'Nirvana', 'Nevermind', 301, '/music/nirvana/smells_like_teen_spirit.mp3'),
('Billie Jean', 'Michael Jackson', 'Thriller', 294, '/music/michael_jackson/billie_jean.mp3'),
('Hey Jude', 'The Beatles', 'Hey Jude', 431, '/music/beatles/hey_jude.mp3'),
('Wonderwall', 'Oasis', 'Whats the Story Morning Glory', 258, '/music/oasis/wonderwall.mp3'),
('November Rain', 'Guns N Roses', 'Use Your Illusion I', 537, '/music/guns_n_roses/november_rain.mp3');
