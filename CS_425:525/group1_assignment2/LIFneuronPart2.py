CAPACITANCE = 4.0
RESISTANCE = 2.5
V_R = 0.0 # resting voltage
V_T = 30.0 # threshold voltage
TIME_THRESHOLD = 100 # time limit for temporal encoding for the neuron to fire or not 
INCREMENT = 0.001 # time increment from Leaky Integrate and Fire neuron
GAMMA = 0.005 # Oja's rule constant 'Gamma'

'''
        Current to Rate Function:
	rate = 7.05632549e-06 * curr + 9.06745868e-03
	curr = (rate - 9.06745868e-03) / 7.05632549e-06
	
	This was calculated by taking a LIF neuron with these exact parameters and running the currents 100 through 50000
	through it and tracking the firing rate with each current. Then a linear function was fitted to the data.
'''
def rate_to_current(rate):
	curr = (rate - 9.06745868e-03) / 7.05632549e-06
	return curr

def current_to_rate(current):
	rate = 7.05632549e-06 * current + 9.06745868e-03
	return rate

class LIFneuron:
	def __init__(self,name, presynapses,postsynapses,teaching):
		self.name = name
		self.presynapses = presynapses
		self.postsynapses = postsynapses
		self.teaching = teaching 
		self.time = 0 # time it spiked
		self.firingrate = 0
		self.spike = False
		self.current = 0
		self.firingrate_history = [0]
		
	'''
	This function processes the current and calculates if the neuron spikes or not 
	based on Temporal encoding (time limit is 100)  
	- if we spike within the allotted then we record it
	'''
	def process_current(self):

    	# calculate the weighted currents by combining all of the currents of the presynapses
		current = 0	
		#for each of the presynapses:  
    		# convert rate to current 
    		# multiply the currents by some weight, and sum them together into 'current'
		for syn in self.presynapses:
			if syn.preneuron.spike:
				syncurrent = rate_to_current(syn.preneuron.firingrate)
				current += syn.weight*syncurrent
		#print(self.name+' current: '+str(current))
			
		# run the LIF algorithm 
		# the voltage right now and instantiate a spike boolean
		vm = V_R  
		self.spike = False
		count = 1
		while count <= TIME_THRESHOLD: 
			dvdt = (current-vm/RESISTANCE)/CAPACITANCE
			#print(dvdt)
			vm += dvdt*INCREMENT
			# if the spike runs 
			if vm >= V_T:
				self.spike = True
				self.firingrate = 1.0 / count
				break 
			# increment counter
			count += 1
		# assign the neuron's time that it spiked to the current count 
		self.time = count
		# if it hasn't spiked yet, we want to note that it DID NOT fire
		if not self.spike: 
			self.firingrate = 0
		self.firingrate_history.append(self.firingrate)
		# print(self.name+' firing rate: '+str(self.firingrate))
		
	def update_synapse_weight(self):
		for syn in self.presynapses:
			if not syn.preneuron.teaching:
				vi = self.firingrate
				avg_vi = sum(self.firingrate_history)/len(self.firingrate_history)
				vj = syn.preneuron.firingrate

				# print('vi: '+str(vi)+' vj: '+str(vj)+' weight: '+str(syn.weight))
				
				count = 0
				while count<TIME_THRESHOLD:
					dwdt = GAMMA*vi*(vj-syn.weight*vi)
					syn.weight += dwdt*INCREMENT
					count += 1
					
	def STDP_weights(self):
		if self.spike:
			for syn in self.presynapses:
				if not syn.preneuron.teaching:
					vi = self.firingrate
					vj = syn.preneuron.firingrate
					count = 0
					while count<TIME_THRESHOLD:
						if syn.preneuron.spike:
							dwdt = GAMMA*vi*(vj-syn.weight*vi)
						else:
							dwdt = -1*GAMMA*vi*(vj-syn.weight*vi)
							
						syn.weight += dwdt*INCREMENT
						count += 1


        
