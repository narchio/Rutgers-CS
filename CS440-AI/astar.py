from openList import *
from gridFunc import *
from time import perf_counter

# global color, black
BLACK = (0, 0, 0)

# global list of prior closed lists
adaptivelist = []

# finds h value of a node
def findh(x, y, goalX, goalY):
    hx = abs(goalX - x)
    hy = abs(goalY - y)
    return hx + hy


# find h adaptive
def findh_adaptive(goalG, prevG):
    return abs(goalG - prevG)


# find f adaptive
def findf_adapt(g, goalG, prevG):
    return g + findh_adaptive(goalG, prevG)


# finds f value of a node
def findf(x, y, g, goalX, goalY):
    return g + findh(x, y, goalX, goalY)


# compare f values on openlist conflict
def comparef(x, y, node, openlist):
    for i in openlist:
        if i.coordinates == (x, y):
            # if node is less than, update it
            # if node.f < i.f:
            if node.smaller_than(i):
                i.f = node.f
                i.parent = node.parent
                i.g = node.g
                siftup(openlist)


def mainEventLoop(pygame):
    # --- Main event loop
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            done = True
        elif event.type == pygame.MOUSEBUTTONDOWN:
            pos = pygame.mouse.get_pos()
            column = pos[0]
            row = pos[1]
            print(pos)
            done = True


# function checks the 4 directions on the grid and inserts if needed
def check_nodes(grid, openlist, closedlist, currentNode, x, y, goalX, goalY):
    # up node, check bounds of x-1 and then if unblocked=0, blocked=1
    if (x - 1) > -1:
        # create the node
        upNode = Treenode(findf(x - 1, y, currentNode.g + 1, goalX, goalY), currentNode.g + 1,
                          findh(x - 1, y, goalX, goalY), currentNode, (x - 1, y))

        if grid[x - 1][y] == 0:
            # add to open list if not in closed list OR openlist already
            inclosed = False
            inopen = False
            for element in closedlist:
                if element.coordinates == (x - 1, y):
                    inclosed = True
                    break
            if inclosed is False:
                for e in openlist:
                    if e.coordinates == (x - 1, y):
                        inopen = True
                        break
            if inclosed is False:
                if inopen is False:
                   openlist = insert(upNode, openlist)
                # compare f values and resiftup()
                else:
                    comparef(x - 1, y, upNode, openlist)
        # goal node
        elif grid[x - 1][y] == 3:
           openlist = insert(upNode, openlist)
        # start node
        elif grid[x - 1][y] == 2:
           openlist = insert(upNode, openlist)

    # down node, check bounds of x+1 and then if unblocked=0, blocked=1
    if (x + 1) < 101:
        # create the node
        downNode = Treenode(findf(x + 1, y, currentNode.g + 1, goalX, goalY), currentNode.g + 1,
                            findh(x + 1, y, goalX, goalY), currentNode, (x + 1, y))

        if grid[x + 1][y] == 0:
            # add to open list if not in closed list OR openlist already
            inclosed = False
            inopen = False
            for element in closedlist:
                if element.coordinates == (x + 1, y):
                    inclosed = True
                    break
            if inclosed is False:
                for e in openlist:
                    if e.coordinates == (x + 1, y):
                        inopen = True
                        break
            if inclosed is False:
                if inopen is False:
                    openlist = insert(downNode, openlist)
                # compare f values and resiftup()
                else:
                    comparef(x + 1, y, downNode, openlist)
        elif grid[x + 1][y] == 3:
            openlist = insert(downNode, openlist)
        elif grid[x + 1][y] == 2:
            openlist = insert(downNode, openlist)

    # right node, check bounds of y+1 and then if unblocked=0, blocked=1
    if (y + 1) < 101:
        # create the node
        rightNode = Treenode(findf(x, y + 1, currentNode.g + 1, goalX, goalY), currentNode.g + 1,
                             findh(x, y + 1, goalX, goalY), currentNode, (x, y + 1))

        if grid[x][y + 1] == 0:
            # add to open list if not in closed list OR openlist already
            inclosed = False
            inopen = False
            for element in closedlist:
                if element.coordinates == (x, y + 1):
                    inclosed = True
                    break
            if inclosed is False:
                for e in openlist:
                    if e.coordinates == (x, y + 1):
                        inopen = True
                        break
            if inclosed is False:
                if inopen is False:
                    openlist = insert(rightNode, openlist)
                # compare f values and resiftup()
                else:
                    comparef(x, y + 1, rightNode, openlist)
        elif grid[x][y + 1] == 3:
            openlist = insert(rightNode, openlist)
        elif grid[x][y + 1] == 2:
            openlist = insert(rightNode, openlist)

    # left node, check bounds of y-1 and then if unblocked=0, blocked=1
    if (y - 1) > -1:
        # creates the node
        leftNode = Treenode(findf(x, y - 1, currentNode.g + 1, goalX, goalY), currentNode.g + 1,
                            findh(x, y - 1, goalX, goalY), currentNode, (x, y - 1))

        if grid[x][y - 1] == 0:
            # add to open list if not in closed list OR openlist already
            inclosed = False
            inopen = False
            for element in closedlist:
                if element.coordinates == (x, y - 1):
                    inclosed = True
                    break
            if inclosed is False:
                for e in openlist:
                    if e.coordinates == (x, y - 1):
                        inopen = True
                        break
            if inclosed is False:
                if inopen is False:
                    openlist = insert(leftNode, openlist)
                # compare f values and resiftup()
                else:
                    comparef(x, y - 1, leftNode, openlist)
        elif grid[x][y - 1] == 3:
            openlist = insert(leftNode, openlist)
        elif grid[x][y - 1] == 2:
            openlist = insert(leftNode, openlist)

    return openlist


