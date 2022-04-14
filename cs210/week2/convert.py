def convert(temp, dir="ctof"):
    if dir == "ctof":
        res = temp*9/5 + 32
    else:
        res = (temp-32)*5/9
    return res

cval = convert(43, "ftoc")
print('{:.2f}'.format(cval))
print(f'{cval:.2f}')
