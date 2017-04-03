# Change Log
All notable changes to this project will
be documented in this file.

## [0.2.x]
### Changed
- Changed logging format to JSON using [this layout](https://github.com/logstash/log4j-jsonevent-layout)
- `log.error` now logs to std error by default, while `log.info` and below logs to std out

## [0.1.x]
### Added
- Simple trait for adding logging to classes/objects.
- Instrumentation helpers on info logging.
