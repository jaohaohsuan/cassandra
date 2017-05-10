#!groovy
podTemplate(label: 'cassandra-deploy', containers: [
        containerTemplate(name: 'jnlp',
                image: 'henryrao/jnlp-slave',
                args: '${computer.jnlpmac} ${computer.name}',
                alwaysPullImage: true),
        containerTemplate(name: 'sbt',
                image: 'henryrao/sbt:2.11.8',
                ttyEnabled: true,
                command: 'cat',
                alwaysPullImage: true)
],
        volumes: [
                hostPathVolume(mountPath: '/root/.kube/config', hostPath: '/root/.kube/config'),
                persistentVolumeClaim(claimName: 'jenkins-ivy2', mountPath: '/home/jenkins/.ivy2', readOnly: false),
								persistentVolumeClaim(claimName: 'helm-repository', mountPath: '/var/helm/repo', readOnly: false)
        ]
) {
    node('cassandra-deploy') {
        ansiColor('xterm') {
            checkout scm
            stage('package') {
                docker.image('henryrao/helm:2.3.1').inside('') { c ->
                    sh '''
                    # packaging
                    helm package --destination /var/helm/repo cassandra
                    helm repo index --url https://grandsys.github.io/helm-repository/ --merge /var/helm/repo/index.yaml /var/helm/repo
                    '''
                }
                build job: 'helm-repository/master'
            } 
        }
    }

}
