package main.groovy.groovuinoml.dsl

import io.github.mosser.arduinoml.kernel.behavioral.ActionSensor
import io.github.mosser.arduinoml.kernel.behavioral.ActionLCD
import io.github.mosser.arduinoml.kernel.behavioral.ComposedCondition
import io.github.mosser.arduinoml.kernel.behavioral.Condition
import io.github.mosser.arduinoml.kernel.behavioral.ConditionSensor
import io.github.mosser.arduinoml.kernel.behavioral.ConditionDelay
import io.github.mosser.arduinoml.kernel.behavioral.ConditionSensor
import io.github.mosser.arduinoml.kernel.behavioral.OPERATOR
import io.github.mosser.arduinoml.kernel.behavioral.SimpleCondition
import io.github.mosser.arduinoml.kernel.behavioral.Action
import io.github.mosser.arduinoml.kernel.behavioral.State
import io.github.mosser.arduinoml.kernel.structural.Actuator
import io.github.mosser.arduinoml.kernel.structural.ActuatorBus
import io.github.mosser.arduinoml.kernel.structural.ActuatorPin
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
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuatorPin(name, n) }]
	}

	// actuatorLCD "name" bus n
	def actuatorLCD(String name) {
		[bus: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuatorBus(name, n) }]
	}
	
	// state "name" means actuator becomes signal [and actuator becomes signal]*n
	def state(String name) {
		List<Action> actions = new ArrayList<Action>()
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createState(name, actions)
		// recursive closure to allow multiple and statements
		def closure

		closure = { actuator -> 
			def sensor = { signal ->
				ActionSensor action = new ActionSensor()
				action.setActuator(actuator instanceof String ? (ActuatorPin)((GroovuinoMLBinding)this.getBinding()).getVariable(actuator) : (ActuatorPin)actuator)
				action.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				actions.add(action)
				[and: closure]
			}

			def lcd = { text ->
				[row: { rowNumber ->
					ActionLCD actionLCD = new ActionLCD()
					actionLCD.setActuatorLCD(actuator instanceof String ? (ActuatorBus)((GroovuinoMLBinding)this.getBinding()).getVariable(actuator) : (ActuatorBus)actuator)
					actionLCD.setDisplayText(true)
					actionLCD.setText(text)
					actionLCD.setRowNumber(rowNumber)
					actions.add(actionLCD)
					[and: closure]
				}]
			}

			[becomes: sensor, display: lcd]
		}

		[means: closure]
	}
	
	// initial state
	def initial(state) {
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().setInitialState(state instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state) : (State)state)
	}
	
	// from state1 to state2 when sensor becomes signal [and/or sensor becomes signal [with actuator becomes signal [and actuator becomes signal]]]
	def from(state1) {
		ComposedCondition currentCondition = new ComposedCondition()
		List<Action> actions = new ArrayList<Action>()

		def andClosure
		def orClosure
		def withClosure
		def afterClosure

		andClosure = { sensor ->
			[becomes: { signal ->
				ConditionSensor condition = new ConditionSensor()
				condition.setSensor(sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor)
				condition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				currentCondition = updateConditionTree(currentCondition, condition, OPERATOR.AND)
				[and: andClosure, or: orClosure, with: withClosure, after: afterClosure]
			}]
		}


		orClosure = { sensor ->
			[becomes: { signal ->
				ConditionSensor condition = new ConditionSensor()
				condition.setSensor(sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor)
				condition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				currentCondition = updateConditionTree(currentCondition, condition, OPERATOR.OR)
				[and: andClosure, or: orClosure, with: withClosure, after: afterClosure]
			}]
		}

		withClosure = { actuator ->
			def sensor = { signal ->
				ActionSensor action = new ActionSensor()
				action.setActuator(actuator instanceof String ? (ActuatorPin)((GroovuinoMLBinding)this.getBinding()).getVariable(actuator) : (ActuatorPin)actuator)
				action.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				actions.add(action)
				[and: withClosure]
			}

			def lcd = { text ->
				[row: { rowNumber ->
					ActionLCD actionLCD = new ActionLCD()
					actionLCD.setActuatorLCD(actuatorLCD instanceof String ? (ActuatorBus)((GroovuinoMLBinding)this.getBinding()).getVariable(actuatorLCD) : (ActuatorBus)actuatorLCD)
					actionLCD.setDisplayText(true)
					actionLCD.setText(text)
					actionLCD.setRowNumber(rowNumber)
					actions.add(actionLCD)
					[and: withClosure]
				}]
			}

			[becomes: sensor, display: lcd]
		}

		afterClosure = { time ->
			Condition condition = new ConditionDelay()
			condition.setDelay((int) time.amount * time.unit.multiplier)
			currentCondition = updateConditionTree(currentCondition, condition, OPERATOR.AND)
			[and: andClosure, or: orClosure, with: withClosure]
		}

		[to: { state2 ->
			[when: { sensor ->
				[becomes: { signal ->
					ConditionSensor condition = new ConditionSensor()
					condition.setSensor(sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor)
					condition.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
					currentCondition.setLeft(condition)
					((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
							state1 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state1) : (State)state1,
							state2 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state2) : (State)state2,
							currentCondition,
							actions)
					[and: andClosure, or: orClosure, with: withClosure, after: afterClosure]
				}]
			}, after: { time ->
				ConditionDelay condition = new ConditionDelay()
				condition.setDelay((int) time.amount * time.unit.multiplier)
				currentCondition.setLeft(condition)
				((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
						state1 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state1) : (State)state1,
						state2 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state2) : (State)state2,
						currentCondition,
						actions)
				[with: withClosure]
			}]
		}]
	}

	def updateConditionTree(ComposedCondition currentCondition, Condition newCondition, OPERATOR operator) {
		if (currentCondition.getRight() == null) {
			currentCondition.setOperator(operator)
			currentCondition.setRight(newCondition)
			return currentCondition
		} else {
			ComposedCondition composedCondition = new ComposedCondition()
			composedCondition.setOperator(operator)
			composedCondition.setLeft(currentCondition.getRight())
			composedCondition.setRight(newCondition)
			currentCondition.setRight(composedCondition)
			return composedCondition
		}
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
