def props

def get_sdp_props() {
    props = readFile file: "sdp.yaml"
    println("Updated properties are:")
    println(props)
    return props
}

pipeline{

    agent any

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
        stage('Checkout') {
            steps {
                script {
                   checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/KyleRobison15/krd-lets-climb-rest']])
                   props = get_sdp_props()
                }
            }
        }
    }

}