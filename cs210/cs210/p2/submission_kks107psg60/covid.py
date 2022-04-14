# Author: Payal Gami : psg60
# Author: Kev Sharma : kks107

import csv
import re
from collections import Counter
from collections import defaultdict

inputfile = "covidTrain.csv"
outputfile = "covidResult.csv"
d_records = []
with open(inputfile) as f:
    f_reader = csv.DictReader(f)
    for row_dict in f_reader:
        d_records.append(row_dict)


def one(listof_covid_dicts):
    # age_pattern_noranges = r'^(\d+)$'
    age_pattern_ranges = r'^(\d+)-(\d+)$'

    for record in listof_covid_dicts:
        rangematch = re.match(age_pattern_ranges, record["age"])
        if rangematch:
            # The age value had a range, replace it with the avg of the two groups found.
            rangematch = rangematch.groups()
            lo = int(rangematch[0])
            hi = int(rangematch[1])
            record["age"] = round((lo + hi) / 2)


def two(listof_covid_dicts):
    for d in listof_covid_dicts:
        d["date_onset_symptoms"] = re.sub(r'^(\d{2})\.(\d{2})\.(\d{4})$', r'\2.\1.\3', d["date_onset_symptoms"])
        d["date_admission_hospital"] = re.sub(r'^(\d{2})\.(\d{2})\.(\d{4})$', r'\2.\1.\3', d["date_admission_hospital"])
        d["date_confirmation"] = re.sub(r'^(\d{2})\.(\d{2})\.(\d{4})$', r'\2.\1.\3', d["date_confirmation"])


def three(listof_covid_dicts):
    setprovinces = set([d["province"] for d in listof_covid_dicts])
    dictprovinces = dict([(sp, ("NaN", "NaN")) for sp in setprovinces])

    # aggregate avgs for each province.
    for province in dictprovinces:
        latitudes = [float(d["latitude"]) for d in listof_covid_dicts if
                     d["province"] == province and d["latitude"] != "NaN"]
        longitudes = [float(d["longitude"]) for d in listof_covid_dicts if
                      d["province"] == province and d["longitude"] != "NaN"]

        # defensive check in ternary operator for latitude being null
        latitudes = round(sum(latitudes) / len(latitudes), 2) if len(latitudes) > 0 else "NaN"
        longitudes = round(sum(longitudes) / len(longitudes), 2) if len(longitudes) > 0 else "NaN"

        dictprovinces[province] = (latitudes, longitudes)

    # dictprovinces contains avg (latitude, longitude) for each province.
    for d in listof_covid_dicts:
        d["latitude"] = dictprovinces[d["province"]][0] if d["latitude"] == "NaN" else d["latitude"]
        d["longitude"] = dictprovinces[d["province"]][1] if d["longitude"] == "NaN" else d["longitude"]


def four(listof_covid_dicts):
    setprovinces = set([d["province"] for d in listof_covid_dicts])
    pc = defaultdict(list)  # key = province, value = list of cities

    # pc will only contain provinces for which there exists at least one record with a non-NaN city
    for d in listof_covid_dicts:
        if d["city"] != "NaN":
            pc[d["province"]].append(d["city"])

    # Need to check that logic here works.
    mostoccuringcityperprovince = dict(
        [(province, Counter(sorted(pc[province])).most_common(1)[0][0]) for province in pc])

    # insert a key for provinces where all records for city are NaN
    for s in setprovinces:
        if s not in mostoccuringcityperprovince:
            mostoccuringcityperprovince[s] = "NaN"

    for d in listof_covid_dicts:
        d["city"] = mostoccuringcityperprovince[d["province"]] if d["city"] == "NaN" else d["city"]


def five(listof_covid_dicts):
    setprovinces = set([d["province"] for d in listof_covid_dicts])
    # province to list of symptoms dictionary
    pls = defaultdict(list)  # key = province, value = list of symptoms
    pattern = r';[ ]?' # match the symptoms list
    # Create a mapping of province : lst of symptoms
    # If all records in a province show symptopms as "NaN" then it wont be added.
    for d in listof_covid_dicts:
        if d["symptoms"] != "NaN":
            symps = [r.strip() for r in re.split(pattern, d["symptoms"])]
            pls[d["province"]].extend(symps) # append a list of words (strs) to a list of strings.

    pls = dict([(p, Counter(sorted(pls[p])).most_common(1)[0][0]) for p in pls])

    # If all records for some province p had symptoms NaN, then that province won't be in pls.
    for p in setprovinces:
        if p not in pls:
            pls[p] = "NaN"

    for d in listof_covid_dicts:
        d["symptoms"] = pls[d["province"]] if d["symptoms"] == "NaN" else d["symptoms"]



# d_records is passed around, so the same dictionary is changed up.
one(d_records)  # turning ranges into avg
two(d_records)  # changing date formats
three(d_records)  # remove NaN in lats/longs
four(d_records)  # replace NaN cities with most occuring one for that record's province
five(d_records) # replace NaN symptoms


with open(outputfile, 'w', newline='') as csvfile:
    writer = csv.DictWriter(csvfile, fieldnames=d_records[0].keys())
    writer.writeheader()
    for record_dict in d_records:
        writer.writerow(record_dict)
