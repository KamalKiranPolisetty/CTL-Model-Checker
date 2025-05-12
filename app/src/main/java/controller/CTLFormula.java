package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.ExpressionsHolder;
import model.KripkeStructure;
import model.State;

public final class CTLFormula {
    public enum TypeSAT {
    	Unknown,
        AllTrue,
        AllFalse,
        Atomic,
        Not,
        And,
        Or,
        Implies,
        AX,
        EX,
        AU,
        EU,
        EF,
        EG,
        AF,
        AG
    }
    public static KripkeStructure _kripke;
    public State state;
    public String _expression;
    public Map<String, String> convertedStr;

    public CTLFormula(String expression, State state, KripkeStructure kripke) {
        this.convertedStr = new HashMap<>();
        this.convertedStr.put("and", "&");
        this.convertedStr.put("or", "|");
        this.convertedStr.put("->", ">");
        this.convertedStr.put("not", "!");
        CTLFormula._kripke = kripke;
        this.state = state;
        this._expression = ConvertToSystemFormula(expression);
    }

    public String ConvertToSystemFormula(String expression) {
        for (String key : this.convertedStr.keySet()) {
            expression = expression.replace(key, this.convertedStr.get(key));
        }

        return expression;
    }

    public boolean IsSatisfy() {
        SATClass satClass = new SATClass();
        Set<State> states = satClass.SAT(_expression);
        for(State returnedStates: states){
            if(returnedStates.equals(state))
                return true;
        }
        return false;
    }

    static boolean isAtomic(String expression) {
        return _kripke.atoms.contains(expression);
    }

    static boolean isBinaryOp(String expression, String symbol, ExpressionsHolder expressionsHolder) {
        boolean isBinaryOp = false;
        if (expression.contains(symbol)) {
            int openParanthesisCount = 0;
            int closeParanthesisCount = 0;

            int i=0;
            while (i < expression.length() - 1) {
                String currentChar = expression.substring(i, i + 1);
                if (currentChar.equals(symbol) && openParanthesisCount == closeParanthesisCount) {
                    expressionsHolder.setLeftExpression(expression.substring(0, i));
                    expressionsHolder.setRightExpression(expression.substring(i + 1));
                    isBinaryOp = true;
                    break;
                } else if (currentChar.equals("(")) {
                    openParanthesisCount++;
                } else if (currentChar.equals(")")) {
                    closeParanthesisCount++;
                }
                i++;
            }
        }
        return isBinaryOp;
    }

    protected static String removeExtraBrackets(String expression) {
        String newExpression = expression;
        if (expression.startsWith("(") && expression.endsWith(")")) {
            newExpression = expression.substring(1);
        } else if (!expression.startsWith("(") && expression.endsWith(")")) {
            newExpression = expression.substring(0, expression.length() - 1);
        } else if (expression.startsWith("(") && !expression.endsWith(")")) {
            newExpression = expression.substring(1);
        }
        
        return newExpression;
    }
}
