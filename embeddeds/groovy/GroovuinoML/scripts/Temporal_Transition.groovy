sensor "button1" pin 9
sensor "button2" pin 10
actuator "led1" pin 11
actuator "led2" pin 12

state "on" means "led1" becomes "high"
state "off" means "led1" becomes "low"

initial "off"

from "on" to "off" when "button1" becomes "high"
from "off" to "on" after 2.min
from "off" to "on" when "button1" becomes "high" after 800.ms
from "off" to "on" when "button2" becomes "high" after 5.s.plus(200.ms) with "led2" becomes "high"

export "Temporal Transition"