#!/bin/groovy
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
    props = gradle.initProperties(propertiesFileName)
    if ("${props.sdpEnvironment}" == "null") {
        // If value in sdp.yaml file is undefined, then use param
        println("Setting sdpEnvironment to Jenkins parameter: ${params.sdpEnvironment}")
        props.sdpEnvironment = "$params.sdpEnvironment"
    } else {
        println("Using sdpEnvironment from sdp.yaml file: ${props.sdpEnvironment}")
    }
    println("Updated properties are " + props)
    return props
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
                    properties([])

                    props = get_sdp_props(propertiesFileName)
                    set_git_revision(props)
                    imageTag = image.adjustImageTagForBranch(props.versionNumber)
                    shouldDeployToDev = should_deploy_to_dev(props)
                }
            }
        }
    }

}