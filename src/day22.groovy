def input = new File('../input-22.txt').text
def bases = input.lines().map {it as long}.toList()

def hash = { base ->
    def sn = ((base << 6) ^ base) & 16_777_215L
    sn = ((sn >> 5) ^ sn) & 16_777_215L
    sn = ((sn << 11) ^ sn) & 16_777_215L
    sn
}

def hashMany = {secret, times -> times.times {secret = hash(secret)}; secret}
def generateHashes = {secret, times, result = [secret] -> times.times { result << hash(result.last)}; result}

def generateSequences = { secret, times -> {
    def (map, nums) = [[:], generateHashes(secret, times)]
    def diffs = nums.indices.drop(1).collect {i->nums[i]%10-nums[i-1]%10}
    diffs.indices.drop(3).collect {i->[[diffs[i-3], diffs[i-2], diffs[i-1], diffs[i-0]], nums[i+1]%10]}.each { key, value ->if(key !in map) map[key] = value}
    map
}}

println bases.collect {hashMany(it, 2000)}.sum()

println bases.collect {generateSequences(it, 2000)}.inject([:]) { a,b ->
    b.each {a.merge(it.key, it.value, Long::sum)}
    a
}.max{a,b ->a.value <=> b.value}.value
