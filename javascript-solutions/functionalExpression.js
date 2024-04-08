"use strict";

const cnst = (a) => (() => a);
const variable = (v) => ((x, y, z) => ({x, y, z})[v]);

const buildExpression = (operation) => (...expressions) => (...args) => operation(...expressions.map((expr) => expr(...args)));

const add = buildExpression((a, b) => a + b);
const subtract = buildExpression((a, b) => a - b);
const multiply = buildExpression((a, b) => a * b);
const divide = buildExpression((a, b) => a / b);

const negate = buildExpression((a) => -a);
const sin = buildExpression((a) => Math.sin(a));
const cos = buildExpression((a) => Math.cos(a));

const one = cnst(1)
const two = cnst(2)

let operators = {
    "negate": {func: negate, length: 1},
    "sin": {func: sin, length: 1},
    "cos": {func: cos, length: 1},

    "+": {func: add, length: 2},
    "-": {func: subtract, length: 2},
    "*": {func: multiply, length: 2},
    "/": {func: divide, length: 2},

    "one": {func: () => one, length: 0},
    "two": {func: () => two, length: 0},
}

const parse = (s) => {
    let elements = [], elementString = "";
    for (let i = 0, ch = s[0]; i < s.length + 1; i++, ch = s[i]) {
        if ((ch === " " || ch === undefined) && elementString.length !== 0) {
            if (operators[elementString] !== undefined) {
                let operator = operators[elementString];
                elements.push(operator.func(...Array.from({length: operator.length}, () => elements.pop()).reverse()));
            } else if (["x", "y", "z"].includes(elementString)) {
                elements.push(variable(elementString));
            } else if (elementString.length !== 0) {
                elements.push(cnst(Number(elementString)));
            }
            elementString = "";
        } else if (ch !== " ") {
            elementString += ch;
        }
    }
    return elements[elements.length - 1];
}

// Test x^2 - 2x + 1

let myExpr = add(subtract(multiply(variable("x"), variable("x")), multiply(cnst(2), variable("x"))), cnst(1));

for (const x of [...Array(11).keys()]) {
    println(myExpr(x));
}