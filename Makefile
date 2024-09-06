
run: a.out
	@echo
	@./a.out

init:
	mkdir out
	mkdir third-party
	wget -P third-party https://gitlab.com/libeigen/eigen/-/archive/3.4.0/eigen-3.4.0.tar.gz
	tar -xvf third-party/eigen-3.4.0.tar.gz -C third-party

includes = -I./include -I./third-party/eigen-3.4.0

objects = out/Main.o out/Policy.o out/Regression.o
third_party = third-party/eigen-3.4.0

a.out: $(objects) $(third_party)
	@echo
	@echo Linking...
	@g++ $(includes) -g $(objects)

out/Main.o: include/Policy.h include/Regression.h libs/Main.cpp
	@echo Compiling out/Main.o
	@g++ $(includes) -o out/Main.o -c -g libs/Main.cpp

out/Policy.o: include/Policy.h libs/Policy.cpp
	@echo Compiling out/Policy.o
	@g++ $(includes) -o out/Policy.o -c -g libs/Policy.cpp

out/Regression.o: include/Regression.h libs/Regression.cpp
	@echo Compiling out/Regression.o
	@g++ $(includes) -o out/Regression.o -c -g libs/Regression.cpp

