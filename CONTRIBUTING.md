# How to contribute

Patches/Pull-requests are always welcome

## Getting Started

* Make sure you have a [GitHub account](https://github.com/signup/free)
* Submit a ticket for your issue, assuming one does not already exist.
  * Clearly describe the issue including steps to reproduce when it is a bug.
  * Make sure you fill in the earliest version that you know has the issue.
* Fork the repository on GitHub


## Submitting Changes

* Submit a pull request to the repository in the s7-connector organization.

## Project structure

### Documentation

The documentation is written in the gh-pages branch as a [tiddlywiki](tiddlywiki.com).
Use the editmode tab on the right to enable additional buttons.
After editing you can [save](http://tiddlywiki.com/#Saving) the wiki with your browser.

### Packages

The main package is *com.github.s7connector*

Sub-packages:
* *api* for interfaces and common classes, should be the main entry point for users
* *blocks* Existing Data-blocks with annotations for serialization to/from the PLC
* *exception* Specific S7Exceptions
* *impl* Implementation classes

# Additional Resources

* [Organization](https://github.com/s7connector)
* [Git Repo](https://github.com/s7connector/s7connector)
* [Issues](https://github.com/s7connector/s7connector/issues)
* [Continuous Integration](https://travis-ci.org/s7connector/s7connector)
* [Main documentation](https://s7connector.github.io/s7connector/)
