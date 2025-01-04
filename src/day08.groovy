def input = new File("../input-08.txt").text
def grid = input.split("\n").collect { it.split("").collect { it } }

def isValid = { x, y -> grid.indices.contains(y) && grid[y].indices.contains(x) }

def towers = [:]
[grid[0].indices.toList(), grid.indices.toList()].combinations().findAll { x, y -> grid[y][x] != '.' }.each { x, y -> towers.computeIfAbsent(grid[y][x], { [] as Set }) << [x, y] }

def findAntinodes = { part2 = false ->
    def (limit, antinodes) = [part2 ? Integer.MAX_VALUE : 1, [] as Set]
    towers.values().each { positions ->
        [positions, positions].combinations().findAll { it[0] != it[1] }.each { tt ->
            def (dx, dy) = [tt[0][0] - tt[1][0], tt[0][1] - tt[1][1]]
            tt.each { cx, cy, s=0 ->
                while (s <= limit && isValid(cx, cy)) {
                    if (part2 || !positions.contains([cx, cy])) {
                        antinodes << [cx, cy]
                    }
                    (cx, cy, s) = [cx - dx, cy - dy, s + 1]
                }
            }
        }
    }
    antinodes
}

def part1 = findAntinodes().size()
println part1

def part2 = findAntinodes(true).size()
println part2
