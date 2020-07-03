import numpy as np
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt

class IzhikevichNeuron():
	def __init__(self,a,b,c,d):
		self.a = a
		self.b = b
		self.c = c
		self.d = d
		self.v = self.c
		self.vt = 30.0
		self.u = self.b * self.v
		self.spike_count = 0
	
	# run the izhikevich algorithm
	def take_current(self,current,time_diff):
		dvdt = ((0.04* (self.v**2))+ (5*self.v) + 140-self.u+current) / 4
		dudt = self.a * ((self.b * self.v) - self.u) / 4

		# integrate
		self.v += dvdt*time_diff
		self.u += dudt*time_diff

		# if the current voltage has reached the threshold, it spikes and then resets
		if self.v > self.vt:
			self.v = self.c
			self.u += self.d
			self.spike_count += 1


# need our number of itterations -> end_time and our time increments (1 millisec)
end_time = 1000
increment = 0.001
times = np.arange(0,end_time,increment)
voltages = [None]*int(end_time/increment)
currents = [None]*int(end_time/increment)
neur = IzhikevichNeuron(0.02,0.2,-65.0,2.0)

# create the neuron and simulate firing 
for i in range(len(voltages)):
        neur.take_current(5.0,increment)
        currents[i] = 5.0
        voltages[i] = neur.v

plt.plot(times,voltages)
plt.title('Izhikevich Neuron, I=5.0')
plt.xlabel('Time (ms)')
plt.ylabel('Membrane Potential (mV)')
plt.savefig('Izh Plot.png')
plt.close()

