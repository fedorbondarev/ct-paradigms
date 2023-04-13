// noinspection JSUnusedGlobalSymbols,JSUnresolvedReference

"use strict";


function Expression(...childExpressions) {
    this._childExpressions = childExpressions;
}

Expression.prototype._callFromChildren = function (func, childMapFunc) {
    return func(...this._childExpressions.map(childMapFunc));
}

Expression.prototype.evaluate = function (...args) {
    return this._callFromChildren(this._apply, child => child.evaluate(...args));
}

Expression.prototype.toString = function () {
    return this._childExpressions.join(" ") + ` ${this.operator}`;
}

Expression.prototype.diff = function (diffVariable) {
    return this._callFromChildren(this._applyDiff,
        (child) => (
            {
                diffValue: child.diff(diffVariable),
                value: child
            }
        )
    )
}


function Variable(name) {
    this.evaluate = (x, y, z) => ({x, y, z}[name]);
    this.toString = () => name;
    this.diff = diffVariable => new Const(diffVariable === name ? 1 : 0);
}

function Const(value) {
    this.evaluate = () => value;
    this.toString = () => value.toString();
    this.diff = () => new Const(0);
}


let operators = {}

function buildComplexExpression(apply, operator, length, applyDiff) {
    function NewExpression(...args) {
        Expression.call(this, ...args);
    }

    NewExpression.prototype = Object.create(Expression.prototype);

    NewExpression.prototype._apply = apply;
    NewExpression.prototype.operator = operator;
    NewExpression.prototype.length = length;

    NewExpression.prototype._applyDiff = applyDiff;

    operators[operator] = NewExpression;

    return NewExpression;
}

let Add = buildComplexExpression(
    (a, b) => a + b,
    "+",
    2,
    (expr1, expr2) => new Add(expr1.diffValue, expr2.diffValue),
)

let Subtract = buildComplexExpression(
    (a, b) => a - b,
    "-",
    2,
    (expr1, expr2) => new Subtract(expr1.diffValue, expr2.diffValue),
)

let Multiply = buildComplexExpression(
    (a, b) => a * b,
    "*",
    2,
    (expr1, expr2) => new Add(
        new Multiply(expr1.diffValue, expr2.value),
        new Multiply(expr2.diffValue, expr1.value),
    ),
)

let Divide = buildComplexExpression(
    (a, b) => a / b,
    "/",
    2,
    (expr1, expr2) => new Divide(
        new Subtract(
            new Multiply(expr1.diffValue, expr2.value),
            new Multiply(expr2.diffValue, expr1.value),
        ),
        new Multiply(expr2.value, expr2.value)
    ),
)

let Negate = buildComplexExpression(
    a => -a,
    "negate",
    1,
    (expr) => new Negate(expr.diffValue),
)

let Exp = buildComplexExpression(
    a => Math.exp(a),
    "exp",
    1,
    (expr) => new Multiply(new Exp(expr.value), expr.diffValue),
)

let Ln = buildComplexExpression(
    Math.log,
    "ln",
    1,
    (expr) => new Multiply(new Divide(new Const(1), expr.value), expr.diffValue),
)

function parse(s) {
    let elements = [], elementString = "";
    for (let i = 0, ch = s[0]; i < s.length + 1; i++, ch = s[i]) {
        if ((ch === " " || ch === undefined) && elementString.length !== 0) {
            if (operators[elementString] !== undefined) {
                let expression = operators[elementString];
                elements.push(new expression(
                    ...Array.from(
                        {length: expression.prototype.length}, () => elements.pop()
                    ).reverse()
                ));
            } else if (["x", "y", "z"].includes(elementString)) {
                elements.push(new Variable(elementString));
            } else if (elementString.length !== 0) {
                elements.push(new Const(Number(elementString)));
            }
            elementString = "";
        } else if (ch !== " ") {
            elementString += ch;
        }
    }
    return elements[elements.length - 1];
}
