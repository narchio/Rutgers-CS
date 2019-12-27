from preprocessing import PreProcessor
from model import Model
import math

class NaiveBayes(Model):

	DEFAULT_K = 0.0001

	"""
	prob_X: dictionary where key = (x feature, x value, y value), value = Pr(x feature = x value | y value)
	prob_y: dictionary where key = y value, value = Pr(y = y value)
	"""
	def __init__(self, X_domain, y_domain):
		super().__init__()
		self.X_domain = X_domain
		self.y_domain = y_domain
		self.prob_y = None
		self.prob_X = None

	def _reset(self):
		super()._reset()
		self.prob_y = None
		self.prob_X = None

	"""
	k = smoothing parameter
	"""
	def train(self, X, y, **kwargs):
		super().train(X, y)
		k = NaiveBayes.DEFAULT_K
		if "k" in kwargs.keys():
			k = kwargs["k"]
		num_obs = len(self.y)
		counts_y = {val: sum([1 for y in self.y if y == val]) for val in self.y_domain}
		self.prob_y = {val: counts_y[val] / num_obs for val in self.y_domain}
		self.prob_X = {(x_feature, x, y): (sum([1 for j in range(num_obs) if (self.y[j] == y and self.X[j][x_feature] == x)]) + k) 
											/ (counts_y[y] + 2 * k)
								for x_feature, domain in self.X_domain.items() for x in domain for y in self.y_domain}

	def _call_train(self, X, y, hparam):
		self.train(X, y, k = hparam)

	def predict_x(self, x):
		num_features = len(self.X[0])
		y_weights = {y: math.log(self.prob_y[y]) 
						+ sum([math.log(self.prob_X[x_feature, x[x_feature], y]) for x_feature in self.X_domain.keys()]) 
						for y in self.y_domain}
		max_weight = max(y_weights.values())
		return [k for k, v in y_weights.items() if v == max_weight][0]


# try differentiating between pixels on the edge and not on the edge?
def test_faces():
	train_prep = PreProcessor("./data/facedata/facedatatrain", "./data/facedata/facedatatrainlabels")
	valid_prep = PreProcessor("./data/facedata/facedatavalidation", "./data/facedata/facedatavalidationlabels")
	nb = NaiveBayes(train_prep.X_domain, train_prep.y_domain)

	"""
	# TUNING PROCEDURE #
	step_size = 0.5
	start_point = 0.5
	end_point = 1.5 # 10
	optimal_k, valid_accuracy = nb.get_optimal_hparam(train_prep.X, train_prep.y, valid_prep.X, valid_prep.y, start_point, end_point, step_size)
	print("Optimal k: {}\nAccuracy: {}".format(optimal_k, valid_accuracy)) # 2.5
	"""
	

	nb.train(train_prep.X, train_prep.y, k = 2.5)
	train_pred_list = nb.predict(train_prep.X)
	valid_pred_list = nb.predict(valid_prep.X)

	print("Training Accuracy: {}".format(nb.compute_accuracy(nb.y, train_pred_list)))
	print("Validation Accuracy: {}".format(nb.compute_accuracy(valid_prep.y, valid_pred_list)))

	
	"""
	# FINAL TEST # 90%
	final_nb = NaiveBayes(train_prep.X_domain, train_prep.y_domain)
	final_nb.train(train_prep.X + valid_prep.X, train_prep.y + valid_prep.y, k = 2.5)
	test_prep = PreProcessor("./data/facedata/facedatatest", "./data/facedata/facedatatestlabels")
	test_pred_list = nb.predict(test_prep.X)
	print("Test Accuracy: {}".format(nb.compute_accuracy(test_prep.y, test_pred_list)))
	"""
	


# what if you remove the differentiation between + and #?
def test_digits():
	train_prep = PreProcessor("./data/digitdata/trainingimages", "./data/digitdata/traininglabels", face_data = False)
	valid_prep = PreProcessor("./data/digitdata/validationimages", "./data/digitdata/validationlabels", face_data = False)
	nb = NaiveBayes(train_prep.X_domain, train_prep.y_domain)

	"""
	# TUNING PROCEDURE #
	step_size = 0.005
	start_point = 0.005
	end_point = 0.035
	optimal_k, valid_accuracy = nb.get_optimal_hparam(train_prep.X, train_prep.y, valid_prep.X, valid_prep.y, start_point, end_point, step_size, print_progress = True)
	print("Optimal k: {}\nAccuracy: {}".format(optimal_k, valid_accuracy)) # 0.02
	"""

	nb.train(train_prep.X, train_prep.y, k = 0.02)
	train_pred_list = nb.predict(train_prep.X)
	valid_pred_list = nb.predict(valid_prep.X)

	print("Training Accuracy: {}".format(nb.compute_accuracy(nb.y, train_pred_list)))
	print("Validation Accuracy: {}".format(nb.compute_accuracy(valid_prep.y, valid_pred_list)))

	"""
	# FINAL TEST # 77.9 %
	final_nb = NaiveBayes(train_prep.X_domain, train_prep.y_domain)
	final_nb.train(train_prep.X + valid_prep.X, train_prep.y + valid_prep.y, k = 0.02)
	test_prep = PreProcessor("./data/digitdata/testimages", "./data/digitdata/testlabels")
	test_pred_list = nb.predict(test_prep.X)
	print("Test Accuracy: {}".format(nb.compute_accuracy(test_prep.y, test_pred_list)))
	"""

def main():
	test_faces()
	test_digits()

	"""
	## checks correctness in self.prob_X calculation ##
	print(all([sum([nb.prob_X[feature, x, y] for x in nb.X_domain[feature]]) == 1] for feature in nb.X_domain.keys() for y in nb.y_domain))
	"""


if __name__ == "__main__":
	main()