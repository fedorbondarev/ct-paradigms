const cnst = (a) => (() => a);
const variable = (v) => ((x, y, z) => ({x, y, z})[v]);

const negate = (expr) => ((...args) => -expr(...args))

const buildBinaryExpression =
    (operation) =>
        (expr1, expr2) =>
            (...args) =>
                operation(expr1(...args), expr2(...args));

const add = buildBinaryExpression((a, b) => a + b);
const subtract = buildBinaryExpression((a, b) => a - b);
const multiply = buildBinaryExpression((a, b) => a * b);
const divide = buildBinaryExpression((a, b) => a / b);

const parse = (s) => {
    let expressions = [];

    let t = "";

    for (let i = 0; i < s.length; i++) {
        let ch = s[i];

        if (/\s/.test(ch)) continue;

        if (
            /\d|\./.test(ch)
            || (ch === "-" && /\d|\./.test(s[i + 1]))
        ) {
            t += ch;
            if (!/\d|\./.test(s[i + 1])) {
                let n = Number(t);
                if (!isNaN(n)) {
                    expressions.push(cnst(n));
                    t = "";
                }
            }
        } else if (["+", "-", "*", "/"].includes(ch)) {
            let expr2 = expressions.pop();
            let expr1 = expressions.pop();

            expressions.push(
                (
                    ({
                        "+": add,
                        "-": subtract,
                        "*": multiply,
                        "/": divide
                    })[ch]
                )(expr1, expr2)
            )
        } else if (["x", "y", "z"].includes(ch)) {
            expressions.push(variable(ch));
        } else if (ch === "n") {
            if (s.substring(i, i + 6) === "negate") {
                expressions.push(negate(expressions.pop()));
            }
        }
    }

    return expressions[expressions.length - 1];
};

// Test x^2 - 2x + 1

let myExpr = add(
    subtract(
        multiply(variable("x"), variable("x")),
        multiply(cnst(2), variable("x"))
    ),
    cnst(1)
);

for (const x of [...Array(11).keys()]) {
    println(myExpr(x));
}
