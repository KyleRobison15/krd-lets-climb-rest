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
                   shouldDeployApp = should_deploy_app(props)

                }
            }
        }
    }

}