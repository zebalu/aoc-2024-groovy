def input = new File('../input-25.txt').text
def (keys, locks) = [[], []]
input.split('\n\n').collect { it.split('\n').collect { it.split('') } }.each { it[0][0] == '#' ? keys << it : locks << it }

def indices = [keys.first.first.indices.toList(), keys.first.indices.toList()].combinations()

println([keys, locks].combinations().findAll { k, l -> indices.every { x, y -> k[y][x] != '#' || l[y][x] != '#' } }.size())
println 'Merry Christmas!'
