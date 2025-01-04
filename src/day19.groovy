def input = new File('../input-19.txt').text
def parts = input.split('\n\n').toList()
def towels = parts[0].split(', ').toList()
def designs = parts[1].split('\n').toList()

def countWays
countWays = { design ->
    towels.findAll { design.startsWith(it) }.collect {
        it == design ? 1L : countWays(design.substring(it.size()))
    }.inject(0L, Long::sum)
}.memoize()

println designs.count { countWays(it) > 0 }
println designs.collect { countWays(it) }.sum()
