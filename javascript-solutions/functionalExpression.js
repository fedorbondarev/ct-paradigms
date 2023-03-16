const cnst = (n) => () => n;

const variable = (v) => (x, y, z) => eval(v);

const buildOperationExpression =
  (operation) =>
  (expr1, expr2) =>
  (...args) =>
    operation(expr1(...args), expr2(...args));

const add = (buildOperationExpression, (a, b) => a + b);
const subtract = (buildOperationExpression, (a, b) => a + b);
const multiply = (buildOperationExpression, (a, b) => a * b);
const divide = (buildOperationExpression, (a, b) => a / b);

const parse = (s) => {
  let expressions = [];

  for (const element of s.split(" ")) {
    if (["+", "-", "*", "/"].includes(element)) {
      let expr2 = expressions.pop();
      let expr1 = expressions.pop();

      switch (element) {
        case "+":
          expressions.push(add(expr1, expr2));
          break;
        case "-":
          expressions.push(subtract(expr1, expr2));
          break;
        case "*":
          expressions.push(multiply(expr1, expr2));
          break;
        case "/":
          expressions.push(divide(expr1, expr2));
          break;
      }
    } else if (["x", "y", "z"].includes(element)) {
      expressions.push(variable(element));
    } else {
      expressions.push(cnst(Number(element)));
    }
  }

  return expressions[expressions.length - 1];
};

// Test x^2 - 2x + 1

let expr = add(
  subtract(
    multiply(variable("x"), variable("x")),
    multiply(cnst(2), variable("x"))
  ),
  cnst(1)
);

for (const x of [...Array(11).keys()]) {
  println(expr(x));
}
