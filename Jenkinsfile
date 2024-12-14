pipeline {
    agent {
        docker {
            image 'maven:3.8.5-openjdk-11'
        }
    }
    environment {
        GH_REF = 'github.com/anneaguiar/desafio-teste-bdd'
        GITHUB_API_KEY = credentials('github-api-token') // Adicione a credencial no Jenkins
    }
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/anneaguiar/desafio-teste-bdd.git'
            }
        }
        stage('Run BDD Tests') {
            steps {
                bat '''
                mvn clean test
                mvn verify
                '''
            }
        }
        stage('Generate Report') {
            steps {
                bat '''
                # Copiar relatório gerado para o volume local
                if not exist "\\var\\jenkins_reports" mkdir "\\var\\jenkins_reports"
                copy /Y target\\site\\cucumber-html-reports\\* \\var\\jenkins_reports
                '''
            }
        }
        stage('Publish to GitHub Pages') {
            steps {
                bat '''
                git init
                git config user.name "GitCI"
                git config user.email "git@git.org"
                git add target\\site\\cucumber-html-reports\\*
                git commit -m "Deploy to GitHub Pages"
                git push --force --quiet "https://githubusername:${GITHUB_API_KEY}@${GH_REF}" HEAD:gh-pages
                '''
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/site/cucumber-html-reports/*', allowEmptyArchive: true
            publishHTML(target: [
                allowMissing: false,
                keepAll: true,
                reportDir: 'target/site/cucumber-html-reports',
                reportFiles: 'index.html',
                reportName: 'BDD Report'
            ])
        }
    }
}
