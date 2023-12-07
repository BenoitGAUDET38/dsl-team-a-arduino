sensor "button1" pin 9
actuator "led1" pin 11

state "on" means "led1" becomes "high"
state "off" means "led1" becomes "low"

initial "off"

from "on" to "off" after 1.min
from "on" to "off" when "button1" becomes "high" after 5000.ms
from "off" to "on" when "button1" becomes "high" after 5.s

export "Temporal Transition"