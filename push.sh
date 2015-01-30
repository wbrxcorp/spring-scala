#!/bin/sh
version=$1
sed 's/__VERSION__/'$version'/' spring-scala.pom > build/libs/spring-scala-$version.pom
s3cmd put -P build/libs/spring-scala-$version*.* s3://mvn.walbrix.com/com/walbrix/spring-scala/$version/

