# Phonebook Application

Application provides small phonebook service with storing data in MySQL DB. 

Application itself is Spring Boot 2 based REST service. Authentication on service uses JWT tokens

MySQL DB 5.7 is launched as required component. DB structure has been created after first launch

User password is kept in DataBase as MD5 hashed string. Password validation performed on service side by comparing MD5 hashes password stored in DB and Request Password Parameter.

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

## 3. Interaction with Service. User stories description
As it was mentioned before REST service uses JWT tokens as authentication mechanics. 
All requests except **User Registration** and **User Login** require `Authorization` HTTP Request Header with valid JWT.

After successful authentication the Service returns response with `Authorization` HTTP header. This header contains JWT token signed with RSA key.
The token contains following claims: `user` (user name), `user_id` (user identifier) and `exp` (token expiration date). 

JWT is valid during one hour.

### 3.1 User

#### 3.1.1 Registration
Request
```http request
POST http://localhost:8080/user/register
Content-Type: application/json

{
"userName": "Luke",
"password": "123456"
}
```

Success Response
```json
{
  "success": true
}
```

Unsuccessful Response
```json
{
  "timestamp": "2018-08-19T22:56:36.476+0000",
  "status": 500,
  "error": "Internal Server Error",
  "message": "User 'Luke' already registered",
  "path": "/user/register"
}
```

 
#### 3.1.2 Authentication
Request
```http request
POST http://localhost:8080/user/auth
Content-Type: application/json

{
"userName": "Luke",
"password": "123456"
}
```

Successful Response
```http request
HTTP/1.1 200 
Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjo2LCJpc3MiOiJwaG9uZWJvb2siLCJleHAiOjE1MzQ3MjMzNTYsInVzZXIiOiJMdWtlIn0.hVvZw546llUwkNe4fVu88I_L0x98-Wqw_HApOD0OhsThypKL2YbjVm7VHFMPwCeEcRWTV4RuoG5nHDCrOgsy79KA1udBg-wV9QUKIxsdP8XAA20KfxpjAfovF3cGMDbnMeiUhXOe4mLczwAyHAWywgzjo4W9zPgcf4qBh54pC39qXxWTzD6JGBYpDSbJ2uHhiKud1QXXgGlPNE7sXPAbsOou_b15hZSdN24G8fxquyF38JRzzqyAdPL2iQdLQPM2ePc3hrOT7L_KTt-da-PUmPnx18SpbEh4UQG-0E_xTeb_UCTHxq_dPhWVnrg0097JqKaNhhdZa42ZbhvcPG7IeQ
Content-Length: 0
Date: Sun, 19 Aug 2018 23:02:36 GMT

<Response body is empty>

Response code: 200; Time: 71ms; Content length: 0 bytes
```

Unsuccessful Response
```json
{
  "timestamp": "2018-08-19T23:04:02.499+0000",
  "status": 500,
  "error": "Internal Server Error",
  "message": "User 'Luke' not authorized",
  "path": "/user/auth"
}
```

### 3.2 Contacts
#### 3.1 Create
Request
```http request
POST http://localhost:8080/contacts/create
Content-Type: application/json
Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjo2LCJpc3MiOiJwaG9uZWJvb2siLCJleHAiOjE1MzQ3MjQyMjUsInVzZXIiOiJMdWtlIn0.t3on_wYMMTwIucjpvQntHm7fz2h89HHACcyputpcVVLD2FJr-_jIOohz4dFyU3KJ_z6G67cIeMMkoohJO_lY_yNgSVnB6eO2uFKBXHY7br_bX4z27byqxSTBGx_bg8UEDTJISMI60Nv65nuofcIwIhkb82Wg2mY8jWtYYEkMGzX58ihiAO8ta3NM3YpTSbAmLzwcfzfJh2URG6cuvH6LD51jAga39eSyFAs22Dqdc-TSvLlj2OlV9okvTXc6DZHXI1G7EEiGA9NdjsIdXnDfQQzkHFqGQ98wkddAgYiix3SuH4XwX4CYd-cB8HG7dYYYh0evjc-cVyfwBUvxV4E15g

{
    "firstName" : "Dart",
    "lastName" : "Waider",
    "phones" : [
      {
         "phone" : "789456123"
      }
    ]
}
```

Successful Response
```json
{
  "contactId": 5
}
```

#### 3.2 Get All Contacts
Request
```http request
GET http://localhost:8080/contacts
Accept: application/json
Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjo2LCJpc3MiOiJwaG9uZWJvb2siLCJleHAiOjE1MzQ3MjQyMjUsInVzZXIiOiJMdWtlIn0.t3on_wYMMTwIucjpvQntHm7fz2h89HHACcyputpcVVLD2FJr-_jIOohz4dFyU3KJ_z6G67cIeMMkoohJO_lY_yNgSVnB6eO2uFKBXHY7br_bX4z27byqxSTBGx_bg8UEDTJISMI60Nv65nuofcIwIhkb82Wg2mY8jWtYYEkMGzX58ihiAO8ta3NM3YpTSbAmLzwcfzfJh2URG6cuvH6LD51jAga39eSyFAs22Dqdc-TSvLlj2OlV9okvTXc6DZHXI1G7EEiGA9NdjsIdXnDfQQzkHFqGQ98wkddAgYiix3SuH4XwX4CYd-cB8HG7dYYYh0evjc-cVyfwBUvxV4E15g
```

Successful Response
```json
[
  {
    "id": 5,
    "userId": 6,
    "firstName": "Dart",
    "lastName": "Waider",
    "phones": [
      {
        "id": 5,
        "contactId": 5,
        "phone": "789456123"
      }
    ]
  },
  {
    "id": 6,
    "userId": 6,
    "firstName": "Khan",
    "lastName": "Solo",
    "phones": [
      {
        "id": 6,
        "contactId": 6,
        "phone": "789456123"
      }
    ]
  }
]
```

