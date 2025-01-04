def input = new File("../input-15.txt").text

def parts = input.split("\n\n")
def grid = parts[0].split('\n').collect { it.split('').toList() }
def moves = parts[1].split('\n').collectMany { it.split('').toList() }

def findStart = { [grid.first.indices.toList(), grid.indices.toList()].combinations().find { x, y -> grid[y][x] == '@' } }
def collectSimpleMoves = { x, y, dx, dy ->
    def (result, nx, ny) = [[[x, y]], x + dx, y + dy]
    while (grid[ny][nx] != '.' && grid[ny][nx] != '#') [result << [nx, ny], (nx, ny) = [nx + dx, ny + dy]]
    if (grid[ny][nx] == "#") result = []
    else result << [nx, ny]
    result
}

def collectNexts = { xys, dxx, dyy, result = [] as Set ->
    xys.findAll { xx, yy -> '.' != grid[yy][xx] }.each { xx, yy, c = grid[yy + dyy][xx + dxx] ->
        if (c == '[') {
            result << [xx + dxx, yy + dyy]
            result << [xx + dxx + 1, yy + dyy]
        } else if (c == ']') {
            result << [xx + dxx - 1, yy + dyy]
            result << [xx + dxx, yy + dyy]
        } else {
            result << [xx + dxx, yy + dyy]
        }
    }
    result
}
def isAnyWall = { xys -> "#" in xys.collect { xx, yy -> grid[yy][xx] } }
def isAllSpace = { xys -> xys.every { xx, yy -> grid[yy][xx] == "." } }
def collectComplexMoves = { xx, yy, dxx, dyy ->
    def result = [[[xx, yy]]]
    def next = collectNexts(result.last, dxx, dyy)
    def allSpace = isAllSpace(next)
    def anyWall = isAnyWall(next)
    while (!allSpace && !anyWall) {
        result << next
        next = collectNexts(next, dxx, dyy)
        allSpace = isAllSpace(next)
        anyWall = isAnyWall(next)
    }
    if (anyWall) {
        result = []
    }
    result
}

def swap = { x1, y1, x2, y2, tmp = grid[y1][x1] -> [grid[y1][x1] = grid[y2][x2], grid[y2][x2] = tmp] }

def applyComplexMoves = { toMove, dx, dy ->
    toMove.reversed().each { line ->
        line.each { x1, y1, x2 = x1 + dx, y2 = y1 + dy, curr = grid[y1][x1] ->
            if (curr != '.') swap(x1, y1, x2, y2)
        }
    }
}

def applySimpleMoves = { toMove -> toMove.indices.drop(1).reversed().each { idx -> swap(*toMove[idx], *toMove[idx - 1]) } }

def calculateScore = { s -> [grid.first.indices.toList(), grid.indices.toList()].combinations().findAll { x, y -> grid[y][x] == s }.collect { x, y -> y * 100 + x }.sum() }

def getMove = { s -> if (s == "^") [0, -1] else if (s == "<") [-1, 0] else if (s == "v") [0, 1] else [1, 0] }

def applyMoves = {
    moves.each { move ->
        def (x, y, dx, dy) = findStart() + getMove(move)
        if (dy == 0) {
            applySimpleMoves(collectSimpleMoves(x, y, dx, dy))
        } else {
            applyComplexMoves(collectComplexMoves(x, y, dx, dy), dx, dy)
        }
    }
}

applyMoves()
def part1 = calculateScore('O')
println part1

grid = parts[0].split("\n").collect { it.split('').collectMany { c ->
    if (c == "#") ['#', '#']
    else if (c == '.') ['.', '.']
    else if (c == 'O') ['[', ']']
    else ['@', '.']
} }

applyMoves()
def part2 = calculateScore('[')
println part2
