
objects = out/Main.class out/Position.class out/CrashGame.class out/CrashGamePolicy.class

all: $(objects)
	java -cp out/ Main

out/Main.class: Main.java
	javac -d out/ Main.java

out/Position.class: Position.java
	javac -d out/ Position.java

out/CrashGame.class: CrashGame.java
	javac -d out/ CrashGame.java

out/CrashGamePolicy.class: CrashGamePolicy.java
	javac -d out/ CrashGamePolicy.java

