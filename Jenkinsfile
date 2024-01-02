String imageTag
def props

pipeline{

    agent any

    // Specify we should use the installation of Gradle we have in our Jenkins tools named 'Gradle-8.5'
    tools {
        gradle 'Gradle-8.5'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        disableConcurrentBuilds()
        skipDefaultCheckout()
        timestamps()
    }

    stages {
        stage('Initialize Pipeline') {
            steps {
                script {
                    echo "Hello!"
                    props = readFile file: 'sdp.yaml'
                    echo props.versionNumber
                }
            }
        }
    }

}