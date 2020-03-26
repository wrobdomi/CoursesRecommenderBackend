# CoursesRecommenderBackend
RESTful style web service for CoursesRecommender.

# Table of Contents 
- [Project Structure](#project-structure)
- [Endpoints](#endpoints)
- [API Data Models](#api-data-models)
- [DB Model](#db-model)
- [Tools](#tools)
- [Technologies](#technologies)

# Project structure
* demo/src/main/java/com/example/demo
  + datalayer
    * entity
    * repository
  + security
  + servicelayer
    * impl
  + shared
    * dto
  + uilayer
    * controllers
    * model

# Endpoints
(Generated with swagger UI)
![b_01](https://user-images.githubusercontent.com/37666186/77698310-afa56200-6fb0-11ea-864f-328a6e85a7c2.PNG)
![b_02](https://user-images.githubusercontent.com/37666186/77698326-b633d980-6fb0-11ea-8d71-195a9995267e.PNG)
![b_03](https://user-images.githubusercontent.com/37666186/77698380-ce0b5d80-6fb0-11ea-834a-8d60889f10d5.PNG)

# API Data Models
(Generated with swagger UI)
![b_04](https://user-images.githubusercontent.com/37666186/77698425-e3808780-6fb0-11ea-8d48-de8aea113f5d.PNG)
![b_05](https://user-images.githubusercontent.com/37666186/77698445-e8453b80-6fb0-11ea-98dd-6a52866f241a.PNG)
![b_06](https://user-images.githubusercontent.com/37666186/77698458-ed09ef80-6fb0-11ea-9cdd-d3c0f076d7df.PNG)

# DB Model
![m_37](https://user-images.githubusercontent.com/37666186/77699321-7c63d280-6fb2-11ea-8689-ec9e9d8f6b2f.png)


# Tools
- PostgreSQL, PgAdmin
- IntelliJ IDEA

# Technologies
- Java, Spring Boot 
- Apache Mahout (recommender systems library) https://mahout.apache.org/
- Mallet (text analysis library) http://mallet.cs.umass.edu/
