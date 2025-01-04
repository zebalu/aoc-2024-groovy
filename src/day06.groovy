def input = new File("../input-06.txt").text
def grid = input.split("\n").collect { it.split('') }

def isValid = { x, y -> grid.indices.contains(y) && grid[y].indices.contains(x) }
def getAt = { x, y -> isValid(x, y) ? grid[y][x] : '*' }
def turn = { d -> d == "^" ? ">" : d == ">" ? "v" : d == "v" ? "<" : "^" }


def coords = grid.indices.collectMany { y -> grid[y].indices.collect { x -> [x, y] } }

def start = coords.find(c -> getAt(c[0], c[1]) == "^")
def path = [] as Set
def (x, y, dx, dy, d) = [start[0], start[1], 0, -1, '^']
while (getAt(x, y) != '*') {
    path << [x, y]
    getAt(x + dx, y + dy) != '#' ? (x, y) = [x + dx, y + dy] : (dx, dy) = [-dy, dx]
}
println path.size()

def part2 = path.drop(1).count { c ->
    (x, y, dx, dy, d) = [start[0], start[1], 0, -1, '^']
    def cPath = [] as Set
    while (cPath.add("$x,$y,$d") && getAt(x, y) != "*") {
        if (c == [x + dx, y + dy] || getAt(x + dx, y + dy) == '#') {
            (d, dx, dy) = [turn(d), -dy, dx]
        } else {
            (x, y) = [x + dx, y + dy]
        }
    }
    getAt(x, y) != "*"
}
println part2
