# BuddyEnglish Backend 🦉

> AI-powered English learning companion for kids — Spring Boot REST API

## Tech Stack

- **Java 21** + **Spring Boot 4.x**
- **Spring Security** (JWT via HTTP-only Cookie)
- **Spring Data JPA** + **SQL Server (MSSQL)**
- **Gemini AI API** (Google) for conversational intelligence
- **Lombok** for boilerplate reduction

## Architecture

37-table normalized schema organized in 14 modules:

```
modules/
├── auth/         # Login, Signup, JWT authentication
├── user/         # User accounts & Role management
├── profile/      # ChildProfile, ParentProfile
├── buddy/        # BuddyProfile, ChildMemory (AI companion)
├── learning/     # World, Adventure, Scenario, ScenarioStep
├── vocabulary/   # VocabularyCategory, Vocabulary
├── progress/     # Child learning progress tracking
├── conversation/ # ConversationSession, Message, Context (Chat engine)
├── mission/      # Daily/Weekly missions
├── reward/       # Shop items, ChildReward
├── achievement/  # Badges and achievements
├── analytics/    # LearningSession, WeeklyReport
├── notification/ # Push notifications
└── feedback/     # User feedback
```

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- SQL Server (local or remote)

### Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=Buddy_English_DB;encrypt=true;trustServerCertificate=true
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
gemini.api.key=YOUR_GEMINI_API_KEY
```

### Run

```bash
# Clean build & run (first time — creates DB schema + seeds data)
mvn clean spring-boot:run

# Subsequent runs
mvn spring-boot:run
```

> ⚠️ On first run, `DatabaseCleanupPostProcessor` will drop all existing tables and recreate the full 37-table schema automatically.

## API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | Login | Public |
| POST | `/api/auth/register` | Register | Public |
| GET | `/api/auth/me` | Get current user | 🔐 Required |
| POST | `/api/auth/logout` | Logout | 🔐 Required |
| POST | `/api/chatbot/chat` | Chat with Buddy AI | 🔐 Required |
| GET | `/api/chatbot/history` | Get chat history | 🔐 Required |
| DELETE | `/api/chatbot/history` | Reset chat history | 🔐 Required |

## Test Account (seeded on first run)

```
Email:    testuser@buddy.com
Password: password123
```
