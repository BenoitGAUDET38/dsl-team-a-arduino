sensor "button1" pin 9
sensor "button2" pin 10
actuator "buzzer1" pin 12

state "on" means "buzzer1" becomes "high"
state "off" means "buzzer1" becomes "low"
state "button1_on"
state "button2_on"

initial "off"

from "off" to "button1_on" when "button1" becomes "high"
from "off" to "button2_on" when "button2" becomes "high"
from "off" to "on" when "button1" becomes "high" and "button2" becomes "high"
from "button1_on" to "off" when "button1" becomes "low"
from "button2_on" to "off" when "button2" becomes "low"
from "on" to "off" when "button1" becomes "low" or "button2" becomes "low"

export "Dual-check alarm"