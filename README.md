# Java/Spring Boot REST API FlashCards Project

![Status](https://img.shields.io/badge/Status-In--Development-orange?style=flat-square)
![Java](https://img.shields.io/badge/Java-21-blue?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.3-brightgreen?style=flat-square&logo=spring-boot)
![React](https://img.shields.io/badge/React-19.2.0-61DAFB?style=flat-square&logo=react&logoColor=black)

## Duel Study modes

### Cram mode ("lazy update weight logic")
For short-term exam preperation, endless cramming mode.
- **User Feedback:** after card has been flipped, user can score from 1-5 based if the got the card correct and how difficult it was for them to retain. This score is used to give the card an updated weight.
- **Weighted lottery:** next cards are randomised, where cards struggled with are mathmatically more probable to appear.
- **Lazy Update:** cards seen often weight's are altered with "pity bonuses" to allow for cards not not seen in a long time to appear.

### Spaced repetition (FSRS algorithm) - COMING SOON
For long-term retention, with a FCFS algorithm.

## Technology Stack
### Backend
* **Language:** Java 21
* **Framework:** Spring Boot 3.5.3 (Web, Data JPA, Security)
* **Database:** PostgreSQL / H2 (Testing)
* **Build Tool:** Maven

### Frontend
* **Language:** JavaScript
* **Libraries** React 19.2.0, React Router 7.13.0
* **Styling** Tailwind CSS 4.1.18
* **Build Tool:** Vite
