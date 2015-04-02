#!/bin/sh
DIRNAME=`dirname $0`
version=`egrep '^version\s*=' $DIRNAME/build.gradle|egrep -o '[0-9\.]+'`
sed 's/__VERSION__/'$version'/' spring-scala.pom > build/libs/spring-scala-$version.pom
s3cmd put -P build/libs/spring-scala-$version*.* s3://mvn.walbrix.com/com/walbrix/spring-scala/$version/

