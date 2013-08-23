CLJSC = $(CLOJURESCRIPT_HOME)/bin/cljsc
VERSION = `node -e "console.log(require('./package.json').version)"`

all: build
release: status tag publish

build:
	$(CLJSC) ./src/lexemic/core.cljs '{:target :nodejs}' > index.js

clean:
	rm -rf ./out
	rm index.js

status:
	@status=$$(git status --porcelain --untracked-files=no); \
	if test "x$${status}" = x; then \
		echo Working directory is clean. >&2; \
	else \
		echo Working directory is dirty. You need to commit any modified files before continuing. >&2; \
		false; \
	fi

tag:
	git tag -a v$(VERSION) -m "Tagging v$(VERSION)"
	git push --tags

public:
	npm publish