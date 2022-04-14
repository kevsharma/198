# Author: Payal Gami : psg60
# Author: Kev Sharma : kks107

import math
import re
from collections import Counter, defaultdict


def ppone(string):
    # Remove all website links. A website link is
    #   a sequence of non-whitespace characters that starts with either "http://" or "https://"
    pattern = r'\s+(https|http)?://\S*'
    string = re.sub(pattern, "", string)

    # Remove all characters that are not words or whitespaces.
    #   Words are sequences of letters (upper and lower case), digits, and underscore
    pattern = r'[^\sa-zA-Z0-9_]'
    string = re.sub(pattern, "", string)

    # Remove extra whitespaces between words. e.g., “Hello World! Let’s   learn    Python!”,
    #   so that there is exactly one whitespace between any pair of words.
    pattern = r'\s+'
    string = re.sub(pattern, " ", string)

    # Convert all words to lowercase
    string = string.lower()

    return string


def pptwo(string):
    with open("stopwords.txt") as sfp:
        stopwords = [word.strip() for word in sfp.readlines() if word.strip()]

    for sw in stopwords:
        pattern = r'\s*\b' + sw + r'\b'
        string = re.sub(pattern, "", string)  # count omitted to replace all occurrences

    return string


def ppthree(string):
    # Words separated by spaces
    pattern = r'ly\s|ing\s|ment\s'
    string = re.sub(pattern, " ", string)
    # Check for the last word as well
    pattern = r'ly$|ing$|ment$'
    string = re.sub(pattern, " ", string)
    return string


def preprocess(filename):
    # read the entire unprocessed file
    with open(filename) as fp:
        contents = fp.read()

    contents = ppone(contents)
    contents = pptwo(contents)
    contents = ppthree(contents)

    # write processed results
    with open("preproc_" + filename, 'w') as fp:
        fp.write(contents)


def compute(filename):
    # read the words from processed file
    with open(filename) as fp:
        words = fp.read()

    words = words.split()  # str ->  list::str
    terms = dict([(t, (tf / len(words))) for t, tf in Counter(words).items()])
    return terms


def print_tfidf(filename, tfdict):
    tuples = [(k, v) for k, v in tfdict.items()]
    tuples = sorted(tuples, key=lambda x: x[0])  # sort alpabetically first
    tuples = sorted(tuples, key=lambda x: x[1], reverse=True)  # sort by weight second
    tuples = tuples[:5]  # get top 5 by weight.

    with open(filename, 'w') as tfidf_fp:
        tfidf_fp.write(str(tuples))


# Read all the docs
with open("tfidf_docs.txt") as f:
    docs = [doc.strip() for doc in f.readlines() if doc.strip()]

# Part 1: Begin preprocessing from tfidf_docs.txt
[preprocess(doc) for doc in docs]

# Part 2: Begin computation on all processed files
# List of dicts where each item is a dict for doc in docs where key = word, val = (# occurrences, total words) in doc
tfdicts = [compute("preproc_" + doc) for doc in docs]
allwords = set([k for d in tfdicts for k in d])
totaldocs = len(docs)
idfdicts = dict()

# idfdicts dict | key = unique word ; val = # of docs it appears in
for word in allwords:
    if word not in idfdicts:
        idfdicts[word] = 0
    for tfs in tfdicts:
        idfdicts[word] += (1 if word in tfs else 0)
# idfdicts dict | key = unique word ; val = int = idf(key))
for k in idfdicts:  # total num docs / num documents the word is found in
    idfdicts[k] = math.log(totaldocs / idfdicts[k]) + 1  # add 1 for some reason by sesh

# ID-TDF
for i, tfdict in enumerate(tfdicts):
    tfdicts[i] = dict([(term, round(tfdict[term] * idfdicts[term], 2)) for term in tfdict])

# part 2, e:
#   now we have the td-idf for each document print it to its doc
[print_tfidf("tfidf_" + docs[i], tfdicts[i]) for i in range(totaldocs)]
