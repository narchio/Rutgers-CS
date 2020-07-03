from LIFneuronHebb import *
from Synapse import *
import math

# If you want user input, uncomment this: 
# print('This SNN returns if a line passes On-center of a circle or Off-center.'+ '\n')
# deg_input = input("Please enter a degree value from 0 to 360 degrees: ")
# deg_input_converted = int(deg_input)
# # bound check
# if deg_input_converted > 270: 
# 	deg_input_converted -= 270



# BIG_CURRENT = 20000
# SMALL_CURRENT = 5000
BIG_CURRENT = 50000
SMALL_CURRENT = 5000


# prints results
def printValues(inputlayer, outputlayer): 
	print('\n' + '\n' + '-------------------------Results---------------------------' + '\n' + '\n')
	# print input layer values
	for i in inputlayer: 
		print('\t' + i.name + ' firing rate: ' + str(i.firingrate))
	# print output later value
	print('\t' + outputlayer[0].name + ' firing rate: ' + str(outputlayer[0].firingrate))
	if outputlayer[0].firingrate > .1: 
		print('\n On-Center! \n')
	else: 
		print('\n Off-Center! \n')

# function runs the teaching algorithm on the synapses of our neurons 
# (uses Hebbian Learning)
def teach_neurons(inputlayer, outputlayer, teacher_1 ,teacher_2 ,teacher_3 , teacher_4, or_teacher, degree, num_iterations):
	# print("input deg is: " + str(deg))
	# degree = math.degrees(deg)
	#print('degree is: ' + str(degree))

	# Case 1: none 
	if degree < math.degrees(math.atan(1.0/3.0)):
		teacher_1.current = SMALL_CURRENT
		teacher_1.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_2.current = SMALL_CURRENT
		teacher_2.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_3.current = SMALL_CURRENT
		teacher_3.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_4.current = SMALL_CURRENT
		teacher_4.firingrate = current_to_rate(SMALL_CURRENT)
		or_teacher.current = SMALL_CURRENT
		or_teacher.firingrate = current_to_rate(SMALL_CURRENT)
		
	# Case 2: only 4 
	elif degree < math.degrees(math.atan(0.5)):
		teacher_1.current = SMALL_CURRENT
		teacher_1.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_2.current = SMALL_CURRENT
		teacher_2.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_3.current = SMALL_CURRENT
		teacher_3.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_4.current = BIG_CURRENT
		teacher_4.firingrate = current_to_rate(BIG_CURRENT)
		or_teacher.current = BIG_CURRENT
		or_teacher.firingrate = current_to_rate(BIG_CURRENT)
	# Case 3: corner of 3, hits 4
	elif degree < math.degrees(math.atan(2.0/3.0)):
		teacher_1.current = SMALL_CURRENT
		teacher_1.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_2.current = SMALL_CURRENT
		teacher_2.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_3.current = BIG_CURRENT
		teacher_3.firingrate = current_to_rate(BIG_CURRENT)
		teacher_4.current = BIG_CURRENT
		teacher_4.firingrate = current_to_rate(BIG_CURRENT)
		or_teacher.current = BIG_CURRENT
		or_teacher.firingrate = current_to_rate(BIG_CURRENT)
	# Case 4: 2, 3, and 4
	elif degree < math.degrees(math.atan(1)):
		teacher_1.current = SMALL_CURRENT
		teacher_1.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_2.current = BIG_CURRENT
		teacher_2.firingrate = current_to_rate(BIG_CURRENT)
		teacher_3.current = BIG_CURRENT
		teacher_3.firingrate = current_to_rate(BIG_CURRENT)
		teacher_4.current = BIG_CURRENT
		teacher_4.firingrate = current_to_rate(BIG_CURRENT)
		or_teacher.current = BIG_CURRENT
		or_teacher.firingrate = current_to_rate(BIG_CURRENT)
	# Case 5: 1, 2, 3
	elif degree < math.degrees(math.atan(1.5)):
		teacher_1.current = BIG_CURRENT
		teacher_1.firingrate = current_to_rate(BIG_CURRENT)
		teacher_2.current = BIG_CURRENT
		teacher_2.firingrate = current_to_rate(BIG_CURRENT)
		teacher_3.current = BIG_CURRENT
		teacher_3.firingrate = current_to_rate(BIG_CURRENT)
		teacher_4.current = SMALL_CURRENT
		teacher_4.firingrate = current_to_rate(SMALL_CURRENT)
		or_teacher.current = BIG_CURRENT
		or_teacher.firingrate = current_to_rate(BIG_CURRENT)
	# Case 6: 1, 3
	elif degree < math.degrees(math.atan(2)): 
		teacher_1.current = BIG_CURRENT
		teacher_1.firingrate = current_to_rate(BIG_CURRENT)
		teacher_2.current = SMALL_CURRENT
		teacher_2.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_3.current = BIG_CURRENT
		teacher_3.firingrate = current_to_rate(BIG_CURRENT)
		teacher_4.current = SMALL_CURRENT
		teacher_4.firingrate = current_to_rate(SMALL_CURRENT)
		or_teacher.current = BIG_CURRENT
		or_teacher.firingrate = current_to_rate(BIG_CURRENT)
	# Case 7: 1
	elif degree < math.degrees(math.atan(3)):
		teacher_1.current = BIG_CURRENT
		teacher_1.firingrate = current_to_rate(BIG_CURRENT)
		teacher_2.current = SMALL_CURRENT
		teacher_2.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_3.current = SMALL_CURRENT
		teacher_3.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_4.current = SMALL_CURRENT
		teacher_4.firingrate = current_to_rate(SMALL_CURRENT)
		or_teacher.current = BIG_CURRENT
		or_teacher.firingrate = current_to_rate(BIG_CURRENT)
	# Case 8: none
	else:
		teacher_1.current = SMALL_CURRENT
		teacher_1.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_2.current = SMALL_CURRENT
		teacher_2.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_3.current = SMALL_CURRENT
		teacher_3.firingrate = current_to_rate(SMALL_CURRENT)
		teacher_4.current = SMALL_CURRENT
		teacher_4.firingrate = current_to_rate(SMALL_CURRENT)
		or_teacher.current = SMALL_CURRENT
		or_teacher.firingrate = current_to_rate(SMALL_CURRENT)

	print('\n')
	# now process the current for each neuron and update its weights
	for i in range(num_iterations):
		#print('Input Layer')
		for neur in inputlayer:
			neur.process_current()
		#print('Output Layer')
		outputlayer[0].process_current()
		outputlayer[0].update_synapse_weight()

		if i == num_iterations-1: 
			printValues(inputlayer, outputlayer)


