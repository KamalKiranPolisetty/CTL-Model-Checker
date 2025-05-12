package controller;

import java.util.LinkedHashSet;
import java.util.Set;

import model.ExpressionsHolder;
import model.State;
import model.Transition;

public class SATClass {
    protected Set<State> SAT(String expression) {
        System.out.println(String.format("Original Formula: %s", expression));
        Set<State> states = new LinkedHashSet<>();
        ExpressionsHolder expressionsHolder = new ExpressionsHolder();
        expression = expression.trim();
        CTLFormula.TypeSAT typeSAT = getTypeSAT(expression, expressionsHolder);

        System.out.println(String.format("Type of SAT: %s", typeSAT.toString()));
        System.out.println(String.format("Left Expression: %s", expressionsHolder.getLeftExpression()));
        System.out.println(String.format("Right Expression: %s", expressionsHolder.getRightExpression()));

        System.out.println("`````````````````````````````````````````````````");

        switch (typeSAT) {
            case AllTrue -> states.addAll(CTLFormula._kripke.states);
            case AllFalse -> {
            }
            case Atomic -> {
                for (State state : CTLFormula._kripke.states) {
                    if (state.atoms.contains(expressionsHolder.getLeftExpression()))
                        states.add(state);
                }
            }
            case Not -> {
                states.addAll(CTLFormula._kripke.states);
                Set<State> f1States = SAT(expressionsHolder.getLeftExpression());

                for (State state : f1States) {
                    if (states.contains(state))
                        states.remove(state);
                }
            }
            case And -> {
                Set<State> andF1States = SAT(expressionsHolder.getLeftExpression());
                Set<State> andF2States = SAT(expressionsHolder.getRightExpression());

                for (State state : andF1States) {
                    if (andF2States.contains(state))
                        states.add(state);
                }
            }
            case Or -> {
                Set<State> orF1States = SAT(expressionsHolder.getLeftExpression());
                Set<State> orF2States = SAT(expressionsHolder.getRightExpression());

                states = orF1States;
                for (State state : orF2States) {
                    if (!states.contains(state))
                        states.add(state);
                }
            }
            case Implies -> {
                String impliesFormula = "!" + expressionsHolder.getLeftExpression() + "|" + expressionsHolder.getRightExpression();
                states = SAT(impliesFormula);
            }
            case AX -> {
                String axFormula = "!" + "EX" + "!" + expressionsHolder.getLeftExpression();

                states = SAT(axFormula);

                Set<State> tempStates = new LinkedHashSet<>();
                for (State sourceState : states) {
                    for (Transition transition : CTLFormula._kripke.transitions) {
                        if (sourceState.equals(transition.fromState)) {
                            tempStates.add(sourceState);
                            break;
                        }
                    }
                }
                states = tempStates;
            }
            case EX -> {
                String exFormula = expressionsHolder.getLeftExpression();
                states = SAT_EX(exFormula);
            }
            case AU -> {
                StringBuilder auFormulaBuilder = new StringBuilder();
                auFormulaBuilder.append("!(E(!");
                auFormulaBuilder.append(expressionsHolder.getRightExpression());
                auFormulaBuilder.append("U(!");
                auFormulaBuilder.append(expressionsHolder.getLeftExpression());
                auFormulaBuilder.append("&!");
                auFormulaBuilder.append(expressionsHolder.getRightExpression());
                auFormulaBuilder.append("))|(EG!");
                auFormulaBuilder.append(expressionsHolder.getRightExpression());
                auFormulaBuilder.append("))");
                states = SAT(auFormulaBuilder.toString());
            }
            case EU -> states = SAT_EU(expressionsHolder.getLeftExpression(), expressionsHolder.getRightExpression());
            case EF -> {
                String efFormula = "E(TU" + expressionsHolder.getLeftExpression() + ")";
                states = SAT(efFormula);
            }
            case EG -> {
                String egFormula = "!AF!" + expressionsHolder.getLeftExpression();
                states = SAT(egFormula);
            }
            case AF -> {
                String afFormula = expressionsHolder.getLeftExpression();
                states = SAT_AF(afFormula);
            }
            case AG -> {
                String agFormula = "!EF!" + expressionsHolder.getLeftExpression();
                states = SAT(agFormula);
            }
            case Unknown -> throw new IllegalArgumentException("Invalid CTL expression");
        }
        return states;
    }

