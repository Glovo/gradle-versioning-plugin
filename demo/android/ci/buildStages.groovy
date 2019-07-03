def call(pipelineParams) {

    stage('Build') {
        echo 'Start build'
        sh './gradlew :app:assemble'
        echo 'Build finished'
    }

}

return this
