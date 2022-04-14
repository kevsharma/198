# Create the Schema - DDL
DROP DATABASE IF EXISTS Music;
CREATE DATABASE IF NOT EXISTS Music;
USE Music;

create table Artist (
	artist_name varchar(150) PRIMARY KEY
);

# album_id is a positive int that automatically ++s from the last entry when null is specified in insert. Since pk (NOT NULL) by default
create table Album (
	album_id int unsigned AUTO_INCREMENT PRIMARY KEY, 
	album_name varchar(250) NOT NULL,
	artist_name varchar(150) NOT NULL,
	album_release_date date NOT NULL, 
	UNIQUE (album_name, artist_name),
	FOREIGN KEY (artist_name) REFERENCES Artist (artist_name) on delete cascade on update cascade
);

create table Song (
	song_id int unsigned AUTO_INCREMENT PRIMARY KEY,
	title varchar(100) NOT NULL,
	artist_name varchar(150) NOT NULL,
	song_release_date date,
	album_id int unsigned,
	UNIQUE (title, artist_name),
	FOREIGN KEY (artist_name) REFERENCES Artist (artist_name) on delete cascade on update cascade,
	FOREIGN KEY (album_id) REFERENCES Album (album_id) on delete cascade,
    
	# Make sure that if the song isn't in an album, it has song_release_date attribute populated
	CONSTRAINT check_release_date check((album_id is not null and song_release_date is null) or (album_id is null and song_release_date is not null))
);

create table Genre (
	gname varchar(100) PRIMARY KEY
);

# Song ==<in>-- Genre
create table SongInGenre(
	gname varchar(100),
	song_id int unsigned,
	PRIMARY KEY (gname, song_id),
	FOREIGN KEY (song_id) REFERENCES Song (song_id) on delete cascade,
	FOREIGN KEY (gname) REFERENCES Genre (gname) on delete cascade on update cascade
);

# User of music db information (user, playlists, ratings) 
create table User_m (
	username varchar(50) PRIMARY KEY
);

# Playlist has User_m as identifiying owner
create table Playlist (
	playlist_id int unsigned AUTO_INCREMENT PRIMARY KEY,
	playlist_title varchar(250) NOT NULL,
	created_dt datetime NOT NULL,
	username varchar(50) NOT NULL,
	UNIQUE (playlist_title, username),
	FOREIGN KEY (username) REFERENCES User_m (username) on delete cascade on update cascade
);

# Playlist ==<>-- Song
create table has(
	playlist_id int unsigned,
	song_id int unsigned,
	primary key (playlist_id, song_id),
	foreign key (playlist_id) references Playlist (playlist_id) on delete cascade,
	foreign key (song_id) references Song (song_id) on delete cascade
);


# User --<>-- song/album/playlist
create table r_song(
	username varchar(50),
	song_id int unsigned,
	rating smallint NOT NULL check(rating >= 1 and rating <= 5) ,
	rated_on date NOT NULL,
	primary key (username, song_id),
	foreign key (username) references User_m (username) on delete cascade on update cascade,
	foreign key (song_id) references Song (song_id) on delete cascade
);

# User ---<>--- Album
create table r_album(
	username varchar(50),
	album_id int unsigned,
	rating smallint NOT NULL check(rating >= 1 and rating <= 5) ,
	rated_on date NOT NULL,
	primary key (username, album_id),
	foreign key (username) references User_m (username) on delete cascade on update cascade,
	foreign key (album_id) references Album (album_id) on delete cascade
);

# User ---<>--- Playlist
create table r_playlist(
	username varchar(50),
	playlist_id int unsigned,
	rating smallint NOT NULL check(rating >= 1 and rating <= 5),
	rated_on date NOT NULL,
	primary key (username, playlist_id),
	foreign key (username) references User_m (username) on delete cascade on update cascade,
	foreign key (playlist_id) references Playlist (playlist_id) on delete cascade	
);
