include build.properties

PROJECT_DIR := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
TARGET_DIR=$(PROJECT_DIR)target

CI_BUILD_NUMBER ?= $(USER)-snapshot
CI_IVY_CACHE ?= $(HOME)/.ivy2
CI_SBT_CACHE ?= $(HOME)/.sbt
CI_WORKDIR ?= $(shell pwd)

VERSION = $(CI_BUILD_NUMBER)
BUILDER_TAG = mup.cr/blt/build-sbt:1.0

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

package-local:
	sbt publishLocal

package:
	docker pull $(BUILDER_TAG)
	docker run \
		--rm \
		-v $(CI_WORKDIR):/data \
		-v $(CI_IVY_CACHE):/root/.ivy2 \
		-v $(CI_SBT_CACHE):/root/.sbt \
		-e CI_BUILD_NUMBER=$(CI_BUILD_NUMBER) \
		$(BUILDER_TAG)

publish:
	# This should publish the snapshot to nexus.

version:
	@echo $(VERSION)
