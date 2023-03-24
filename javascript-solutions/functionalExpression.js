"use strict";

const cnst = (a) => (() => a);
const variable = (v) => ((x, y, z) => ({x, y, z})[v]);

const buildExpression =
    (operation) =>
        (...expressions) =>
            (...args) => operation(...expressions.map((expr) => expr(...args)));

const add = buildExpression((a, b) => a + b);
const subtract = buildExpression((a, b) => a - b);
const multiply = buildExpression((a, b) => a * b);
const divide = buildExpression((a, b) => a / b);

const negate = buildExpression((a) => -a);
const sin = buildExpression((a) => Math.sin(a));
const cos = buildExpression((a) => Math.cos(a));

const one = cnst(1)
const two = cnst(2)

const parse = (s) => {
    let binaryExpressions = {"+": add, "-": subtract, "*": multiply, "/": divide,};
    let unaryExpressions = {negate, sin, cos};
    let constants = {one, two};

    let elements = [];

    for (const element of s.split(/\s+/g)) {
        if (binaryExpressions[element] !== undefined) {
            let expr2 = elements.pop();
            let expr1 = elements.pop();
            elements.push(binaryExpressions[element](expr1, expr2))
        } else if (unaryExpressions[element] !== undefined) {
            elements.push(unaryExpressions[element](elements.pop()));
        } else if (constants[element] !== undefined) {
            elements.push(constants[element]);
        } else if (["x", "y", "z"].includes(element)) {
            elements.push(variable(element));
        } else if (element.length !== 0) {
            elements.push(cnst(Number(element)))
        }
    }

    return elements[elements.length - 1]
}

// Test x^2 - 2x + 1

let myExpr = add(
    subtract(
        multiply(variable("x"), variable("x")),
        multiply(cnst(2), variable("x"))
    ), cnst(1)
);

for (const x of [...Array(11).keys()]) {
    println(myExpr(x));
}
