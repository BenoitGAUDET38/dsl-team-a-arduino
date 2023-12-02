package main.groovy.groovuinoml.dsl

import io.github.mosser.arduinoml.kernel.behavioral.ActionLCD
import io.github.mosser.arduinoml.kernel.behavioral.Condition
import io.github.mosser.arduinoml.kernel.behavioral.OPERATOR
import io.github.mosser.arduinoml.kernel.structural.ActuatorLCD

import java.util.List;

import io.github.mosser.arduinoml.kernel.behavioral.Action
import io.github.mosser.arduinoml.kernel.behavioral.State
import io.github.mosser.arduinoml.kernel.structural.Actuator
import io.github.mosser.arduinoml.kernel.structural.Sensor
import io.github.mosser.arduinoml.kernel.structural.SIGNAL

abstract class GroovuinoMLBasescript extends Script {
	// sensor "name" pin n
	def sensor(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n) },
		onPin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n)}]
	}
	
	// actuator "name" pin n
	def actuator(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuator(name, n) }]
	}

	// actuatorLCD "name" bus n
	def actuatorLCD(String name) {
		[bus: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuatorLCD(name, n) }]
	}
	
	// state "name" means actuator becomes signal [and actuator becomes signal]*n
	def state(String name) {
		List<Action> actions = new ArrayList<Action>()
		List<ActionLCD> actionLCDS = new ArrayList<ActionLCD>()
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createState(name, actions, actionLCDS)
		// recursive closure to allow multiple and statements
		def closure
		closure = { actuator -> 
			[becomes: { signal ->
				Action action = new Action()
				action.setActuator(actuator instanceof String ? (Actuator)((GroovuinoMLBinding)this.getBinding()).getVariable(actuator) : (Actuator)actuator)
				action.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				actions.add(action)
				[and: closure]
			}]
		}

		def closureLCD
		closureLCD = { actuatorLCD ->
			[display: { text ->
				ActionLCD actionLCD = new ActionLCD()
				actionLCD.setActuatorLCD(actuatorLCD instanceof String ? (ActuatorLCD)((GroovuinoMLBinding)this.getBinding()).getVariable(actuatorLCD) : (ActuatorLCD)actuatorLCD)
				actionLCD.setDisplayText(true)
				actionLCD.setText(text)
				actionLCDS.add(actionLCD)
				[and: closureLCD]
			},
			hide: { text ->
				ActionLCD actionLCD = new ActionLCD()
				actionLCD.setActuatorLCD(actuatorLCD instanceof String ? (ActuatorLCD)((GroovuinoMLBinding)this.getBinding()).getVariable(actuatorLCD) : (ActuatorLCD)actuatorLCD)
				actionLCD.setDisplayText(false)
				actionLCD.setText("")
				actionLCDS.add(actionLCD)
				[and: closureLCD]
			}
			]
		}
		[means: closure, meansLCD: closureLCD]
	}
	
	// initial state
	def initial(state) {
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().setInitialState(state instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state) : (State)state)
	}
	
	// from state1 to state2 when sensor becomes signal [and/or sensor becomes signal [with actuator becomes signal [and actuator becomes signal]]]
	def from(state1) {
		List<Condition> conditions = new ArrayList<Condition>()
		List<Action> actions = new ArrayList<Action>()

		def andClosure
		def orClosure
		def withClosure
		andClosure = { sensor ->
			[becomes: { signal ->
				Condition condition = new Condition()
				condition.setOperator(OPERATOR.AND)
				condition.setSensor(sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor)
				condition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				conditions.add(condition)
				[and: andClosure, or: orClosure, with: withClosure]
			}]
		}


		orClosure = { sensor ->
			[becomes: { signal ->
				Condition condition = new Condition()
				condition.setOperator(OPERATOR.OR)
				condition.setSensor(sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor)
				condition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				conditions.add(condition)
				[and: andClosure, or: orClosure, with: withClosure]
			}]
		}

		withClosure = { actuator ->
			[becomes: { signal ->
				Action action = new Action()
				action.setActuator(actuator instanceof String ? (Actuator) ((GroovuinoMLBinding) this.getBinding()).getVariable(actuator) : (Actuator) actuator)
				action.setValue(signal instanceof String ? (SIGNAL) ((GroovuinoMLBinding) this.getBinding()).getVariable(signal) : (SIGNAL) signal)
				actions.add(action)
				[and: withClosure]
			}]
		}

		[to: { state2 ->
			((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
					state1 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state1) : (State)state1,
					state2 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state2) : (State)state2,
					conditions, actions)
			[when: { sensor ->
				[becomes: { signal ->
					Condition condition = new Condition()
					condition.setOperator(OPERATOR.EMPTY)
					condition.setSensor(sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor)
					condition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
					conditions.add(condition)
					[and: andClosure, or: orClosure, with: withClosure]
				}]
			}]
		}]
	}
	
	// export name
	def export(String name) {
		println(((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().generateCode(name).toString())
	}
	
	// disable run method while running
	int count = 0
	abstract void scriptBody()
	def run() {
		if(count == 0) {
			count++
			scriptBody()
		} else {
			println "Run method is disabled"
		}
	}
}
