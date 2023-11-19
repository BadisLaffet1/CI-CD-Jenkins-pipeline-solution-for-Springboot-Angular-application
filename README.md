# CI/CD Jenkins Pipeline Solution for Spring Boot Angular Application

This repository houses a robust CI/CD solution leveraging Jenkins pipelines for seamless integration and deployment of a Spring Boot Angular application. The project includes a MySQL database and is integrated with Jenkins, SonarQube, Nexus, Prometheus, and Grafana.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Installation](#installation)
  - [Configuration](#configuration)
- [Development](#development)
  - [Built With](#built-with)
  - [Running Locally](#running-locally)
- [Continuous Integration](#continuous-integration)
- [Code Quality](#code-quality)
- [Artifact Repository](#artifact-repository)
- [Monitoring](#monitoring)
- [Contributing](#contributing)
- [License](#license)

## Prerequisites

Ensure you have the following software and tools installed before setting up and running the project. Links to official documentation or download pages are provided for reference.

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Node.js](https://nodejs.org/)
- [MySQL Database](https://www.mysql.com/)
- [Docker](https://www.docker.com/)
- [Jenkins](https://www.jenkins.io/)
- [SonarQube](https://www.sonarqube.org/)
- [Nexus Repository Manager](https://www.sonatype.com/nexus/repository-oss)
- [Prometheus](https://prometheus.io/)
- [Grafana](https://grafana.com/)

## Getting Started

Follow these steps to set up and run the project.

### Installation

Clone the repository:

```bash
git clone https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application.git
```
```bash
cd CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application
```
## Configuration

Before running the project, ensure that you have completed the following configuration steps:

PS : this is a global view of the pipeline : 
![image](https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application/assets/125974896/9d97b0ac-72f7-4173-aa41-e55e8ab09526)


1. **Database Configuration:**

   - Create a MySQL database for the application.
   - Update the `application.properties` file located in the `src/main/resources` directory with your database credentials:

     ```properties
     spring.datasource.url=jdbc:mysql://(MYSQL_CONTAINER_NAME):3306/your_database
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```
  - PS : Change MYSQL_CONTAINER_NAME with same name of the container hosting the sql server . 

2. **Jenkins Configuration:**

   - Set up a Jenkins instance and create a new pipeline for this project.
   - You can follow regular installations methods for Jenkins, Java , Maven
   - Configure a new Jenkins Pipeline, ensure that your script contains the necessary stages, steps, and any necessary environment variables based on your need.


      ```groovy
         stage('Checkout') {
      steps {
        script {
            checkout([
                $class: 'GitSCM',
                branches: [[name: 'YOU_BRANCH_NAME']], 
                userRemoteConfigs: [[url: 'YOUR_GITHUB_URL', credentialsId: 'github']]
            ])
        }
          }}
      ```
  - PS : Change YOUR_BRANCH_NAME by the name of your current branch and YOUR_GITHUB_URL by your repository url.

  - Clean and build the spring boot project 
     ```groovy
        stage('Clean and Build') {
            steps {
                script {
                    dir('DevOps_Project') {
                        sh 'mvn clean package  -Dmaven.test.skip=true '
                        sh 'mvn package -DskipTests'
                        
                    }
                }
            }
        }
      ```

   - Prepare metrics by adding test stage :
     ```groovy
       stage('Build and Test') {
            steps {
          script {
                    dir('Backend') {
                sh 'mvn clean test'
                sh 'mvn jacoco:report'
                        
                    }
            }}
        }

      ```
 
     
3. **SonarQube Configuration:**

   - SonarQube is beign also hosted on a separate container. check docker-compose file inside the monitoring-stack folder for more informations.  
   - Configure the `sonar-project.properties` file in the root of your project with the appropriate SonarQube server information:

     ```properties
     sonar.host.url=http://your-sonarqube-server:9000
     sonar.login=your-sonar-token
     ```
   - Aternatively , you can add a specific token to your jenkins credentials.
  
      ```groovy  
        stage('Static Code Analysis') {
            environment {
                SONAR_URL = "YOUR_IP:9000"
            }
            steps {
                dir('Backend') {
                    withCredentials([string(credentialsId: 'CREDENTIAL_ID', variable: 'SONAR_AUTH_TOKEN')]) {
                        sh 'mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=${SONAR_URL}'
                    }
                }
            }
        }    
        ```
      - Make sure to change the script based on your configuration.
    
     

4. **Nexus Configuration:**

   - Nexus is beign also hosted on a separate container. check docker-compose file inside the monitoring-stack folder for more informations.  
   - Update the `pom.xml` file with the Nexus repository information for Maven dependencies based on your configuration.

     ```xml
     <distributionManagement>
         <repository>
             <id>nexus-releases</id>
             <url>http://your-nexus-server:8081/repository/maven-releases/</url>
         </repository>
     </distributionManagement>
     ```
   - Finaly , you can add this stage to your pipeline script.
  
      ```groovy  
       stage('Deploy artifact to Nexus') {
      steps {
      dir('Backend') {
                         sh 'mvn deploy -DskipTests'               
                        
                    }
      }
      
            
    
        ```

5. **Prometheus and Grafana Configuration:**

   - Use the provided Docker Compose file in the `monitoring` directory to start Prometheus and Grafana containers:

     ```bash
     cd monitoring
     docker-compose up -d
     ```

   - Access Grafana at http://localhost:3000 and set up Prometheus as a data source.

These configuration steps ensure that your Spring Boot Angular application integrates seamlessly with the specified tools and services. Adjust the configurations based on your environment and requirements.

Development
Provide details about the development environment and any relevant information for developers.

Built With
List the main technologies, frameworks, and tools used in your project.

Spring Boot
Angular
MySQL
Running Locally
Provide instructions on how to run the project locally for development purposes.

bash
Copy code
# Build and run the Spring Boot application
./mvnw spring-boot:run

# Navigate to the Angular project folder
cd frontend

# Install dependencies and run the Angular application
npm install
ng serve
Continuous Integration
Explain how the Jenkins pipeline is set up for continuous integration.

Code Quality
Describe how SonarQube is integrated to ensure code quality.

Artifact Repository
Explain how Nexus is used as an artifact repository for managing dependencies.

Monitoring
Provide information on how Prometheus and Grafana are set up for monitoring and visualizing metrics.

Contributing
Explain how others can contribute to your project. Include guidelines for pull requests, coding standards, and any other relevant information.

License
Specify the license under which your project is distributed.
