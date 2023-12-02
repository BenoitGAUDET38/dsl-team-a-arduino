sensor "button1" pin 9
actuatorLCD "screen" bus 1

state "on" means "screen" display "Mouradox UwU"
state "off" means "screen" hide ""

initial "off"

from "off" to "on" when "button1" becomes "high"
from "on" to "off" when "button1" becomes "low"

export "Very simple LCD "