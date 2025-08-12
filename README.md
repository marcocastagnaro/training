# Backend Training - Books Catalog

## 📚 Training Information
This project is part of the Backend Training program. For detailed instructions and requirements, please visit:
**[Training Backend - Books Catalog](https://www.notion.so/Training-Backend-Cat-logo-de-libros-247138f44582802cb144c0945897f4ff)**

## Prerequisites

- [Java] 21
- [Gradle] 
- [Docker](https://www.docker.com/get-started)
- [Docker compose](https://docs.docker.com/compose/install/)


### 1. clone the repository

Go to [https://github.com/marcocastagnaro/training](https://github.com/marcocastagnaro/training) and click the **"Clone"** button at the top right. Then choose HTTPS and copy the url.

---

Use the following command to clone the repository to your local machine, replacing `<your-username>` with your GitHub username:

```bash
git clone <repository-url>
cd training
```

### 2. Create a new branch

Create a new branch for your changes. Use the following formart for the branch name: training/<surname>

do this by running the following command in your terminal:

```bash
git checkout -b training/<surname>
```


## Running the app
```bash
docker compose up --build
```
### The API will be available at http://localhost:8080

## 🌱 Seed Data
This project includes seed data for testing your implementation:
- **Location**: `seed-data/books-seed.json` (50 sample books)
- **Instructions**: See `seed-data/README.md` for implementation details