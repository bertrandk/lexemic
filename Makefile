CLJSC = $(CLOJURESCRIPT_HOME)/bin/cljsc

all: build

build:
	$(CLJSC) ./src/lexemic/core.cljs '{:target :nodejs}' > index.js

clean:
	rm -rf ./out
	rm index.js