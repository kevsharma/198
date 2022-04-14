# Author: Payal Gami : psg60
# Author: Kev Sharma : kks107

import csv
from collections import defaultdict
from collections import Counter


# Assumptions and observations:
# Each line in the csv in the file given to me has 10 attributes
def one(records):
    records_fire = [r for r in records if r[4] == "fire"]

    records_fireGE40 = [r for r in records_fire if float(r[2]) >= 40]
    total_fireTypes = len(records_fire)
    total_fireTypesGE40 = len(records_fireGE40)
    #check to avoid division by zero
    if total_fireTypes == 0:
        percentage = 0
    else:
        percentage = (total_fireTypesGE40 / total_fireTypes) * 100

    # I know that round(x) will go to nearest even number.
    # The assignment contradicts itself when it states to use round() and to go down if its 12.5 (what about 13.5)?
    return f"Percentage of fire type Pokemons at or above level 40 = {round(percentage)}"


# Replace NaN types
def two(records):
    # Create a dictionary of weakness key - type value so we can work backwards.
    weaknesses = defaultdict(list)

    # r[5] is weakness. # r[4] is type.
    for r in records:
        if r[4] != "NaN":
            weaknesses[r[5]].append(r[4])

    weaknesses = dict([(k, Counter(sorted(v)).most_common(1)[0][0]) for k, v in weaknesses.items()])

    # Replace the NaN types:
    for r in records:
        if r[4] == "NaN":
            r[4] = weaknesses[r[5]]

    return records


# note: csv file lets me assume that each pokemon must have level.
def three(records):
    threshold = 40
    # r[2] is level and that can't be NaN
    records_g_threshold = [r for r in records if float(r[2]) > threshold]
    records_le_threshold = [r for r in records if float(r[2]) <= threshold]

    gatk, gdef, ghps = compute_avgs(records_g_threshold)
    leatk, ledef, lehps = compute_avgs(records_le_threshold)

    # Substitute in the correct values provided existing value is NaN
    for r in records:
        if float(r[2]) > threshold:
            if r[6] == 'NaN':
                r[6] = gatk
            if r[7] == 'NaN':
                r[7] = gdef
            if r[8] == 'NaN':
                r[8] = ghps
        else:
            if r[6] == 'NaN':
                r[6] = leatk
            if r[7] == 'NaN':
                r[7] = ledef
            if r[8] == 'NaN':
                r[8] = lehps
    return records


def compute_avgs(records):
    atks = [float(r[6]) for r in records if r[6] != "NaN"]
    defs = [float(r[7]) for r in records if r[7] != "NaN"]
    hps = [float(r[8]) for r in records if r[8] != "NaN"]

    # round to 1 decimal place, but if every atk value in record is NaN, then prevent divide by 0 error
    avg = lambda lst: str(round(sum(lst) / len(lst), 1)) if len(lst) > 0 else "NaN"
    return avg(atks), avg(defs), avg(hps)



def four(records):
    listOfTypes = [r[4] for r in records]
    # keys should be ordered alpabetically
    d = defaultdict(set, {k: set() for k in sorted(listOfTypes)})
    for r in records:
        # key = type, val = set of personalities for no duplicates
        d[r[4]].add(r[3])

    # Change from defaultdict to dict, and sort value lists.
    return dict([(k, sorted(list(v))) for k, v in d.items()])


# a helper function to print four's output
def printFour(fourDict):
    str = "Pokemon type to personality mapping:\n"
    for key in fourDict:
        str += f"\t{key}: {', '.join(fourDict[key])}\n"
    return str


def five(records):
    # Find out the average Hit Points ("hp" r[8]) for pokemons of stage 3.0 r[9].
    recs = [float(r[8]) for r in records if r[9] != "NaN" and float(r[9]) >= 3.0 and r[8] != "NaN"]
    # need defensive check in case all pokemons in records are under stage 3
    avghps = round(sum(recs) / len(recs)) if len(recs) > 0 else "NaN"
    return f"Average hit point for Pokemons of stage 3.0 = {avghps}"

#0 id
#1 name
#2 level
#3 personality
#4 type
#5 weakness
#6 atk
#7 def
#8 hp
#9 stage


input_filename = "pokemonTrain.csv"
output_filename = "pokemonResult.csv"

#READ FROM THE FILE FOR PARTS 1-3
record_rows = list()
with open(input_filename) as f:
    f_reader = csv.reader(f)  # this is a iterable reader object.
    attributes = next(f_reader)
    # f_reader supports iterator protocol
    for i, line in enumerate(f_reader):
        record_rows.append(line)


p1 = one(record_rows)
with open('pokemon1.txt', 'w') as f_one:
    f_one.write(p1)

p2 = two(record_rows)
p3 = three(p2)

#write p3 records to outputfile
# PASTE THE OUTPUT OF PART 3 INTO pokemonResult.csv
with open(output_filename, 'w') as f:
    f.write(','.join(attributes))
    f.write("\n")
    for record in p3:
        f.write(','.join(record))
        f.write("\n")


# reading from the file again (we could've reused but just following prompt to the T)
# READ pokemonResult.csv AGAIN TO 4-5
record_rows = list()
with open(output_filename) as f:
    f_reader = csv.reader(f)  # this is a iterable reader object.
    attributes = next(f_reader)
    # f_reader supports iterator protocol
    for i, line in enumerate(f_reader):
        record_rows.append(line)

# COMPUTE AND WRITE 4
dict_four = four(record_rows) # not p3, must use record_rows again.
with open('pokemon4.txt', 'w') as f_four:
    f_four.write(printFour(dict_four))

# COMPUTE WRITE 5
five_output = five(p3)
with open('pokemon5.txt', 'w') as f_five:
    f_five.write(five_output)


