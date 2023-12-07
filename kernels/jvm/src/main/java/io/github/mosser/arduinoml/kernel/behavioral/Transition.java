package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.*;

import java.util.ArrayList;
import java.util.List;

public class Transition implements Visitable {

	private State next;


	private Condition condition;

	private List<Action> actions = new ArrayList<>();

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public State getNext() {
		return next;
	}

	public void setNext(State next) {
		this.next = next;
	}


	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}


	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "Transition{" +
				"next=" + next.toString() +
				"\ncondition=" + condition +
				"\nactions=" + actions +
				'}';
	}
}
