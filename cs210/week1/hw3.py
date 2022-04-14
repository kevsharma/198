from collections import defaultdict

class Node:
    def __init__(self, int64):
        self.val = int64
        self.left = None
        self.right = None


def insert(root, val):
    if root is None:
        return Node(val)

    prev = root
    ptr = root
    while (True):
        if ptr is None:
            if val <= prev.val:
                prev.left = Node(val)
            else:
                prev.right = Node(val)
            break

        # ptr isn't none, so go further
        prev = ptr
        ptr = ptr.left if val <= ptr.val else ptr.right

    return root

numbers = []
with open("textfile.txt") as fp:
    for line in fp.readlines():
        numbers.append(int(line.strip()))

root = None
for val in numbers:
    root = insert(root, val)


# Print the numbers.
def printTree(root, d, level=0):
    if root is None:
        d[level].append("X")
        return

    d[level].append(str(root.val))
    printTree(root.left, d, level+1)
    printTree(root.right, d, level+1)

d = defaultdict(list)
printTree(root, d) # writes into d

with open("result_numbers.txt", 'w') as fp:
    for level, values in d.items():
        [fp.write(v+" ") for v in values]
        fp.write("\n")
