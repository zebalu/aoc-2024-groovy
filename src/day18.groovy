def input = new File('../input-18.txt').text
def coords = input.split('\n').collect { it.split(',').collect { it as int } }
def isValid = { x, y -> x in 0..70 && y in 0..70 }
def nexts = { x, y -> [[x - 1, y], [x + 1, y], [x, y - 1], [x, y + 1]] }

def (start, end) = [[0, 0], [70, 70]]
def findWay = { map ->
    def (queue, seen, length) = [[[*start, 0]] as Queue, [start] as Set, Integer.MIN_VALUE]
    while (queue) {
        def (x, y, steps) = queue.poll()
        if ([x, y] == end) [queue.clear(), length = steps]
        else nexts(x, y).findAll { xx, yy -> [xx, yy] !in map && isValid(xx, yy) && seen.add([xx, yy]) }.each { xx, yy -> queue << [xx, yy, steps + 1] }
    }
    length
}

println findWay([*(coords.take(1024))] as Set)

def good = 1024
def bad = coords.indices.last
def middle = (good + bad).intdiv(2)
while (1 < bad - good) {
    def length = findWay([*(coords.take(middle + 1))] as Set)
    if (0 < length) good = middle
    else bad = middle
    middle = (good + bad).intdiv(2)
}
println coords[bad].join(',')
