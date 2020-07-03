
class Model:

	def __init__(self):
		self.X = None
		self.y = None

	def predict(self, X):
		return [self.predict_x(x) for x in X]

	def compute_error(self, actual_list, pred_list):
		return sum([1 for i in range(len(actual_list)) if actual_list[i] != pred_list[i]]) / len(actual_list)

	def compute_accuracy(self, actual_list, pred_list):
		return 1 - self.compute_error(actual_list, pred_list)

	"""
	print_progress: print progress in tuning
	UPDATE: ONLY SUPPORTS ONE HYPERPARAMETER
	"""	
	def get_optimal_hparam(self, X, y, tuning_X, tuning_y, start, end, step_size, print_progress = False):
		hparam_list = [i * step_size for i in range(int(start / step_size), int(end / step_size))]
		valid_accuracy = self.tune(X, y, tuning_X, tuning_y, start, end, step_size, print_progress)
		print(valid_accuracy) # for debugging
		max_accuracy = max(valid_accuracy.values())
		return [(k, v) for k, v in valid_accuracy.items() if v == max_accuracy][0]

	"""
	print_progress: print progress in tuning
	"""
	def tune(self, X, y, tuning_X, tuning_y, start, end, step_size, print_progress = False):
		hparam_list = [i * step_size for i in range(int(start / step_size), int(end / step_size))]
		valid_y = tuning_y
		valid_accuracy = {}
		for hparam in hparam_list:
			self._call_train(X, y, hparam)
			valid_accuracy[hparam] = self.compute_accuracy(valid_y, self.predict(tuning_X))
			if print_progress:
				print("Finished hparam = {}, onto next hparam = {}...".format(hparam, hparam + step_size))
		self._reset()
		return valid_accuracy

	def predict_x(self, x):
		pass

	def _reset(self):
		self.X = None
		self.y = None

	def _call_train(self, X, y, hparam):
		pass

	def train(self, X, y):
		self.X = X
		self.y = y
