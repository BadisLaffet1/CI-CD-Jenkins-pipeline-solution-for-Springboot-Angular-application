# CI/CD Jenkins Pipeline Solution for Spring Boot Angular Application

Based on various DevOps best practices, this repository houses a robust CI/CD solution leveraging Jenkins pipelines for seamless integration and deployment of a Spring Boot Angular application. The project includes a MySQL database and is integrated with Jenkins, SonarQube, Nexus, Prometheus, and Grafana.

## Table of Contents

- [Installation](#installation)
  - [Clone the Repository](#clone-the-repository)

- [Configuration](#configuration)  
  - [Jenkins Configuration](#jenkins-configuration)
  - [SonarQube Configuration](#sonarqube-configuration)
  - [Nexus Configuration](#nexus-configuration)
  - [Deploy Application](#deploy-application)
  - [Prometheus and Grafana Configuration](#prometheus-and-grafana-configuration)
- [Examples and Integration](#examples-and-integration)
  - [Prometheus Target Section](#prometheus-target-section)
  - [Access Grafana Interface](#access-grafana-interface)
  - [Examples of Integrated Dashboards](#examples-of-integrated-dashboards)

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


This is the final stage view of our pipeline
![image](https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application/assets/125974896/8eca2c66-446b-4e02-a6b6-d5acfdbe5522)


Clone the repository:

```bash
git clone https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application.git
```
```bash
cd CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application
```
## Configuration


Before running the project, ensure that you have completed the following configuration steps:

1. **Database Configuration:**
   - Create a MySQL database for the application.
   - Update the `application.properties` file located in the `src/main/resources` directory with your database credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://(MYSQL_CONTAINER_NAME):3306/your_database
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```
   - PS: Change MYSQL_CONTAINER_NAME with the same name of the container hosting the SQL server.


2. **Jenkins Configuration:**
   - Set up a Jenkins instance and create a new pipeline for this project.
   - Follow regular installation methods for Jenkins, Java, and Maven.
   - Configure a new Jenkins Pipeline with the necessary stages, steps, and environment variables:
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
         }
     }
     ```
     - PS: Change YOUR_BRANCH_NAME with the name of your current branch and YOUR_GITHUB_URL with your repository URL.
     - Clean and build the Spring Boot project:
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
     - Prepare metrics by adding a test stage:
     ```groovy
     stage('Build and Test') {
         steps {
             script {
                 dir('Backend') {
                     sh 'mvn clean test'
                     sh 'mvn jacoco:report'
                 }
             }
         }
     }
     ```

3. **SonarQube Configuration:**
   - SonarQube is being hosted on a separate container (check docker-compose file inside the monitoring-stack folder for more information).
   - Configure the `sonar-project.properties` file in the root of your project with the appropriate SonarQube server information:
     ```properties
     sonar.host.url=http://your-sonarqube-server:9000
     sonar.login=your-sonar-token
     ```
   - Alternatively, you can add a specific token to your Jenkins credentials.
   - Add a SonarQube stage to your Jenkins pipeline script:
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

     - You should have the following analysis on your sonarqube projects section

    ![image](https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application/assets/125974896/c48b024b-fe3c-4ba6-8477-2a282bd8d20e)


4. **Nexus Configuration:**
   - Nexus is being hosted on a separate container (check docker-compose file inside the monitoring-stack folder for more information).
   - Update the `pom.xml` file with the Nexus repository information for Maven dependencies based on your configuration:
     ```xml
     <distributionManagement>
         <repository>
             <id>nexus-releases</id>
             <url>http://your-nexus-server:8081/repository/maven-releases/</url>
         </repository>
     </distributionManagement>
     ```
   - Finally, add a Deploy artifact to Nexus stage to your pipeline script:
     ```groovy  
     stage('Deploy artifact to Nexus') {
         steps {
             dir('Backend') {
                 sh 'mvn deploy -DskipTests'               
             }
         }
     }
     ```
    - You should have your spring boot JAR uploaded to your nexus repository.
      
    ![image](https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application/assets/125974896/dfb914cf-ecb6-43be-a69c-ff98a70d7a9d)



 5. **Deploy application :**
   - Use the provided Docker Compose file in the main directory to start mysql, spring boot app and the angular front app.
     ```groovy  
     stage('deploy application') {
            steps{
               script {
          dir('.') {
                    sh "docker compose up -d"
              }}
            }
        }

     }
     ```



   - After this stage , you should have these containers with running status :
  
  ![image](https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application/assets/125974896/1c560ea1-8f30-4361-a053-aeb04dd27a5d)



6. **Prometheus and Grafana Configuration:**
   - Use the provided Docker Compose file in the `monitoring` directory to also start Prometheus and Grafana containers , also consider adding services for cadvisor , redis etc ... 
   - Access Grafana at http://YOURIP:3000 and set up Prometheus as a data source.
   - PS : configure the jobs either by editing the file inside the container prometheus/prometheus.yml or by editing the file located at the same location with the compose file .

These configuration steps ensure that your Spring Boot Angular application integrates seamlessly with the specified tools and services. Adjust the configurations based on your environment and requirements.

  
  -To make sure you have efficiently synchronized your metrics with the prometheus data sure , you should have a similar prometheus target section : 

  ![image](https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application/assets/125974896/c1d69540-5177-495b-905f-e5e584274631)

  -In final step , access your Grafana interface , add prometheus datasource , and start configuring dashboards based on your preference . 

  -The following examples of dashboards that i have integrated to the project : 

  Spring APM
  ![image](https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application/assets/125974896/b3323e35-93a4-4732-946d-acd326b95042)

  Docker container & host metrics 
  ![image](https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application/assets/125974896/da2a82e7-b622-4295-8d19-c9e74cecb2bc)

  Jenkins performance and health overview
  ![image](https://github.com/BadisLaffet1/CI-CD-Jenkins-pipeline-solution-for-Springboot-Angular-application/assets/125974896/6c618b8b-d543-433e-905a-2a5ab7bc9836)


 
MIT License

Copyright (c) 2023 Badis Laffet


FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