######################Run SNN algorithm#######################################

# initialize layers (input, output)
inputlayer = []
outputlayer = []

# initialize input layer and output layer neurons
for i in ['1', '2', '3', '4']: 
	inputlayer.append(LIFneuron(i,[],[],False))
outputlayer.append(LIFneuron('or', [], [], False))

# now create synapses between each of the layers (one per connection/vertice on a graph)
for i in inputlayer:
	syn = Synapse(i,outputlayer[0],0.1)
	i.postsynapses.append(syn)
	outputlayer[0].presynapses.append(syn)

# now create a teaching neuron for each neuron that we have, 
# with a synapse between the teaching neuron and the regular neuron 
	# for.. teaching
	# for create extra synapse

teacher_1 = LIFneuron('teacher_1',[],[],True)
teacher_2 = LIFneuron('teacher_2',[],[],True)
teacher_3 = LIFneuron('teacher_3',[],[],True)
teacher_4 = LIFneuron('teacher_4',[],[],True)
or_teacher = LIFneuron('or_teacher',[],[],True)

teacher_1.spike = True
teacher_2.spike = True
teacher_3.spike = True
teacher_4.spike = True
or_teacher.spike = True

# create the synapses for our connections to the output later
syn1 = Synapse(teacher_1,inputlayer[0],1)
syn2 = Synapse(teacher_2,inputlayer[1],1)
syn3 = Synapse(teacher_3,inputlayer[2],1)
syn4 = Synapse(teacher_4,inputlayer[3],1)
or_syn = Synapse(or_teacher,outputlayer[0],1)

# now append and build the network 
teacher_1.postsynapses.append(syn1)
inputlayer[0].presynapses.append(syn1)
teacher_2.postsynapses.append(syn2)
inputlayer[1].presynapses.append(syn2)
teacher_3.postsynapses.append(syn3)
inputlayer[2].presynapses.append(syn3)
teacher_4.postsynapses.append(syn4)
inputlayer[3].presynapses.append(syn4)
or_teacher.postsynapses.append(or_syn)
outputlayer[0].presynapses.append(or_syn)

#run teach_neurons
#teach_neurons(inputlayer, outputlayer, teacher_1, teacher_2, teacher_3, teacher_4, or_teacher, deg_input_converted, 100)

deg_input_converted = 0
i = 0
while i <= 360: 
	deg_input_converted = i
	print("Degrees: " + str(deg_input_converted))
	if deg_input_converted >= 180 and deg_input_converted <= 270: 
		deg_input_converted -= 180
	teach_neurons(inputlayer, outputlayer, teacher_1, teacher_2, teacher_3, teacher_4, or_teacher, deg_input_converted, 100)
	i += 20










