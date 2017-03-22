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
        containerTemplate(name: 'sbt',
                image: 'henryrao/sbt:211',
                ttyEnabled: true,
                command: 'cat',
                alwaysPullImage: true)
],
        volumes: [
                hostPathVolume(mountPath: '/root/.kube/config', hostPath: '/root/.kube/config'),
                persistentVolumeClaim(claimName: 'jenkins-ivy2', mountPath: '/home/jenkins/.ivy2', readOnly: false)
        ]
) {
    node('cassandra-deploy') {
        ansiColor('xterm') {
            checkout scm
            container('kubectl') {

                stage('deploy') {
                    echo "create a service to track all cassandra statefulset nodes"
                    sh """
                        kubectl apply -f cassandra-service.yaml
                        kubectl get svc cassandra
                        kubectl apply -f cassandra-statefulset.yaml
                    """
                }

                stage('validate') {
                    sh 'kubectl get pods -l="app=cassandra"'
                    timeout(3) {
                        waitUntil {
                            def r = sh script: 'kubectl exec cassandra-0 -- nodetool status', returnStatus: true
                            return (r == 0)
                        }
                    }
                }

            }

            stage('setup') {
                dir('test/akka-persistence') {
                    container('sbt') {
                        parallel(
                                "akka-persistence-schema": {
                                    sh 'sbt run'
                                },
                                "others": {
                                    echo "nothing to do"
                                }
                        )
                    }
                }
            }
        }
    }

}
