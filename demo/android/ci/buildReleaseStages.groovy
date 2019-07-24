static String readReleaseNotes() {
    return """\
        - Released on: ${new Date().toString()}
        """.stripIndent()
}

def call(pipelineParams, stageConfig, stageParams, input) {

    withAgent(pipelineParams.mainAgent) {
        stage('Build') {
            checkout scm
            dir('demo/android') {
                sh './gradlew --no-daemon :app:assemble'
            }
        }
        stage('Release to GitHub') {
            withAuth.secretText('mr-archano-github-token') { githubToken ->
                def version = "v$BRANCH_NAME" - 'release/' + "-RC$BUILD_NUMBER"
                githubRelease {
                    apiToken = githubToken
                    repo = 'glovo/gradle-mobile-release-plugin'
                    tag = version
                    name = "Release $version"
                    body = "Body for $version"
                    isDraft = false
                    asset('**/*.apk')
                }
            }

        }
        stage('Deploy to HockeyApp') {
            withCredentials([string(credentialsId: 'hockey-app-token', variable: 'HOCKEY_API_TOKEN')]) {
                hockeyApp(
                        applications: [
                                [
                                        apiToken          : "$HOCKEY_API_TOKEN",
                                        downloadAllowed   : true,
                                        filePath          : 'demo/android/app/build/outputs/apk/debug/app-debug.apk',
                                        mandatory         : true,
                                        notifyTeam        : true,
                                        releaseNotesMethod: manual(releaseNotes: readReleaseNotes(), isMarkDown: true),
                                        uploadMethod      : versionCreation(appId: 'e4bacf5c548547b0bf48a37b671b1bad')
                                ]
                        ],
                        baseUrl: '',
                        debugMode: true,
                        failGracefully: false
                )
            }
        }
    }

}

return this
