def input = new File("../input-07.txt").text
def lines = input.split("\n").collect { it.split(": ") }.collect { l -> [l[0] as long, l[1].findAll(/\d+/).collect { it as long }] }

def endsWith = { a, b -> a > b && Long.toString(a).endsWith(Long.toString(b)) }
def deconcatenate = { a, b -> Long.parseLong(Long.toString(a).substring(0, Long.toString(a).size() - Long.toString(b).size())) }

def solve = { part2 = false ->
    lines.findAll { line ->
        def results = [line[0]] as Set
        for (num in line[1].drop(1).reversed()) {
            def coll = [] as Set
            for (res in results) {
                if (res - num > 0) coll << res - num
                if (res % num == 0) coll << res.intdiv(num)
                if (part2 && endsWith(res, num)) coll << deconcatenate(res, num)
            }
            results = coll
        }
        results.contains(line[1][0])
    }.sum { it[0] }
}

def part1 = solve()
println part1

def part2 = solve(true)
println part2
