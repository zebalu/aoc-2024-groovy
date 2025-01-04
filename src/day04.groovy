def input = new File('../input-04.txt').text
def grid = input.lines().findAll()

def directions = [[-1, 0], [1, 0], [0, -1], [0, 1], [-1, -1], [-1, 1], [1, -1], [1, 1]]
def getChar = { yx -> yx[0] < 0 || grid.size() <= yx[0] || yx[1] < 0 || grid[0].size() <= yx[1] ? '#' : grid[yx[0]][yx[1]] }
def move = { yx, dyx -> [yx[0] + dyx[0], yx[1] + dyx[1]] }

def coords = [grid.indices, 0..<grid[0].size()].combinations()

def part1 = 0
coords.each { position ->
    if (getChar(position) == 'X') {
        def S = 'X'
        for (dir in directions) {
            S = 'X'
            next = position
            3.times {
                next = move(next, dir)
                S += getChar(next)
            }
            if (S == 'XMAS') {
                ++part1
            }
        }
    }
}
println part1

def part2 = 0
coords.each { position ->
    if (getChar(position) == 'A') {
        def S1 = "${getChar(move(position, [-1, -1]))}A${getChar(move(position, [1, 1]))}"
        def S2 = "${getChar(move(position, [-1, 1]))}A${getChar(move(position, [1, -1]))}"
        if (S1 ==~ /(MAS)|(SAM)/ && S2 ==~ /(MAS)|(SAM)/) {
            ++part2
        }
    }
}
println part2
