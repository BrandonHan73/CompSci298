
base_objects = out/base/Config.class out/base/Position.class out/base/State.class out/base/Log.class out/base/Utility.class
environment_objects = out/environment/CrashGame.class out/environment/CrashGameTest.class out/environment/CycleGame.class out/environment/CycleGameTest.class out/environment/Game.class out/environment/RockPaperScissors.class
policy_objects = out/policy/DiscreteGamePolicy.class out/policy/LogisticRegression.class out/policy/NashSolver.class out/policy/Policy.class out/policy/RandomPolicy.class out/policy/StateQ.class

objects = $(environment_objects) $(policy_objects) $(base_objects) 

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

out/base/Log.class: base/Log.java
	@echo Compiling base/Log.class  
	@javac -d out/ -cp $(class_path) base/Log.java

out/base/Utility.class: base/Utility.java
	@echo Compiling base/Utility.class  
	@javac -d out/ -cp $(class_path) base/Utility.java

out/environment/CrashGame.class: environment/CrashGame.java
	@echo Compiling environment/CrashGame.class
	@javac -d out/ -cp $(class_path) environment/CrashGame.java

out/environment/CrashGameTest.class: environment/CrashGameTest.java
	@echo Compiling environment/CrashGameTest.class
	@javac -d out/ -cp $(class_path) environment/CrashGameTest.java

out/environment/CycleGame.class: environment/CycleGame.java
	@echo Compiling environment/CycleGame.class
	@javac -d out/ -cp $(class_path) environment/CycleGame.java

out/environment/CycleGameTest.class: environment/CycleGameTest.java
	@echo Compiling environment/CycleGameTest.class
	@javac -d out/ -cp $(class_path) environment/CycleGameTest.java

out/environment/Game.class: environment/Game.java
	@echo Compiling environment/Game.class
	@javac -d out/ -cp $(class_path) environment/Game.java

out/environment/RockPaperScissors.class: environment/RockPaperScissors.java
	@echo Compiling environment/RockPaperScissors.class
	@javac -d out/ -cp $(class_path) environment/RockPaperScissors.java

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

