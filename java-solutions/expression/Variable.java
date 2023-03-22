package expression;

import java.util.Objects;

public class Variable implements ExpressionCommon {

    public enum VariableName {
        X, Y, Z;

        @Override
        public String toString() {
            return switch (this) {
                case X -> "x";
                case Y -> "y";
                case Z -> "z";
            };
        }
    }

    private final VariableName variableName;

    public Variable(String variableName) {
        this(switch (variableName) {
            case "x" -> VariableName.X;
            case "y" -> VariableName.Y;
            case "z" -> VariableName.Z;
            default -> throw new IllegalArgumentException("Variable name " + variableName + " is not supported");
        });
    }

    public Variable(VariableName variableName) {
        this.variableName = variableName;
    }

    @Override
    public int getPriority() {
        return Priority.CONST;
    }

    @Override
    public int evaluate() {
        throw new IllegalArgumentException("Value for variable " + variableName + " was not found");
    }

    @Override
    public int evaluate(int x) {
        if (variableName.equals(VariableName.X)) {
            return x;
        }
        throw new IllegalArgumentException("Value for variable " + variableName + " was not found");
    }

    @Override
    public int evaluate(int x, int y) {
        return switch (variableName) {
            case X -> x;
            case Y -> y;
            default -> throw new IllegalArgumentException("Value for variable " + variableName + " was not found");
        };
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (variableName) {
            case X -> x;
            case Y -> y;
            case Z -> z;
        };
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return variableName == variable.variableName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableName);
    }

    @Override
    public String toString() {
        return variableName.toString();
    }
}
