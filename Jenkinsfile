String imageTag
def props
def buildWkspace

def get_sdp_props() {
    props = readFile file: 'sdp.yaml'
    println("Updated properties are " + props)
    return props
}

def should_deploy_to_dev(props) {
    props.triggerDeploymentBranch = props.triggerDeploymentBranch.replaceAll(/"/, '')
    def shouldDeployToDev = "${env.BRANCH_NAME}" == "master" || "${env.BRANCH_NAME}" == "${props.triggerDeploymentBranch}"
    println("Will we deploy to dev? " + shouldDeployToDev)
    return shouldDeployToDev
}

def set_git_revision(props) {
    if ("${props.git_revision}" == "null") {
        props.git_revision = "${env.BRANCH_NAME}"
    }
    println("git_revision: " + "${props.git_revision}")
}

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
        stage('Checkout') {
            steps {
                script {
                    checkout scm
                }
            }
        }
    }

}