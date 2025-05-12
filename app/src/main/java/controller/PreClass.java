package controller;

import java.util.LinkedHashSet;
import java.util.Set;

import static controller.CTLFormula._kripke;
import model.State;
import model.Transition;

public class PreClass {
    static Set<State> PreE(Set<State> y)
    {
        Set<State> states = new LinkedHashSet<>();
        for (State sourceState: _kripke.states) {
            for (State destState: y) {
                Transition myTransition = new Transition(sourceState, destState);

                if (_kripke.transitions.contains(myTransition)) {
                    if (!states.contains(sourceState)) {
                        states.add(sourceState);
                    }
                }
            }
        }
        return states;
    }

    static Set<State> PreA(Set<State> y)
    {
        Set<State> PreEY = PreE(y);
        Set<State> S_Minus_Y = new LinkedHashSet<>();
        S_Minus_Y.addAll(_kripke.states);
        for (State state : y) {
            if (S_Minus_Y.contains(state))
                S_Minus_Y.remove(state);
        }
        Set<State> PreE_S_Minus_Y = PreE(S_Minus_Y);
        for (State state: PreE_S_Minus_Y) {
            if (PreEY.contains(state))
                PreEY.remove(state);
        }
        return PreEY;
    }
}
