CLJSC = $(CLOJURESCRIPT_HOME)/bin/cljsc
VERSION = `node -e "console.log(require('./package.json').version)"`

all: build-dev
release: build-prod tag publish

build-dev:
	$(CLJSC) ./src '{:optimizations :simple :pretty-print true :target :nodejs}' > ./lexemic.js

build-prod:
	$(CLJSC) ./src '{:optimizations :advanced :target :nodejs}' > ./bin/lexemic
	chmod +x ./bin/lexemic

clean:
	rm -rf ./out
	rm lexemic.js

status:
	@status=$$(git status --porcelain --untracked-files=no); \
	if test "x$${status}" = x; then \
		echo Working directory is clean. >&1; \
	else \
		echo Working directory is dirty. You need to commit any modified files before continuing. >&2; \
		false; \
	fi

tag: status
	git tag -a v$(VERSION) -m "Tagging v$(VERSION)"
	git push --tags

publish: status
	npm publish
