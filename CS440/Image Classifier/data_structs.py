import random

class Collection:

	def __init__(self, elements = []):
		self.list = elements

	def get_size(self):
		return len(self.list)

	def is_empty(self):
		return self.get_size() == 0

	def contains(self, obj):
		if not hasattr(obj, "equals"):
			return any([obj == element for element in self.list])
		return any([obj.equals(element) for element in self.list])

	def __str__(self):
		return "Contents: {}".format(", ".join([("{" + str(obj) + "}") for obj in self.list]))


class Stack(Collection):
	
	def __init__(self, elements = []):
		super().__init__(elements)

	def push(self, obj):
		self.list.append(obj)
		return True

	def pop(self):
		if self.is_empty():
			return None
		popped = self.list[-1]
		del self.list[-1]
		return popped


class Queue(Collection):
	
	def __init__(self, elements = []):
		super().__init__(elements)

	def enqueue(self, obj):
		self.list.append(obj)
		return True

	def dequeue(self):
		if self.is_empty():
			return None
		dequeued = self.list[0]
		del self.list[0]
		return dequeued


class Heap(Collection):
	
	def __init__(self, elements = [], compare = None, min_heap = True):
		super().__init__()
		self.compare = compare # returns positive if first arg is greater than second arg, zero if equal, negative otherwise
		self.min_heap = min_heap
		for elem in elements:
			self.list.append(elem)
			self._siftup()

	def insert(self, obj):
		self.list.append(obj)
		self._siftup()

	def pop(self):
		popped = self.list[0]
		self.list[0] = self.list[-1]
		del self.list[-1]
		self._siftdown()
		return popped

	def _siftup(self, index = None):
		k = index if index else self.get_size() - 1
		while k > 0:
			p = (k - 1) // 2
			if self.compare:
				if (self.min_heap and self.compare(self.list[k], self.list[p]) < 0) or ((not self.min_heap) and self.compare(self.list[k], self.list[p]) > 0):
					self.list[k], self.list[p] = self.list[p], self.list[k]
					k = p
				else:
					break
			else:
				if (self.min_heap and self.list[k] < self.list[p]) or ((not self.min_heap) and self.list[k] > self.list[p]):
					self.list[k], self.list[p] = self.list[p], self.list[k]
					k = p
				else:
					break

	def _siftdown(self):
		if self.get_size() <= 1:
			return
		k = 0
		left = 2 * k + 1
		while left < self.get_size():
			extreme_index = left
			right = left + 1
			if right < self.get_size():
				if self.compare:
					if (self.min_heap and self.compare(self.list[right], self.list[left]) < 0) or ((not self.min_heap) and self.compare(self.list[right], self.list[left]) > 0):
						extreme_index = right
				else:
					if (self.min_heap and self.list[right] < self.list[left]) or ((not self.min_heap) and self.list[right] > self.list[left]):
						extreme_index = right
			if self.compare:
				if (self.min_heap and self.compare(self.list[k], self.list[extreme_index]) > 0) or ((not self.min_heap) and self.compare(self.list[k], self.list[extreme_index]) < 0):
					self.list[k], self.list[extreme_index] = self.list[extreme_index], self.list[k]
				else:
					break
			else:
				if (self.min_heap and self.list[k] > self.list[extreme_index]) or ((not self.min_heap) and self.list[k] < self.list[extreme_index]):
					self.list[k], self.list[extreme_index] = self.list[extreme_index], self.list[k]
				else:
					break
			k = extreme_index
			left = 2 * k + 1

def test_data_struct(Data_struct, num_elements = 10, upper_bound = 20):
	my_struct = Data_struct()
	insert, delete = None, None
	if Data_struct == Queue:
		insert = my_struct.enqueue
		delete = my_struct.dequeue
	elif Data_struct == Stack:
		insert = my_struct.push
		delete = my_struct.pop
	elif Data_struct == Heap:
		insert = my_struct.insert
		delete = my_struct.pop
	for i in range(num_elements):
		added = random.randint(0, upper_bound)
		print("adding {}".format(added))
		insert(added)
	for i in range(num_elements):
		removed = delete()
		print("removing {}".format(removed))


def main():
	print("Queue")
	test_data_struct(Queue)
	print("Stack")
	test_data_struct(Stack)
	print("Heap")
	test_data_struct(Heap)


if __name__ == "__main__":
	main()