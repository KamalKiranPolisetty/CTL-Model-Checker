package model;

public class ExpressionsHolder {
    private String leftExpression;
    private String rightExpression;

    public ExpressionsHolder() {
        this.leftExpression = "";
        this.rightExpression = "";
    }

    public void setLeftExpression(String leftExpression) {
        this.leftExpression = leftExpression;
    }

    public void setRightExpression(String rightExpression) {
        this.rightExpression = rightExpression;
    }

    public String getLeftExpression() {
        return this.leftExpression;
    }

    public String getRightExpression() {
        return this.rightExpression;
    }
}
