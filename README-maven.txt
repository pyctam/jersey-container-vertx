Table of contents:
  * Maintaining licence header


Maintaining licence header
----------
In order to apply the Apache License, Version 2.0 boilerplate notice to the project source files
com.google.code.maven-license-plugin:maven-license-plugin is used. For detailed information about
plugin please check http://code.mycila.com/license-maven-plugin (was actual as of March 1, 2015).

Template of the license file is located in ${PARENT_PROJECT_ROOT}/src/license/header.txt file.

Use below plugin goals in order to:
  mvn license:check  - check what files do not contain licence header
  mvn license:format - apply licence header from the template
  mvn license:remove - remove licence header from source files