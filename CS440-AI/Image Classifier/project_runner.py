from preprocessing import PreProcessor
from naive_bayes import NaiveBayes
from random import shuffle
from time import time
from statistics import stdev, mean

def main():
	train_prep_face = PreProcessor("./data/facedata/facedatatrain", "./data/facedata/facedatatrainlabels")
	valid_prep_face = PreProcessor("./data/facedata/facedatavalidation", "./data/facedata/facedatavalidationlabels")
	test_prep_face = PreProcessor("./data/facedata/facedatatest", "./data/facedata/facedatatestlabels")
	train_prep_digit = PreProcessor("./data/digitdata/trainingimages", "./data/digitdata/traininglabels", face_data = False)
	valid_prep_digit = PreProcessor("./data/digitdata/validationimages", "./data/digitdata/validationlabels", face_data = False)
	test_prep_digit = PreProcessor("./data/digitdata/testimages", "./data/digitdata/testlabels", face_data = False)
	print("Naive Bayes")
	print("=" * 80)
	print("Face Recognition")
	print("-" * 80)
	face_nb = NaiveBayes(train_prep_face.X_domain, train_prep_face.y_domain)
	print_results(face_nb, train_prep_face.X, train_prep_face.y, test_prep_face.X, test_prep_face.y, k = 2.5)
	print("Digit Recognition")
	print("-" * 80)
	digit_nb = NaiveBayes(train_prep_digit.X_domain, train_prep_digit.y_domain)
	print_results(digit_nb, train_prep_digit.X, train_prep_digit.y, test_prep_digit.X, test_prep_digit.y, k = 0.02)

	"""
	print("K-Nearest-Neighbors")
	print("=" * 80)
	print("Face Recognition")
	print("-" * 80)
	"""


def print_results(model, train_X, train_y, test_X, test_y, **kwargs):
	results, mean_error, std_error = get_results(model, train_X, train_y, test_X, test_y, **kwargs)
	print(results)
	print("Average Prediction Error: {}".format(mean_error))
	print("Standard Deviation: {}\n".format(std_error))


# Standard Deviation is sample version, kwargs is for extra arguments going into model.train()
def get_results(model, train_X, train_y, test_X, test_y, **kwargs):
	zipped = list(zip(train_X, train_y))
	shuffle(zipped)
	unzipped = [[i for i, j in zipped], [j for i, j in zipped]]
	shuffled_train_X = unzipped[0]
	shuffled_train_Y = unzipped[1]
	results = {"Percentage of Training Data": [], "Training Time": [], "Prediction Error": []}
	num_obs = len(train_X)
	for i in range(1, 11):
		limit = i * num_obs // 10
		start_time = time()
		model.train(shuffled_train_X[:limit], shuffled_train_y[:limit], **kwargs)
		elapsed_time = time() - start_time
		results["Percentage of Training Data"].append(i * 10)
		results["Training Time"].append(elapsed_time)
		test_preds = model.predict(test_X)
		results["Prediction Error"].append(model.compute_error(test_y, test_preds))
	return results, mean(results["Prediction Error"]), stdev(results["Prediction Error"])


if __name__ == "__main__":
	main()