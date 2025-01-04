def input = new File('../input-24.txt').text
def parts = input.split("\n\n").toList()
def bits = parts.first.split('\n').collect { it.split(': ') }.collectEntries { [(it[0]): it[1] as int] }
def toRule = { prts -> [in1: prts[0], in2: prts[2], out: prts[4], op: prts[1]] }
def rules = parts.last.split('\n').collect { toRule(it.split(' ')) }
def apply = { rule, in1 = bits[(rule.in1)], in2 = bits[(rule.in2)] -> if (rule.op == 'XOR') in1 ^ in2 else if (rule.op == 'AND') in1 & in2 else in1 | in2 }

def collectBitKeys = { String prefix -> bits.keySet().findAll { ((String) it).startsWith(prefix) }.sort() }
def xs = collectBitKeys('x')
def ys = collectBitKeys('y')
def zs = (xs.size()..0).collect { 'z' + (it < 10 ? '0' : '') + it }

def part1 = {
    def (todo, changed) = [rules, true]
    while (todo && changed) {
        def (skipped) = [[], changed = false]
        todo.each { rule ->
            if (rule.in1 in bits.keySet() && rule.in2 in bits.keySet()) [bits[rule.out] = apply(rule), changed = true]
            else skipped << rule
        }
        todo = skipped
    }
    Long.parseLong(zs.collect { bits[it] }.join(''), 2)
}

def findMatchingRule = { in1, in2, op -> rules.find { [it.in1, it.in2].containsAll([in1, in2]) && it.op == op } }

def fullAdder = { a, b, carry, swapped ->
    def (basicSum, basicCarry) = [findMatchingRule(a, b, 'XOR'), findMatchingRule(a, b, 'AND')]
    if (!carry) {
        return List.of(basicSum.out, basicCarry.out);
    } else {
        def carryForward1 = findMatchingRule(basicSum.out, carry, 'AND')
        if (!carryForward1) {
            (basicSum, basicCarry) = [basicCarry, basicSum]
            swapped.addAll([basicSum.out, basicCarry.out])
            carryForward1 = findMatchingRule(basicSum.out, carry, 'AND')
        }
        def finalSum = findMatchingRule(basicSum.out, carry, 'XOR')
        if (basicSum.out.startsWith('z') && finalSum) {
            (basicSum, finalSum) = [finalSum, basicSum]
            swapped.addAll([finalSum.out, basicSum.out])
        }
        if (basicCarry.out.startsWith('z') && finalSum) {
            (basicCarry, finalSum) = [finalSum, basicCarry]
            swapped.addAll([basicCarry.out, finalSum.out])
        }
        if (carryForward1 && carryForward1.out.startsWith('z') && finalSum) {
            (carryForward1, finalSum) = [finalSum, carryForward1]
            swapped.addAll([carryForward1.out, finalSum.out])
        }
        def (finalCarry) = [(!carryForward1) ?: findMatchingRule(carryForward1.out, basicCarry.out, 'OR')]
        [finalSum?.out, finalCarry?.out]
    }
}

def part2 = {
    def (swapped, carry) = [[],]
    xs.indices.each { idx ->
        def (xKey, yKey) = [xs[idx], ys[idx]]
        def outputs = fullAdder(xKey, yKey, carry, swapped)
        if (outputs.last && outputs.last.startsWith("z") && outputs.last != zs.first) [outputs = outputs.reversed(), swapped.addAll(outputs)]
        if (outputs.last) carry = outputs.last
        else carry = findMatchingRule(xKey, yKey, 'AND')?.rout
    }
    return swapped.sort().join(",")
}

println part1()
println part2()
