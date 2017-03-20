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

 ansiColor('xterm') {

     node('cassandra-deploy') {

         checkout scm
         container('kubectl') {

             stage('deploy') {
                 echo "create a service to track all cassandra statefulset nodes"
                 sh "kubectl apply -f cassandra-service.yaml"
                 sh "kubectl get svc cassandra"
                 sh "kubectl apply -f cassandra-statefulset.yaml"
             }

             stage('validate') {
                 sh 'kubectl get pods -l="app=cassandra"'
                 timeout(5) {
                    waitUntil {
                        def r = sh script: 'kubectl exec cassandra-0 -- nodetool status', returnStatus: true
                        return (r == 0)
                    }
                 }
             }

         }
     }

  }

}
