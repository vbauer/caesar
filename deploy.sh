#!/bin/bash

# Exit with nonzero exit code if anything fails
set -e

# Lets work only for master
if ! [ "$TRAVIS_BRANCH" = "master" ]
then
   echo "Not a master, not deploying"
   exit 0
fi

# Generate Maven site
mvn site

# Go to the generated directory and create a *new* Git repo
cd target/site
git init

# Inside this git repo we'll pretend to be a new user
git config user.name "Vladislav Bauer"
git config user.email "bauer.vlad@gmail.com"

# The first and only commit to this new Git repo contains all the
# files present with the commit message "Generate Maven Site"
git add .
git commit -m "Generate Maven Site"

# Force push from the current repo's master branch to the remote
# repo's gh-pages branch. (All previous history on the gh-pages branch
# will be lost, since we are overwriting it.) We redirect any output to
# /dev/null to hide any sensitive credential data that might otherwise be exposed
git push --force --quiet "https://${GH_TOKEN}@${GH_REF}" master:gh-pages > /dev/null 2>&1
