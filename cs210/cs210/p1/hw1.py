# Author: Kev Sharma : kks107
# Author: Payal Gami : psg60

from collections import defaultdict
from collections import Counter

ratingsFile = "movieRatingSample.txt"
moviesFile = "genreMovieSample.txt"

# 1.1
def read_ratings_data(f):
    movie_ratings_dict = defaultdict(list)
    # open the file, read key as movie and append the rating, close the file.
    with open(f) as f1:
        for line in f1:
            tokens = line.split("|")
            movie_ratings_dict[tokens[0].strip()].append(float(tokens[1].strip()))
    # want to return a type dict not default dict
    return dict(movie_ratings_dict)


# 1.2
def read_movie_genre(f):
    # read file
    movie_to_genre_dict = dict()
    with open(f) as f1:
        for line in f1:
            tokens = line.split("|")
            movie_to_genre_dict[tokens[2].strip()] = tokens[0].strip()

    return movie_to_genre_dict


result11 = read_ratings_data(ratingsFile) # type(result11) = <class 'dict'>
result12 = read_movie_genre(moviesFile) # type(result12) = <class 'dict'>

# ------------------------------------------------------------------------------------------------

# 2.1
def create_genre_dict(movie_to_genre):
    # assumes movie_to_genre contains k,v of type String
    movies_by_genre = defaultdict(list)
    for m,g in movie_to_genre.items():
        movies_by_genre[g].append(m)
    return movies_by_genre

movies_by_genre_dict = create_genre_dict(result12)
result21 = movies_by_genre_dict

# 2.2
def calculate_average_rating(movie_ratings_dict):
    # assumes movie_ratings_dict contains k of type String and v of type list.
    avg = lambda lst : sum(lst) / len(lst) # note that 5/2 = 2.5, and 5//2 = 2
    return dict([(movie, avg(ratingList)) for movie, ratingList in movie_ratings_dict.items()]) # dictionary unpacks tuple into key/val


movie_to_average_dict = calculate_average_rating(result11)
result22 = movie_to_average_dict

# -----------------------------------------------------------------------------------------------

# returns a dictionary of most common
def top_elements_count(d, n):
    return dict(Counter(d).most_common(n))


# 3.1
def get_popular_movies(movie_to_average_dict, n=10):
    return top_elements_count(movie_to_average_dict, n)


result31 = get_popular_movies(result22, 10)


# 3.2
def filter_movies(movie_to_average_dict, threshold=3):
    return dict([(movie, avgRating) for movie, avgRating in movie_to_average_dict.items() if avgRating >= threshold])


result32 = filter_movies(result22)


# 3.3
def get_popular_in_genre(genre, genre_to_movies_dict, movie_to_average_dict, n=5):
    movies_from_genre = genre_to_movies_dict[genre]
    # we need only those movies from movie_to_average_dict that are in movies_from_genre list
    tempDict = dict([(m,v) for m,v in movie_to_average_dict.items() if m in movies_from_genre])
    return get_popular_movies(tempDict, n)


result33 = get_popular_in_genre("Adventure", result21, result22, 10)


# 3.4
def get_genre_rating(genre, genre_to_movies_dict, movie_to_average_dict):
    movies_from_genre = genre_to_movies_dict[genre]
    subset_movies_from_genre = [avgRating for movie, avgRating in movie_to_average_dict.items() if movie in movies_from_genre]
    if len(subset_movies_from_genre)==0:
        return 0
    else:
        return sum(subset_movies_from_genre) / len(subset_movies_from_genre)

result34 = get_genre_rating("Adventure", result21, result22)


# 3.5
def genre_popularity(genre_to_movies_dict, movie_to_average_dict, n=5):
    # returns the top-n rated genres as a dictionary of genre:average rating.
    genre_averages_dict = dict([(g, get_genre_rating(g, genre_to_movies_dict, movie_to_average_dict)) for g in genre_to_movies_dict])
    return top_elements_count(genre_averages_dict, n)


result35 = genre_popularity(result21, result22, n=10)


# part 4

#4.1
def read_user_ratings(f):
    user_to_movies_dict = defaultdict(list) # a list of tuples
    with open(f) as f1:
        for line in f1:
            tokens = line.split("|")
            user_to_movies_dict[int(tokens[2].strip())].append((tokens[0].strip(), float(tokens[1].strip())))
    # want to return a type dict not default dict
    return dict(user_to_movies_dict)

result41 = read_user_ratings(ratingsFile)


#4.2                   6       result41           result12
def get_user_genre(userID, user_to_movies_dict, movie_to_genre_dict):
    #step1 - genre_to_rating_dict where key = genre, value = (rating, num ratings)
    # step2 - map the values to avg
    # step3 - get the genre mapping to the highest avg
    genre_to_rating_dict = dict() # step 1
    if userID not in user_to_movies_dict:
        return None

    usersMovies = user_to_movies_dict[userID]
    for m,r in usersMovies: # note that m is the movie's name, and r is the userID's assigned m rating.
        if m in movie_to_genre_dict:
            g_m = movie_to_genre_dict[m]
            if g_m in genre_to_rating_dict: # g_m is the genre name
                runningSum, runningNumRatings = genre_to_rating_dict[g_m]  # add the rating of m
                genre_to_rating_dict[g_m] = (runningSum + r, runningNumRatings+1) # num ratings++
            else:
                genre_to_rating_dict[g_m] = (r, 1)

    genre_to_avgs_dict = dict([(g, (t[0]/t[1])) for g,t in genre_to_rating_dict.items()]) # step2
    return Counter(genre_to_avgs_dict).most_common(1)[0][0] # step3


result42 = get_user_genre(43, result41, result12)


# 4.3                           result41            result12            result22
def recommend_movies(userID, user_to_movies_dict, movie_to_genre_dict, movie_to_average_dict):
    if userID not in user_to_movies_dict:
        return None

    userID_topGenre = get_user_genre(userID, user_to_movies_dict, movie_to_genre_dict)
    movies_by_genre_dict = create_genre_dict(movie_to_genre_dict)

    movies_from_userID_topGenre = set(movies_by_genre_dict[userID_topGenre])
    userRated_movies = set([mrTuple[0] for mrTuple in user_to_movies_dict[userID]])

    unrated_from_userID_topGenre = movies_from_userID_topGenre.difference(userRated_movies)
    unrated_from_userID_topGenre_moviesAndAvgRating = dict([(m, movie_to_average_dict[m]) for m in unrated_from_userID_topGenre])
    return top_elements_count(unrated_from_userID_topGenre_moviesAndAvgRating, 3)


result43 = recommend_movies(43, result41, result12, result22)
