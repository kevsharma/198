from collections import Counter
from collections import defaultdict


# make a list of words
# make a list of tuples using a Counter class
# then make a list of keys where the keys are word lengths, default dict them to a list
# then add the words from the list of tuples to each key based on word length

def getWords(file):
    words = [w.strip() for line in open(file) for w in line.split(" ")]
    valids = [word.strip(".,!?") for word in words]
    acceptable = lambda w : w.isalpha() and len(w) >= 4
    valids = [word for word in valids if acceptable(word)]
    return valids


def classify(inputfile):
    words = getWords(inputfile)
    existing_lengths = sorted(set([len(word) for word in words]))
    listOf_word_occurence_tuples = Counter(words)

    d = defaultdict(list)
    for item in listOf_word_occurence_tuples.items():
        w = item[0]
        key = len(w)
        d[key].append(item)

    # must turn into a list, but sort the values first
    for key,listVals in d.items():
        d[key] = sorted(listVals)

    # return needs a list
    return [(length, d[length]) for length in existing_lengths]



d = {}
d['jan'] = '01'
d['feb'] = '02'
d['mar'] = '03'
d['apr'] = '04'
d['may'] = '05'
d['jun'] = '06'
d['jul'] = '07'
d['aug'] = '08'
d['sep'] = '09'
d['oct'] = '10'


def getLines(f):
    lines = list()
    for line in open(f):
        line = line.strip()
        lines.append(line)
    return lines

def getWordsInEachLine(lines):
    properLines = list()
    for line in lines:
        fields = line.split(":")
        formattedProperly = list()
        for i in range(0, 3):
            field = fields[i]
            field = field.strip()

            if i == 0: # dealing with the march, 15 stuff
                mondate = [v for v in field.split(" ") if len(v) >= 1]
                mon = mondate[0].lower().strip()
                date = mondate[1].strip()
                mon = mon[0:3]
                newString = d[mon] + "/" + date
                formattedProperly.append(date)

            elif i == 1: # dealing with the number
                properNum = ""
                for s in field:
                    if s == ",":
                        continue
                    else:
                        properNum += s
                formattedProperly.append(int(properNum))

            elif i == 2: # dealing with the vaccine
                formattedProperly.append(field)

        properLines.append(formattedProperly)
    return properLines


def getShots(inputfile,  lowerbound, upperbound):
    lines = getLines(inputfile)
    lines = getWordsInEachLine(lines)
    withinBounds = lambda x : x <= upperbound and x >= lowerbound
    resultList = sorted([line for line in lines if withinBounds(line[1])], key=lambda x : x[1])
    myList = list()
    for val in resultList:
        myList.insert(0, val)
    return myList


r = getShots("shots.txt", 10000, 500000)
print(r)