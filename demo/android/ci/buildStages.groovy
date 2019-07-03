def call(pipelineParams) {

    stage('Build') {
        dir('demo/android') {
            echo 'Start build'
            sh './gradlew :app:assemble'
            echo 'Build finished'
        }
    }

}

return this
