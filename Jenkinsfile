#!/bin/groovy
String imageTag
String propertiesFileName = "sdp.yaml"
def props

def get_sdp_props(propertiesFileName){
    props = gradle.initProperties(propertiesFileName)
    println("Setting sdpEnvironment to Jenkins parameter: ${params.sdpEnvironment}")
    props.sdpEnvironment = "$params.sdpEnvironment"
    return props
}

def should_deploy_app(props) {
    props.triggerDeploymentBranch = props.triggerDeploymentBranch.replaceAll(/"/, '')
    def shouldDeployApp = "${env.BRANCH_NAME}" == "${props.triggerDeploymentBranch}"
    println("Deploy app?" + shouldDeployApp)
    return shouldDeployApp
}

def set_git_revision(props){
    if("${props.git_revision}" == "null") {
        props.git_revision = "${env.BRANCH_NAME}"
    }
    println("Set git_revision to: " + "${props.git_revision}")
}

pipeline{

    agent any

    tools {
        jdk "JDK 1.17"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
        disableConcurrentBuilds()
        skipDefaultCheckout()
        timestamps()
    }

    stages {

        stage('Initialize Pipeline') {
            steps {
                script{

                    properties([])

                    props = get_sdp_props(propertiesFileName)
                    set_git_revision(props)
                    imageTag = image.adjustImageTagForBranch(props.versionNumber)

                    gradle.modifyDockerFile(imageTag, props.versionNumber, props.dockerfileLocation)
                    gradle.modifyGradleFile(props.versionNumber)

                    shouldDeployApp = should_deploy_app(props)

                }
            }
        }

        stage('Build Project') {
            steps {
                script{
                    gradle.configureGradleWithWrapper()
                    gradle.build(props.dockerfileLocation)
                    buildWorkspace = "${env.WORKSPACE}"
                    echo "Workspace in Build Project stage-" + buildWorkspace
                }
            }
        }

        stage('Create, Scan and Push Docker Image') {
            when {
                expression { shouldDeployApp };
            }

            agent {
                label 'docker'
            }

            steps {
                script{
                    deleteDir()
                    image.build(props.imageName, imageTag, props.dockerfileLocation)
                    image.push(props.imageName, imageTag)
                }
            }
        }

    }

}