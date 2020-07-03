'''
Nicolas Carchio (nfc28), undergraduate
Dan Sumetsky (ds1461), undergraduate
Zain Sayed (zjs15), undergraduate
Yashshree Patil (yap14), graduate

Original paper: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC1392413/pdf/jphysiol01442-0106.pdf
Other resources: https://neuronaldynamics.epfl.ch/online/Ch2.S2.html, 
https://www.st-andrews.ac.uk/~wjh/hh_model_intro/

Potential initial values for E (ENa, EK, EL) and gx (gNa, gK, gL):
x 	Ex	 gx
Na  55   40
K   -77  35
L   -65  0.3

The Hodgkin-Huxley model is: 
	I = Cm dVm/dt + gK * n^4(EK) + gNa * m^3 * h(ENa) + gL(EL)
'''
import math
import numpy as np
import matplotlib.pyplot as plt

class HHneuron: 

	def __init__(self, capacitance, gK, gNa, gL, n, m, h):
		self.capacitance = capacitance
		self.gK = gK
		self.gNa = gNa
		self.gL = gL
		self.n = n
		self.m = m
		self.h = h
		self.Vr = -65.0
		self.Vm = self.Vr
		# define alphas and beta (n, m, h)
		self.alpha_n = (-0.01 * (self.Vm + 60)) / (math.exp((60 + self.Vm) / -10) - 1)
		self.beta_n = 0.125 * math.exp((self.Vm + 70) / 80)
		self.alpha_m = (-0.1 * (self.Vm + 45)) / (math.exp((45 + self.Vm) / -10) - 1)
		self.beta_m = 4 * math.exp((self.Vm + 70) / -18)
		self.alpha_h = 0.07 * math.exp((self.Vm + 70) / -20)
		self.beta_h = 1 / (math.exp((40 + self.Vm) / -10) + 1)
		
		# initial values for E(x)
		self.EK = -77
		self.ENa = 55
		self.EL = -65


	# run the Hodgkin-Huxley algorithm
	def take_current(self, current, time_diff):
		# define alphas and beta (n, m, h)
		self.alpha_n = (-0.01 * (self.Vm + 60)) / (math.exp((60 + self.Vm) / -10) - 1)
		self.beta_n = 0.125 * math.exp((self.Vm + 70) / 80)
		self.alpha_m = (-0.1 * (self.Vm + 45)) / (math.exp((45 + self.Vm) / -10) - 1)
		self.beta_m = 4 * math.exp((self.Vm + 70) / -18)
		self.alpha_h = 0.07 * math.exp((self.Vm + 70) / -20)
		self.beta_h = 1 / (math.exp((40 + self.Vm) / -10) + 1)

		# differentiate the Vm (voltage)
		dVmdt = (current - (self.gK * self.n**4 * (self.Vm-self.EK) + 
							self.gNa * self.m**3 * self.h * (self.Vm-self.ENa) + 
							self.gL * (self.Vm-self.EL))) / self.capacitance

		# differentiate (n, m, h)
		dndt = self.alpha_n * (1 - self.n) - self.beta_n * self.n
		dmdt = self.alpha_m * (1 - self.m) - self.beta_m * self.m
		dhdt = self.alpha_h * (1 - self.h) - self.beta_h * self.h

		# update vars 
		self.Vm += dVmdt*time_diff
		self.n += dndt*time_diff
		self.m += dmdt*time_diff
		self.h += dhdt*time_diff


# need our number of itterations -> end_time and our time increments (1 millisec)
end_time = 1000
end_time = 50
increment = 0.0001
times = np.arange(0, end_time, increment)
voltages = [None] * int(end_time/increment)
neur = HHneuron(0.01, 0.36, 1.2, 0.003, 0.05, 0.33, 0.59)
current = 0.3

# now loop through the HH model 
for i in range(len(voltages)): 
	neur.take_current(current, increment)
	voltages[i] = neur.Vm

plt.plot(times,voltages)
plt.title('Hodgkin-Huxley Neuron, I=0.3')
plt.xlabel('Time (ms)')
plt.ylabel('Membrane Potential (mV)')
plt.savefig('HH.png')
plt.close()
