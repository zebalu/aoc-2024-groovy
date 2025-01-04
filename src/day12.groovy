def input = new File("../input-12.txt").text

def grid = input.split("\n").collect { it.split('').toList() }

def nexts = { x, y -> [[x - 1, y], [x + 1, y], [x, y - 1], [x, y + 1]] }

def positions = [grid[0].indices.toList(), grid.indices.toList()].combinations() as Set

def collectRegions = {
    def regions = [] as Set
    def seen = [] as Set
    positions.each { pos ->
        if (seen.add(pos)) {
            def region = [pos] as Set
            def queue = [pos] as Queue
            while (queue) {
                def (x, y) = queue.poll()
                nexts(x, y).findAll { positions.contains(it) }.findAll { nx, ny -> grid[ny][nx] == grid[y][x] }.findAll { seen.add(it) }.each {
                    queue << it
                    region << it
                }
            }
            regions << region
        }
    }
    regions
}

def regions = collectRegions()

def part1 = regions.collect { region -> region.size() * region.collect { pos -> nexts(pos).findAll { !region.contains(it) }.size() }.sum() }.sum()
println part1

def sidesOf = { region ->
    def sides = [] as Set
    region.each { pos ->
        nexts(pos).findAll { !region.contains(it) }.each { ox, oy ->
            def (ix, iy) = pos
            def (side, cix, ciy, cox, coy, dx, dy) = [[[ix, iy, ox, oy]] as Set, ix, iy, ox, oy, Math.abs(iy - oy) as int, Math.abs(ix - ox) as int]
            while (region.contains([cix + dx, ciy + dy]) && !region.contains([cox + dx, coy + dy])) {
                (cix, ciy, cox, coy) = [cix + dx, ciy + dy, cox + dx, coy + dy]
                side << [cix, ciy, cox, coy]
            }
            (cix, ciy, cox, coy) = [ix, iy, ox, oy]
            while (region.contains([cix - dx, ciy - dy]) && !region.contains([cox - dx, coy - dy])) {
                (cix, ciy, cox, coy) = [cix - dx, ciy - dy, cox - dx, coy - dy]
                side << [cix, ciy, cox, coy]
            }
            sides << side
        }
    }
    sides
}

def part2 = regions.collect { region -> region.size() * sidesOf(region).size() }.sum()
println part2