# function checks the 4 directions on the grid and inserts if needed
def check_nodes_adaptive(grid, openlist, closedlist, currentNode, x, y, goalX, goalY):
    # declare vars
    g = currentNode.g + 1
    prevG = 0
    goalG = 0
    h = 0
    f = 0
    isFound = False
    # prevG = if (x,y) in closedlist, assign that g
    # goalG = if (x,y) in closed list, assign that g

    if (x - 1) > -1:
        # create the node
        for item in adaptivelist:
            # goal node
            if item.coordinates == (goalX, goalY):
                goalG = item.g
                break
        for item in adaptivelist:
            # prev node
            if item.coordinates == (x - 1, y):
                prevG = item.g
                h = findh_adaptive(goalG, prevG)
                f = findf_adapt(g, goalG, prevG)
                isFound = True
                break
        # the item is not in the closed adaptivelist
        if isFound is False:
            h = findh(x-1, y, goalX, goalY)
            f = findf(x-1, y, g, goalX, goalY)

        # up node, check bounds of x-1 and then if unblocked=0, blocked=1
        upNode = Treenode(f, g, h, currentNode, (x - 1, y))

        if grid[x - 1][y] == 0:
            # add to open list if not in closed list OR openlist already
            inclosed = False
            inopen = False
            for element in closedlist:
                if element.coordinates == (x - 1, y):
                    inclosed = True
                    break
            if inclosed is False:
                for e in openlist:
                    if e.coordinates == (x - 1, y):
                        inopen = True
                        break
            if inclosed is False:
                if inopen is False:
                   openlist = insert(upNode, openlist)
                # compare f values and resiftup()
                else:
                    comparef(x - 1, y, upNode, openlist)
        # goal node
        elif grid[x - 1][y] == 3:
           openlist = insert(upNode, openlist)
        # start node
        elif grid[x - 1][y] == 2:
           openlist = insert(upNode, openlist)

    # down node, check bounds of x+1 and then if unblocked=0, blocked=1
    if (x + 1) < 101:
        # create the node
        for item in adaptivelist:
            # goal node
            if item.coordinates == (goalX, goalY):
                goalG = item.g
                break
        for item in adaptivelist:
            # prev node
            if item.coordinates == (x + 1, y):
                prevG = item.g
                h = findh_adaptive(goalG, prevG)
                f = findf_adapt(g, goalG, prevG)
                isFound = True
                break
        # the item is not in the closed adaptivelist
        if isFound is False:
            h = findh(x + 1, y, goalX, goalY)
            f = findf(x + 1, y, g, goalX, goalY)

        # create the node
        downNode = Treenode(f, g, h, currentNode, (x + 1, y))

        if grid[x + 1][y] == 0:
            # add to open list if not in closed list OR openlist already
            inclosed = False
            inopen = False
            for element in closedlist:
                if element.coordinates == (x + 1, y):
                    inclosed = True
                    break
            if inclosed is False:
                for e in openlist:
                    if e.coordinates == (x + 1, y):
                        inopen = True
                        break
            if inclosed is False:
                if inopen is False:
                    openlist = insert(downNode, openlist)
                # compare f values and resiftup()
                else:
                    comparef(x + 1, y, downNode, openlist)
        elif grid[x + 1][y] == 3:
            openlist = insert(downNode, openlist)
        elif grid[x + 1][y] == 2:
            openlist = insert(downNode, openlist)

    # right node, check bounds of y+1 and then if unblocked=0, blocked=1
    if (y + 1) < 101:
        # create the node
        for item in adaptivelist:
            # goal node
            if item.coordinates == (goalX, goalY):
                goalG = item.g
                break
        for item in adaptivelist:
            # prev node
            if item.coordinates == (x, y+1):
                prevG = item.g
                h = findh_adaptive(goalG, prevG)
                f = findf_adapt(g, goalG, prevG)
                isFound = True
                break
        # the item is not in the closed adaptivelist
        if isFound is False:
            h = findh(x, y+1, goalX, goalY)
            f = findf(x, y+1, g, goalX, goalY)

        # create the node
        rightNode = Treenode(f, g, h, currentNode, (x, y + 1))

        if grid[x][y + 1] == 0:
            # add to open list if not in closed list OR openlist already
            inclosed = False
            inopen = False
            for element in closedlist:
                if element.coordinates == (x, y + 1):
                    inclosed = True
                    break
            if inclosed is False:
                for e in openlist:
                    if e.coordinates == (x, y + 1):
                        inopen = True
                        break
            if inclosed is False:
                if inopen is False:
                    openlist = insert(rightNode, openlist)
                # compare f values and resiftup()
                else:
                    comparef(x, y + 1, rightNode, openlist)
        elif grid[x][y + 1] == 3:
            openlist = insert(rightNode, openlist)
        elif grid[x][y + 1] == 2:
            openlist = insert(rightNode, openlist)

    # left node, check bounds of y-1 and then if unblocked=0, blocked=1
    if (y - 1) > -1:
        # create the node
        for item in adaptivelist:
            # goal node
            if item.coordinates == (goalX, goalY):
                goalG = item.g
                break
        for item in adaptivelist:
            # prev node
            if item.coordinates == (x, y-1):
                prevG = item.g
                h = findh_adaptive(goalG, prevG)
                f = findf_adapt(g, goalG, prevG)
                isFound = True
                break
        # the item is not in the closed adaptivelist
        if isFound is False:
            h = findh(x, y-1, goalX, goalY)
            f = findf(x, y-1, g, goalX, goalY)


        # creates the node
        leftNode = Treenode(f, g, h, currentNode, (x, y - 1))

        if grid[x][y - 1] == 0:
            # add to open list if not in closed list OR openlist already
            inclosed = False
            inopen = False
            for element in closedlist:
                if element.coordinates == (x, y - 1):
                    inclosed = True
                    break
            if inclosed is False:
                for e in openlist:
                    if e.coordinates == (x, y - 1):
                        inopen = True
                        break
            if inclosed is False:
                if inopen is False:
                    openlist = insert(leftNode, openlist)
                # compare f values and resiftup()
                else:
                    comparef(x, y - 1, leftNode, openlist)
        elif grid[x][y - 1] == 3:
            openlist = insert(leftNode, openlist)
        elif grid[x][y - 1] == 2:
            openlist = insert(leftNode, openlist)

    return openlist




