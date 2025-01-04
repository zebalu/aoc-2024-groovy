def input = new File("../input-05.txt").text
def groups = input.split("\n\n")

def rules = groups[0].split("\n").collect { it.split(/\|/).collect { it as int } }
def pages = groups[1].split("\n").collect { it.split(",").collect { it as int } }

def isRulePass = { rule, pageOrder -> !pageOrder.containsAll(rule) || pageOrder.indexOf(rule[0]) < pageOrder.indexOf(rule[1]) }
def isCorrect = { pageOrder -> rules.collect { rule -> isRulePass(rule, pageOrder) }.inject(true) { a, b -> a && b } }

def part1 = pages.findAll { isCorrect(it) }.collect { it[it.size() / 2] }.sum()
println part1

def part2 = pages.findAll { !isCorrect(it) }.collect { pageOrder ->
    def activeRules = rules.findAll { pageOrder.containsAll(it) }
    while (!isCorrect(pageOrder)) {
        activeRules.each { rule ->
            if (!isRulePass(rule, pageOrder)) {
                Collections.swap(pageOrder, pageOrder.indexOf(rule[0]), pageOrder.indexOf(rule[1]))
            }
        }
    }
    pageOrder
}.collect { it[it.size() / 2] }.sum()
println part2
