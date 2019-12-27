from openList import *
import random

openlist = []
for i in range(20):
	tn = Treenode(random.randint(0, 4), random.randint(0, 4), None, None, None)
	print("Inserting f = {}, g = {}".format(tn.f, tn.g))
	openlist = insert(tn, openlist)

while len(openlist) != 0:
	node, openlist = pop(openlist)
	print("({}, {})".format(node.f, node.g))