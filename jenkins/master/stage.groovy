def call(pipelineParams, stageConfig, stageParams, input) {
    withAgent(pipelineParams.mainAgent) {

        sh './gradlew incrementSemanticVersion --patch'
        def String version = getVersion()

        stage('Bump Version') {
            git.authenticated(pipelineParams.gitCredentialsId) {
                git.addRemoteBranch('master')
                sh "git checkout master"

                //This file is modified by the above call to updateVersionFile
                sh "git add version.properties"
                sh "git commit -m '[NO-CI] Bump app version to $version'"
                sh "git push origin master"
            }
        }

        stage('Build plugins') {
            try {
                sh "./gradlew --stacktrace clean build"
            }
            finally {
                junit "**/build/test-results/**/*.xml"
            }
        }

        stage('Tag version') {
            git.authenticated(pipelineParams.gitCredentialsId) {
                git.tag(version)
            }
        }

        stage('Publish SDK artifacts') {
            sh "./gradlew --stacktrace artifactoryPublish --debug"
        }

        stage('Release to GitHub') {
            def techReleaseNotes = "Automatic release of gradle versioning plugin"
            withAuth.secretText(pipelineParams.sharedConfig.githubTokenId) { secretText ->
                github.release {
                    apiToken = secretText
                    repo = 'Glovo/gradle-versioning-plugin'
                    tag = version
                    name = "Release $version"
                    body = techReleaseNotes
                    asset('**/build/libs/*.jar')
                }
            }
        }
    }
}

private String getVersion() {
    def properties = readProperties file: 'version.properties'
    return properties["version"]
}

return this