# This will compute the current path by running Astar on the known world and will return
# the list of coordinates (from the goal state) for the repeated-forward algorithm to follow
#
# The goalCoord remains constant, but the startCoord must begin from the last blocked node from the
# previous call, which's coordinates will be passed in to this new call of astar as the startCoord
def astar(pygame, grid, startCoord, goalCoord, time, clock, screen, goalType):
    # create the openlist and the closedlists
    openlist = []
    closedlist = []
    path_of_coordinates = []

    # total time and shortest path
    shortest_path = 0
    count = 0

    # separate the goal coords for manipulation
    startX, startY = startCoord
    goalX, goalY = goalCoord

    # Initialize start node and if we find the goal state
    startNode = Treenode(0, 0, 0, None, startCoord)
    goalfound = False

    # insert starting node into openlist
    openlist = insert(startNode, openlist)
    c, o = openlist[0].coordinates

    # loop through the open list
    while (len(openlist) != 0) and (goalfound is False):
        # assigns current node to the removed node and openlist to the modified openlist
        temp = pop(openlist)
        currentNode, openlist = temp

        # get current x and y coords
        x, y = currentNode.coordinates

        # now check the 4 different directions and add to open list if needed (update if there, skip if blocked)
        if goalType == 8:
            openlist = check_nodes_adaptive(grid, openlist, closedlist, currentNode, x, y, goalX, goalY)
        else:
            openlist = check_nodes(grid, openlist, closedlist, currentNode, x, y, goalX, goalY)

        # add current node to closed list and change color
        closedlist.append(currentNode)
        if grid[x][y] == 2:
            count = count + 1
            grid[x][y] = 2
        else:
            count = count + 1
        # if we hit the goal
        if (x == goalX) and (y == goalY):
            goalfound = True

        # if openlist is 0, then we cannot find the goal and have exhausted all our options
        if len(openlist) == 0:
            print("In Astar and cannot find goal, path is blocked!")
            # time.sleep(1)
            path_of_coordinates = []
            return path_of_coordinates
        # else, we found the goal and we need to backtrack and return the list of coords
        elif goalfound is True:
            ptr = closedlist[-1]
            currX, currY = ptr.coordinates

            ###### backtrack begins here #########
            while ptr.coordinates != startCoord:
                # now append the coords of the current node to the path_of_coordinates list
                path_of_coordinates.append(ptr.coordinates)
                # increment ptr
                ptr = ptr.parent
                currX, currY = ptr.coordinates
                shortest_path += 1

            # now append the coords of the current node to the path_of_coordinates list
            path_of_coordinates.append(ptr.coordinates)

    # now keep remaining screen up for 60 seconds
    # forward
    if goalType == 3:
        path_of_coordinates.reverse()
        return path_of_coordinates
    # backward
    elif goalType == 2:
        return path_of_coordinates
    # adaptive
    elif goalType == 8:
        adaptivelist = closedlist
        path_of_coordinates.reverse()
        return path_of_coordinates


