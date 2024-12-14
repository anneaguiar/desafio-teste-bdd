pipeline {
    agent any // Use 'any' se o Docker não estiver configurado corretamente no Jenkins
    environment {
        GH_REF = 'github.com/anneaguiar/desafio-teste-bdd'
        GITHUB_API_KEY = credentials('github-api-token') // Credencial no Jenkins
    }
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/anneaguiar/desafio-teste-bdd.git'
            }
        }
        stage('Run BDD Tests') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                        mvn clean test
                        mvn verify
                        '''
                    } else {
                        bat '''
                        mvn clean test
                        mvn verify
                        '''
                    }
                }
            }
        }
        stage('Generate Report') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                        mkdir -p /var/jenkins_reports
                        cp -R target/site/cucumber-html-reports/* /var/jenkins_reports/
                        '''
                    } else {
                        bat '''
                        if not exist "\\var\\jenkins_reports" mkdir "\\var\\jenkins_reports"
                        copy /Y target\\site\\cucumber-html-reports\\* \\var\\jenkins_reports
                        '''
                    }
                }
            }
        }
        stage('Publish to GitHub Pages') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                        git init
                        git config user.name "GitCI"
                        git config user.email "git@git.org"
                        git add target/site/cucumber-html-reports/*
                        git commit -m "Deploy to GitHub Pages"
                        git push --force --quiet "https://${GITHUB_API_KEY}@${GH_REF}" HEAD:gh-pages
                        '''
                    } else {
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
