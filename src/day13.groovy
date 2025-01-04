def input = new File("../input-13.txt").text
def machines = input.split("\n\n").collect { it.findAll(/\d+/).collect { it as long } }

def diff = 10000000000000

def solve = { x1, y1, x2, y2, px, py, part2=false ->
    if (part2) [px += diff, py += diff]
    def b = (py * x1 - px * y1) / (y2 * x1 - x2 * y1)
    def a = (px - b * x2) / x1
    a % 1L == 0L && b % 1L == 0L ? a * 3L + b : 0L
}

def part1 = machines.collect { solve(it) }.sum()
println part1

def part2 = machines.collect { solve(*it, true) }.sum()
println part2
