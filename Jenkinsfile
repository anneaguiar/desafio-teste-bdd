pipeline {
    agent any // Use 'any' para um ambiente genérico
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
                        cd bdd-teste
                        mvn clean test
                        mvn verify
                        '''
                    } else {
                        bat '''
                        cd bdd-teste
                        mvn clean test
                        mvn verify
                        '''
                    }
                }
            }
        }
        stage('Check Test Results') {
            steps {
                script {
                    def result = currentBuild.result
                    if (result == 'SUCCESS') {
                        echo "Tests passed, proceeding with report generation."
                    } else {
                        error "Tests failed, stopping the pipeline."
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
                        cp -R bdd-teste/target/site/cucumber-html-reports/* /var/jenkins_reports/
                        '''
                    } else {
                        bat '''
                        if not exist "\\var\\jenkins_reports" mkdir "\\var\\jenkins_reports"
                        copy /Y bdd-teste\\target\\site\\cucumber-html-reports\\* \\var\\jenkins_reports
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
                        cd bdd-teste
                        git init
                        git config user.name "GitCI"
                        git config user.email "git@git.org"
                        git add target/site/cucumber-html-reports/*
                        git commit -m "Deploy to GitHub Pages"
                        git push --force --quiet "https://${GITHUB_API_KEY}@${GH_REF}" HEAD:gh-pages
                        '''
                    } else {
                        bat '''
                        cd bdd-teste
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
            archiveArtifacts artifacts: 'bdd-teste/target/site/cucumber-html-reports/*', allowEmptyArchive: true
            publishHTML(target: [
                allowMissing: false,
                keepAll: true,
                reportDir: 'bdd-teste/target/site/cucumber-html-reports',
                reportFiles: 'index.html',
                reportName: 'BDD Report'
            ])
        }
    }
}
