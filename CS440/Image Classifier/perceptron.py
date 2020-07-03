from preprocessing import PreProcessor
import random
import math
import time

class Perceptron():

	def __init__(self, y, weights, digit_weights):
		self.y_values = y
		self.weights = weights
		self.digit_weights = digit_weights

##########################################################################################################						
										#Face Functions  											
##########################################################################################################
	'''
		calculates 
		- f(xi, weights) = w0*o(xi) + w1*o(xi) + w2*o(xi)... + w4071*o(xi) + w4072
	'''
	def calculate_f_face(self, xi): 
		f_xi_weights = 0
		for i in range(len(xi)):
			f_xi_weights += (self.weights[i] * xi[i])
		# add our last feature to the end, the newest one 
		f_xi_weights += xi[-1]
		return f_xi_weights

	'''
		update the wieghts when a prediction is incorrect
	'''
	def update_weights_face(self, xi, add): 
		multiplier = .9 # 1 default, 73.6% - .9
		# have to subtract when add is false
		updated = []
		if add is False: 
			for i in range(len(xi)):
				updated.append(self.weights[i] - (multiplier)*xi[i])
			updated.append((multiplier)*1 - self.weights[-1])
		# have to add when add is true
		elif add is True: 
			for i in range(len(xi)):
				updated.append(self.weights[i] + (multiplier)*xi[i])
			updated.append((multiplier)*1 + self.weights[-1]) 
		return updated

	'''
		compares values of the final final guesses with the actual ys
		0 -> <= 0 is correct (is not an image) 
		1 -> >= 0 is correct (is an image) 
	'''
	def percentage_correct_face(self, guesses): 
		correct = 0
		total = len(self.y_values)
		for i in range(len(self.y_values)): 
			if (self.y_values[i] == 0) and (guesses[i] <= 0): 
				correct += 1
			elif (self.y_values[i] == 1) and (guesses[i] >= 0):
				correct += 1
		#print("number correct is " + str(correct))
		#print("total is " + str(total))
		return (correct/total) * 100

	'''
		if_face is a 0 for false and a 1 for true
			- if 0 then do digits if false then do faces
		weights will be of size 4720 + 1 (the w0 is the extra weight)
		- our features are each individual pixel, and each pixel has either a 
		0, 1, or 2 (for numbers) and a 0, 1 for faces
		- f(xi, weights) = w0 + w1*o(xi) + w2*o(xi) + w3*o(xi)... + w4072*o(xi)
	'''
	def train_face(self, X_train, y_train): 
		pixels = 70 * 61 # face image pixels
		self.weights = [0 for i in range(pixels+1)]
		self.y_values = y_train
		# now itterate through the flattened list, and do the training on each image
		guess = 0
		guesses = []
		pct_correct = -1
		correct = 0
		total = 0
		random.seed(2)
		while pct_correct < 0.995:
			i = random.randint(0, len(X_train)-1)
			current_image = X_train[i]
			# now, we have current image, so calculate the f value
			guess = self.calculate_f_face(current_image)
			guesses.append(guess)

			# if not face and we are positive, have to subtract 
			if (self.y_values[i] == 0) and (guess >= 0): 
				add = False
				while (guess >= 0): 
					# update the weights
					self.weights = self.update_weights_face(current_image, add) 
					# then, recalculate
					guess = self.calculate_f_face(current_image)
			
			# if face and we are negative, have to subtract 
			elif (self.y_values[i] == 1) and (guess <= 0): 
				add = True
				while (guess <= 0): 
					self.weights = self.update_weights_face(current_image, add) 
					guess = self.calculate_f_face(current_image)
			else:
				correct = correct + 1
			total = total + 1
			pct_correct = correct / total
		return str(pct_correct * 100)
			

	def predict_face(self, X_Validation, y_Validation): 
		self.y_values = y_Validation
		guess = 0
		guesses = []
		for i in range(len(X_Validation)):
			current_image = X_Validation[i]
			# now, we have current image, so calculate the f value
			guess = self.calculate_f_face(current_image)
			guesses.append(guess)

		percent_correct = self.percentage_correct_face(guesses)
		return str(percent_correct)

##########################################################################################################				
										# Digit Functions  										
