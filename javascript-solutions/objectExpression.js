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

function Add(expr1, expr2) {
    Expression.call(this, expr1, expr2);
}

Add.prototype = Object.create(Expression.prototype);

Add.prototype._apply = (a, b) => a + b;
Add.prototype.operator = "+";
Add.prototype._applyDiff = (expr1, expr2) => new Add(expr1.diffValue, expr2.diffValue);

function Subtract(expr1, expr2) {
    Expression.call(this, expr1, expr2);
}

Subtract.prototype = Object.create(Expression.prototype);

Subtract.prototype._apply = (a, b) => a - b;
Subtract.prototype.operator = "-";
Subtract.prototype._applyDiff = (expr1, expr2) => new Subtract(expr1.diffValue, expr2.diffValue);

function Multiply(expr1, expr2) {
    Expression.call(this, expr1, expr2);
}

Multiply.prototype = Object.create(Expression.prototype);

Multiply.prototype._apply = (a, b) => a * b;
Multiply.prototype.operator = "*";
Multiply.prototype._applyDiff = (expr1, expr2) => new Add(
    new Multiply(expr1.diffValue, expr2.value),
    new Multiply(expr2.diffValue, expr1.value),
);


function Divide(expr1, expr2) {
    Expression.call(this, expr1, expr2);
}

Divide.prototype = Object.create(Expression.prototype);

Divide.prototype._apply = (a, b) => a / b;
Divide.prototype.operator = "/";
Divide.prototype._applyDiff = (expr1, expr2) => new Divide(
    new Subtract(
        new Multiply(expr1.diffValue, expr2.value),
        new Multiply(expr2.diffValue, expr1.value),
    ),
    new Multiply(expr2.value, expr2.value)
);

function Negate(expr) {
    Expression.call(this, expr);
}

Negate.prototype = Object.create(Expression.prototype);

Negate.prototype._apply = a => -a;
Negate.prototype.operator = "negate";
Negate.prototype._applyDiff = (expr) => new Negate(expr.diffValue);

function Exp(expr) {
    Expression.call(this, expr);
}

Exp.prototype = Object.create(Expression.prototype);

Exp.prototype._apply = a => Math.exp(a);
Exp.prototype.operator = "exp";
Exp.prototype._applyDiff = (expr) => new Multiply(new Exp(expr.value), expr.diffValue);

function Ln(expr) {
    Expression.call(this, expr);
}

Ln.prototype = Object.create(Expression.prototype);

Ln.prototype._apply = a => Math.log(a);
Ln.prototype.operator = "ln";
Ln.prototype._applyDiff = (expr) => new Multiply(new Divide(new Const(1), expr.value), expr.diffValue);

let expressions = [Variable, Const, Add, Subtract, Multiply, Divide, Negate, Exp, Ln]
let operators = {}

for (const expression of expressions) {
    operators[expression.prototype.operator] = expression;
}

function parse(s) {
    let elements = [], elementString = "";
    for (let i = 0, ch = s[0]; i < s.length + 1; i++, ch = s[i]) {
        if ((ch === " " || ch === undefined) && elementString.length !== 0) {
            if (operators[elementString] !== undefined) {
                let expression = operators[elementString];
                elements.push(new expression(
                    ...Array.from({length: expression.length}, () => elements.pop()).reverse()
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