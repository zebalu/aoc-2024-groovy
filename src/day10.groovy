def input = new File("../input-10.txt").text
def grid = input.split("\n").collect { it.split('').collect { it as int } }

def next = { x, y -> [[x - 1, y], [x + 1, y], [x, y - 1], [x, y + 1]] }
def isValid = { x, y -> grid.indices.contains(y) && grid[0].indices.contains(x) }

def solve = { part2 = false ->
    [grid[0].indices.toList(), grid.indices.toList()].combinations().findAll { x, y -> grid[y][x] == 0 }.collect { sx, sy ->
        def queue = !part2 ? ([[sx, sy]] as Queue) : ([[[sx, sy]]] as Queue)
        def seen = !part2 ? ([[sx, sy]] as Set) : ([[[sx, sy]]] as Set)
        def found = [] as Set
        while (!queue.isEmpty()) {
            def actual = queue.poll()
            def (x, y) = !part2 ? actual : actual.last
            def height = grid[y][x]
            if (height == 9) {
                found << actual
            } else {
                next(x, y).findAll { isValid(it) }.findAll { ((!part2) ? (!seen.contains(it)) : (!seen.contains(actual + it))) }.each { nx, ny ->
                    if (grid[ny][nx] == height + 1) {
                        queue << (!part2 ? ([nx, ny]) : (actual + [[nx, ny]]))
                        seen << (!part2 ? ([nx, ny]) : (actual + [[nx, ny]]))
                    }
                }
            }
        }
        found.size()
    }
}

def part1 = solve().sum()
println part1

def part2 = solve(true).sum()
println part2
