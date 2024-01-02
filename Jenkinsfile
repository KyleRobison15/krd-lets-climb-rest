String imageTag
def props

pipeline{

    agent any

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
                    // Read properties from sdp.yaml
                    props = readYaml(file: 'sdp.yaml')

                    // Access versionNumber property
                    def versionNumber = props.versionNumber

                    // Parse git HEAD for the branch name
                    def branchName = env.BRANCH_NAME ?: 'main' // Default to 'main' if env.BRANCH_NAME is null

                    // Create the imageTag string
                    imageTag = "${versionNumber}-${sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()}"

                    echo "Image Tag: ${imageTag}"
                }
            }
        }
    }

}