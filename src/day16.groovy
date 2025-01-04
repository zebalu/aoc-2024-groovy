def input = new File("../input-16.txt").text
def grid = input.split('\n').collect { it.split('').toList() }

def SnE = [grid.first.indices.toList(), grid.indices.toList()].combinations().findAll { x, y -> grid[y][x] ==~ 'S|E' }
def start = SnE.find { x, y -> grid[y][x] == 'S' }
def end = SnE.find { x, y -> grid[y][x] == 'E' }

def turns = { x, y -> [[-y, x], [y, -x]] }
def cloneExtend = { coll, newVal -> [coll].collect { coll.clone() }.collect { it << newVal }.first }

def bestPaths = {
    def queue = new PriorityQueue<>({ a, b -> a.first <=> b.first })
    queue << [0, [1, 0], [start] as Set, start]
    def costs = [[start, [1, 0]]: 0]
    def pathElements = [start: [start] as Set]
    def cheapest = Integer.MAX_VALUE;
    while (queue) {
        def (cost, facing, possibleTiles, at) = queue.poll()
        if (cost < cheapest) {
            def (x, y, dx, dy) = [*at, *facing]
            if (at == end) {
                def best = costs[[[x, y], [dx, dy]]] ?: Integer.MAX_VALUE
                if (cost < best) {
                    costs[[[x, y], [dx, dy]]] = cost
                    cheapest = cost
                    pathElements[end] = possibleTiles
                } else if (best == cost) {
                    pathElements[end].addAll(possibleTiles)
                }
            }
            if (grid[y + dy][x + dx] != '#') {
                def best = costs[[[x + dx, y + dy], facing]] ?: Integer.MAX_VALUE
                if ((cost + 1) < best && cost + 1 < cheapest) {
                    def newSet = cloneExtend(possibleTiles, [x + dx, y + dy])
                    queue << [cost + 1, facing, newSet, [x + dx, y + dy]]
                    costs[[x, y], [dx, dy]] = cost + 1
                    pathElements[[x + dx, y + dy]] = newSet
                } else if (cost + 1 == best) {
                    pathElements[[x + dx, y + dy]].addAll(possibleTiles)
                }
            }
            turns(dx, dy).each { dxx, dyy ->
                def best = costs[[[x, y], [dxx, dyy]]] ?: Integer.MAX_VALUE
                if ((cost + 1000) < best && cost + 1000 < cheapest) {
                    queue << [cost + 1000, [dxx, dyy], possibleTiles, [x, y]]
                    costs[[x, y], [dxx, dyy]] = cost + 1000
                    pathElements[[x, y]] = possibleTiles
                } else if (cost + 1000 == best) {
                    pathElements[[x, y]].addAll(possibleTiles)
                }
            }
        }
    }
    [price: cheapest, allTilesCount: pathElements[end].size()]
}

def solution = bestPaths()

println solution.price

println solution.allTilesCount
