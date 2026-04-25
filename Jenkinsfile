pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome'
            args '-u root:root -v /var/lib/jenkins/.m2:/root/.m2'
        }
    }
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/Khawaja-Muhammad-Haseeb/Selenium-Test.git'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }
    post {
        always {
            script {
                sh "git config --global --add safe.directory ${env.WORKSPACE}"
                def committer = sh(
                    script: "git log -1 --pretty=format:'%ae'",
                    returnStdout: true
                ).trim()
                def raw = sh(
                    script: "grep -h \"<testcase\" target/surefire-reports/*.xml",
                    returnStdout: true
                ).trim()
                int total = 0, passed = 0, failed = 0, skipped = 0
                def details = ""
                raw.split('\n').each { line ->
                    total++
                    def name = (line =~ /name=\"([^\"]+)\"/)[0][1]
                    if (line.contains("<failure")) {
                        failed++
                        details += "${name} — FAILED\n"
                    } else if (line.contains("<skipped")) {
                        skipped++
                        details += "${name} — SKIPPED\n"
                    } else {
                        passed++
                        details += "${name} — PASSED\n"
                    }
                }
                def emailBody = """
Test Summary (Build #${env.BUILD_NUMBER})

Total:   ${total}
Passed:  ${passed}
Failed:  ${failed}
Skipped: ${skipped}

Details:
${details}
"""
                emailext(
                    to: committer,
                    subject: "Build #${env.BUILD_NUMBER} Test Results",
                    body: emailBody
                )
            }
        }
    }
}
