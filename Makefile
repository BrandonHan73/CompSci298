
run: a.out
	./a.out

includes = -I. -I./third-party/eigen-3.4.0

objects = out/Main.o out/Policy.o out/Regression.o

a.out: $(objects)
	g++ $(includes) -g $(objects)

out/Main.o: include/Policy.h libs/Main.cpp
	g++ $(includes) -o out/Main.o -c -g libs/Main.cpp

out/Policy.o: include/Policy.h libs/Policy.cpp
	g++ $(includes) -o out/Policy.o -c -g libs/Policy.cpp

out/Regression.o: include/Regression.h libs/Regression.cpp
	g++ $(includes) -o out/Regression.o -c -g libs/Regression.cpp

