grammar Arduinoml;


/******************
 ** Parser rules **
 ******************/

root            :   declaration bricks states EOF;

declaration     :   'application' name=IDENTIFIER;

bricks          :   (sensor|actuator|actuatorLCD)+;
    sensor      :   'sensor'   location ;
    actuator    :   'actuator' location ;
    actuatorLCD :   'actuatorLCD' location ;
    location    :   id=IDENTIFIER ':' port=NUMBER;

states          :   state+;
    state       :   initial? name=IDENTIFIER '{'  (action+)? (actionLCD+)? (transition+)?  '}';
    action      :   receiver=IDENTIFIER '<=' value=SIGNAL;
    actionLCD   :   receiver=IDENTIFIER (':' text=STRING ('row' rowNumber=NUMBER)? )? '<=' isDisplayed=BOOLEAN;
    transition  :   (('after' time=NUMBER 'ms') | (trigger=IDENTIFIER 'is' value=SIGNAL (more=condition)?) ('(' mealy=newAction ')')?)) '=>' next=IDENTIFIER ;
    condition   :   operator=OPERATOR trigger=IDENTIFIER 'is' value=SIGNAL (more=condition)?;
    newAction   :   receiver=IDENTIFIER '<=' value=SIGNAL (', ' mealy=newAction)?;
    initial     :   '->';

/*****************
 ** Lexer rules **
 *****************/

NUMBER          :   [1-9][0-9]*;
IDENTIFIER      :   LOWERCASE (LOWERCASE|UPPERCASE)+;
SIGNAL          :   'HIGH' | 'LOW';
OPERATOR        :   'AND' | 'OR';
BOOLEAN         :   'TRUE' | 'FALSE';
STRING          :   ["][' 'a-zA-Z1-9:]*["];

/*************
 ** Helpers **
 *************/

fragment LOWERCASE  : [a-z];                                 // abstract rule, does not really exists
fragment UPPERCASE  : [A-Z];
NEWLINE             : ('\r'? '\n' | '\r')+      -> skip;
WS                  : ((' ' | '\t')+)           -> skip;     // who cares about whitespaces?
COMMENT             : '#' ~( '\r' | '\n' )*     -> skip;     // Single line comments, starting with a #
