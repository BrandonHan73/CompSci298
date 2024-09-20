
objects = out/Position.class out/CrashGame.class out/CrashGamePolicy.class

all: $(objects)

out/Position.class: Position.java
	javac -d out/ Position.java

out/CrashGame.class: CrashGame.java
	javac -d out/ CrashGame.java

out/CrashGamePolicy.class: CrashGamePolicy.java
	javac -d out/ CrashGamePolicy.java

