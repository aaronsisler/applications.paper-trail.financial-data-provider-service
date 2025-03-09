# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Legend

* `Added` for new features.
* `Changed` for changes in existing functionality.
* `Deprecated` for soon-to-be removed features.
* `Removed` for now removed features.
* `Fixed` for any bug fixes.
* `Security` in case of vulnerabilities.

## [0.9.0] Integration Test Framework

### Added

- Framework for starting Docker compose and running integration tests

## [0.8.0] User: Get By Id

### Added

- User GET BY ID with full test suite

## [0.7.0] User: Delete

### Added

- User DELETE with full test suite

## [0.6.0] User: Update

### Added

- User PUT / UPDATE with full test suite

## [0.5.0] User: Create All

### Added

- User POST / CREATE ALL with full Cucumber test suite

### Changed

- Tweaked how we are using Open API given the Spring JPA Entity

## [0.4.0] User Features

### Added

- Greater understanding of mocking the User DAO using Mockito

## [0.3.0] Base User Controller

### Added

- User Controller GET ALL with empty list response

### Fixed

- Added in the Maven Failsafe plugin that runs Cucumber features from CLI

## [0.2.0] Base Repo with base Cucumber test

### Added

- Info Actuator feature test

## [0.1.0] Base Repo

### Added

- Health Actuator
- Info Actuator
- Check Style