FROM payara/micro
LABEL maintainer="trendevfr@gmail.com"
COPY ./target/webapp1-12.1.war $DEPLOY_DIR
CMD ["--deploy", "/opt/payara/deployments/webapp1-12.1.war"]