##########################################################################################################
	'''
		Calculate f values for each digit 0-9 and take the max value
			f(xi, weights) = w0*o(xi) + w1*o(xi) + w2*o(xi)... + w4071*o(xi) + w4072
		Self.weights is a 2D array of size 10 with indeces 0-9 representing those digits, with thier cooresponding list 
		equal to the weights for that digit 
		Returns the index of the max f value, or the 'digit' that we have guessed with the highest certainty 
	'''
	def calculate_f_digit(self, xi): 
		#print("length of the list at : " + str(i) + " " + str(self.digit_weights[i][-1]))
		f_digits = []
		for i in range(10):
			sum = 0
			for j in range(len(xi)):
				sum += (self.digit_weights[i][j] * xi[j])
			sum += self.digit_weights[i][-1]
			f_digits.append(sum)
		maxInt = f_digits.index(max(f_digits))
		return maxInt


	'''
		If correct guess, move on. 
		If incorrect guess, 2 things must happen: 
			1. The incorrect guess's weights must be either decremented by each of its features
			2. The weights for the correct digit's weights must be incremented, the opposite of step 1 
	'''
	def check_guess_accuracy_and_update(self, guess, y, xi): 
		multiplier = .5 # 81.2% validation w/ .5 with 99% accuracy on the training data
		add_to_guesses = []
		add_to_ys = []

		if y == guess: 
			return True
		else: 
			for j in range(len(xi)):
				add_to_guesses.append(self.digit_weights[guess][j] - (multiplier * xi[j]))
				add_to_ys.append(self.digit_weights[y][j] + (multiplier * xi[j]))
			add_to_guesses.append(self.digit_weights[guess][-1] - (multiplier*1))
			add_to_ys.append(self.digit_weights[y][-1] + (multiplier*1))

			self.digit_weights[guess] = add_to_guesses
			self.digit_weights[y] = add_to_ys
			return False


	def percentage_correct_digit(self, guesses): 
		correct_zipped = list(zip(self.y_values, guesses))
		correct = [i for i, j in correct_zipped if i == j]
		return (len(correct)/len(self.y_values)) * 100


	'''
		weights will be of size 4720 + 1 (the w0 is the extra weight)
		Length of training set (X_train is 451 (usually) aka how many images we have)
			- our features are each individual pixel, and each pixel has either a 0, 1, or 2 (for numbers) 
			- f(xi, weights) = w0 + w1*o(xi) + w2*o(xi) + w3*o(xi)... + w4072*o(xi)
	'''
	def train_digit(self, X_train, y_train): 
		pixels = 28 * 29 # digit image pixels
		self.digit_weights = [[0] * (pixels+1)] * 10
		self.y_values = y_train
		# initialize the weights list, to be 1 more than the # pixels in the image
		guess = 0
		guesses = []
		pct_correct = -1
		correct = 0
		total = 0
		random.seed(2)
		while pct_correct < 0.95:
			i = random.randint(0, len(X_train)-1)
			current_image = X_train[i]			
			# Calculate the f value and assign the 'digit' we have guessed to guess
			guess = self.calculate_f_digit(current_image)
			guesses.append(guess)
			check = self.check_guess_accuracy_and_update(guess, self.y_values[i], current_image)
			if check is True: 
				correct += 1
			total += 1
			pct_correct = correct / total
			# if pct_correct > .8999: 
			# 	print("number correct: " + str(correct) + "total " + str(total))
			# 	print(pct_correct)
		return str(pct_correct * 100)


	def predict_digit(self, X_Validation, y_Validation): 
		self.y_values = y_Validation
		guess = 0
		guesses = []
		for i in range(len(X_Validation)):
			current_image = X_Validation[i]
			# now, we have current image, so calculate the f value
			guess = self.calculate_f_digit(current_image)
			guesses.append(guess)

		percent_correct = self.percentage_correct_digit(guesses)
		return str(percent_correct) 


##########################################################################################################							
										# Statistic Functions  							
