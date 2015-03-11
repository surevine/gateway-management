#!/bin/bash

# Because of limitations of the play2-maven-plugin, it is possible to
# build an excutable dist but not to install it into the local Maven
# repository. To get around this issue, some additional maven-install
# -plugin configuration has been created to install the generated
# dist manually.
#
# This script wraps the necessary Maven calls to ensure the dist is
# produced and subsequently installed.

mvn package && mvn play2:dist && mvn install

