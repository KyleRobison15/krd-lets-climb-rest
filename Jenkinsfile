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
    sh(returnStdout: true, script: """sed -i 's/IMAGE_TAG/"${imageTag}"/g' ${dockerfileLocation}""")
    sh(returnStdout: true, script: """sed -i 's/IMAGE_CREATED_DT/"${imageCreatedDt}"/g' ${dockerfileLocation}""")
    sh(returnStdout: true, script: """sed -i 's/IMAGE_LATEST_COMMIT/"${gitHash}"/g' ${dockerfileLocation}""")
    sh(returnStdout: true, script: """sed -i 's/VERSION_NUMBER/${versionNumber}/g' ${dockerfileLocation}""")

    echo "dockerfile file after modification"
    echo sh(returnStdout: true, script: "cat ./${dockerfileLocation}")
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
        stage('Initialize Pipeline') {
            steps {
                script {

                   def branchName = "${env.BRANCH_NAME}" ?: "main"

                   checkout scmGit(branches: [[name: '*/' + "${branchName}"]], extensions: [], userRemoteConfigs: [[url: 'https://github.com/KyleRobison15/krd-lets-climb-rest']])

                   props = get_sdp_props()
                   set_git_revision(props)

                   def versionNumber = props.versionNumber
                   imageTag = "${versionNumber}-${sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()}"
                   echo "Image Tag: ${imageTag}"

                   modifyDockerFile(imageTag, props.versionNumber, props.dockerfileLocation)

                   shouldDeployApp = should_deploy_app(props)

                }
            }
        }
    }

}