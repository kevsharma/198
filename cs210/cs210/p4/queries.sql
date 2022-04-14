# 1. Which 3 genres are most represented in terms of number of songs in that genre?
# The result must have two columns, named genre and number_of_songs.
SELECT SG.gname as genre, count(*) as number_of_songs
FROM SongInGenre SG
GROUP BY SG.gname
ORDER BY number_of_songs DESC
LIMIT 3;

#=============================================================================================================
#=============================================================================================================


# 2. Find names of artists who have songs that are in albums as well as outside of albums (singles).
# The result must have one column, named artist_name
SELECT DISTINCT artist_name
FROM Song
WHERE song_release_date IS NOT NULL # confirmed as Single
	AND artist_name IN (
		# artist_name of Songs that are in Album
		SELECT S.artist_name
		FROM Song S, Album A
		WHERE S.album_id is not null AND S.album_id = A.album_id
);


#=============================================================================================================
#=============================================================================================================


# 3 What were the top 10 most highly rated albums (highest average user rating) in the period 1990 - 1999?
# Break ties using alphabetical order of album names
# The result must have two columns, named album_name anad average_user_rating
SELECT A.album_name, AVG(R.rating) as average_user_rating
FROM Album A JOIN r_album R on A.album_id = R.album_id # if an album has no ratings, it is not included
WHERE YEAR(R.rated_on) between 1990 AND 1999 
GROUP BY A.album_name, A.artist_name # same album by two artists should not be paired
ORDER BY 
	average_user_rating DESC,
    A.album_name ASC
LIMIT 10;


#=============================================================================================================
#=============================================================================================================


# 4 Which were the top 3 most rated genres (this is the number of ratings of songs in genres, not the actual rating scores) in the years 1991-1995? 
# (Years refers to rating date, NOT date of release)
# The result must have two columns, named genre_name and number_of_song_ratings.
SELECT SG.gname as genre_name, count(*) as number_of_song_ratings
FROM r_song R, SongInGenre SG
WHERE R.song_id = SG.song_id AND YEAR(R.rated_on) between 1991 AND 1995
GROUP BY SG.gname
LIMIT 3;


#=============================================================================================================
#=============================================================================================================


# 5 Which users have a playlist that has an average song rating of 4.0 or more? 
# (This is the average of the average song rating for each song in the playlist.) 
# A user may appear multiple times in the result if more than one of their playlists make the cut.
# The result must 3 columns named username, playlist_title, average_song_rating

# T1 - first part: find the avg rating for each song
# T2 - second part: get those playlists with avg of those avg ratings > 4.0
SELECT P.username, P.playlist_title, avg(T1.avg_rating_of_song) as average_song_rating
FROM Playlist P, has H, (
		SELECT song_id, avg(rating) as avg_rating_of_song
		FROM r_song
		GROUP BY song_id
	) as T1
WHERE P.playlist_id = H.playlist_id AND H.song_id = T1.song_id
GROUP BY H.playlist_id
HAVING avg(T1.avg_rating_of_song) >= 4.0;


#=============================================================================================================
#=============================================================================================================


# 6. Who are the top 5 most engaged users in terms of number of ratings that they have given to songs or albums?
# (In other words, they have given the most number of ratings to songs or albums combined)
# The result must have 2 columns, named username and number_of_ratings

# logic: join r_album, r_song.. then groupby username. return username, count(*) in desc order limit 5
SELECT username, count(*) as number_of_ratings
FROM r_album join r_song using (username)
GROUP BY username
ORDER BY number_of_ratings DESC
LIMIT 5;


#=============================================================================================================
#=============================================================================================================


# 7. Find the top 10 most prolific artists (most number of songs) in the years 1990-2010? Count each song in an album individually.
# The result must have 2 columns, named artist_name and number_of_songs.

SELECT T.artist_name, count(*) as number_of_songs
FROM (
		SELECT S.artist_name
		FROM Song S left join Album A on S.album_id = A.album_id
		WHERE (YEAR(S.song_release_date) between 1990 AND 2010) or (YEAR(A.album_release_date) between 1990 AND 2010)
    ) as T
GROUP BY T.artist_name
ORDER BY number_of_songs DESC
LIMIT 10;


#=============================================================================================================
#=============================================================================================================


#8. Find the top 10 songs that are in most number of playlists. Break ties in alphabetical order of song titles.
# The result must have a 2 columns, named song_title and number_of_playlists.
SELECT S.title as song_title, count(*) as number_of_playlists
FROM has H join Song S on H.song_id = S.song_id
GROUP BY H.song_id # Now we don't care about artist name, since each song title is uniquely identified
ORDER BY 
	number_of_playlists DESC,
    song_title ASC
LIMIT 10;


#=============================================================================================================
#=============================================================================================================


# 9 Find the top 20 most rated singles (songs that are not part of an album).
# Most rated meaning number of ratings, not actual rating scores. The result must have 3 columns, named song_title, artist_name, number_of_ratings

SELECT S.title as song_title, S.artist_name, count(*) as number_of_ratings
FROM r_song R, Song S
WHERE R.song_id = S.song_id AND S.album_id is null
GROUP BY S.song_id
ORDER BY number_of_ratings DESC
LIMIT 20;

#=============================================================================================================
#=============================================================================================================


# 10. Find all artists who discontinued making music after 1993
SELECT artist_name as artist_title
FROM Artist
WHERE artist_name NOT IN (
	# Exists if produced a song/album after 1993
	SELECT DISTINCT S.artist_name
	FROM Song S left join Album A on S.album_id = A.album_id
    WHERE (YEAR(S.song_release_date) > 1993) or (YEAR(A.album_release_date) > 1993)
);