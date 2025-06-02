# 🤝 Contributing to BookStudio

Thank you for your interest in contributing to **BookStudio**!

## 📋 Table of Contents

* [Code of Conduct](#code-of-conduct)
* [Getting Started](#getting-started)
* [Contribution Types](#contribution-types)
* [Git Workflow](#git-workflow)
* [Branch Naming](#branch-naming)
* [Commit Messages](#commit-messages)
* [Pull Requests](#pull-requests)
* [Bugs & Feature Requests](#bugs--feature-requests)
* [Support](#support)

## Code of Conduct

Please be respectful and inclusive. Harassment and discrimination are not tolerated.

## Getting Started

Ensure you have:

* GitHub account & Git installed
* Basic knowledge of Java, JSP, and web development
* Read the [README](README.md) for project setup

## Contribution Types

You can contribute by:

* Reporting bugs or suggesting features
* Fixing issues
* Improving documentation
* Refactoring code or adding tests

## Git Workflow

1. **Fork** the repo and clone your fork
2. Add upstream:

   ```bash
   git remote add upstream https://github.com/jason-vila/bookstudio-v1.git
   ```
3. Sync before work:

   ```bash
   git checkout main
   git fetch upstream
   git merge upstream/main
   git push origin main
   ```
4. Create a branch:

   ```bash
   git checkout -b feat/42-search-feature
   ```
5. Make changes, commit, push
6. Open a Pull Request (PR) to `main`

## Branch Naming

Use this format:

| Type        | Format                          |
| ----------- | ------------------------------- |
| ✨ Feature   | `feat/<issue>-<short-desc>`     |
| 🐛 Fix      | `fix/<issue>-<short-desc>`      |
| 📝 Docs     | `docs/<issue>-<short-desc>`     |
| 🎨 Style    | `style/<issue>-<short-desc>`    |
| ♻️ Refactor | `refactor/<issue>-<short-desc>` |
| 🧪 Test     | `test/<issue>-<short-desc>`     |
| 🔧 Chore    | `chore/<issue>-<short-desc>`    |

**Example:** `feat/42-add-search`

## Commit Messages

Use **Conventional Commits** with emojis when possible:

```
<emoji> <type>: <description> (#<issue>)
```

### Examples

```
✨ feat: add search functionality (#42)
🐛 fix: prevent login crash (#15)
📝 docs: update installation guide (#20)
```

Use small, descriptive commits. Group related changes in the same PR.

## Pull Requests

1. PR title should follow commit format
2. Reference issues (e.g., `Closes #42`)
3. Include:

   * Description of changes
   * Screenshots (if UI)
   * Checklist:

     * [ ] Code style
     * [ ] Tests (if needed)
     * [ ] Docs updated
     * [ ] Builds and runs

Wait for review and make any requested changes.

## Bugs & Feature Requests

Use [GitHub Issues](https://github.com/jason-vila/bookstudio-v1/issues). Include:

* Title
* Description
* Steps to reproduce (for bugs)
* Problem solved (for features)
* Screenshots/logs (if applicable)

## Support

Open an issue with the `question` label if you need help.