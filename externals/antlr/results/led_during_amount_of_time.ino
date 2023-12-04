[INFO] Scanning for projects...
[INFO] 
[INFO] -------------< io.github.mosser.arduinoml:external-antlr >--------------
[INFO] Building external-antlr 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ external-antlr ---


Running the ANTLR compiler for ArduinoML
Using input file: C:\Users\youbi\Desktop\Cours\SI5\B2\DSL\dsl-team-a-arduino\externals\antlr\scenarios\led_during_amount_of_time.arduinoml
[WARNING] 
org.antlr.v4.runtime.misc.ParseCancellationException: line 13:17 extraneous input '=>' expecting {'}', 'after', IDENTIFIER}
    at io.github.mosser.arduinoml.externals.antlr.StopErrorListener.syntaxError (StopErrorListener.java:13)
    at org.antlr.v4.runtime.ProxyErrorListener.syntaxError (ProxyErrorListener.java:41)
    at org.antlr.v4.runtime.Parser.notifyErrorListeners (Parser.java:544)
    at org.antlr.v4.runtime.DefaultErrorStrategy.reportUnwantedToken (DefaultErrorStrategy.java:349)
    at org.antlr.v4.runtime.DefaultErrorStrategy.sync (DefaultErrorStrategy.java:247)
    at io.github.mosser.arduinoml.externals.antlr.grammar.ArduinomlParser.state (ArduinomlParser.java:627)
    at io.github.mosser.arduinoml.externals.antlr.grammar.ArduinomlParser.states (ArduinomlParser.java:475)
    at io.github.mosser.arduinoml.externals.antlr.grammar.ArduinomlParser.root (ArduinomlParser.java:130)
    at Main.buildModel (Main.java:48)
    at Main.main (Main.java:22)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:77)
    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke (Method.java:568)
    at org.codehaus.mojo.exec.ExecJavaMojo$1.run (ExecJavaMojo.java:282)
    at java.lang.Thread.run (Thread.java:833)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.741 s
[INFO] Finished at: 2023-12-04T11:16:57+01:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.codehaus.mojo:exec-maven-plugin:1.6.0:java (default-cli) on project external-antlr: An exception occured while executing the Java class. line 13:17 extraneous input '=>' expecting {'}', 'after', IDENTIFIER} -> [Help 1]
