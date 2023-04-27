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

Expression.prototype.isComplexExpression = false;

Expression.prototype.prefix = function () {
    let result = `${this.operator} ` + (this._childExpressions.map((expr) => expr.prefix()).join(" "));
    if (this.isComplexExpression) {
        return `(${result})`;
    } else {
        return result;
    }
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
    this.toString = () => name.toString();
    this.prefix = () => name.toString();
    this.diff = diffVariable => new Const(diffVariable === name ? 1 : 0);
}

function Const(value) {
    this.evaluate = () => value;
    this.toString = () => value.toString();
    this.prefix = () => value.toString();
    this.diff = () => new Const(0);
}


let Operators = {}

function buildComplexExpression(apply, operator, length, applyDiff) {
    function NewExpression(...args) {
        Expression.call(this, ...args);
    }

    NewExpression.prototype = Object.create(Expression.prototype);

    NewExpression.prototype._apply = apply;
    NewExpression.prototype.operator = operator;
    NewExpression.prototype.length = length;

    NewExpression.prototype.isComplexExpression = true;

    NewExpression.prototype._applyDiff = applyDiff;

    Operators[operator] = NewExpression;

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
    Math.exp,
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

let Sum = buildComplexExpression(
    (...args) => args.reduce((accumulator, current) => accumulator + current, 0),
    "sum",
    -1,
    (...expressions) => new Sum(...expressions.map((expr) => expr.diffValue)),
)

let Avg = buildComplexExpression(
    (...args) => args.reduce((accumulator, current) => accumulator + current, 0) / args.length,
    "avg",
    -1,
    (...expressions) => new Divide(new Sum(...expressions.map((expr) => expr.diffValue)), new Const(expressions.length)),
)


function parse(s) {
    let elements = [], elementString = "";
    for (let i = 0, ch = s[0]; i < s.length + 1; i++, ch = s[i]) {
        if ((ch === " " || ch === undefined) && elementString.length !== 0) {
            if (Operators[elementString] !== undefined) {
                let expression = Operators[elementString];
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


class BaseParser {
    #stringIterator;

    #position = 0;

    #next;

    _init(s) {
        this.#stringIterator = s[Symbol.iterator]();

        this.#position = 0;
        this.#next = undefined;

        this._take();
    }

    _checkInit() {
        if (this.#stringIterator === undefined) {
            throw new NotInitializedParserError();
        }
    }

    _take() {
        this._checkInit();

        if (this.#next !== undefined && this.#next.done) {
            return undefined;
        }

        let result = this.#next === undefined ? undefined : this.#next.value;

        this.#next = this.#stringIterator.next();
        this.#position++;

        return result;
    }

    _test(ch) {
        this._checkInit();
        return !this._testEnd() && this.#next.value === ch;
    }

    _testEnd() {
        return this.#next !== undefined && this.#next.done;
    }

    _testNumber() {
        this._checkInit();
        let ch = this.#next.value;
        return !this._testEnd() && ch >= '0' && ch <= '9';
    }

    _testStringTokenEnd() {
        this._checkInit();
        return this._test('(') || this._test(')') || this._test(' ') || this._testEnd();
    }

    _expect(ch) {
        if (!this._test(ch)) {
            throw new IllegalArgumentParserError(this._positionErrorMessage(`Invalid char, expected ${ch}`));
        }
    }

    _expectEnd() {
        if (!this._testEnd()) {
            throw new IllegalArgumentParserError(this._positionErrorMessage(`Invalid char, expected end`));
        }
    }

    _positionErrorMessage(message) {
        return `Error at position ${this.#position}: ${message}`;
    }
}

class NotInitializedParserError extends Error {
    name = "NotInitializedParserError";

    constructor() {
        super("Parser is not initialized");
    }
}

class IllegalArgumentParserError extends Error {
    name = "IllegalArgumentParserError";

    constructor(message) {
        super(message);
    }
}

let stringOrEmpty = (s, empt = "\'empty\'") => s.length === 0 ? empt : s;

class PrefixParser extends BaseParser {
    #POSSIBLE_VARIABLES = ["x", "y", "z"]

    constructor() {
        super();
    }

    parse(s) {
        this._init(s);
        let result = this.#parseExpression();

        this.#skipWhitespaces();

        if (!this._testEnd()) this._expectEnd();
        return result;
    }

    #parseExpression() {
        this.#skipWhitespaces();
        if (this._test('(')) {
            return this.#parseComplexExpression();
        } else if (this._testNumber() || this._test('-')) {
            return this.#parseNumber();
        } else {
            return this.#parseVariable();
        }
    }

    #parseComplexExpression() {
        this.#skipWhitespaces();

        this._expect('(');
        this._take();

        let operator = this.#parseOperator();

        let operatorArguments = [];

        if (operator.prototype.length >= 0) {
            for (let i = 0; i < operator.prototype.length; i++) {
                operatorArguments.push(this.#parseExpression());
            }

            this.#skipWhitespaces();

            this._expect(')');
        } else {
            this.#skipWhitespaces();

            while (!this._test(')')) {
                operatorArguments.push(this.#parseExpression());
                this.#skipWhitespaces();

                if (this._testEnd()) {
                    throw new UnexpectedEndOfFilePrefixParserError();
                }
            }
        }

        this._take();

        return new operator(...operatorArguments);
    }

    #skipWhitespaces() {
        while (this._test(' ')) this._take();
    }

    #parseOperator() {
        this.#skipWhitespaces();

        let operatorChars = [];

        while (!this._testStringTokenEnd()) {
            operatorChars.push(this._take());
        }

        let operatorString = operatorChars.join("");
        let operator = Operators[operatorString];

        if (operator === undefined) {
            throw new IllegalOperatorPrefixParserError(
                this._positionErrorMessage(`Illegal operator - ${stringOrEmpty(operatorString)}`)
            );
        }

        return operator;
    }

    #parseVariable() {
        this.#skipWhitespaces();
        let variableChars = [];

        while (!this._testStringTokenEnd()) {
            variableChars.push(super._take());
        }

        let variableString = variableChars.join("");

        if (!this.#POSSIBLE_VARIABLES.includes(variableString)) {
            throw new IllegalVariableNamePrefixParserError(
                this._positionErrorMessage(`Illegal variable name - ${stringOrEmpty(variableString)}`));
        }

        return new Variable(variableString);
    }

    #parseNumber() {
        this.#skipWhitespaces();
        let numberChars = [];

        if (this._test('-')) {
            numberChars.push(this._take());
        }

        while (this._testNumber()) {
            numberChars.push(this._take());
        }

        let numberString = numberChars.join("");

        return new Const(Number(numberString));
    }
}

class IllegalVariableNamePrefixParserError extends Error {
    name = "IllegalVariableNamePrefixParserError";

    constructor(message) {
        super(message);
    }
}

class IllegalOperatorPrefixParserError extends Error {
    name = "IllegalOperatorPrefixParserError";

    constructor(message) {
        super(message);
    }
}

class UnexpectedEndOfFilePrefixParserError extends Error {
    name = "UnexpectedEndOfFilePrefixParserError";

    constructor() {
        super("Unexpected end of file");
    }
}


let prefixParser = new PrefixParser();
let parsePrefix = (s) => prefixParser.parse(s);