# follows the path given by astar
def follow_path(pygame, pathlist, forward_grid, time, clock, screen):
    # decouple the start coords
    i = 0
    x, y = pathlist[i]
    currentX = x
    currentY = y
    blockedX = -1
    blockedY = -1

    # loop through the path now
    path = []
    goalfound = False
    blocked = False

    # iterate through the pathlist until the goal ist found or the path is blocked
    while (goalfound is False) and (blocked is False):
        # helps display the loop, fill initial screen black and update grid colors
        mainEventLoop(pygame)
        screen.fill(BLACK)
        gridColor(screen, forward_grid)

        # increment the x and y to the newest coord
        x, y = pathlist[i]

        # if it is unblocked, we move the agent
        if forward_grid[x][y] == 0:  # unblocked
            # increment counter
            currentX = x
            currentY = y
            path.append((x, y))

            # display moving ... Screen things first
            forward_grid[x][y] = 4
            mainEventLoop(pygame)
            screen.fill(BLACK)
            # then, update grid colors and --- Limit to 60 frames per second
            gridColor(screen, forward_grid)
            clock.tick(120)

        elif forward_grid[x][y] == 1:  # blocked
            blocked = True
            blockedX = x
            blockedY = y

            # display moving ... Screen things first
            mainEventLoop(pygame)
            screen.fill(BLACK)
            # then, update grid colors and --- Limit to 60 frames per second
            gridColor(screen, forward_grid)
            clock.tick(120)

        elif forward_grid[x][y] == 3:  # goal state
            goalfound = True
            currentX = x
            currentY = y
            path.append((x, y))

            # display moving ... Screen things first
            mainEventLoop(pygame)
            screen.fill(BLACK)
            # then, update grid colors and --- Limit to 60 frames per second
            gridColor(screen, forward_grid)
            clock.tick(120)
        # elif goalType == 2:
        #     if forward_grid[x][y] == 2:  # goal state
        #         goalfound = True
        #         currentX = x
        #         currentY = y
        #         path.append((x, y))
        #
        #         # display moving ... Screen things first
        #         mainEventLoop(pygame)
        #         screen.fill(BLACK)
        #         # then, update grid colors and --- Limit to 60 frames per second
        #         gridColor(screen, forward_grid)
        #         clock.tick(120)

        # increment counter
        i += 1
    # return the blocked coords and the current coords
    return (blockedX, blockedY), (currentX, currentY), path


