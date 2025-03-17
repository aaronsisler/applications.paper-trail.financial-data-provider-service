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

## [0.15.0] Account Create and Get All Full Test Suite

### Added

- Account POST / GET ALL with full Cucumber test suite
- Account POST / GET ALL with integration test

## [0.14.0] Household Member Create and Get All Integration Test

### Added

- Household Member POST / GET ALL with integration test

## [0.13.0] Household Member

### Added

- Household Member POST / CREATE with full Cucumber test suite
- Household Member GET / GET ALL with full Cucumber test suite
- Household Member GET / GET ALL By User Id with full Cucumber test suite

## [0.12.0] User Service and Household Service Get By Id

### Changed

- Return type of the User and Household Service Get By Id to be Optional

## [0.11.0] Household

### Added

- Household GET / GET ALL with behavior test suite
- Household GET BY ID with behavior test suite
- Household POST / CREATE ALL with behavior test suite
- Household DELETE with behavior test suite
- Household PUT / UPDATE with behavior test suite
- Household integration test suite

## [0.10.0] GitHub Actions using Docker for integration testing

### Added

- Usage of a new plugin that keeps the application version in one property location

### Changed

- Added GitHub action adoption for Docker based integration testing

## [0.9.0] Integration Test Framework

### Added

- Framework for starting Docker compose and running integration tests

### Changed

- Moved the Contracts folder up a layer so it could be used to build the domain objects in integrations tests

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