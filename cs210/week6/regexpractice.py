import re
from pprint import pprint

mystring1 = "something is off"

# get rid of all the vowels
vowels = []
pattern = r'[aeiou]+'
mystring2 = re.sub(pattern, "!", mystring1)

print(mystring2)

# you can use ? to make something nongreedy
# for instance,

htmlstr = 'Before comment...<!-- This is a comment -->, and after comment'
res = re.sub(r'<!--.*-->', '', htmlstr)  # replace comment with nothing
print(res)


htmlstr = 'Before first... <!-- comment1 -->between first and second <!-- comment2--> ... after second'
res = re.sub(r'<!--.*?-->', '', htmlstr)  # replace comment with nothing
print(res)

# What does the ? do? ? stands for zero or one.
# + stands for one or more
# * stands for zero or more.

# using ? here turns it into saying, only one occurrance of everything then --->

test_str='18.0   8.   307.0      130.0      3504.      12.0   70.  1.	"chevrolet chevelle malibu"'

car_reg = re.compile(r"""
                \s*                    # skip over whitespaces at start
                (?P<mpg>\d{2}\.\d)     # mpg field is of the form dd.d
                \s*                    # skip white spaces
                (?P<cyl>\d)\.          # cylinders field is of the form d., only want d
                .*                     # skip all intervening stuff
                (?P<yy>\d{2})\.        # year is of form dd., only want dd
                \s*                    # skip whitespaces
                \d\.                   # origin is of the form d.
                .*                     # skip intervening stuff
                (?P<name>".*")         # car name is in double quotes, want double quotes
            """, re.VERBOSE)

# we want a dictionary of matches
x = car_reg.match(test_str)
#print(x.group())
print(x.groups())
pprint(x.groupdict())


# note that we gave group names like follows: r'(?P<name>)'
# we prefixed the pattern in the group by a name.

mystring = "nice nissan"

pattern = r'\s*(?P<first>ni).*(?P<sec>nis)\.*'
mp = re.compile(pattern)
x = mp.match(mystring)
print(x)
print(x.groupdict())


mystr2 = "look at me go!"
pattern = r'[aeiou]'
resultmystr2 = re.sub(pattern, "!", mystr2)
print(resultmystr2)