import json
from pprint import pprint

json1 = '{"hill center": null, "AB":"College Ave"}'
jsondict = json.loads(json1)

# json1 needs to be a string
# keys must be in double quotes
    # x =  "{ 'name':'John', 'age':30, 'city':'New York'}" won't work
    # see that 'name' is not in double quotes.

# values don't even need to be in quotes.
# null gets converted to None

pprint(jsondict)


# we can loads and dumps (dump string)
# for dumps we take a dictionary and turn it into a string
    # any keys that are non strings will get turned into strings when
    # doing a loads from that dumps-d string.
d1ct = {2 : "kev"}

print(d1ct)
json2 = json.dumps(d1ct)
print(json2)

d1ct1 = json.loads(json2)
print(d1ct1)


json4 = '{"quiz_scores" : [{"name": "Anika", "scores":[38,40,36,40,32]}, {"name": "Amir", "scores":[36,38,40,30,34]}]}'
dict4 = json.loads(json4)
pprint(dict4)

import random

sequencelist = ["memorials day", "christmas", "halloween", "thanksgiving"]

for i in range(10):
    print(random.choice(sequencelist))


import requests
nobel_url = ' http://api.nobelprize.org/v1/prize.json'
resp = requests.get(nobel_url)
nobels = json.loads(resp.text)
pprint(nobels)