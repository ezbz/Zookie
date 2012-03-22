# Zookie - A Monitoring and Management tool for Apache Zookeeper

## Overview
Gives access to selected portions of the Zookeeper Node Tree via HTTP, resulting in JSON response.

## Requirements
* Was tested with Apache Zookeeper 3.3.2, 3.3.3 and 3.4.0 versions.
* Maven for building the war

## Installation
* Clone the repoistory, use mvn install to build the war file, deploy the war file to any J2EE servlet container.
* To control the ensemble property either modify application.properties before you build or supply the system property for:

-Dorg.projectx.zookeeper.ensemble=zk1:2181,zk2:2181


## Examples

## Tree

Queries the ZNode tree, parameters:

root: the root node to start querying from
depth: the depth of the tree to query (0 for flat single level, -1 for infinite - use with care in large tress)
data: show the data
stat: show the ZNode stat


1.  http://localhost:8080/Zookie/tree

{"path":"/","children":[{"path":"/storm","data":"\u0007"},{"path":"/zookeeper"},{"path":"/services"}]}

* defaults to a root = /, depth of 1, data = false, stat = false

2. http://localhost:8080/Zookie/tree?root=/services&depth=2&data=true&stat=true


{"path":"/services","children":[{"path":"/services/prod","children":[{"path":"/services/prod/ServiceA"},
{"path":"/services/prod/ServiceB"}]}]}


## Stat

Show the server stat, parameters

hosts: a list of hosts to query (lea



1. http://localhost:8080/Zookie/server/stat, http://localhost:8080/Zookie/server/stat?hosts=zk1:2181&zk2:2181

{
"zk01.nydc1:2181": {
"version": "3.3.4-cdh3u3--1",
"mode": "Follower",
"buildDate": "01/26/2012 20:09 GMT",
"clients": [
{
"host": "192.168.252.34",
"port": 37668,
"received": 116563,
"sent": 116563,
"ops": 1,
"queued": 0
},
"minLatency": 0,
"avgLatency": 0,
"maxLatency": 481,
"received": 13582767,
"sent": 13572817,
"outstanding": 0,
"zxId": "0x8900044da8",
"nodes": 2547
}
}

## Environment

Show the server env, parameters

hosts: a list of hosts to query (lea


1. http://localhost:8080/Zookie/server/env, http://localhost:8080/Zookie/server/env?hosts=zk1:2181&zk2:2181

{
"localhost:2181": {
"attributes": {
...
"os.arch": "x86_64",
"zookeeper.version": "3.4.0-1142383, built on 07/03/2011 07:48 GMT",
"java.version": "1.6.0_29",
"java.library.path": "/usr/local/mysql/lib::.:/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java",
"java.vendor": "Apple Inc."
}
}
}

Erez Mazor, erezmazor@gmail.com, @mazorE on Twitter
