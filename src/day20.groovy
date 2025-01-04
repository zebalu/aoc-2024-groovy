def input = new File('../input-20.txt').text
def grid = input.split('\n').collect { it.split('').toList() }

def directions = [[-1, 0], [1, 0], [0, 1], [0, -1]]
def isValid = { x, y -> y in grid.indices && x in grid[y].indices }
def neighbours = { x, y -> directions.collect { dx, dy -> [x + dx, y + dy] } }
def distance = { x1, y1, x2, y2 -> Math.abs(x1 - x2) + Math.abs(y1 - y2) }
def available = { x, y, steps, available = [[x, y]] as Set, seen = [] as Set ->
    steps.times { available.findAll { seen.add(it) }.each { xx, yy -> neighbours(xx, yy).findAll { isValid(it) }.each { available << it } } }
    available
}
def SE = [grid.first.indices.toList(), grid.indices.toList()].combinations().findAll { x, y -> grid[y][x] ==~ /(S|E)/ }.collectEntries { x, y -> grid[y][x] == 'S' ? ([start: [x, y]]) : ([end: [x, y]]) }
def priceMapFrom = { from ->
    def (prices, queue) = [[:], [from] as Queue]
    prices[from] = 0
    while (queue) {
        def at = queue.poll()
        neighbours(at).findAll { x, y -> isValid(x, y) && grid[y][x] != '#' && !prices.containsKey([x, y]) }.each { [queue << it, prices[it] = prices[at] + 1] }
    }
    prices
}
def startCost = priceMapFrom(SE.start)
def endCost = priceMapFrom(SE.end)


def noCheatCost = startCost[SE.end]

def withCheat = { maxCheat ->
    [grid.first.indices.toList(), grid.indices.toList()].combinations().findAll { x, y -> grid[y][x] != '#' }
            .collect { at ->
                available(*at, maxCheat).findAll { x, y ->
                    grid[y][x] != '#' && startCost[at] + distance(*at, x, y) + endCost[[x, y]] <= noCheatCost - 100
                }.size()
            }.sum()
}

println withCheat(2)

println withCheat(20)