##########################################################################################################

	# gets the percentages of face training data from 10% -> 100% and tests them on the validation data
	def get_percentage_of_train_face(self, dataX, dataY, data_valX, data_valY): 
		# random.seed(1)
		start = time.time()
		acc = []
		time_list = []
		percent = 10
		while percent <= 100: 
			length = len(dataX) 
			x_percent = int(length * percent/100)
			
			x_percent_dataX = []
			x_percent_dataY = []
			for i in range(0, x_percent):
				num = random.randint(0, length-1)
				x_percent_dataX.append(dataX[num])
				x_percent_dataY.append(dataY[num])
			# now run the training
			correct = self.train_face(x_percent_dataX, x_percent_dataY)
			# now run the validation
			validation = self.predict_face(data_valX, data_valY)
			item = validation[:4]
			acc.append(item)
			# print(item + "%")
			percent += 10
			elapsed = time.time() - start
			time_list.append(elapsed)
		return acc, time_list


	# gets the percentages of digit training data from 10% -> 100% and tests them on the validation data
	def get_percentage_of_train_digit(self, dataX, dataY, data_valX, data_valY): 
		# random.seed(1)
		start = time.time()
		acc = []
		time_list = []
		percent = 10
		while percent <= 100: 
			length = len(dataX) 
			x_percent = int(length * percent/100)
			#print("length = " + str(length) + " x_percent is " + str(x_percent))
			
			x_percent_dataX = []
			x_percent_dataY = []
			for i in range(0, x_percent):
				num1 = random.randint(0, length-1)
				x_percent_dataX.append(dataX[num1])
				x_percent_dataY.append(dataY[num1])
			# now run the training
			correct = self.train_digit(x_percent_dataX, x_percent_dataY)
			# and now validate the training 
			validation = self.predict_digit(data_valX, data_valY)
			#print ("validation is : " + str(validation))
			item = validation[:4]
			acc.append(item)
			# print(item + "%")
			percent += 10
			elapsed = time.time() - start
			time_list.append(elapsed)
		return acc, time_list


# for testing purposes
def main():
	random.seed(1)
	face_data = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatatrain", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatatrainlabels")
	# create structure
	perceptron = Perceptron([],[],[]) 
	correct = perceptron.train_face(face_data.X, face_data.y)
	print("The percentage correct on training data is " + str(correct))
	print('\n')


	face_data_validation = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatavalidation", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatavalidationlabels")
	# create structure
	correctV = perceptron.predict_face(face_data_validation.X, face_data_validation.y)
	print("The percentage correct on validation data is " + str(correctV))
	print('\n')


	# face test data
	test_face = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatatest", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatatestlabels")
	correct_test_f = perceptron.predict_face(test_face.X, test_face.y)
	print("The percentage correct on face test data is " + str(correct_test_f))
	print('\n')

	# i = 0; 
	# while (i < 5): 
	# 	face_data3 = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatatrain", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatatrainlabels")
	# 	face_data_validation3 = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatavalidation", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/facedata/facedatavalidationlabels")
	# 	perceptron3 = Perceptron([],[],[]) 	

	# 	totals, time_list_face = perceptron3.get_percentage_of_train_face(face_data3.X, face_data3.y, face_data_validation3.X, face_data_validation3.y)
	# 	print("face totals of " + str(i) + " " + str(totals))
	# 	print("face times of " + str(i) + " " + str(time_list_face))
	# 	print('\n')


	# 	digit_data3 = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/trainingimages", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/traininglabels")
	# 	digit_data_validation3 = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/validationimages", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/validationlabels")
	# 	perceptron_digit = Perceptron([],[],[])

	# 	totals_digit, time_list_digit = perceptron_digit.get_percentage_of_train_digit(digit_data3.X, digit_data3.y, digit_data_validation3.X, digit_data_validation3.y)
	# 	print("digit totals of " + str(i) + " " + str(totals_digit))
	# 	print("digit times of " + str(i) + " " + str(time_list_digit))
	# 	print('\n')

	# 	i = i + 1

	digit_data = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/trainingimages", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/traininglabels")
	# create structure
	perceptron_digit = Perceptron([],[],[]) 
	correctd = perceptron_digit.train_digit(digit_data.X, digit_data.y)
	print("The percentage correct on training data is " + correctd)
	print('\n')


	digit_data = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/validationimages", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/validationlabels")
	# create structure
	correctVd = perceptron_digit.predict_digit(digit_data.X, digit_data.y)
	print("The percentage correct on validation data is " + correctVd)
	print('\n')


	# digit test data
	test_digits = PreProcessor("/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/testimages", "/Users/nicolascarchio/Desktop/AI_FinalProj/data/digitdata/testlabels")
	correct_test_d = perceptron_digit.predict_digit(test_digits.X, test_digits.y)
	print("The percentage correct on digit test data is " + correct_test_d)
	print('\n')



if __name__ == "__main__":
	main()	


			