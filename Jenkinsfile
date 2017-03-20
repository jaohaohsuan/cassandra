#!groovy
podTemplate(label: 'cassandra-deploy', containers: [
        containerTemplate(name: 'jnlp',
                image: 'henryrao/jnlp-slave',
                args: '${computer.jnlpmac} ${computer.name}',
                alwaysPullImage: true),
        containerTemplate(name: 'kubectl',
                image: 'henryrao/kubectl:1.5.2',
                ttyEnabled: true,
                command: 'cat'),
],
        volumes: [ hostPathVolume(mountPath: '/root/.kube/config', hostPath: '/root/.kube/config') ],
) {
    properties([
            pipelineTriggers([]),
            parameters([]),
    ])

    node('cassandra-deploy') {

        checkout scm
        container('kubectl') {

            stage('deploy') {
                sh "kubectl apply -f cassandra-service.yaml"
            }

        }
    }
}
