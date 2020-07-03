from LIFneuronHebb import *
from Synapse import *

print('This SNN generates the values for an XOR gate based on a 0 or 1 input from 2 input neurons.'+ '\n')
x_in = input("Please enter the Value of the x input neuron (0 or 1): ")
x_input = int(x_in)
y_in = input("Please enter the Value of the y input neuron (0 or 1): ")
y_input = int(y_in)

# BIG_CURRENT = 20000
# SMALL_CURRENT = 5000
BIG_CURRENT = 50000
SMALL_CURRENT = 5000

def printValues(inputlayer, hiddenlayer, outputlayer): 
	print('\n' + '\n' + '-------------------------Results---------------------------' + '\n' + '\n')
	# CASE 1: XOR -> does not fire 
	if x_input == 0 and y_input == 0: 
		print('Layer 1')
		print('\t' + 'x -> OFF' + ' firing rate: ' + str(inputlayer[0].firingrate))
		print('\t' + 'y -> OFF' + ' firing rate: ' + str(inputlayer[1].firingrate))
		print('\t' + 'OR -> OFF' + ' firing rate: ' + str(hiddenlayer[0].firingrate))
		print('\t' + 'NAND -> ON' + ' firing rate: ' + str(hiddenlayer[1].firingrate))
		print('\t' + '(OR) AND (NAND) -> OFF' + ' firing rate: ' + str(outputlayer[0].firingrate))

	# CASE 2: XOR -> does fire 
	elif x_input == 0 and y_input == 1: 
		print('Layer 1')
		print('\t' + 'x -> OFF' + ' firing rate: ' + str(inputlayer[0].firingrate))
		print('\t' + 'y -> ON' + ' firing rate: ' + str(inputlayer[1].firingrate))
		print('\t' + 'OR -> ON' + ' firing rate: ' + str(hiddenlayer[0].firingrate))
		print('\t' + 'NAND -> ON' + ' firing rate: ' + str(hiddenlayer[1].firingrate))
		print('\t' + '(OR) AND (NAND) -> ON' + ' firing rate: ' + str(outputlayer[0].firingrate))

	# CASE 3: XOR -> does fire 
	elif x_input == 1 and y_input == 0: 
		print('Layer 1')
		print('\t' + 'x -> ON' + ' firing rate: ' + str(inputlayer[0].firingrate))
		print('\t' + 'y -> OFF' + ' firing rate: ' + str(inputlayer[1].firingrate))
		print('\t' + 'OR -> ON' + ' firing rate: ' + str(hiddenlayer[0].firingrate))
		print('\t' + 'NAND -> ON' + ' firing rate: ' + str(hiddenlayer[1].firingrate))
		print('\t' + '(OR) AND (NAND) -> ON' + ' firing rate: ' + str(outputlayer[0].firingrate))

	# CASE 4: XOR -> does not fire 
	elif x_input == 1 and y_input == 1: 
		print('Layer 1')
		print('\t' + 'x -> ON' + ' firing rate: ' + str(inputlayer[0].firingrate))
		print('\t' + 'y -> ON' + ' firing rate: ' + str(inputlayer[1].firingrate))
		print('\t' + 'OR -> ON' + ' firing rate: ' + str(hiddenlayer[0].firingrate))
		print('\t' + 'NAND -> OFF' + ' firing rate: ' + str(hiddenlayer[1].firingrate))
		print('\t' + '(OR) AND (NAND) -> OFF' + ' firing rate: ' + str(outputlayer[0].firingrate))


