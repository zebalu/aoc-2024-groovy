def input = new File("../input-01.txt").text
def left = [], right = []
input.lines().map { it.split($/ {3}/$) }.forEach {[left << (it[0] as int), right << (it[1] as int)]}

def part1 = [left.sort(), right.sort()].transpose().collect { Math.abs(it[0] - it[1]) }.sum()
println part1

def part2 = left.collect(lhs -> lhs * right.count { lhs == it }).sum()
println part2
