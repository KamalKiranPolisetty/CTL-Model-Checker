package model;

public class Transition {
    public String transitionName;
    public State fromState;
    public State toState;

    public Transition(State fromState, State toState) {
        this.transitionName = "";
        this.fromState = fromState;
        this.toState = toState;
    }

    public Transition(String transitionName, State fromState, State toState) {
        this.transitionName = transitionName;
        this.fromState = fromState;
        this.toState = toState;
    }

    @Override
    public boolean equals(Object obj) {
        // If the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(obj instanceof Transition)) {
            return false;
        }

        Transition other = (Transition) obj;
        if (this.fromState == other.fromState && this.toState == other.toState) {
            return true;
        }
        return false;
    }
}