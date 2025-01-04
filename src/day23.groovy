def input = new File('../input-23.txt').text
def edges = input.lines().map { it.split("-").toList() as Set }.toList()

def network = [:]
edges.each {
    def (a, b) = it
    [network.computeIfAbsent(a, { [] as Set }) << b, network.computeIfAbsent(b, { [] as Set }) << a]
}
def part1 = network.keySet().findAll { it.startsWith('t') }.collectMany { a -> network[a].collectMany { b -> network[a].findAll { c -> c != b && network[b].contains(c) }.collect { c -> [a, b, c] as Set } } }.toSet().size()

println part1
def findLargestClique = {
    def (result, processed) = [[] as Set, [] as Set]
    network.keySet().each { core ->
        def (queue, seen) = [[[core] as Set] as Queue , [[core] as Set] as Set ]
        while (queue) {
            def (clique, extended) = [queue.poll(), false]
            network[core].findAll { c -> c !in clique && c !in processed }.each { candidate ->
                if (network[candidate].containsAll(clique)) {
                    def next = clique + candidate
                    if (next !in seen) [queue << next, seen << next, extended = true]
                }
            }
            if (!extended && clique.size() > result.size()) {
                result = clique
            }
        }
        processed << core
    }
    result
}

def part2 = findLargestClique().sort().join(',')
println part2
