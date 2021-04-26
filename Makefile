PROJECT_DIR := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
TARGET_DIR=$(PROJECT_DIR)target

CI_BUILD_NUMBER ?= $(USER)-SNAPSHOT
CI_IVY_CACHE ?= $(HOME)/.ivy2
CI_SBT_CACHE ?= $(HOME)/.sbt
CI_WORKDIR ?= $(shell pwd)

TARGET ?= __package-sbt

GROUP_ID?="com.meetup"
ARTIFACT_ID?=scala-logger_2.11

VERSION ?= 0.3.$(CI_BUILD_NUMBER)
BUILDER_TAG = "meetup/sbt-builder:0.1.3"

help:
	@echo Public targets:
	@grep -E '^[^_][^_][a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'
	@echo "Private targets: (use at own risk)"
	@grep -E '^__[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[35m%-20s\033[0m %s\n", $$1, $$2}'

package: __contained-target ## Packages jar artifact.

publish: __set-publish __contained-target ## Publishes jar artifact.

version: ## Prints artifact version.
	@echo $(VERSION)

__clean: # Cleans sbt artifacts
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
		+publishLocal \
		component:test

__publish-sbt: __package-sbt
	mvn deploy:deploy-file -DgroupId=${GROUP_ID} \
		-DartifactId=${ARTIFACT_ID} \
		-Dversion=${VERSION} \
		-Dfile=./target/scala-2.11/${ARTIFACT_ID}-${VERSION}.jar \
		-DpomFile=./target/scala-2.11/${ARTIFACT_ID}-${VERSION}.pom \
		-Djavadoc=./target/scala-2.11/${ARTIFACT_ID}-${VERSION}-javadoc.jar \
		-Dsources=./target/scala-2.11/${ARTIFACT_ID}-${VERSION}-sources.jar \
		-DrepositoryId=github \
		-Durl=https://maven.pkg.github.com/meetup/meetup

__set-publish:
	$(eval TARGET=__publish-sbt)

__contained-target:
	docker run \
		--rm \
		-v $(CI_WORKDIR):/data \
		-v $(CI_IVY_CACHE):/root/.ivy2 \
		-v $(CI_SBT_CACHE):/root/.sbt \
		-v $(HOME)/.bintray:/root/.bintray \
		-e CI_BUILD_NUMBER=$(CI_BUILD_NUMBER) \
		-e TRAVIS_JOB_ID=$(TRAVIS_JOB_ID) \
		-e TRAVIS_PULL_REQUEST=$(TRAVIS_PULL_REQUEST) \
		$(BUILDER_TAG) \
		make $(TARGET)
