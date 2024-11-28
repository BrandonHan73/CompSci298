
base_obj = out/base/*.class
environment_obj = out/environment/*.class
example_obj = out/example/*.class
network_obj = out/network/*.class
policy_obj = out/policy/*.class
util_obj = out/util/*.class

objects = $(base_obj) $(environment_obj) $(example_obj) $(network_obj) $(policy_obj) $(util_obj)

libraries = lib/Jama-1.0.3.jar

class_path = src/:$(libraries)

all: $(libraries) $(objects)
	@echo Compiling Main.java
	@javac -d out/ -cp $(class_path) src/Main.java
	@echo
	@echo Executing Main
	@echo
	@java -cp out:lib/Jama-1.0.3.jar Main

lib/Jama-1.0.3.jar:
	@wget -P lib https://math.nist.gov/javanumerics/jama/Jama-1.0.3.jar

out/%.class: src/%.java
	@echo Compiling $@
	@javac -d out/ -cp $(class_path) $<

