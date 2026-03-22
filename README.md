
# 🚀 CloudEagle_Repository_Analyzer

A Spring Boot application that authenticates users using GitHub OAuth 2.0 and generates a report showing which users (collaborators) have access to which repositories in a given GitHub organization.

---

# 📌 Features

* 🔐 GitHub OAuth 2.0 Login
* 📦 Fetch organization repositories
* 👥 Fetch collaborators for each repository
* 📘 Swagger UI for easy testing

---

# 🛠 Tech Stack

* Java 17
* Spring Boot
* Spring Security (OAuth2 Client)
* RestTemplate
* Maven
* Swagger (Springdoc OpenAPI)

---

# 📁 Project Structure

```
com.cloudeagle
├── controller
│   └── AnalyzerController.java
├── service
│   ├── GithubService.java
│   └── impl
│       └── GithubServiceImpl.java
├── dto
│   ├── RepoDto.java
│   └── CollabratorDto.java
├── exception
│   └── AppExceptionHandler.java
└── config
    └── SecurityConfig.java
```

---

# ⚙️ How to Run the Project

## 📌 Prerequisites

- ☕ Java 17 or higher  
- 📦 Maven  

## 1️⃣ Clone the Repository

```
git clone https://github.com/Rajesh-Killadi/CloudEagle_Repository_Analyzer.git

```

---

## 2️⃣ Navigate inside the project

```
cd CloudEagle_Repository_Analyzer

```
---

## 3️⃣ Run the Application

```
mvn spring-boot:run
```

---

## 🔐 Login with GitHub

After starting the application, open the following URL in your browser:

http://localhost:8080/login

- Click on **GitHub**
- You will be redirected to GitHub for authentication
- After successful login, you will be redirected to Swagger UI

Swagger UI: http://localhost:8080/swagger-ui/index.html

# 📡 API Usage

## ✅ Endpoint

GET /repositories/{orgName}

👉 Replace orgName with your actual GitHub organization name.

---

## ✅ Sample Response

{
  "alice": ["RepoA", "RepoB"],
  "bob": ["RepoA"]
}

👉 Meaning:

* **alice** has access to RepoA and RepoB
* **bob** has access to RepoA

---

# 🧠 Design Decisions

* Used **OAuth2** for secure authentication
* Used **RestTemplate** for GitHub API calls
* Used **CompletableFuture** for parallel processing (performance optimization)
* Used **ConcurrentHashMap** for thread-safe aggregation
* Only collaborators are considered (permissions not included for simplicity)

---

# ⚠️ Important Notes: Access Requirements for Organization Repositories

To successfully fetch organization repositories (especially private ones), the following conditions must be met:

🔐 Required Access
You must login using an organization admin account to access to organization

---
# ⚠️ Troubleshooting: Private Repositories Not Visible

If the API returns only public repositories and not private ones, it is likely due to GitHub organization restrictions.

🔍 Reason

GitHub organizations can restrict access to private repositories for OAuth applications, even if the user has access.

✅ Solution

Follow these steps to allow access:

Open your GitHub Organization
Navigate to:

Settings → Third-party access → OAuth App Policy -> Remove Restriction
