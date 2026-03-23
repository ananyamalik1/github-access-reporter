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

Step 3: Set Environment Variable

Windows PowerShell:
$env:GITHUB_TOKEN="ghp_your_token_here"

Windows Command Prompt:
set GITHUB_TOKEN=ghp_your_token_here

Mac/Linux:
export GITHUB_TOKEN="ghp_your_token_here"

---

Step 4: Run the Application

Windows:
.\mvnw spring-boot:run

Mac/Linux:
./mvnw spring-boot:run

Wait for the application to start. You should see:

  .   __          _            __ _ _
 /\\ / _'_ _ _ _()_ _  _ _ \ \ \ \
( ( )\_ | '_ | '| | ' \/ _` | \ \ \ \
 \\/  _)| |)| | | | | || (| |  ) ) ) )
  '  |_| .|| ||| |\_, | / / / /
 =========||==============|_/=////

 :: Spring Boot ::                (v3.5.12)

Started ReporterApplication in 3.423 seconds
✅ GitHub Access Reporter Started!


Figure 2: Terminal showing application successfully started

---

Step 5: Test the Application

Open another terminal and run:

Test Health:
curl http://localhost:8080/health
Expected: OK

Test Application:
curl http://localhost:8080/test
Expected: ✅ GitHub Access Reporter is Working!


Figure 4: Health check endpoint returning OK

Test GitHub Connection:
curl http://localhost:8080/test/octocat
Expected: JSON list of repositories

Generate Access Report:
curl http://localhost:8080/api/v1/reports/github/octocat
Expected: JSON access report with users and repositories

---

Authentication Configuration

How Authentication Works

| Component | Description |
|-----------|-------------|
| Token Storage | Environment variable GITHUB_TOKEN |
| Configuration | application.yml reads token via ${GITHUB_TOKEN} |
| WebClient Setup | GitHubConfig.java adds token to Authorization header |
| Security | Token never hardcoded in source code |

Configuration File

src/main/resources/application.yml:

server:
  port: 8080

github:
  token: ${GITHUB_TOKEN}
  api:
    base-url: https://api.github.com

Token Scopes Required

| Scope | Purpose |
|-------|---------|
| repo | Access to public and private repositories |
| read:org | Read organization data and members |
| read:user | Read user profile information |

---

API Endpoints

Base URL
http://localhost:8080

Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /health | Health check |
| GET | /test | Simple test endpoint |
| GET | /test/{organization} | Test GitHub connection |
| GET | /api/v1/reports/github/{organization} | Main endpoint - Access report |

Detailed API Documentation

1. Health Check
GET /health
Response: OK

2. Test Application
GET /test
Response: ✅ GitHub Access Reporter is Working!

3. Test GitHub Connection
GET /test/{organization}
Example: curl http://localhost:8080/test/octocat
Response:
[
  {
    "id": 1296269,
    "name": "Hello-World",
    "fullName": "octocat/Hello-World",
    "archived": false,
    "privateRepo": false
  }
]

4. Generate Access Report (Main Requirement)
GET /api/v1/reports/github/{organization}
Example: curl http://localhost:8080/api/v1/reports/github/octocat

---

Example Response

{
  "organization": "octocat",
  "generatedAt": 1701234567890,
  "totalRepositories": 5,
  "totalUsers": 3,
  "users": [
    {
      "username": "alice",
      "repositories": [
        {
          "repository": "frontend-app",
          "access": "admin",
          "fullName": "octocat/frontend-app"
        },
        {
          "repository": "backend-api",
          "access": "admin",
          "fullName": "octocat/backend-api"
        }
      ]
    },
    {
      "username": "bob",
      "repositories": [
        {
          "repository": "frontend-app",
          "access": "write",
          "fullName": "octocat/frontend-app"
        }
      ]
    },
    {
      "username": "charlie",
      "repositories": [
        {
          "repository": "database",
          "access": "read",
          "fullName": "octocat/database"
        }
      ]
    }
  ]
}

Response Fields Explanation

| Field | Description |
|-------|-------------|
| organization | GitHub organization name |
| generatedAt | Timestamp when report was generated |
| totalRepositories | Total number of repositories in organization |
| totalUsers | Total number of unique users with access |
| users | List of users with their repository access |
| username | GitHub username |
| repositories | List of repositories user can access |
| repository | Repository name |
| access | Access level (admin/write/read) |
| fullName | Full repository name (org/repo) |

---

Assumptions

1. GitHub Token: User provides a valid GitHub Personal Access Token with required scopes (repo, read:org, read:user)
2. Organization Name: Provided organization name is valid and accessible with the given token
3. Token Validity: Token is assumed to be active and not expired
4. Public Repositories: Token can access public repositories, but private repositories require token
5. Access Levels: Users can have three access levels:
   - admin - Full administrative access
   - write - Can push code
   - read - Can only view
6. Network Connectivity: Assumes stable internet connection to GitHub API

---

Design Decisions

| Decision | Rationale |
|----------|-----------|
| Personal Access Token | Simpler than OAuth for users to set up and use |
| Spring WebFlux | Non-blocking calls for better performance with multiple repositories |
| Parallel Processing | Fetch collaborators for multiple repositories simultaneously |
| ConcurrentHashMap | Thread-safe aggregation during parallel processing |
| Pagination (per_page=100) | Efficiently fetch large datasets from GitHub API |
| Sorted Output | Sort users by username for consistent, readable results |
| Global Exception Handler | Consistent error responses across all endpoints |
| Separation of Concerns | Clean separation between controller, service, and model layers |

---

Scale Considerations

Performance Goals

| Requirement | Target | Implementation |
|-------------|--------|----------------|
| 100+ repositories | ✅ | Parallel API calls (max 10 concurrent) |
| 1000+ users | ✅ | HashMap aggregation for O(1) lookups |
| Large datasets | ✅ | Pagination with per_page=100 |
| Rate limiting | ✅ | Non-blocking I/O with WebClient |

Performance Metrics

| Organization Size | Repositories | Users | Approximate Time |
|-------------------|--------------|-------|------------------|
| Small | less than 10 | less than 100 | less than 2 seconds |
| Medium | 50 | 500 | 5-10 seconds |
| Large | 100+ | 1000+ | 15-20 seconds |

---

Project Structure

src/main/java/com/cloudeagle/reporter/
├── ReporterApplication.java          # Main entry point
├── config/
│   └── GitHubConfig.java              # WebClient configuration
├── controller/
│   ├── TestController.java            # Test endpoints
│   └── AccessReportController.java    # Main API endpoint
├── model/
│   ├── Repository.java                # Repository data model
│   └── Collaborator.java              # User/Collaborator model
└── service/
    ├── GitHubService.java             # GitHub API calls
    └── ReportService.java             # Report generation logic

src/main/resources/
└── application.yml                    # Application configuration

---

Configuration

application.yml

server:
  port: 8080                    # Application port (change if 8080 is busy)

github:
  token: ${GITHUB_TOKEN}        # Read token from environment variable
  api:
    base-url: https://api.github.com

Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| GITHUB_TOKEN | GitHub Personal Access Token | Yes |

---

Troubleshooting

Common Issues and Solutions

| Issue | Possible Cause | Solution |
|-------|---------------|----------|
| Port 8080 already in use | Another application using port 8080 | Change port in application.yml to 8081 |
| 401 Unauthorized | Token is invalid or expired | Generate new token at GitHub.com |
| 404 Not Found | Organization doesn't exist | Verify organization name |
| Connection refused | Application not running | Run .\mvnw spring-boot:run |
| Rate limit exceeded | Too many API calls | Wait or implement caching |
| Token not recognized | Token not set in environment | Run $env:GITHUB_TOKEN="token" again |
| Build failed | Dependencies not downloaded | Run .\mvnw clean install |
| mvn not recognized | Maven not installed | Use .\mvnw (Maven wrapper included) |

Debugging Steps

1. Check if app is running:
   curl http://localhost:8080/health

2. Check if token is set:
   echo $env:GITHUB_TOKEN

3. Check GitHub API directly:
   curl -H "Authorization: Bearer $env:GITHUB_TOKEN" https://api.github.com/user

4. Check application logs:
   Look for errors in the terminal where the app is running

5. Verify Java version:
   java -version
   Should show Java 17 or higher

---

Security Considerations

| Consideration | Implementation |
|---------------|----------------|
| Token Protection | Never committed to version control |
| Environment Variables | Always use environment variables for sensitive data |
| HTTPS | All GitHub API calls use HTTPS |
| Least Privilege | Token only has minimal required scopes |
| No Hardcoded Secrets | All secrets read from environment variables |

---

Conclusion

This GitHub Access Reporter successfully meets all the requirements:
1. ✅ Secure GitHub authentication using Personal Access Token
2. ✅ Retrieves all repositories from a GitHub organization
3. ✅ Determines which users have access to each repository
4. ✅ Generates aggregated view mapping users to repositories
5. ✅ Exposes REST API endpoint returning JSON report
6. ✅ Designed to handle 100+ repositories and 1000+ users

The service uses Spring Boot with WebFlux for non-blocking API calls, implements parallel processing for efficiency, and provides a clean, well-documented API.

---

---

Author

Ananya 

---

---
