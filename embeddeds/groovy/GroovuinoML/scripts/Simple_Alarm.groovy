sensor "button1" pin 9
actuator "led1" pin 11
actuator "buzzer1" pin 12

state "on" means "led1" becomes "high" and "buzzer1" becomes "high"
state "off" means "led1" becomes "low" and "buzzer1" becomes "low"

initial "off"

from "off" to "on" when "button1" becomes "high"
from "on" to "off" when "button1" becomes "low"

export "Very simple alarm"