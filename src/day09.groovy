def input = new File("../input-09.txt").text

def generateDisk = {
    def currId = 0
    def onFile = true
    input.strip().split('').collectMany { str, num = str as int ->
        def ids = []
        num.times { ids << (onFile ? "$currId" : '.') }
        (currId, onFile) = [onFile ? currId + 1 : currId, !onFile]
        ids
    }
}

def checksum = { fullFs ->
    def checksum = 0L
    fullFs.eachWithIndex { value, index ->
        checksum += (value == '.' ? 0L : value as long) * index
    }
    checksum
}

def part1 = {
    def fullFs = generateDisk()
    def (i, j) = [0, fullFs.indices.last]
    while (i < j) {
        while (i < fullFs.size() && fullFs[i] != '.') {
            ++i
        }
        while (0 <= j && fullFs[j] == '.') {
            --j
        }
        if (i < j) {
            fullFs.swap(i, j)
            (i, j) = [i + 1, j - 1]
        }
    }
    checksum(fullFs)
}()
println part1

def part2 = {
    def fullFs = generateDisk()
    def startOf = {j, cj = j ->
        while (0 <= cj && fullFs[cj] == fullFs[j]) {
            --cj
        }
        ++cj
    }
    def spaceStartFor = { length, limit ->
        def (i, s, l) = [0, Integer.MIN_VALUE, 0]
        while (i < fullFs.size() && i <= limit && l < length) {
            if (fullFs[i] == '.') {
                if (0 <= s) {
                    ++l
                } else {
                    (s, l) = [i, 1]
                }
            } else {
                (s, l) = [Integer.MIN_VALUE, 0]
            }
            ++i
        }
        s
    }
    def j = fullFs.indices.last
    while (0 <= j) {
        while (0 <= j && fullFs[j] == '.') {
            --j
        }
        def start = startOf(j)
        def length = j - start + 1
        def spaceStart = spaceStartFor(length, start)
        if (0 <= spaceStart) {
            length.times {
                fullFs.swap(spaceStart, j)
                (spaceStart, j) = [spaceStart + 1, j - 1]
            }
        } else {
            j = start - 1
        }
    }
    checksum(fullFs)
}()
println part2
