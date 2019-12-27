from preprocessing import PreProcessor
from model import Model
import data_structs
import math

class KNearestNeighbors(Model):

	dist_metrics = ["unweighted differences", "weighted differences", "euclidean"]
	DEFAULT_K = 10

	def __init__(self, dist_metric = "unweighted differences"):
		super().__init__()
		if not dist_metric in KNearestNeighbors.dist_metrics:
			raise ValueError("Invalid distance metric")
		self.dist_metric = dist_metric
		self.k = None

	def predict_x(self, x):
		Xy = list(zip(self.X, self.y))
		distances_to_x = [(self._compute_distance(x, x_obs), y_label, x_obs) for x_obs, y_label in Xy]
		dist_heap = data_structs.Heap(elements = distances_to_x, 
			compare = lambda tup1, tup2: 1 if tup1[0] > tup2[0] else -1)
		k_closest_labels = []
		for i in range(self.k):
			k_closest_labels.append(dist_heap.pop()[1])
		return max(set(k_closest_labels), key = k_closest_labels.count)

	def train(self, X, y, **kwargs):
		super().train(X, y)
		self.k = KNearestNeighbors.DEFAULT_K
		if "k" in kwargs.keys():
			self.k = kwargs["k"]

	def _call_train(self, X, y, hparam):
		self.train(X, y, k = hparam)

	def _compute_distance(self, x1, x2):
		if self.dist_metric == "unweighted differences":
			return self._unweighted_diff(x1, x2)
		elif self.dist_metric == "weighted differences":
			return self._weighted_diff(x1, x2)
		elif self.dist_metric == "euclidean":
			return self._euclidean(x1, x2)

	def _unweighted_diff(self, x1, x2):
		return math.sqrt(sum([1 for i in range(len(x1)) if x1[i] != x2[i]]))

	def _weighted_diff(self, x1, x2):
		return math.sqrt(sum([self._get_weight(x1[i], x2[i]) for i in range(len(x1))]))

	def _euclidean(self, x1, x2):
		return math.sqrt(sum([(x1[i] - x2[i]) ** 2 for i in range(len(x1))]))

	def _get_weight(self, val1, val2):
		return 0 if val1 == val2 else 1 if ((val1 == '+' and val2 != '+') or (val1 != '+' and val2 == '+')) else 2


def add_partition_features(prep, grid_length, grid_width):
	feat_dicts = [] # length of list is number of obs, each element is dictionary of agged feature
	for i in range(len(prep.raw_data)):
		mapped_image = prep.get_mapped_image(i)
		part_dict = PreProcessor.partition_image(mapped_image, grid_length = grid_length, grid_width = grid_width)
		feat_dicts.append(PreProcessor.agg_partitions(part_dict))
	key_list = list(feat_dicts[0].keys())
	for key in key_list:
		prep.add_feature(None, [feat_dicts[i][key] for i in range(len(feat_dicts))])


def test_faces():
	grid_length = 4
	grid_width = 3

	train_prep = PreProcessor("./data/facedata/facedatatrain", "./data/facedata/facedatatrainlabels")
	add_partition_features(train_prep, grid_length, grid_width)
	reduced_train_prep = [x[-1 * grid_length * grid_width:] for x in train_prep.X]

	valid_prep = PreProcessor("./data/facedata/facedatavalidation", "./data/facedata/facedatavalidationlabels")
	add_partition_features(valid_prep, grid_length, grid_width)
	reduced_valid_prep = [x[-1 * grid_length * grid_width:] for x in valid_prep.X]
	kNN = KNearestNeighbors(dist_metric = "euclidean")

	"""
	# TUNING PROCEDURE #
	step_size = 1
	start_point = 1
	end_point = 31
	optimal_k, valid_accuracy = kNN.get_optimal_hparam(reduced_train_prep, train_prep.y, reduced_valid_prep, valid_prep.y, 
														start_point, end_point, step_size)
	print("Optimal k: {}\nAccuracy: {}".format(optimal_k, valid_accuracy)) # 6
	"""
	
	
	kNN.train(reduced_train_prep, train_prep.y, k = 6)
	# train_pred_list = kNN.predict(reduced_train_prep)
	# print("Training Accuracy: {}".format(kNN.compute_accuracy(kNN.y, train_pred_list)))
	valid_pred_list = kNN.predict(reduced_valid_prep)
	print("Validation Accuracy: {}".format(kNN.compute_accuracy(valid_prep.y, valid_pred_list)))
	
	
	"""
	# FINAL TEST #
	final_kNN = KNearestNeighbors()
	final_kNN.train(train_prep.X + valid_prep.X, train_prep.y + valid_prep.y)
	test_prep = PreProcessor("./data/facedata/facedatatest", "./data/facedata/facedatatestlabels")
	test_pred_list = kNN.predict(test_prep.X)
	print("Test Accuracy: {}".format(final_kNN.compute_accuracy(test_prep.y, test_pred_list)))
	"""


def test_digits():
	pass


def main():
	test_faces()
	test_digits()


if __name__ == "__main__":
	main()