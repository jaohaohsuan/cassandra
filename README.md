# cassandra

[origin](https://github.com/kubernetes/kubernetes/tree/master/examples/storage/cassandra)

using cqlsh
```
apk add --no-cache py-pip
pip install cqlsh
cqlsh cassandra-discovery.default.svc.cluster.local 9042 --cqlversion=3.4.2
```

connetct cassandra with *cassandra-discovery* to avoid dns relov pod ip address
