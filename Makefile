PROJECT_DIR := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
TARGET_DIR=$(PROJECT_DIR)target

CI_BUILD_NUMBER ?= $(USER)-SNAPSHOT
CI_IVY_CACHE ?= $(HOME)/.ivy2
CI_SBT_CACHE ?= $(HOME)/.sbt
CI_WORKDIR ?= $(shell pwd)

TARGET ?= __package-sbt

VERSION ?= 0.1.$(CI_BUILD_NUMBER)
BUILDER_TAG = "meetup/sbt-builder:0.1.3"

# lists all available targets
list:
	@sh -c "$(MAKE) -p no_op__ | \
		awk -F':' '/^[a-zA-Z0-9][^\$$#\/\\t=]*:([^=]|$$)/ {split(\$$1,A,/ /);\
		for(i in A)print A[i]}' | \
		grep -v '__\$$' | \
		grep -v 'make\[1\]' | \
		grep -v 'Makefile' | \
		sort"

# required for list
no_op__:

clean:
	@sbt clean
	rm -rf $(TARGET_DIR)

__package-sbt:
	sbt clean \
		"set coverageEnabled := true" \
		"set coverageOutputHTML := false" \
		test \
		coverageReport \
		coverallsMaybe \
		coverageOff \
		publishLocal \
		component:test

__publish-sbt: __package-sbt
	sbt publish cleanLocal

package: __contained-target

__set-publish:
	$(eval TARGET=__publish-sbt)

publish: __set-publish __contained-target

__contained-target:
	docker run \
		--rm \
		-v $(CI_WORKDIR):/data \
		-v $(CI_IVY_CACHE):/root/.ivy2 \
		-v $(CI_SBT_CACHE):/root/.sbt \
		-e CI_BUILD_NUMBER=$(CI_BUILD_NUMBER) \
		-e TRAVIS_JOB_ID=$(TRAVIS_JOB_ID) \
		-e TRAVIS_PULL_REQUEST=$(TRAVIS_PULL_REQUEST) \
		$(BUILDER_TAG) \
		make $(TARGET)

version:
	@echo $(VERSION)