#### 3.3 Get Single Contact
Request
```http request
GET http://localhost:8080/contacts/5
Accept: application/json
Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjo2LCJpc3MiOiJwaG9uZWJvb2siLCJleHAiOjE1MzQ3MjQyMjUsInVzZXIiOiJMdWtlIn0.t3on_wYMMTwIucjpvQntHm7fz2h89HHACcyputpcVVLD2FJr-_jIOohz4dFyU3KJ_z6G67cIeMMkoohJO_lY_yNgSVnB6eO2uFKBXHY7br_bX4z27byqxSTBGx_bg8UEDTJISMI60Nv65nuofcIwIhkb82Wg2mY8jWtYYEkMGzX58ihiAO8ta3NM3YpTSbAmLzwcfzfJh2URG6cuvH6LD51jAga39eSyFAs22Dqdc-TSvLlj2OlV9okvTXc6DZHXI1G7EEiGA9NdjsIdXnDfQQzkHFqGQ98wkddAgYiix3SuH4XwX4CYd-cB8HG7dYYYh0evjc-cVyfwBUvxV4E15g
```

Successful Response
```json
{
  "id": 5,
  "userId": 6,
  "firstName": "Dart",
  "lastName": "Waider",
  "phones": [
    {
      "id": 5,
      "contactId": 5,
      "phone": "789456123"
    }
  ]
}
```

Unsuccessful Response
```json
{
  "timestamp": "2018-08-19T23:23:36.550+0000",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Contact not found",
  "path": "/contacts/8"
}
```

#### 3.4 Update Contact
Request
```http request
PUT http://localhost:8080/contacts/update
Content-Type: application/json
Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjo2LCJpc3MiOiJwaG9uZWJvb2siLCJleHAiOjE1MzQ3MjQyMjUsInVzZXIiOiJMdWtlIn0.t3on_wYMMTwIucjpvQntHm7fz2h89HHACcyputpcVVLD2FJr-_jIOohz4dFyU3KJ_z6G67cIeMMkoohJO_lY_yNgSVnB6eO2uFKBXHY7br_bX4z27byqxSTBGx_bg8UEDTJISMI60Nv65nuofcIwIhkb82Wg2mY8jWtYYEkMGzX58ihiAO8ta3NM3YpTSbAmLzwcfzfJh2URG6cuvH6LD51jAga39eSyFAs22Dqdc-TSvLlj2OlV9okvTXc6DZHXI1G7EEiGA9NdjsIdXnDfQQzkHFqGQ98wkddAgYiix3SuH4XwX4CYd-cB8HG7dYYYh0evjc-cVyfwBUvxV4E15g

{
    "id" : 5,
    "firstName" : "Dart",
    "lastName" : "Waider123"

}
```

Successful Response
```json
{
  "success": true
}
```

Unsuccessful Response
```json
{
  "timestamp": "2018-08-19T23:25:30.450+0000",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Contact not found",
  "path": "/contacts/update"
}
```

#### 3.5 Delete Contact
Request
```http request
DELETE http://localhost:8080/contacts/delete
Content-Type: application/json
Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjo2LCJpc3MiOiJwaG9uZWJvb2siLCJleHAiOjE1MzQ3MjQyMjUsInVzZXIiOiJMdWtlIn0.t3on_wYMMTwIucjpvQntHm7fz2h89HHACcyputpcVVLD2FJr-_jIOohz4dFyU3KJ_z6G67cIeMMkoohJO_lY_yNgSVnB6eO2uFKBXHY7br_bX4z27byqxSTBGx_bg8UEDTJISMI60Nv65nuofcIwIhkb82Wg2mY8jWtYYEkMGzX58ihiAO8ta3NM3YpTSbAmLzwcfzfJh2URG6cuvH6LD51jAga39eSyFAs22Dqdc-TSvLlj2OlV9okvTXc6DZHXI1G7EEiGA9NdjsIdXnDfQQzkHFqGQ98wkddAgYiix3SuH4XwX4CYd-cB8HG7dYYYh0evjc-cVyfwBUvxV4E15g

{
    "id" : 5
}
```

Successful Response
```json
{
  "success": true
}
```

#### 3.6 Add Phone to Existing Contact
Request
```http request
POST http://localhost:8080/contacts/6/phone
Content-Type: application/json
Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjo2LCJpc3MiOiJwaG9uZWJvb2siLCJleHAiOjE1MzQ3MjQyMjUsInVzZXIiOiJMdWtlIn0.t3on_wYMMTwIucjpvQntHm7fz2h89HHACcyputpcVVLD2FJr-_jIOohz4dFyU3KJ_z6G67cIeMMkoohJO_lY_yNgSVnB6eO2uFKBXHY7br_bX4z27byqxSTBGx_bg8UEDTJISMI60Nv65nuofcIwIhkb82Wg2mY8jWtYYEkMGzX58ihiAO8ta3NM3YpTSbAmLzwcfzfJh2URG6cuvH6LD51jAga39eSyFAs22Dqdc-TSvLlj2OlV9okvTXc6DZHXI1G7EEiGA9NdjsIdXnDfQQzkHFqGQ98wkddAgYiix3SuH4XwX4CYd-cB8HG7dYYYh0evjc-cVyfwBUvxV4E15g

{
    "phone" : "123"
}
```

Successful Response
```json
{
  "success": true
}
```

Unsuccessful Response
```json
{
  "timestamp": "2018-08-19T23:28:50.446+0000",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Contact not found",
  "path": "/contacts/8/phone"
}
```
