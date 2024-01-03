String imageTag
String propertiesFileName = "sdp.yaml"
def props
def buildWkspace

def should_deploy_to_dev(props) {
    props.triggerDeploymentBranch = props.triggerDeploymentBranch.replaceAll(/"/, '')
    def shouldDeployToDev = "${env.BRANCH_NAME}" == "master" || "${env.BRANCH_NAME}" == "${props.triggerDeploymentBranch}"
    println("Will we deploy to dev? " + shouldDeployToDev)
    return shouldDeployToDev
}

def get_sdp_props(propertiesFileName) {
    props = readFile file: "sdp.yaml"
    println("Updated properties are:")
    println(props)
    return props
}

def set_git_revision(props) {
    if ("${props.git_revision}" == "") {
        props.git_revision = "${env.BRANCH_NAME}"
    }
    println("git_revision: " + "${props.git_revision}")
}

pipeline{

    agent any

    tools {
        jdk 'JDK 1.17'
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
                    props = get_sdp_props(propertiesFileName)
                    set_git_revision(props)
                    imageTag = image.adjustImageTagForBranch(props.versionNumber)
                }
            }
        }
    }

}