kk
# CTL Model Checker

[![Java](https://img.shields.io/badge/Java-21.0.2-orange)](https://www.oracle.com/java/)
[![Build](https://img.shields.io/badge/Build-Gradle-green)](https://gradle.org/)
[![Academic](https://img.shields.io/badge/Academic-Project-blueviolet)](https://www.cs.txstate.edu/)

## 📝 Overview

A Java-based standalone application for CTL (Computation Tree Logic) model validation and temporal logic verification. This tool allows users to verify properties of state transition systems using CTL formulas.

&nbsp;

## ✨ Features

- **Kripke Structure Upload** - Import models from text files
- **CTL Formula Verification** - Check if a Kripke structure satisfies CTL formulas
- **State Selection** - Choose specific states for formula verification
- **Syntax Validation** - Built-in error checking for Kripke structures and formulas
- **User-Friendly GUI** - Simple interface for model checking operations

&nbsp;

## 🖥️ Application Features

### Main Interface
- Clean and intuitive graphical user interface
- Text field for CTL formula input
- State selection dropdown
- Result display area
- Error message notifications

### Model Loading Capabilities
- File selection dialog for Kripke structure files
- Automatic parsing and validation
- Support for various model formats

### Verification System
- Real-time formula checking
- Detailed results reporting
- Comprehensive error detection

&nbsp;

## 🧪 Test Cases

The application has been tested with various models:

### 🔄 Microwave Model
- CTL formula: `(start and EG(not heat))`
  - Starting state: s1 - Property does not hold
  - Starting state: s2 - Property holds

### 🔢 Model 1
- CTL formula: `(not AGp) or EFq`
  - Starting state: s1 - Property holds
- CTL formula: `EGp and AGq`
  - Starting state: s1 - Property does not hold

### 🔢 Model 2
- CTL formula: `AG(EF(p or r))`
  - Starting state: s3 - Property holds
- CTL formula: `AG(AFq)`
  - Starting state: s1 - Property does not hold

### 🔢 Model 4
- CTL formula: `AG(t1 -> AF c1)`
  - Starting state: s3 - Property holds
  - Starting state: s5 - Property holds

&nbsp;

## ⚠️ Error Handling

The application provides clear error messages in various scenarios:
- When transitions refer to undefined states
- When CTL formulas are invalid
- When no CTL formula is entered

&nbsp;

## 🏗️ Architecture

The system follows object-oriented design principles. A detailed class structure is available in the UML diagram included in the project repository.

&nbsp;

## 🛠️ Setup and Build

### Prerequisites
- JDK 21.0.2 or higher
- Gradle (for building)

### Building the Application
```bash
# Clone the repository
git clone https://github.com/yourusername/ctl-model-checker.git
cd ctl-model-checker

# Build with Gradle
gradle build
```

### Running the Application
```bash
gradle run
```

&nbsp;

## 📁 Project Structure

```
modelCheckCTL/
├── src/                  # Source code
├── test/                 # Test files
├── examples/             # Example Kripke structures
└── UML Diagram.png       # System architecture diagram
```

&nbsp;

## 🎯 Project Goal

The aim was to create a Java standalone program for CTL model validation and CTL temporal logic verification with a user-friendly interface that allows for:
- Uploading Kripke structure definitions
- Verifying CTL formulas against these structures
- Selecting specific states for verification
- Providing clear feedback on verification results
