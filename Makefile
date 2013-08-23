CLJSC = $(CLOJURESCRIPT_HOME)/bin/cljsc
VERSION = `node -e "console.log(require('./package.json').version)"`

all: build
release: tag publish

build:
	$(CLJSC) ./src '{:target :nodejs}' > index.js

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

tag: status
	git tag -a v$(VERSION) -m "Tagging v$(VERSION)"
	git push --tags

public: status
	npm publish
