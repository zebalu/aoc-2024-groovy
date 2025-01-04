def input = new File("../input-02.txt").text
def lines = input.lines().map { it.split(" ") }.map { it.collect { it as int } }.toList()

def isSafe = { line ->
    def diffs = line.indices.drop(1).collect { i -> line[i] - line[i - 1] }
    diffs.count { Math.signum(it) == Math.signum(diffs[0]) && 0 < Math.abs(it) && Math.abs(it) <= 3 } == diffs.size()
}

def part1 = lines.count { isSafe(it) }
println part1

def part2 = lines.count { line ->
    def dampened = line.indices.collect { idx -> line.indices.findAll { it != idx }.collect { line[it] } }
    isSafe(line) || dampened.collect { isSafe(it) }.inject(false) { value, acc -> value || acc }
}
println part2
