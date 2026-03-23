# github-access-reporter
# GitHub Access Reporter

## Problem Statement

Organizations often need visibility into who has access to which repositories in GitHub. This service connects to GitHub and generates a report showing which users have access to which repositories within a given organization.

## Features

- 🔐 Authenticate with GitHub using secure authentication mechanism (Personal Access Token)
- 📦 Retrieve all repositories belonging to a GitHub organization
- 👥 Determine which users have access to each repository
- 📊 Generate aggregated view mapping users to the repositories they can access
- 🚀 Expose REST API endpoint that returns access report in JSON format
- ⚡ Supports organizations with 100+ repositories and 1000+ users

## Technologies Used

- Java 17
- Spring Boot 3.5.12
- Spring WebFlux (WebClient for non-blocking API calls)
- Maven
- Lombok

---

## How to Run the Project

### Prerequisites

- Java 17 or higher installed
- GitHub account
- GitHub Personal Access Token (with required scopes)

### Step 1: Get GitHub Token

1. Log in to GitHub.com
2. Go to **Settings** → **Developer settings** → **Personal access tokens** → **Tokens (classic)**
3. Click **Generate new token (classic)**
4. Give it a name: `Access Reporter`
5. Select expiration: `30 days` or `90 days`
6. Select scopes:
   - ✅ `repo` (full control of private repositories)
   - ✅ `read:org` (read organization data)
   - ✅ `read:user` (read user data)
7. Click **Generate token**
8. **Copy the token** (starts with `ghp_`). You won't see it again!

### Step 2: Clone the Repository

```bash
git clone https://github.com/ananyamalik1/github-access-reporter.git
cd github-access-reporter