def teach_neurons(inputlayer,hiddenlayer,outputlayer,x_teacher,y_teacher,or_teacher,nand_teacher,and_teacher,permutation,num_iterations):
	print("permutation is: " + str(permutation))

	if permutation==[0,0]:
		x_teacher.current = SMALL_CURRENT
		x_teacher.firingrate = current_to_rate(SMALL_CURRENT)
		y_teacher.current = SMALL_CURRENT
		y_teacher.firingrate = current_to_rate(SMALL_CURRENT)
		or_teacher.current = SMALL_CURRENT
		or_teacher.firingrate = current_to_rate(SMALL_CURRENT)
		nand_teacher.current = BIG_CURRENT
		nand_teacher.firingrate = current_to_rate(BIG_CURRENT)
		and_teacher.current = SMALL_CURRENT
		and_teacher.firingrate = current_to_rate(SMALL_CURRENT)
		
	elif permutation==[0,1]:
		x_teacher.current = SMALL_CURRENT
		x_teacher.firingrate = current_to_rate(SMALL_CURRENT)
		y_teacher.current = BIG_CURRENT
		y_teacher.firingrate = current_to_rate(BIG_CURRENT)
		or_teacher.current = BIG_CURRENT
		or_teacher.firingrate = current_to_rate(BIG_CURRENT)
		nand_teacher.current = BIG_CURRENT
		nand_teacher.firingrate = current_to_rate(BIG_CURRENT)
		and_teacher.current = BIG_CURRENT
		and_teacher.firingrate = current_to_rate(BIG_CURRENT)
	elif permutation==[1,0]:
		x_teacher.current = BIG_CURRENT
		x_teacher.firingrate = current_to_rate(BIG_CURRENT)
		y_teacher.current = SMALL_CURRENT
		y_teacher.firingrate = current_to_rate(SMALL_CURRENT)
		or_teacher.current = BIG_CURRENT
		or_teacher.firingrate = current_to_rate(BIG_CURRENT)
		nand_teacher.current = BIG_CURRENT
		nand_teacher.firingrate = current_to_rate(BIG_CURRENT)
		and_teacher.current = BIG_CURRENT
		and_teacher.firingrate = current_to_rate(BIG_CURRENT)
	elif permutation==[1,1]:
		x_teacher.current = BIG_CURRENT
		x_teacher.firingrate = current_to_rate(BIG_CURRENT)
		y_teacher.current = BIG_CURRENT
		y_teacher.firingrate = current_to_rate(BIG_CURRENT)
		or_teacher.current = BIG_CURRENT
		or_teacher.firingrate = current_to_rate(BIG_CURRENT)
		nand_teacher.current = SMALL_CURRENT
		nand_teacher.firingrate = current_to_rate(SMALL_CURRENT)
		and_teacher.current = SMALL_CURRENT
		and_teacher.firingrate = current_to_rate(SMALL_CURRENT)

	print(inputlayer[0].presynapses[0].preneuron.firingrate)
	print(inputlayer[1].presynapses[0].preneuron.firingrate)
	print(hiddenlayer[0].presynapses[2].preneuron.firingrate)
	print(hiddenlayer[1].presynapses[2].preneuron.firingrate)
	print(outputlayer[0].presynapses[2].preneuron.firingrate)
	print('\n')
	for i in range(num_iterations):
		print('LAYER 1')
		for neur in inputlayer:
			neur.process_current()
		print('LAYER 2')
		for neur in hiddenlayer:
			neur.process_current()
			neur.update_synapse_weight()
		print('LAYER 3')
		outputlayer[0].process_current()
		outputlayer[0].update_synapse_weight()

		if i == num_iterations-1: 
			printValues(inputlayer, hiddenlayer, outputlayer)

inputlayer = []
hiddenlayer = []
outputlayer = []

# initialize input layer, hidden layer and output layer neurons
for i in ['x', 'y']: 
	inputlayer.append(LIFneuron(i,[],[],False))
for i in ['or', 'not and']: 
	hiddenlayer.append(LIFneuron(i,[],[],False))
outputlayer.append(LIFneuron('and', [], [], False))

# now create synapses between each of the layers (one per connection/vertice on a graph)
for i in inputlayer:
	for j in hiddenlayer:
		syn = Synapse(i,j,0.1)
		i.postsynapses.append(syn)
		j.presynapses.append(syn)
		
for i in hiddenlayer:
	syn = Synapse(i,outputlayer[0],0.1)
	i.postsynapses.append(syn)
	outputlayer[0].presynapses.append(syn)

# now create a teaching neuron for each neuron that we have, 
# with a synapse between the teaching neuron and the regular neuron 
x_teacher = LIFneuron('x_teacher',[],[],True)
y_teacher = LIFneuron('y_teacher',[],[],True)
or_teacher = LIFneuron('or_teacher',[],[],True)
nand_teacher = LIFneuron('nand_teacher',[],[],True)
and_teacher = LIFneuron('and_teacher',[],[],True)

x_teacher.spike = True
y_teacher.spike = True
or_teacher.spike = True
nand_teacher.spike = True
and_teacher.spike = True

x_syn = Synapse(x_teacher,inputlayer[0],1)
y_syn = Synapse(y_teacher,inputlayer[1],1)
or_syn = Synapse(or_teacher,hiddenlayer[0],1)
nand_syn = Synapse(nand_teacher,hiddenlayer[1],1)
and_syn = Synapse(and_teacher,outputlayer[0],1)

x_teacher.postsynapses.append(x_syn)
inputlayer[0].presynapses.append(x_syn)
y_teacher.postsynapses.append(y_syn)
inputlayer[1].presynapses.append(y_syn)
or_teacher.postsynapses.append(or_syn)
hiddenlayer[0].presynapses.append(or_syn)
nand_teacher.postsynapses.append(nand_syn)
hiddenlayer[1].presynapses.append(nand_syn)
and_teacher.postsynapses.append(and_syn)
outputlayer[0].presynapses.append(and_syn)

#run teach_neurons
teach_neurons(inputlayer,hiddenlayer,outputlayer,x_teacher,y_teacher,or_teacher,nand_teacher,and_teacher,[x_input, y_input],100)

