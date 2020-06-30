# What?

This example project will demonstrate good and bad exceptionhandling in your REST resources.

The **before** examples has some code with common occurring mistakes. The **after** examples
addresses these mistakes. 

# Requirements

* maven 3.6.1 (or higher)
* java 11 (or higher)

# TL;DR

Just run all tests: `mvn clean test`

Note: for the integration tests we use SpringCloudContract to generate the tests before they 
are run by maven as a regular unit test.

For more on integration testing examples see: https://p-bitbucket.nl.eu.abnamro.com:7999/projects/COESD/repos/devcon2019

# Tips

see: https://stackoverflow.com/questions/107701/how-can-i-remove-ds-store-files-from-a-git-repository


find . -name .DS_Store -print0 | xargs -0 git rm -f --ignore-unmatch

add to .gitignore:
.DS_Store

git add .gitignore
git commit -m '.DS_Store banished!'
