def input = new File("../input-03.txt").text.replace("\n", "")

def sumInstructions = { text -> text.findAll(/(mul\(\d+,\d+\))/).collect { it.findAll(/\d+/) }.collect(l -> l.collect { it as long }.inject(1L) { a, v -> a * v }).sum() }

def part1 = sumInstructions(input)
println part1

def part2 = "do()${input}don't()".findAll(/do\(\).*?don't\(\)/).collect { sumInstructions(it) }.sum()
println part2
