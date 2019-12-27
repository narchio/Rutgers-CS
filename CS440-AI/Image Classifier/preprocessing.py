
class PreProcessor:
	"""
	Face data: Each image is 70 x 61 = 4270 "pixels"
	Digit data: Each image is 28 x 29 = 812 "pixels"

	Open data and label files and "process" the data to be ready for model.

	y: a list of the response variable value for each observation (face or not face, which digit)
	X: a list of lists of size equal to number of observations, each list has size equal to number of features
	y_domain: a set of values that y can take on
	X_domain: dictionary where key = name of feature, value = set of values that feature can take on

	UPDATE: FOR NOW, ONLY NUMERICAL FEATURE NAMES ARE SUPPORTED
	"""
	def __init__(self, data_path, label_path, face_data = True):
		with open(data_path) as file_reader:
			raw_file = file_reader.readlines()
		with open(label_path) as file_reader:
			raw_file_labels = file_reader.readlines()
		lines_per_image = len(raw_file) // len(raw_file_labels)

		image_list = [raw_file[(lines_per_image * i):((i + 1) * lines_per_image)] for i in range(len(raw_file_labels))]
	
		self.raw_data = [tup for tup in zip(image_list, [int(line) for line in raw_file_labels])]
		self.y = [self.raw_data[i][1] for i in range(len(self.raw_data))]
		self.X = [self.get_flattened_image(i) for i in range(len(self.raw_data))]
		self.img_length = lines_per_image
		self.img_width = len(raw_file[0])
		if face_data:
			self.y_domain = {0, 1}
			self.X_domain = {i: {0, 1} for i in range(len(self.X[0]))}
		else:
			self.y_domain = set(range(10))
			self.X_domain = {i: {0, 1, 2} for i in range(len(self.X[0]))}

	def get_image(self, index):
		return "".join([line for line in self.raw_data[index][0]])

	def get_flattened_image(self, index):
		return list(map(lambda c: 1 if c == '#' else 2 if c == '+' else 0, [char for strline in self.raw_data[index][0] for char in strline]))

	def get_mapped_image(self, index):
		return list(map(lambda strline: list(map(lambda c: 1 if c == '#' else 2 if c == '+' else 0, [char for char in strline])), self.raw_data[index][0]))

	def add_feature(self, feature_domain, feature_values):
		feature_name = len(self.X[0])
		for i in range(len(self.X)):
			self.X[i].append(feature_values[i])
		self.X_domain[feature_name] = feature_domain

	# NOTE: THIS METHOD WILL LEAVE BEHIND REMAINING PARTS OF THE IMAGE IN LOWER RIGHT
	@staticmethod
	def partition_image(image, grid_length = 2, grid_width = 2):
		length = len(image)
		width = len(image[0])
		part_length = length // grid_length
		part_width = width // grid_width
		partition_dict = {}
		for i in range(grid_length):
			for j in range(grid_width):
				if i == grid_length - 1 and j == grid_width - 1:
					sublist = [row[j * part_width:] for row in image[i * part_length:]]
				else:
					sublist = [row[j * part_width:(j + 1) * part_width] for row in image[i * part_length:(i + 1) * part_length]]
				partition_dict[(i, j)] = sublist
		return partition_dict

	@staticmethod
	def agg_partitions(partition_dict, agg_func = None):
		if not agg_func:
			agg_func = PreProcessor.unweighted_freq
		return {key: agg_func(partition_dict[key]) for key in partition_dict.keys()}

	@staticmethod
	def unweighted_freq(list_of_lists):
		return sum([1 for sublist in list_of_lists for elem in sublist if elem != 0])


def main():
	face_prep = PreProcessor("./data/facedata/facedatatrain", "./data/facedata/facedatatrainlabels")

	
	# TEST PARTITION FUNCTION #
	part_dict = PreProcessor.partition_image(face_prep.get_mapped_image(0), grid_length = 4, grid_width = 3)
	for key, value in part_dict.items():
		print("=" * 40)
		print(key)
		for strline in value:
			for c in strline:
				if c == 1:
					print('#', end = "")
				else:
					print(' ', end = "")
			print()
	

	# digit_prep = PreProcessor("./data/digitdata/trainingimages", "./data/digitdata/traininglabels", face_data = False)


if __name__ == "__main__":
	main()