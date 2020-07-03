import numpy as np
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt

'''
	Leaky Integrate and Fire Neuron
		- To overcome the issues with the IF (Integrate and Fire model), it introduces
		a 'leak' in the current levels, as it is not realistic always have a constant current. 
		Additionally, it introduces a resistance perameter as well. 
'''

class LIFneuron():
	
	def __init__(self,vr,vt,capacitance,resistance):
		self.vr = vr
		self.capacitance = capacitance
		self.resistance = resistance
		self.vm = self.vr
		self.vt = vt
		self.spike_count = 0
	
	# run the leaky integrate and fire neuron algorithm
	def take_current(self,current,time_diff):
		dvdt = (current-self.vm/self.resistance)/self.capacitance
		self.vm += dvdt*time_diff
		if self.vm>=self.vt:
			self.vm = self.vr
			self.spike_count += 1


# need our number of itterations -> end_time and our time increments (1 millisec)
end_time = 1000
increment = 0.001
times = np.arange(0,end_time,increment)
voltages = [None]*int(end_time/increment)
currents = [None]*int(end_time/increment)

# create the neuron and simulate firing 
neur = LIFneuron(0.0,30.0,4.0,2.5)
for i in range(len(voltages)):
	if i<len(voltages)/10:
		neur.take_current(0.0,increment)
		currents[i] = 0.0
	elif i<len(voltages)*3/10:
		neur.take_current(10.0,increment)
		currents[i] = 10.0
	elif i<len(voltages)/2:
		neur.take_current(0.0,increment)
		currents[i] = 0.0
	elif i<len(voltages)*4/5:
		neur.take_current(12.5,increment)
		currents[i] = 12.5
	else:
		neur.take_current(0.0,increment)
		currents[i] = 0.0
	voltages[i] = neur.vm