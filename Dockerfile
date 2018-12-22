FROM payara/micro
ADD ./target/webapp1-12.1.war $DEPLOY_DIR
CMD ["--deploy", "$DEPLOY_DIR/webapp1-12.1.war"]
