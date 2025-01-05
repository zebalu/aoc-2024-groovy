# Groovy scripts for Advent of Code 2024

Simple scripts to solve 2024 challenge.

## How to run

By default all scripts are looking for the input files one folder above. All input files should be named like `input-<2 character long day number>.txt` such as `input-01.txt` or `input-25.txt`. 
Each input should have unix line endings (a simple `\n` at the end of the line). Then, standing in the `src` folder, simply add the command `groovy day<XY>.groovy` (like `groovy day01.groovy` or
`groovy day25.groovy`). If you want to change the input file location or name, it is __always__ the first line of every script.

## Tested with
* Groovy 4.0.24
* Java 23.0.1

## Expected runtime
* On "normal" hardware <10 s for every scripts
* On a Raspberry Pi 4 <4 min for every scripts (average: ~36 sec)

## Why is this repo created?
Simply sharing my code with anybody who is interested. I have created the codes to practice with groovy and to learn new stuff, which I have done a lot.
