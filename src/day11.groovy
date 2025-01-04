def input = new File("../input-11.txt").text
def numbers = input.split(" ").collect { it as long }

def isEvenLength = { n -> Long.toString(n).size() % 2 == 0 }.memoize()

def split = { n, str=Long.toString(n) -> [str.substring(0, str.size().intdiv(2)) as long, str.substring(str.size().intdiv(2)) as long]}.memoize()

def size
size = { stone, steps ->
    if (steps == 0) 1L
    else if (stone == 0) size(1, steps - 1)
    else if (isEvenLength(stone)) size(split(stone).first, steps - 1) + size(split(stone).last, steps - 1)
    else size(stone * 2024L, steps - 1)
}.memoize()

println numbers.collect { size(it, 25) }.sum()

println numbers.collect { size(it, 75) }.sum()
