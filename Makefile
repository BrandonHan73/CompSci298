
base_objects = out/base/Config.class out/base/Position.class out/base/State.class out/base/Utility.class
game_objects = out/game/CrashGame.class out/game/CrashGameTest.class out/game/CycleGame.class out/game/CycleGameTest.class out/game/Game.class out/game/RockPaperScissors.class
policy_objects = out/policy/DiscreteGamePolicy.class out/policy/LogisticRegression.class out/policy/NashSolver.class out/policy/Policy.class out/policy/RandomPolicy.class out/policy/StateQ.class

objects = $(game_objects) $(policy_objects) $(base_objects) 

libraries = lib/Jama-1.0.3.jar

class_path = .:lib/Jama-1.0.3.jar

all: $(libraries) $(objects) 
	@echo Compiling Main.java
	@javac -d out/ -cp $(class_path) base/Main.java
	@echo
	@echo Executing Main
	@echo
	@java -cp out:lib/Jama-1.0.3.jar base.Main

lib/Jama-1.0.3.jar:
	@wget -P lib https://math.nist.gov/javanumerics/jama/Jama-1.0.3.jar

out/base/Config.class: base/Config.java
	@echo Compiling base/Config.class  
	@javac -d out/ -cp $(class_path) base/Config.java

out/base/Position.class: base/Position.java
	@echo Compiling base/Position.class  
	@javac -d out/ -cp $(class_path) base/Position.java

out/base/State.class: base/State.java
	@echo Compiling base/State.class  
	@javac -d out/ -cp $(class_path) base/State.java

out/base/Utility.class: base/Utility.java
	@echo Compiling base/Utility.class  
	@javac -d out/ -cp $(class_path) base/Utility.java

out/game/CrashGame.class: game/CrashGame.java
	@echo Compiling game/CrashGame.class
	@javac -d out/ -cp $(class_path) game/CrashGame.java

out/game/CrashGameTest.class: game/CrashGameTest.java
	@echo Compiling game/CrashGameTest.class
	@javac -d out/ -cp $(class_path) game/CrashGameTest.java

out/game/CycleGame.class: game/CycleGame.java
	@echo Compiling game/CycleGame.class
	@javac -d out/ -cp $(class_path) game/CycleGame.java

out/game/CycleGameTest.class: game/CycleGameTest.java
	@echo Compiling game/CycleGameTest.class
	@javac -d out/ -cp $(class_path) game/CycleGameTest.java

out/game/Game.class: game/Game.java
	@echo Compiling game/Game.class
	@javac -d out/ -cp $(class_path) game/Game.java

out/game/RockPaperScissors.class: game/RockPaperScissors.java
	@echo Compiling game/RockPaperScissors.class
	@javac -d out/ -cp $(class_path) game/RockPaperScissors.java

out/policy/DiscreteGamePolicy.class: policy/DiscreteGamePolicy.java
	@echo Compiling policy/DiscreteGamePolicy.class
	@javac -d out/ -cp $(class_path) policy/DiscreteGamePolicy.java

out/policy/LogisticRegression.class: policy/LogisticRegression.java
	@echo Compiling policy/LogisticRegression.class
	@javac -d out/ -cp $(class_path) policy/LogisticRegression.java

out/policy/NashSolver.class: policy/NashSolver.java
	@echo Compiling policy/NashSolver.class
	@javac -d out/ -cp $(class_path) policy/NashSolver.java

out/policy/Policy.class: policy/Policy.java
	@echo Compiling policy/Policy.class
	@javac -d out/ -cp $(class_path) policy/Policy.java

out/policy/RandomPolicy.class: policy/RandomPolicy.java
	@echo Compiling policy/RandomPolicy.class
	@javac -d out/ -cp $(class_path) policy/RandomPolicy.java

out/policy/StateQ.class: policy/StateQ.java
	@echo Compiling policy/StateQ.class
	@javac -d out/ -cp $(class_path) policy/StateQ.java