    private CTLFormula.TypeSAT getTypeSAT(String expression, ExpressionsHolder expressionsHolder)
    {
        expression = CTLFormula.removeExtraBrackets(expression);

        if (expression.contains(">")) {
            if (CTLFormula.isBinaryOp(expression, ">", expressionsHolder))
                return CTLFormula.TypeSAT.Implies;
        }

        if (expression.contains("&")) {
            if (CTLFormula.isBinaryOp(expression, "&", expressionsHolder))
                return CTLFormula.TypeSAT.And;
        }

        if (expression.contains("|")) {
            if (CTLFormula.isBinaryOp(expression, "|", expressionsHolder))
                return CTLFormula.TypeSAT.Or;
        }

        if (expression.startsWith("A(")) {
            String strippedExpression = expression.substring(2);
            if (CTLFormula.isBinaryOp(strippedExpression, "U", expressionsHolder))
                return CTLFormula.TypeSAT.AU;
        }

        if (expression.startsWith("E(")) {
            String strippedExpression = expression.substring(2);
            if (CTLFormula.isBinaryOp(strippedExpression, "U", expressionsHolder))
                return CTLFormula.TypeSAT.EU;
        }

        if (expression.equals("T")) {
            expressionsHolder.setLeftExpression(expression);
            return CTLFormula.TypeSAT.AllTrue;
        }
        if (expression.equals("F")) {
            expressionsHolder.setLeftExpression(expression);
            return CTLFormula.TypeSAT.AllFalse;
        }
        if (CTLFormula.isAtomic(expression)) {
            expressionsHolder.setLeftExpression(expression);
            return CTLFormula.TypeSAT.Atomic;
        }
        if (expression.startsWith("!")) {
            expressionsHolder.setLeftExpression(expression.substring(1));
            return CTLFormula.TypeSAT.Not;
        }
        if (expression.startsWith("AX")) {
            expressionsHolder.setLeftExpression(expression.substring(2));
            return CTLFormula.TypeSAT.AX;
        }
        if (expression.startsWith("EX")) {
            expressionsHolder.setLeftExpression(expression.substring(2));
            return CTLFormula.TypeSAT.EX;
        }
        if (expression.startsWith("EF")) {
            expressionsHolder.setLeftExpression(expression.substring(2));
            return CTLFormula.TypeSAT.EF;
        }
        if (expression.startsWith("EG")) {
            expressionsHolder.setLeftExpression(expression.substring(2));
            return CTLFormula.TypeSAT.EG;
        }
        if (expression.startsWith("AF")) {
            expressionsHolder.setLeftExpression(expression.substring(2));
            return CTLFormula.TypeSAT.AF;
        }
        if (expression.startsWith("AG")) {
            expressionsHolder.setLeftExpression(expression.substring(2));
            return CTLFormula.TypeSAT.AG;
        }
        if (expression.equals("")) {
            expressionsHolder.setLeftExpression(expression);
            return CTLFormula.TypeSAT.AllTrue;
        }

        return CTLFormula.TypeSAT.Unknown;
    }

    private Set<State> SAT_EX(String expression) {
        Set<State> x = SAT(expression);
        Set<State> y = PreClass.PreE(x);
        return y;
    }

    private Set<State> SAT_EU(String leftExpression, String rightExpression) {
        Set<State> w = SAT(leftExpression);
        Set<State> x = new LinkedHashSet<>();
        Set<State> y = SAT(rightExpression);

        x.addAll(CTLFormula._kripke.states);
        while (!AreListStatesEqual(x, y)) {
            x = y;
            Set<State> newY = new LinkedHashSet<>();
            Set<State> preEStates = PreClass.PreE(y);

            newY.addAll(y);
            Set<State> wAndPreE = new LinkedHashSet<>();
            for (State state: w) {
                if (preEStates.contains(state))
                    wAndPreE.add(state);
            }
            for (State state: wAndPreE) {
                if (!newY.contains(state))
                    newY.add(state);
            }
            y = newY;
        }
        return y;
    }

    private Set<State> SAT_AF(String expression) {
        Set<State> x = new LinkedHashSet<>();
        x.addAll(CTLFormula._kripke.states);
        Set<State> y = SAT(expression);
        while (!AreListStatesEqual(x, y)) {
            x = y;
            Set<State> newY = new LinkedHashSet<>();
            Set<State> preAStates = PreClass.PreA(y);
            newY.addAll(y);
            for (State state: preAStates) {
                if (!newY.contains(state))
                    newY.add(state);
            }
            y = newY;
        }
        return y;
    }

    private boolean AreListStatesEqual(Set<State> set1, Set<State> set2) {
        if (set1.size() != set2.size())
            return false;
        for (State state: set1) {
            if (!set2.contains(state))
                return false;
        }
        return true;
    }
}
