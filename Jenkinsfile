String imageTag
String fullImageName
def props

def get_sdp_props() {
    props = readYaml file: "sdp.yaml"
    println("Updated properties are:")
    println(props)
    return props
}

def set_git_revision(props) {
    if ("${props.git_revision}" == "null") {
        props.git_revision = "${env.BRANCH_NAME}"
    }
    println("git_revision: " + "${props.git_revision}")
}

def should_deploy_app(props) {
    props.triggerDeploymentBranch = props.triggerDeploymentBranch.replaceAll(/"/, '')
    def shouldDeployApp= "${env.BRANCH_NAME}" == "main" || "${env.BRANCH_NAME}" == "${props.triggerDeploymentBranch}"
    println("Will we deploy? " + shouldDeployApp)
    return shouldDeployApp
}

// Updates dockerfile with variable information
def modifyDockerFile(String imageTag, String versionNumber, String dockerfileLocation) {
    def gitHash = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
    def imageCreatedDt = new Date().format('yyyyMMdd-HH:mm:ss')

    echo """ Image Tag: ${imageTag}
    Image Created Date ${imageCreatedDt}
    Git Hash: ${gitHash}
    Version Number: ${versionNumber}"""

    echo "dockerfile file before modification"
    echo sh(returnStdout: true, script: "cat ./${dockerfileLocation}" )

    echo "modifying dockerfile"
    sh(returnStdout: true, script: """sed -i '' 's/IMAGE_TAG/${imageTag}/g' "${dockerfileLocation}" """)
    sh(returnStdout: true, script: """sed -i '' 's/IMAGE_CREATED_DT/${imageCreatedDt}/g' "${dockerfileLocation}" """)
    sh(returnStdout: true, script: """sed -i '' 's/IMAGE_LATEST_COMMIT/${gitHash}/g' "${dockerfileLocation}" """)
    sh(returnStdout: true, script: """sed -i '' 's/VERSION_NUMBER/${versionNumber}/g' "${dockerfileLocation}" """)

    echo "dockerfile file after modification"
    echo sh(returnStdout: true, script: "cat ./${dockerfileLocation}")
}

//Updates gradle file with variable information
def modifyGradleFile(String versionNumber) {
    echo "Version Number: ${versionNumber}"

    def buildFile = 'build.gradle'

    echo "modifying ${buildFile}"
    sh(returnStdout: true, script: """sed -i '' 's/VERSION_NUMBER/${versionNumber}/g' "${buildFile}" """)

}

//Updates Kubernetes Objects with variable information
def modifyKubernetesManifest(props, String fullImageName) {
    echo "APP_NAME: ${props.imageName}"
    echo "FULL_IMAGE_NAME: ${fullImageName}"

    echo "Modifying ${props.deploymentManifestLocation}"
    sh(returnStdout: true, script: """sed -i '' 's/APP_NAME/${props.imageName}/g' "${props.deploymentManifestLocation}" """)
    sh(returnStdout: true, script: """sed -i '' 's#FULL_IMAGE_NAME#${fullImageName}#g' "${props.deploymentManifestLocation}" """)

    echo "Modifying ${props.serviceManifestLocation}"
    sh(returnStdout: true, script: """sed -i '' 's/APP_NAME/${props.imageName}/g' "${props.serviceManifestLocation}" """)

    echo "Deployment Manifest after Modification: "
    echo sh(returnStdout: true, script: "cat ./${props.deploymentManifestLocation}")

    echo "Service Manifest after Modification: "
    echo sh(returnStdout: true, script: "cat ./${props.serviceManifestLocation}")

}

pipeline{

    agent any

    environment {
        GRADLE_OPTS = "-Dorg.gradle.daemon=false"
    }

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

                   def branchName = "${env.BRANCH_NAME}" ?: "main"

                   checkout scmGit(branches: [[name: '*/' + "${branchName}"]], extensions: [], userRemoteConfigs: [[url: 'https://github.com/KyleRobison15/krd-lets-climb-rest']])

                   props = get_sdp_props()
                   set_git_revision(props)

                   imageTag = "${props.versionNumber}-${sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()}"
                   echo "Image Tag: ${imageTag}"

                   fullImageName = "${props.dockerHubNamespace}/${props.dockerHubRepo}:${imageTag}"
                   echo "Full Image Name: ${fullImageName}"

                   modifyDockerFile(imageTag, props.versionNumber, props.dockerfileLocation)
                   modifyGradleFile(props.versionNumber)
                   modifyKubernetesManifest(props, fullImageName)

                   shouldDeployApp = should_deploy_app(props)

                }
            }
        }

        stage('Build Project') {
            steps {
                script {

                    sh "./gradlew clean build"
                    archiveArtifacts "build/libs/*.jar,${props.dockerfileLocation}"

                }
            }
        }

        stage('Build Docker Image') {
            when {
                expression { shouldDeployApp };
            }
            steps {
                script {
                    echo "Full Image Name: ${fullImageName}"
                    sh "docker build -f ${props.dockerfileLocation} -t ${fullImageName} ."
                }
            }
        }

        stage('Push Docker Image') {
            when {
                expression { shouldDeployApp };
            }
            steps {
                script {
                    withCredentials([string(credentialsId: 'DockerHubPwd', variable: 'dockerHubPwd')]) {
                      sh 'docker login -u kylerobison -p ${dockerHubPwd}'
                      sh "docker push ${fullImageName}"
                    }
                }
            }
        }

        stage('Deploy to K8s Cluster') {
            when {
                expression { shouldDeployApp };
            }
            steps {
                script {
                    kubeconfig(caCertificate: 'LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURKekNDQWcrZ0F3SUJBZ0lDQm5Vd0RRWUpLb1pJaHZjTkFRRUxCUUF3TXpFVk1CTUdBMVVFQ2hNTVJHbG4KYVhSaGJFOWpaV0Z1TVJvd0dBWURWUVFERXhGck9ITmhZWE1nUTJ4MWMzUmxjaUJEUVRBZUZ3MHlOREF4TURVeQpNalF3TVRoYUZ3MDBOREF4TURVeU1qUXdNVGhhTURNeEZUQVRCZ05WQkFvVERFUnBaMmwwWVd4UFkyVmhiakVhCk1CZ0dBMVVFQXhNUmF6aHpZV0Z6SUVOc2RYTjBaWElnUTBFd2dnRWlNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0SUIKRHdBd2dnRUtBb0lCQVFEVnVkeFlyS2lrZk5UNit4YnpkMlRjOTlkTkVIMENQZWNtY1dtTWpGRy9CZ1JMMTNPVAorOWJzcGd5aUx6eS9wY3BmYURaM0wyRWtvSGN1OTRBalEySFNtUG5wajVycHFlMkhqYUpjcWZFL2tiYnM4L01HCkxLaDJ0cHdpeXJBWERCZzJldksxd2ZLdnFEc0VWcExHeS9PUk5YV1MvaDhZNmdVbnZlQUQ2a3FyczdNRUlkTFYKRCtxWmp4VlYzYUtSRnB0cC9ya01TenNjOS9SUFRLQXZzTGUzMFFlY3FMZHUrcWRPNG5TTW1GOU9qU05oaGpVcwpWQjdESUdzNndpY0N5d1BheVkvWGpHNG5WRHREZWVIbHZWajQ4MWRIYjhram95eHBDTnJZTVU4OXNXZW9WVTRaCkV4MHhPVFErRm42WDNUOXl3RU01ZWhBaFBOU2tPZFpKbzV1WkFnTUJBQUdqUlRCRE1BNEdBMVVkRHdFQi93UUUKQXdJQmhqQVNCZ05WSFJNQkFmOEVDREFHQVFIL0FnRUFNQjBHQTFVZERnUVdCQlQ2QU5MTmdzcjRsRWNsd0hwYwpVVWp0MkozUDdUQU5CZ2txaGtpRzl3MEJBUXNGQUFPQ0FRRUFzaVovQkk5WElFaVdOcTRmL2RTemVjUE1ZdEttCklUN0o3THc4elFrWDBOUXc0NnpnMVAwVGdFUm9jMUtycW1uWDY5ZUJLN2FtZW5aZlhYQVNPQUlaUm1Nc3o5WjkKWEV3VGtVWFZxaGtJd1I2VldTaHo1OXlNQkFwQUEzN3R5Z3JqV3NmVndCRXZVV3FPbWcwalZ0U21Mb0I3MkxyYQp5WVZ2K1NjcU9TOFpuZ0FmWlBiS0wySGNDc1dqZnliWHNJTExxaU44bmpsTmJ3ZXh1Qm94MHRIY2h1dEFlZll2CjIrWksyc2ZoRWlTbTdZTE5uSXcwbDIxRDVranZvU2c0RTFCcE03Z1BPd2NNYWxSbVNVcGZ5REl0WnZZdEh1YkUKYXlKVnAyL3BHbDBVUnpHSkk0ekRmdzZmUytyOUZtQUNoWGhIWld5TE5WTy9aTXZONmRaVVNCSkhSQT09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K', credentialsId: 'KRDev_DO_Token', serverUrl: 'https://bb277271-cfd3-45a9-aff9-06fa62c60a42.k8s.ondigitalocean.com') {
                        sh "kubectl apply -f ${props.deploymentManifestLocation}"
                        sh "kubectl apply -f ${props.serviceManifestLocation}"
                    }
                }
            }
        }

    }

}