#!/usr/bin/env bash
echo "B=$TRAVIS_BRANCH T=$TRAVIS_TAG RQ=$TRAVIS_PULL_REQUEST"
if [ ! -z "$TRAVIS_TAG" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
	echo "Deploy $TRAVIS_TAG"
	echo "Preparing key"
	openssl aes-256-cbc -K $encrypted_3d7f448369a7_key -iv $encrypted_3d7f448369a7_iv -in deploy/signingkey.asc.enc -out deploy/signingkey.asc -d
	echo "Importing key"
	gpg --fast-import deploy/signingkey.asc
	echo "Maven deploy"
	mvn deploy -P sign,build-extras --settings deploy/settings.xml
else
	echo "Not for deploy"
fi

