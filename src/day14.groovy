def input = new File("../input-14.txt").text
def robots = input.split("\n").collect { it.findAll(/-?\d+/).collect { it as int } }
def width = 101
def height = 103
def move = { x, y, dx, dy -> [(width + x + dx) % width, (height + y + dy) % height, dx, dy] }

def inQuadrant = { x, y ->
    def xCut = width.intdiv(2)
    def yCut = height.intdiv(2)
    if (x < xCut && y < yCut) 0
    else if (x < xCut && yCut < y) 1
    else if (xCut < x && y < yCut) 2
    else if (xCut < x && yCut < y) 3
    else 4
}

def quadrants = {
    def qs = [0, 0, 0, 0, 0]
    robots.each { x, y, dx, dy -> ++qs[inQuadrant(x, y)] }
    qs
}

100.times {
    robots = robots.collect { move(it) }
}
def part1 = quadrants().take(4).inject(1) { a, b -> a * b }
println(part1)

def part2 = 100
while (!(quadrants().take(4).max() > robots.size().intdiv(2))) {
    (part2, robots) = [part2 + 1, robots.collect { move(it) }]
}
println part2
