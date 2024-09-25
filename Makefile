
objects = out/Config.class out/CrashGame.class out/Game.class out/Main.class out/Policy.class out/Position.class out/LogisticRegression.class out/FictitiousPlay.class
libraries = lib/Jama-1.0.3.jar

class_path = .:lib/Jama-1.0.3.jar

all: $(objects) $(libraries)
	@echo
	@echo Executing Main
	@echo
	@java -cp out:lib/Jama-1.0.3.jar Main

lib/Jama-1.0.3.jar:
	@wget -P lib https://math.nist.gov/javanumerics/jama/Jama-1.0.3.jar

out/Config.class: Config.java
	@echo
	@echo Compiling Config.java
	@javac -d out/ -cp $(class_path) Config.java

out/CrashGame.class: CrashGame.java
	@echo
	@echo Compiling CrashGame.java
	@javac -d out/ -cp $(class_path) CrashGame.java

out/Game.class: Game.java
	@echo
	@echo Compiling Game.java
	@javac -d out/ -cp $(class_path) Game.java

out/Main.class: Main.java
	@echo
	@echo Compiling Main.java
	@javac -d out/ -cp $(class_path) Main.java

out/Policy.class: Policy.java
	@echo
	@echo Compiling Policy.java
	@javac -d out/ -cp $(class_path) Policy.java

out/Position.class: Position.java
	@echo
	@echo Compiling Position.java
	@javac -d out/ -cp $(class_path) Position.java

out/LogisticRegression.class: LogisticRegression.java
	@echo
	@echo Compiling LogisticRegression.java
	@javac -d out/ -cp $(class_path) LogisticRegression.java

out/FictitiousPlay.class: FictitiousPlay.java
	@echo
	@echo Compiling FictitiousPlay.java
	@javac -d out/ -cp $(class_path) FictitiousPlay.java

