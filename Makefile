
objects = out/Main.class out/Position.class out/CrashGame.class out/CrashGamePolicy.class

all: $(objects)
	@echo
	@echo Executing Main
	@echo
	@java -cp out/ Main

out/Main.class: Main.java
	@echo
	@echo Compiling Main.java
	@javac -d out/ Main.java

out/Position.class: Position.java
	@echo
	@echo Compiling Position.java
	@javac -d out/ Position.java

out/CrashGame.class: CrashGame.java
	@echo
	@echo Compiling CrashGame.java
	@javac -d out/ CrashGame.java

out/CrashGamePolicy.class: CrashGamePolicy.java
	@echo
	@echo Compiling CrashGamePolicy.java
	@javac -d out/ CrashGamePolicy.java

