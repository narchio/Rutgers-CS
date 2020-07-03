"""
 @author:zain
"""
import random
import pygame
import time
#from astarold import *
from astar import *
from openList import *
from gridFunc import *
import xlwt

# Define some colors
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
GREEN = (0, 255, 0)
RED = (255, 0, 0)
BLUE = (0, 0, 255)
LBLUE = (3, 232, 252)

#setting height and width of cells
BLOCK_WIDTH = 5
BLOCK_HEIGHT = 5
#Setting margin
MARGIN = 0

# start coords
rand_start_x = random.randint(0, 100)
rand_start_y = random.randint(0, 100)
startCoord = (rand_start_x, rand_start_y)

# goal coords
rand_goal_x = random.randint(0, 100)
rand_goal_y = random.randint(0, 100)
goalCoord = (rand_goal_x, rand_goal_y)

# initialize grid
random.seed(1)
forward_grid = grid_init(startCoord, goalCoord)
#forward_grid = grid_init_white(startCoord, goalCoord)
astar_grid = grid_init_white(startCoord, goalCoord)


# initialize grid for backward
random.seed(1)
backward_grid = grid_init(startCoord, goalCoord)
#forward_grid = grid_init_white(startCoord, goalCoord)
backward_astar_grid = grid_init_white(startCoord, goalCoord)


# initialize grid for adaptive
random.seed(1)
adaptive_grid = grid_init(startCoord, goalCoord)
#forward_grid = grid_init_white(startCoord, goalCoord)
adaptive_astar_grid = grid_init_white(startCoord, goalCoord)


        #The zero/one here is appending a cell

# initialize the game (grid)
pygame.init()

# call the forward Astar algorithm
repeated_astar(pygame, forward_grid, astar_grid, startCoord, goalCoord, time, 3)

# call the backward Astar algorithm
#repeated_astar(pygame, backward_grid, backward_astar_grid, startCoord, goalCoord, time, 2)

# call adaptive Astar algorithm
adaptive_astar(pygame, adaptive_grid, adaptive_astar_grid, startCoord, goalCoord, time, 8)

# Close the window and quit.
pygame.quit()

