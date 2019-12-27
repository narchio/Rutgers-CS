"""
 @author:zain
"""
import random
import pygame

BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
GREEN = (0, 255, 0)
RED = (255, 0, 0)
BLUE = (0, 0, 255)
LBLUE = (3, 232, 252)
PINK = (252, 3, 148)

BLOCK_WIDTH = 5
BLOCK_HEIGHT = 5


def grid_copy(grid):
    return [grid[i].copy() for i in range(len(grid))]
    

def grid_init(start, goal):
    grid = []
    for row in range(101):
        grid.append([])
        for column in range(101):
            coinFlip = random.randint(0, 100)
            if (coinFlip > 29):
                grid[row].append(0)
            else:
                grid[row].append(1)
    startx,starty = start
    goalx,goaly = goal
    grid[startx][starty] = 2
    grid[goalx][goaly] = 3
    return grid


def grid_init_white(start, goal):
    grid = []
    for row in range(101):
        grid.append([])
        for column in range(101):
            grid[row].append(0)
    # now set start coords
    startx,starty = start
    goalx,goaly = goal
    grid[startx][starty] = 2
    grid[goalx][goaly] = 3
    return grid


def gridColor(screen, grid):
    for row in range(101):
        for column in range(101):

            if (grid[row][column] == 0):
                color = WHITE
                #unblocked is white
            elif (grid[row][column] == 2):
                color = GREEN
                #Starting point is GREEN
            elif (grid[row][column] == 3):
                color = RED
                #Goal is RED

            elif (grid[row][column] == 4):
                color = LBLUE
                #Path is LIGHT BLUE
            elif (grid[row][column] == 9):
                color = PINK
                #Shortest Path to Goal is Pink
            else:
                color = BLACK
                #blocked is black

            pygame.draw.rect(screen,color,[BLOCK_WIDTH*column,BLOCK_HEIGHT*row,BLOCK_WIDTH,BLOCK_HEIGHT])

    # updating the screen
    pygame.display.flip()
