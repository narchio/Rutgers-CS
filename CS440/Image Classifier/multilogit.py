import math

class MultiLogit(Model):

	def __init__(self, y_domain, init_weight_val = 0, learning_rate = 1, max_iterations = 1000, threshold = 0.0001):
		super().__init__()
		self.y_domain = y_domain
		self.init_weight_val = init_weight_val
		self.learning_rate = learning_rate
		self.max_iterations = max_iterations
		self.threshold = threshold
		# key is class, value is list of weights
		self.weight_vector_dict = {key: [self.init_weight_val for _ in range(len(self.X[0]))] for key in self.y_domain} 

	def predict_x(self, x):
		pass

	def _reset(self):
		super()._reset()
		self.y_domain = None
		self.init_weight_val = None
		self.learning_rate = None
		self.max_iterations = None
		self.threshold = None

	"""
	lambda_param = hyper-parameter for penalized logistic regression
	TRAIN METHOD WILL ADD INTERCEPT AUTOMATICALLY
	"""
	def train(self, X, y, lambda_param = 0):
		self.X = [[1] + x for x in X]
		self.y = y
		self.train_class(self.weight_vector_dict, key, lambda_param)

	def _call_train(self, X, y, hparam):
		self.train(X, y, lambda_param = hparam)

	def train_class(self, weight_vector_dict, key, lambda_param = 0):
		new_y = list(map(lambda y_label: 1 if y_label == key else 0), self.y)


	def _get_loglikelihood(self, y_vector, class_key):
		probs = [MultiLogit._get_prob(x, self.weight_vector_dict[class_key]) for x in self.X]
		penalty = self.lambda_param * sum(self.weight_vector_dict[class_key])
		return sum([y_vector[i] * math.log(probs[i]) + (1 - y_vector[i]) * math.log(1 - probs[i]) for i in range(len(probs))]) + penalty

	@staticmethod
	def _sigmoid(val):
		return 1 / (1 + math.exp(-1 * val))

	@staticmethod
	def _linear_comp(x, weights):
		return sum([x[i] * weights[i] for i in range(len(x))])

	@staticmethod
	def _get_prob(x, weights):
		return MultiLogit._sigmoid(MultiLogit._linear_comp(x, weights))


