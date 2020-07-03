import astar
import pygame
import gridFunc
import xlwt
import time
import random

NUM_TIMES = 10

def write_row(sheet, row, row_list):
	for i in range(len(row_list)):
		sheet.write(row, i, row_list[i])

def main():
	book = xlwt.Workbook()
	fwd_sheet = book.add_sheet("Forward")
	# bwd_sheet = book.add_sheet("Backward")
	adpt_sheet = book.add_sheet("Adaptive")
	sheets = [fwd_sheet, adpt_sheet]
	columns = ["Observation", "StartX", "StartY", "EndX", "EndY", "Time", "Cells Expanded"]
	for i in range(len(columns)):
		for sheet in sheets:
			sheet.write(0, i, columns[i])

	random.seed(7)

	for i in range(NUM_TIMES):
		pygame.init()
		# start coords
		rand_start_x = random.randint(0, 100)
		rand_start_y = random.randint(0, 100)
		startCoord = (rand_start_x, rand_start_y)

		# goal coords
		rand_goal_x = random.randint(0, 100)
		rand_goal_y = random.randint(0, 100)
		goalCoord =  (rand_goal_x, rand_goal_y)

		# initialize grid
		forward_grid = gridFunc.grid_init(startCoord, goalCoord)
		astar_grid = gridFunc.grid_init_white(startCoord, goalCoord)

		# # initialize grid for backward
		# backward_grid = gridFunc.grid_copy(forward_grid)
		# backward_astar_grid = gridFunc.grid_init_white(startCoord, goalCoord)

		# initialize grid for adaptive
		adaptive_grid = gridFunc.grid_copy(forward_grid)
		adaptive_astar_grid = gridFunc.grid_init_white(startCoord, goalCoord)

		fwd_results = astar.repeated_astar(pygame, forward_grid, astar_grid, startCoord, goalCoord, time, 3)
		write_row(fwd_sheet, i + 1, [i + 1] + list(fwd_results))
		# bwd_results = astar.repeated_astar(pygame, backward_grid, backward_astar_grid, startCoord, goalCoord, time, 2)
		# write_row(bwd_sheet, i + 1, [i + 1] + list(bwd_results))
		adpt_results = astar.adaptive_astar(pygame, adaptive_grid, adaptive_astar_grid, startCoord, goalCoord, time, 8)
		write_row(adpt_sheet, i + 1, [i + 1] + list(adpt_results))
		

		pygame.quit()
	book.save("Results.xls")


main()