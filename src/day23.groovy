def input = new File('../input-23.txt').text
def edges = input.lines().map { it.split("-").toList() as Set }.toList()

def network = [:]
edges.each {
    def (a, b) = it
    [network.computeIfAbsent(a, { [] as Set }) << b, network.computeIfAbsent(b, { [] as Set }) << a]
}
def part1 = network.keySet().findAll { it.startsWith('t') }.collectMany { a -> network[a].collectMany { b -> network[a].findAll { c -> c != b && network[b].contains(c) }.collect { c -> [a, b, c] as Set } } }.toSet().size()

println part1

def bronKerbosch = {
    def (result, queue) = [[] as Set, [[[] as Set, network.keySet(), [] as Set]] as Queue]
    while (queue) {
        def (clique, candidates, exclude) = queue.poll()
        if (!candidates && !exclude) {
            result << clique
        } else {
            for (node in candidates as List) {
                queue << [clique + node, network[node].intersect(candidates), network[node].intersect(exclude)]
                candidates -= node
                exclude << node
            }
        }
    }
    result
}

def findLargestClique = { bronKerbosch().max { it.size() } }

def part2 = findLargestClique().sort().join(',')
println part2
