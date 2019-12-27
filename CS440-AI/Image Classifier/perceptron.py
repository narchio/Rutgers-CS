
class Perceptron(Model):

	def __init__(self):
		super().__init__(num_classes, init_weight_val = 0, learning_rate = 1)
		self.num_classes = num_classes
		self.learning_rate = learning_rate
		self.weight_vector_list = [[init_weight_val for i in range(len(self.X[0] + 1))] for i in range(num_classes)]

	def train(self, X, y):
		super().train(X, y)
		for x in self.X:
			pass

	def predict_x(self, x):
		pass

	def _reset(self):
		super()._reset()
		pass