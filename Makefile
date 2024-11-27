
libraries = lib/Jama-1.0.3.jar

class_path = src/:$(libraries)

all: $(libraries) 
	@echo Compiling Main.java
	@javac -d out/ -cp $(class_path) src/Main.java
	@echo
	@echo Executing Main
	@echo
	@java -cp out:lib/Jama-1.0.3.jar Main

lib/Jama-1.0.3.jar:
	@wget -P lib https://math.nist.gov/javanumerics/jama/Jama-1.0.3.jar

out/base/Config.class: src/base/Config.java
	@echo Compiling base/Config.class  
	@javac -d out/ -cp $(class_path) src/base/Config.java

out/base/Position.class: src/base/Position.java
	@echo Compiling base/Position.class  
	@javac -d out/ -cp $(class_path) src/base/Position.java

out/base/State.class: src/base/State.java
	@echo Compiling base/State.class  
	@javac -d out/ -cp $(class_path) src/base/State.java

out/base/Log.class: src/base/Log.java
	@echo Compiling base/Log.class  
	@javac -d out/ -cp $(class_path) src/base/Log.java

out/base/Utility.class: src/base/Utility.java
	@echo Compiling base/Utility.class  
	@javac -d out/ -cp $(class_path) src/base/Utility.java

out/environment/CrashGame.class: src/environment/CrashGame.java
	@echo Compiling environment/CrashGame.class
	@javac -d out/ -cp $(class_path) src/environment/CrashGame.java

out/environment/CrashGameTest.class: src/environment/CrashGameTest.java
	@echo Compiling environment/CrashGameTest.class
	@javac -d out/ -cp $(class_path) src/environment/CrashGameTest.java

out/environment/Game.class: src/environment/Game.java
	@echo Compiling environment/Game.class
	@javac -d out/ -cp $(class_path) src/environment/Game.java

out/environment/RockPaperScissors.class: src/environment/RockPaperScissors.java
	@echo Compiling environment/RockPaperScissors.class
	@javac -d out/ -cp $(class_path) src/environment/RockPaperScissors.java

out/policy/DiscreteGamePolicy.class: src/policy/DiscreteGamePolicy.java
	@echo Compiling policy/DiscreteGamePolicy.class
	@javac -d out/ -cp $(class_path) src/policy/DiscreteGamePolicy.java

out/policy/LogisticRegression.class: src/policy/LogisticRegression.java
	@echo Compiling policy/LogisticRegression.class
	@javac -d out/ -cp $(class_path) src/policy/LogisticRegression.java

out/policy/NashSolver.class: src/policy/NashSolver.java
	@echo Compiling policy/NashSolver.class
	@javac -d out/ -cp $(class_path) src/policy/NashSolver.java

out/policy/Policy.class: src/policy/Policy.java
	@echo Compiling policy/Policy.class
	@javac -d out/ -cp $(class_path) src/policy/Policy.java

out/policy/RandomPolicy.class: src/policy/RandomPolicy.java
	@echo Compiling policy/RandomPolicy.class
	@javac -d out/ -cp $(class_path) src/policy/RandomPolicy.java

out/policy/StateQ.class: src/policy/StateQ.java
	@echo Compiling policy/StateQ.class
	@javac -d out/ -cp $(class_path) src/policy/StateQ.java

