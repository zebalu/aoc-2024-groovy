def input = new File('../input-17.txt').text
def nums = input.findAll(/\d+/).collect { it as long }
def (A, B, C, program) = [nums[0], nums[1], nums[2], nums.drop(3)]

def getCombo = { o ->
    if (o in 0L..3L) o
    else if (o == 4L) A
    else if (o == 5L) B
    else if (o == 6L) C
    else 7L
}

def execute = {
    def (ipr, out) = [0, []]
    while (ipr < program.size()) {
        def (incr, operator, operand) = [true, program[ipr], program[ipr + 1]]
        if (operator == 0) A = A.intdiv(2L**getCombo(operand))
        else if (operator == 1) B = B ^ operand
        else if (operator == 2) B = getCombo(operand) % 8L
        else if (operator == 3 && A != 0L) (incr, ipr) = [false, operand as int]
        else if (operator == 4) B = B ^ C
        else if (operator == 5) out << (getCombo(operand) % 8L)
        else if (operator == 6) B = A.intdiv(2L**getCombo(operand))
        else if (operator == 7) C = A.intdiv(2L**getCombo(operand))
        if (incr) ipr += 2
    }
    out
}
println execute().join(",")

def (queue, min) = [new PriorityQueue<>({ a, b -> a.first <=> b.first }), Long.MAX_VALUE]
queue << [0L, 0]
while (queue && min == Long.MAX_VALUE) {
    def (prevRegA, length) = queue.poll()
    def (sufix, nextRegisterABase) = [program.subList(program.size() - length - 1, program.size()), prevRegA << 3]
    for (long i = 0L; i < 8L; ++i) {
        def nextRegisterA = nextRegisterABase | i
        (A, B, C) = [nextRegisterA, 0, 0]
        def out = execute()
        if (out == program) min = nextRegisterA
        else if (out == sufix) queue << [nextRegisterA, length + 1]
    }
}

println min
