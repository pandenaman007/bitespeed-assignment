# Bitespeed Identity Reconciliation Assignment

This is a Spring Boot application for handling identity resolution logic based on email and phone numbers.

## 🚀 Hosted Endpoint

**Base URL:** [https://your-app-url.onrender.com](#)
> (Update this once you deploy your app on Render or other hosting)

## 📦 Tech Stack

- Java
- Spring Boot
- MySQL
- Gradle

## 📌 API

### `POST /identify`

Accepts:
```json
{
  "email": "naman@example.com",
  "phoneNumber": "9876543210"
}
```
Returns:
```agsl
{
  "contact": {
    "primaryContatctId": 1,
    "emails": ["naman@example.com"],
    "phoneNumbers": ["9876543210"],
    "secondaryContactIds": []
  }
}
```

