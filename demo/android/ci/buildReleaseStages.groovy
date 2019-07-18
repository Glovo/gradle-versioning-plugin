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
                echo 'Start build'
                sh './gradlew --no-daemon :app:assemble'
                echo 'Build finished'
                echo 'Pushing to HockeyApp'
                withCredentials([string(credentialsId: 'hockey-app-token', variable: 'HOCKEY_API_TOKEN')]) {
                    hockeyApp(
                            applications: [
                                    [
                                            apiToken          : "$HOCKEY_API_TOKEN",
                                            downloadAllowed   : true,
                                            filePath          : 'app/build/outputs/apk/debug/app-debug.apk',
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

}

return this