# repeated forward A* algorithm
def repeated_astar(pygame, forward_grid, astar_grid, startCoord, goalCoord, time, goalType):
    # Set the width and height of the screen [width, height], clock and display grid (and counter for time elapsed)
    size = (505, 505)
    screen = pygame.display.set_mode(size)
    if goalType == 3:
        pygame.display.set_caption("Repeated Forward A* Grid")
    else:
        pygame.display.set_caption("Repeated Backward A* Grid")

    clock = pygame.time.Clock()
    a = perf_counter()

    # node for current start coordinate
    currentStart = startCoord
    currentX, currentY = currentStart
    to_be_added = []
    totalpath = []


    # loop until the curr is the goal
    while currentStart != goalCoord:
        # look around to all points near the current node
        if currentX > 0 and forward_grid[currentX - 1][currentY] == 1:
            astar_grid[currentX - 1][currentY] = 1

        elif currentX < 100 and forward_grid[currentX + 1][currentY] == 1:
            astar_grid[currentX + 1][currentY] = 1

        elif currentY > 0 and forward_grid[currentX][currentY - 1] == 1:
            astar_grid[currentX][currentY - 1] = 1

        elif currentY < 100 and forward_grid[currentX][currentY + 1] == 1:
            astar_grid[currentX][currentY + 1] = 1

        # now call astar
        # forward
        if goalType == 3:
            current_path_of_coordinates = astar(pygame, astar_grid, (currentX, currentY), goalCoord, time, clock, screen, goalType)
        # backward
        elif goalType == 2:
            current_path_of_coordinates = astar(pygame, astar_grid, goalCoord, (currentX, currentY), time, clock, screen, goalType)


        # if we cannot find the path
        if current_path_of_coordinates == []:
            print("The current_path_of_coordinates is empty and thus the path is blocked and cannot be found")
            totalpath = []
            break

        # now follow the path laid by astar in follow_path
        blockedCoords, currentStart, to_be_added = follow_path(pygame, current_path_of_coordinates, forward_grid, time, clock, screen)
        # increment currentX, currentY
        currentX, currentY = currentStart

        # add the elements from to_be_added to our final list
        for element in to_be_added:
            totalpath.append(element)

        # if we found the goal state
        if currentStart == goalCoord:
            print("Found goal in forward A!")

            # display full path
            # helps display the loop, fill initial screen black and update grid colors
            mainEventLoop(pygame)
            screen.fill(BLACK)
            gridColor(screen, forward_grid)
            # iterate through totalpath and show that on the grid
            for coordinate in totalpath:
                # decouple the coordinates
                totalX, totalY = coordinate

                # make the coord blue, then display it
                forward_grid[totalX][totalY] = 9
                # Screen things first: helps display the loop, make the node at ptr pink
                mainEventLoop(pygame)
                screen.fill(BLACK)
                # then, update grid colors and --- Limit to 60 frames per second
                gridColor(screen, forward_grid)
                clock.tick(120)
        else:
            # then we found a new blocked node and update the astar grid with the new blocked coords
            blockedX, blockedY = blockedCoords
            astar_grid[blockedX][blockedY] = 1

    # this keeps track of the total time elapsed
    b = perf_counter()
    total_time = b - a
    print("The Total Time Elapsed is: " + str(total_time))
    # time.sleep(1)
    pygame.display.quit()

    startx, starty = startCoord
    endx, endy = goalCoord
    
    return startx, starty, endx, endy, total_time, len(totalpath)


