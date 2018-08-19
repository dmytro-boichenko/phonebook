# Phonebook Application

Application provides small phonebook with storing data in MySQL DB. 

Application itself is Spring Boot 2 based REST service. 

MySQL DB 5.7 is launched as required component. DB structure has been created after first launch

## 1. Build

### 1.1 Run Maven build
```text
    mvnw clean install
```

### 1.2 Build Docker Container with dependencies
```text
    docker build -t boichenko/phonebook .
```

## 2. Run Application
```text
    docker-compose up -d
```

## 3. 