# Soar to nuXmv Translator

## Overview

The **Soar to nuXmv Translator** is a tool designed to convert Soar cognitive architecture models into the nuXmv symbolic model verifier format. This conversion enables the formal verification of Soar models by leveraging the model checking capabilities of nuXmv. Built with ANTLR, the translator parses Soar programs, maps their rules and structures into nuXmv's formal representation, and generates a corresponding `.smv` file suitable for verification tasks.

In addition to the standalone tool, a **web-based translator** is available for online use at the following URL:
- [Soar to nuXmv Web Translator](https://assistresearchlab.fit.edu/)
## Features

- **ANTLR-Powered Parsing**: Uses ANTLR to define and implement a grammar for Soar programs, enabling efficient and robust parsing.
- **Translation to nuXmv**: Converts parsed Soar models into the input format required by nuXmv, supporting key constructs like states, transitions, and actions.
- **Support for Key Soar Constructs**: Handles Soar rules, productions, and working memory elements, mapping them to equivalent constructs in nuXmv.
- **Extensible Framework**: Built with modularity in mind, allowing future support for additional Soar constructs and further optimizations for nuXmv translations.

## Requirements

- **Java 8+**: Required to run ANTLR and the translation tool.
- **ANTLR 4**: Used for grammar generation and parser generation.
- **nuXmv**: Model checking tool where the generated `.smv` files will be verified.
  
