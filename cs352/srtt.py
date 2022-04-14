alpha = 0.5
beta = 3
measurements = [2,3,4,1,5]

srttUpdate = lambda prev, curr, a: prev * a + curr * (1-a)
computeTimeout = lambda srtts, b: [b*s for s in srtts]

def computeSRTT(lst, a):
    result = [lst[0]]
    n = len(lst)
    for ptr in range(1, n):
        result.append(srttUpdate(result[ptr-1], lst[ptr], a))
    return result


two = computeSRTT(measurements, alpha)
three = computeTimeout(two, beta)

print("RTT Meas.\t SRTT\t\tTimeout")
print("-------------------------------------")
for t in range(len(measurements)):
    print(measurements[t], "\t","\t", two[t], "\t", "\t", three[t])
