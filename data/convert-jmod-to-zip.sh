#!/usr/bin/env bash

for filepath in /Users/seashubi/.asdf/installs/java/corretto-21.0.7.6.1/jmods/*.jmod; do
    filename=$(basename "$filepath")
    echo "extracting into $filename from $filepath"
    jmod extract --dir "generated/jmods/$filename" "$filepath"
done
