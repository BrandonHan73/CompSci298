
objects = out/Config.class out/CrashGame.class out/Game.class out/Main.class out/Policy.class out/Position.class

all: $(objects)
	@echo
	@echo Executing Main
	@echo
	@java -cp out/ Main

out/Config.class: Config.java
	@echo
	@echo Compiling Config.java
	@javac -d out/ Config.java

out/CrashGame.class: CrashGame.java
	@echo
	@echo Compiling CrashGame.java
	@javac -d out/ CrashGame.java

out/Game.class: Game.java
	@echo
	@echo Compiling Game.java
	@javac -d out/ Game.java

out/Main.class: Main.java
	@echo
	@echo Compiling Main.java
	@javac -d out/ Main.java

out/Policy.class: Policy.java
	@echo
	@echo Compiling Policy.java
	@javac -d out/ Policy.java

out/Position.class: Position.java
	@echo
	@echo Compiling Position.java
	@javac -d out/ Position.java

