def input = new File('../input-21.txt').text
def codes = input.lines().toList()

def numpad = [['7', '8', '9'], ['4', '5', '6'], ['1', '2', '3'], ['#', '0', 'A']]
def dirpad = [['#', '^', 'A'], ['<', 'v', '>']]

def diffs = [[diff: [-1, 0], dir: '<'], [diff: [1, 0], dir: '>'], [diff: [0, -1], dir: '^'], [diff: [0, 1], dir: 'v']]
def isValid = { x, y, pad -> y in pad.indices && x in pad[y].indices && pad[y][x] != '#' }
def neighbours = { x, y -> diffs.collect { diff, dx = diff.diff[0], dy = diff.diff[1] -> [pos: [x + dx, y + dy], dir: diff.dir] } }
def locate = { ch, pad -> [pad.first.indices.toList(), pad.indices.toList()].combinations().find { x, y -> pad[y][x] == ch } }.memoize()

def paths = { from, to, pad ->
    def (min, result, queue) = [Integer.MAX_VALUE, [], [[from, '', 0]] as Queue]
    while (queue) {
        def (at, path, cost) = queue.poll()
        if (at == to) {
            if (cost < min) [result = [path], min = cost]
            else if (cost == min) result << path
        } else neighbours(at).findAll { isValid(*(it.pos), pad) && cost < min }.each { queue << [it.pos, path + it.dir, cost + 1] }
    }
    result.collect { it + 'A' }
}.memoize()

def codeToPaths = { code ->
    def (curr, results) = [locate('A', numpad), [''] as Set]
    code.split('').each { ch ->
        def to = locate(ch, numpad)
        def ps = paths(curr, to, numpad)
        [results = results.collectMany { r -> ps.collect { p -> r + p } }.toSet(), curr = to]
    }
    results
}

def pathCost
pathCost = { String path, int depth ->
    if (depth == 0) {
        path.size() as long
    } else {
        def (curr, results) = [locate('A', dirpad), [0L] as Set]
        path.split('').each { ch ->
            def to = locate(ch, dirpad)
            def ps = paths(curr, to, dirpad)
            [results = [results.collectMany { r -> ps.collect { p -> r + pathCost(p, depth - 1) } }.min()], curr = to]
        }
        results.min()
    }
}.memoize()

def codeToNum = { code -> code.substring(0, code.size() - 1) as long }
def codeToScore = { code, depth -> codeToPaths(code).collect { pathCost(it, depth) }.min() * codeToNum(code) }

println codes.collect { code -> codeToScore(code, 2) }.sum()
println codes.collect { code -> codeToScore(code, 25) }.sum()