# adaptive A* algorithm
def adaptive_astar(pygame, forward_grid, astar_grid, startCoord, goalCoord, time, goalType):
    # Set the width and height of the screen [width, height], clock and display grid (and counter for time elapsed)
    size = (505, 505)
    screen = pygame.display.set_mode(size)
    pygame.display.set_caption("Repeated Forward A* Grid")
    clock = pygame.time.Clock()
    a = perf_counter()

    # node for current start coordinate
    currentStart = startCoord
    currentX, currentY = currentStart
    to_be_added = []
    totalpath = []

    # loop until the curr is the goal
    while currentStart != goalCoord:
        # look around to all points near the current node
        if currentX > 0 and forward_grid[currentX - 1][currentY] == 1:
            astar_grid[currentX - 1][currentY] = 1

        elif currentX < 100 and forward_grid[currentX + 1][currentY] == 1:
            astar_grid[currentX + 1][currentY] = 1

        elif currentY > 0 and forward_grid[currentX][currentY - 1] == 1:
            astar_grid[currentX][currentY - 1] = 1

        elif currentY < 100 and forward_grid[currentX][currentY + 1] == 1:
            astar_grid[currentX][currentY + 1] = 1

        # now call astar
        current_path_of_coordinates = astar(pygame, astar_grid, (currentX, currentY), goalCoord, time, clock, screen, goalType)

        # if we cannot find the path
        if current_path_of_coordinates == []:
            print("The current_path_of_coordinates is empty and thus the path is blocked and cannot be found")
            break

        # now follow the path laid by astar in follow_path
        blockedCoords, currentStart, to_be_added = follow_path(pygame, current_path_of_coordinates, forward_grid, time, clock, screen)
        # increment currentX, currentY
        currentX, currentY = currentStart

        # add the elements from to_be_added to our final list
        for element in to_be_added:
            totalpath.append(element)

        # if we found the goal state
        if currentStart == goalCoord:
            print("Found goal in forward A!")

            # display full path
            # helps display the loop, fill initial screen black and update grid colors
            mainEventLoop(pygame)
            screen.fill(BLACK)
            gridColor(screen, forward_grid)
            # itterate through totalpath and show that on the grid
            for coordinate in totalpath:
                # decouple the coordinates
                totalX, totalY = coordinate

                # make the coord blue, then display it
                forward_grid[totalX][totalY] = 9
                # Screen things first: helps display the loop, make the node at ptr pink
                mainEventLoop(pygame)
                screen.fill(BLACK)
                # then, update grid colors and --- Limit to 60 frames per second
                gridColor(screen, forward_grid)
                clock.tick(120)
        else:
            # then we found a new blocked node and update the astar grid with the new blocked coords
            blockedX, blockedY = blockedCoords
            astar_grid[blockedX][blockedY] = 1

    # this keeps track of the total time elapsed
    b = perf_counter()
    total_time = b - a
    print("The Total Time Elapsed is: " + str(total_time))
    # time.sleep(5)
    pygame.display.quit()

    startx, starty = startCoord
    endx, endy = goalCoord

    return startx, starty, endx, endy, total_time, len(totalpath)

