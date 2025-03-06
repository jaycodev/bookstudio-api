# 🤝 Contributing to BookStudio

Thank you for your interest in contributing to BookStudio! This document provides a step-by-step guide on how to contribute to the project.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Git Workflow](#git-workflow)
- [Branch Naming Convention](#branch-naming-convention)
- [Commit Message Standards](#commit-message-standards)
- [Pull Request Process](#pull-request-process)
- [Development Environment Setup](#development-environment-setup)
- [Reporting Bugs](#reporting-bugs)
- [Feature Requests](#feature-requests)
- [Questions and Support](#questions-and-support)

## Code of Conduct

By participating in this project, you agree to maintain a respectful and inclusive environment for everyone. Please be kind and courteous to others, and avoid any form of harassment or discrimination.

## Getting Started

Before you start contributing, make sure you have:

1. A GitHub account
2. Git installed on your local machine
3. Basic understanding of Git commands
4. Familiarity with Java, JSP, and web development concepts
5. Read the project's README.md file

## How to Contribute

There are many ways to contribute to BookStudio:

- Reporting bugs and issues
- Suggesting new features or improvements
- Fixing bugs
- Adding new features
- Improving documentation
- Refactoring code
- Writing or improving tests

## Git Workflow

We follow a standard Git workflow based on the GitHub Flow:

1. Fork the repository to your GitHub account
2. Clone your fork to your local machine
3. Create a new branch from the `main` branch
4. Make your changes
5. Commit your changes with clear and descriptive commit messages
6. Push your branch to your fork
7. Open a Pull Request from your branch to the original repository's `main` branch

### Step-by-Step Contributing Guide

#### 1. Fork the Repository

1. Navigate to the [BookStudio repository](https://github.com/jason-vila/bookstudio)
2. Click the "Fork" button in the top-right corner
3. Wait for GitHub to create your fork

#### 2. Clone Your Fork

```bash
git clone https://github.com/YOUR-USERNAME/bookstudio.git
cd bookstudio
```

#### 3. Add Upstream Remote

```bash
git remote add upstream https://github.com/jason-vila/bookstudio.git
```

#### 4. Keep Your Fork Updated

Before creating a new branch, make sure your fork is up to date:

```bash
git checkout main
git fetch upstream
git merge upstream/main
git push origin main
```

#### 5. Create a Working Branch

Create a new branch from `main` using the appropriate naming convention:

```bash
git checkout main
git checkout -b feature/42-add-search-functionality
```

#### 6. Make Your Changes

Work on your feature or bug fix. Remember to:
- Follow the project's code style and conventions
- Write clear, commented code
- Add tests if applicable
- Update documentation if needed

#### 7. Commit Your Changes

Make small, focused commits with clear messages:

```bash
git add .
git commit -m "feat: add advanced search functionality (#42)"
```

#### 8. Push Your Changes

```bash
git push origin feature/42-add-search-functionality
```

#### 9. Create a Pull Request

1. Navigate to your fork on GitHub
2. Click "Compare & pull request" button for your branch
3. Ensure the base repository is `jason-vila/bookstudio` and the base branch is `main`
4. Fill in the PR template with details about your changes
5. Click "Create pull request"

## Branch Naming Convention

When creating branches, use one of the following formats based on the type of change:

- **✨ New Features:**  
  `feature/<issue-number>-<short-description>`  
  *Example:* `feature/12-new-search-function`
  
- **🐛 Bug Fixes:**  
  `fix/<issue-number>-<short-description>`  
  *Example:* `fix/15-login-error`
  
- **📝 Documentation:**  
  `docs/<issue-number>-<short-description>`  
  *Example:* `docs/20-update-installation-guide`
  
- **🎨 Formatting/Styles:**  
  `style/<issue-number>-<short-description>`  
  *Example:* `style/25-improve-button-styling`
  
- **♻️ Refactoring:**  
  `refactor/<issue-number>-<short-description>`  
  *Example:* `refactor/30-optimize-dao-classes`
  
- **🧪 Tests:**  
  `test/<issue-number>-<short-description>`  
  *Example:* `test/35-add-loan-service-tests`
  
- **🔧 Auxiliary Tasks:**  
  `chore/<issue-number>-<short-description>`  
  *Example:* `chore/40-update-dependencies`

## Commit Message Standards

We follow the Conventional Commits specification for our commit messages. This helps maintain a clear and traceable history.

### Format

```
<type>: <description> (#<issue-number>)

[optional body]

[optional footer]
```

### Types

| **Type**         | **Description**                                      |
|------------------|------------------------------------------------------|
| `feat:`          | A new feature                                        |
| `fix:`           | A bug fix                                            |
| `docs:`          | Documentation changes                                |
| `style:`         | Formatting changes (no code change)                  |
| `refactor:`      | Code refactoring (no feature or bug fix)             |
| `test:`          | Adding or improving tests                            |
| `chore:`         | Changes to build process or auxiliary tools          |

### Examples

```
feat: add dark mode toggle (#23)
```

```
fix: prevent timeout on large database queries (#45)

Added connection timeout parameter and optimized query execution.
```

## Pull Request Process

1. **Title:** Use the same convention as commit messages, e.g., `feat: add search functionality (#42)`
2. **Description:** Include a detailed description of the changes, why they're needed, and any relevant context
3. **Referenced Issue:** Link to the issue this PR addresses using `Closes #42` or `Fixes #42`
4. **Checklist:**
   - [ ] Code follows project style guidelines
   - [ ] Tests have been added or updated
   - [ ] Documentation has been updated if needed
   - [ ] The code builds and runs locally
   - [ ] All tests pass
5. **Review:** Wait for code review from the maintainer(s)
6. **Changes:** Make any requested changes and push to the same branch
7. **Approval:** Once approved, the maintainer will merge your PR

## Development Environment Setup

For detailed setup instructions, refer to the "Installation and Setup" section in the [README.md](README.md). 

In summary:
1. Clone the repository
2. Import the project in Eclipse EE
3. Configure Tomcat 8.5
4. Set up the MySQL database
5. Update database connection parameters
6. Run the application

## Reporting Bugs

If you find a bug, create an issue on GitHub with the following information:

1. A clear, descriptive title
2. Steps to reproduce the issue
3. Expected behavior
4. Actual behavior
5. Screenshots (if applicable)
6. Environment details (browser, OS, etc.)
7. Any relevant logs or error messages

## Feature Requests

For feature requests, create an issue with:

1. A clear, descriptive title
2. A detailed description of the proposed feature
3. The problem it solves or the value it adds
4. Any relevant examples, mockups, or diagrams

## Questions and Support

If you have questions or need help, you can:
- Open an issue with the "question" label
- Contact the project maintainer

Thank you for considering contributing to BookStudio! Your efforts help make this project better for everyone.
